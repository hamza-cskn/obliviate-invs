package mc.obliviate.inventory;

import com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;
import com.github.Anon8281.universalScheduler.scheduling.tasks.MyScheduledTask;
import com.google.common.base.Preconditions;
import mc.obliviate.inventory.event.customclosevent.FakeInventoryCloseEvent;
import mc.obliviate.inventory.util.NMSUtil;
import net.kyori.adventure.text.Component;
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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class Gui implements InventoryHolder {

	private final Map<Integer, GuiIcon> registeredIcons;
	private final List<MyScheduledTask> taskList = new ArrayList<>();
	private final String id;
	private final InventoryType inventoryType;
	public final Player player;
	private Inventory inventory;
	private String title;
	private int size;
	private boolean isClosed = false;

	public Gui(@Nonnull Player player, @Nonnull String id, String title, @Nonnegative int rows) {
		this.registeredIcons = new HashMap<>(rows * 9);
		this.player = player;
		this.size = rows * 9;
		this.title = title;
		this.id = id;
		this.inventoryType = InventoryType.CHEST;
	}

	public Gui(@Nonnull Player player, @Nonnull String id, String title, InventoryType inventoryType) {
		this.registeredIcons = new HashMap<>(inventoryType.getDefaultSize());
		this.player = player;
		this.size = inventoryType.getDefaultSize();
		this.title = title;
		this.id = id;
		this.inventoryType = inventoryType;
	}

	public Gui(@Nonnull Player player, @Nonnull String id, Component title, @Nonnegative int rows) {
		this(player, id, NMSUtil.LEGACY.serialize(title), rows);
	}

	public Gui(@Nonnull Player player, @Nonnull String id, Component title, InventoryType inventoryType) {
		this(player, id, NMSUtil.LEGACY.serialize(title), inventoryType);
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
	 * @return force to uncancel, should be allowed.
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
	 * @return force to uncancel, should be allowed.
	 */
	public boolean onDrag(InventoryDragEvent event) {
		return false;
	}


	/**
	 * @param event The InventoryOpenEvent that triggered when the Gui opened.
	 */
	public void onOpen(InventoryOpenEvent event) {

	}

	/**
	 * @param event The InventoryCloseEvent that triggered when the Gui closed
	 *              WARN: Always call super.onClose() event! This method is not empty!
	 */
	public void onClose(InventoryCloseEvent event) {
		if (event instanceof FakeInventoryCloseEvent) return;
		final Gui gui = InventoryAPI.getInstance().getGuiFromInventory(event.getPlayer().getOpenInventory().getTopInventory());
		if (gui == null) return;
		if (!gui.equals(this)) return;
		gui.stopAllTasks();
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

	public void fillGui(GuiIcon icon) {
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

	public void fillGui(GuiIcon icon, Iterable<Integer> blacklisted_slots) {
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
	public void fillRow(GuiIcon item, @Nonnegative int row) {
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
	public void fillColumn(GuiIcon item, @Nonnegative int column) {
		Preconditions.checkArgument(column < 9);
		for (int i = 0; i < (size/9); i++) {
			this.addItem((i * 9 + column), item);
		}
	}

	public void addItem(@Nonnegative int slot, @Nullable GuiIcon icon) {
		if (this.inventory.getSize() <= slot) {
			throw new IndexOutOfBoundsException("Slot cannot be bigger than inventory size! [ " + slot + " >= " + this.inventory.getSize() + " ]");
		}

		this.registeredIcons.put(slot, icon);
		this.inventory.setItem(slot, (icon == null ? null : icon.getItem()));
	}

	public void addItem(@Nullable GuiIcon item, @Nonnull Integer... slots) {
		for (int slot : slots) {
			this.addItem(slot, item);
		}
	}

	public void addItem(@Nullable GuiIcon icon, @Nonnull Iterable<Integer> slots) {
		for (Integer slot : slots) {
			this.addItem(slot, icon);
		}
	}

	public void addItem(@Nonnegative int slot, @Nullable ItemStack item) {
		this.addItem(slot, new Icon(item));
	}

	public void addItem(@Nonnull GuiIcon icon) {
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
	public void updateTask(@Nonnegative long runDelayInTicks, @Nonnegative long periodInTicks, @Nonnull final Consumer<MyScheduledTask> update) {
		Preconditions.checkNotNull(InventoryAPI.getInstance(), "InventoryAPI is not initialized.");
		final MyScheduledTask[] scheduledTask = new MyScheduledTask[]{null};
		scheduledTask[0] = InventoryAPI.getScheduler().runTaskTimer(() -> update.accept(scheduledTask[0]), runDelayInTicks, periodInTicks);
		taskList.add(scheduledTask[0]);
	}

	/**
	 * Creates a delayed-task that will run later
	 * when the Gui closed, this task will be cancelled.
	 *
	 * @param runDelayInTicks
	 * @param update
	 */
	public void runTaskLater(@Nonnegative long runDelayInTicks, @Nonnull final Consumer<MyScheduledTask> update) {
		Preconditions.checkNotNull(InventoryAPI.getInstance(), "InventoryAPI is not initialized.");
		final MyScheduledTask[] scheduledTask = new MyScheduledTask[]{null};
		scheduledTask[0] = InventoryAPI.getScheduler().runTaskLater(() -> update.accept(scheduledTask[0]), runDelayInTicks);
		taskList.add(scheduledTask[0]);
	}

	@Nonnull
	public Map<Integer, GuiIcon> getItems() {
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

	public List<MyScheduledTask> getTaskList() {
		return Collections.unmodifiableList(taskList);
	}

	public void stopTask(@Nonnull MyScheduledTask task) {
		Preconditions.checkNotNull(task, "task cannot be null");
		task.cancel();
		taskList.remove(task);
	}

	public void stopAllTasks() {
		taskList.forEach(MyScheduledTask::cancel);
		taskList.clear();
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
