package mc.obliviate.inventory;

import com.google.common.base.Preconditions;
import mc.obliviate.inventory.event.customclosevent.FakeInventoryCloseEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class Gui implements InventoryHolder {

    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.builder()
            .hexColors().useUnusualXRepeatedCharacterHexFormat().build();

    private final Map<Integer, Icon> registeredIcons = new HashMap<>();
    private final List<BukkitTask> taskList = new ArrayList<>();
    private final String id;
    private final InventoryType inventoryType;
    public final Player player;
    private Inventory inventory;
    private String title;
    private int size;
    private boolean isClosed = false;

    /**
     * @deprecated Use components instead
     */
    @Deprecated
    public Gui(@Nonnull Player player, @Nonnull String id, String title, @Nonnegative int rows) {
        this.player = player;
        this.size = rows * 9;
        this.title = title;
        this.id = id;
        this.inventoryType = InventoryType.CHEST;
    }

    /**
     * @deprecated Use components instead
     */
    @Deprecated
    public Gui(@Nonnull Player player, @Nonnull String id, String title, InventoryType inventoryType) {
        this.player = player;
        this.size = 0;
        this.title = title;
        this.id = id;
        this.inventoryType = inventoryType;
    }

    public Gui(@Nonnull Player player, @Nonnull String id, Component title, @Nonnegative int rows) {
        this.player = player;
        this.size = rows * 9;
        // todo find a better way to do this using paper
        this.title = LEGACY.serialize(title);
        this.id = id;
        this.inventoryType = InventoryType.CHEST;
    }

    public Gui(@Nonnull Player player, @Nonnull String id, Component title, InventoryType inventoryType) {
        this.player = player;
        this.size = 0;
        // todo find a better way to do this using paper
        this.title = LEGACY.serialize(title);
        this.id = id;
        this.inventoryType = inventoryType;
    }

    /**
     * Gets instance of registered plugin.
     *
     * @return Instance of registered plugin.
     */
    @Nonnull
    public Plugin getPlugin() {
        return InventoryAPI.getInstance().getPlugin();
    }

    /**
     * Calls when the inventory event triggered.
     * <p>
     * If returns true, ObliviateInvs does not cancel
     * click event. Also, Icon click event triggering after
     * this check. So you can override from icon click event.
     *
     * @param event Called event.
     * @return force to uncancel
     */
    public boolean onClick(InventoryClickEvent event) {
        return false;
    }

    /**
     * Calls when the inventory event triggered.
     * <p>
     * If returns true, ObliviateInvs does not cancel
     * drag event. Also, Icon drag event triggering after
     * this check. So you can override from icon drag event.
     *
     * @param event Called event.
     * @return force to uncancel
     */
    public boolean onDrag(InventoryDragEvent event) {
        return false;
    }


    public void onOpen(InventoryOpenEvent event) {

    }

    /**
     * @param event event
     * @return force to uncancel
     */
    public void onClose(InventoryCloseEvent event) {
        if (event instanceof FakeInventoryCloseEvent) return;
        final Gui gui = InventoryAPI.getInstance().getGuiFromInventory(event.getPlayer().getOpenInventory().getTopInventory());
        if (gui == null) return;
        if (!gui.equals(this)) return;
        taskList.forEach(BukkitTask::cancel);
    }

    public void open() {
        Preconditions.checkNotNull(InventoryAPI.getInstance(), "Inventory API is not initialized. Please use new InventoryAPI().init() also, you can visit wiki of obliviate-invs.");
        final Gui gui = InventoryAPI.getInstance().getPlayersCurrentGui(this.player);
        if (gui != null) {
            //call Bukkit's inventory close event
            Bukkit.getPluginManager().callEvent(new FakeInventoryCloseEvent(this.player.getOpenInventory()));
        }

        InventoryAPI.getInstance().getPlayers().put(this.player.getUniqueId(), this);

        if (this.inventoryType.equals(InventoryType.CHEST)) {
            this.inventory = Bukkit.createInventory(null, this.size, this.title);
        } else {
            this.inventory = Bukkit.createInventory(null, this.inventoryType, this.title);
        }

        this.player.openInventory(inventory);
    }

    public void fillGui(Icon icon) {
        for (int slot = 0; slot < size; slot++) {
            this.addItem(slot, icon);
        }
    }

    public void fillGui(ItemStack item) {
        this.fillGui(new Icon(item));
    }

    public void fillGui(Material material) {
        this.fillGui(new Icon(material));
    }

    public void fillGui(Icon icon, Iterable<Integer> blacklisted_slots) {
        for (int slot = 0; slot < size; slot++) {
            if (!checkContainsInt(slot, blacklisted_slots)) {
                this.addItem(slot, icon);
            }
        }
    }

    /**
     * Puts an icon to entire a row of inventory.
     * Row numbers starts from 0.
     *
     * @param item
     * @param row
     */
    public void fillRow(Icon item, @Nonnegative int row) {
        Preconditions.checkArgument(row < this.size / 9);
        for (int i = 0; i < 9; i++) {
            this.addItem((row * 9 + i), item);
        }
    }

    /**
     * Puts an icon to entire a column of inventory.
     * Column numbers starts from 0.
     *
     * @param item
     * @param column
     */
    public void fillColumn(Icon item, @Nonnegative int column) {
        Preconditions.checkArgument(column < 9);
        for (int i = 0; i < 9; i++) {
            this.addItem((i * 9 + column), item);
        }
    }

    public void addItem(@Nonnegative int slot, @Nullable Icon icon) {
        if (this.inventory.getSize() <= slot) {
            throw new IndexOutOfBoundsException("Slot cannot be bigger than inventory size! [ " + slot + " >= " + this.inventory.getSize() + " ]");
        }

        this.registeredIcons.put(slot, icon);
        this.inventory.setItem(slot, (icon == null ? null : icon.getItem()));
    }

    public void addItem(Icon item, Integer... slots) {
        for (int slot : slots) {
            this.addItem(slot, item);
        }
    }

    public void addItem(@Nonnegative int slot, @Nullable ItemStack item) {
        this.addItem(slot, new Icon(item));
    }

    public void addItem(@Nonnull Icon icon) {
        this.addItem(this.inventory.firstEmpty(), icon);
    }

    public void addItem(@Nullable ItemStack item) {
        this.addItem(this.inventory.firstEmpty(), new Icon(item));
    }

    public void addItem(@Nonnull Material material) {
        this.addItem(this.inventory.firstEmpty(), new Icon(material));
    }

    public void addItem(@Nonnegative int slot, Material material) {
        this.addItem(slot, new Icon(material));
    }

    /**
     * Creates a repeat-task that will continue
     * until the gui has closed.
     *
     * @param runDelayInTicks
     * @param periodInTicks
     * @param update
     */
    public void updateTask(@Nonnegative long runDelayInTicks, @Nonnegative long periodInTicks, @Nonnull final Consumer<BukkitTask> update) {
        Preconditions.checkNotNull(InventoryAPI.getInstance(), "InventoryAPI is not initialized.");
        final BukkitTask[] bukkitTask = new BukkitTask[]{null};
        bukkitTask[0] = Bukkit.getScheduler().runTaskTimer(getPlugin(), () -> update.accept(bukkitTask[0]), runDelayInTicks, periodInTicks);
        taskList.add(bukkitTask[0]);
    }

    /**
     * Creates a delayed-task that will run later
     * when the Gui closed, this task will be cancelled.
     *
     * @param runDelayInTicks
     * @param update
     */
    public void runTaskLater(@Nonnegative long runDelayInTicks, @Nonnull final Consumer<BukkitTask> update) {
        Preconditions.checkNotNull(InventoryAPI.getInstance(), "InventoryAPI is not initialized.");
        final BukkitTask[] bukkitTask = new BukkitTask[]{null};
        bukkitTask[0] = Bukkit.getScheduler().runTaskLater(getPlugin(), () -> update.accept(bukkitTask[0]), runDelayInTicks);
        taskList.add(bukkitTask[0]);
    }

    @Nonnull
    public Map<Integer, Icon> getItems() {
        return registeredIcons;
    }

    @Nonnull
    public String getId() {
        return id;
    }

    @Override
    @Nonnull
    public Inventory getInventory() {
        return this.inventory;
    }

    @Nullable
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets title of GUI. Without update.
     *
     * @param title new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Automatically updates GUI title and reopens inventory
     *
     * @param title new title
     */
    public void sendTitleUpdate(@Nonnull String title) {
        this.title = Objects.requireNonNull(title, "title cannot be null!");
        this.open();
    }

    /**
     * Automatically updates GUI size and reopens inventory
     *
     * @param sizeUpdate new size
     */
    public void sendSizeUpdate(@Nonnegative int sizeUpdate) {
        this.size = sizeUpdate;
        this.open();
    }

    /**
     * Gets size of inventory.
     *
     * @return Size of inventory.
     */
    public int getSize() {
        return this.size;
    }

    /**
     * @return biggest slot number of the gui
     */
    public int getLastSlot() {
        return this.size - 1;
    }

    /**
     * Sets size of inventory.
     *
     * @param size Size of inventory.
     */
    public void setSize(@Nonnegative int size) {
        this.size = size;
    }

    /**
     * Checks inventory is closed.
     *
     * @return Returns true, if closed.
     */
    public boolean isClosed() {
        return this.isClosed;
    }

    /**
     * Sets inventory as closed.
     *
     * @param closed Closed or not.
     */
    public void setClosed(boolean closed) {
        this.isClosed = closed;
    }

    private boolean checkContainsInt(int i, Iterable<Integer> ints) {
        for (int number : ints) {
            if (number == i) {
                return true;
            }
        }
        return false;
    }
}
