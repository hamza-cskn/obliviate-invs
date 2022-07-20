package mc.obliviate.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class Gui implements InventoryHolder {

	private final Map<Integer, Icon> registeredIcons = new HashMap<>();
	private final String id;
	private final InventoryType inventoryType;
	public final Player player;
	private Inventory inventory;
	private String title;
	private int size;
	private boolean isClosed = false;

	public Gui(@NotNull Player player, @NotNull String id, String title, int rows) {
		this.player = player;
		this.size = rows * 9;
		this.title = title;
		this.id = id;
		this.inventoryType = InventoryType.CHEST;
	}

	public Gui(@NotNull Player player,@NotNull  String id, String title, InventoryType inventoryType) {
		this.player = player;
		this.size = Integer.MAX_VALUE;
		this.title = title;
		this.id = id;
		this.inventoryType = inventoryType;
	}

	/**
	 * Gets instance of registered plugin.
	 *
	 * @return Instance of registered plugin.
	 */
	@NotNull
	public Plugin getPlugin() {
		return InventoryAPI.getInstance().getPlugin();
	}

	/**
	 * Calls when the inventory event triggered.
	 *
	 * @param event Called event.
	 * @return force to uncancel
	 */
	public boolean onClick(InventoryClickEvent event) {
		return false;
	}

	/**
	 * @param event event
	 * @return force to uncancel
	 */
	public boolean onDrag(InventoryDragEvent event) {
		return false;
	}


	public void onOpen(InventoryOpenEvent event) {

	}

	public void onClose(InventoryCloseEvent event) {

	}

	public void open() {
		final Gui gui = InventoryAPI.getInstance().getPlayersCurrentGui(this.player);
		if (gui != null) {
			//call Bukkit's inventory close event
			Bukkit.getPluginManager().callEvent(new InventoryCloseEvent(this.player.getOpenInventory()));
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

	public void fillGui(Icon icon, Integer... blacklisted_slots) {
		for (int slot = 0; slot < size; slot++) {
			if (!checkContainsInt(slot, blacklisted_slots)) {
				this.addItem(slot, icon);
			}
		}
	}

	public void fillRow(Icon item, int row) {
		for (int i = 0; i < 9; i++) {
			this.addItem((row * 9 + i), item);
		}
	}

	public void fillColumn(Icon item, int column) {
		for (int i = 0; i < 9; i++) {
			this.addItem((i * 9 + column), item);
		}
	}

	public void addItem(int slot, Icon item) {
		if (this.inventory.getSize() <= slot) {
			throw new IndexOutOfBoundsException("Slot cannot be bigger than inventory size! [ " + slot + " >= " + this.inventory.getSize() + " ]");
		}
		if (item == null) {
			throw new NullPointerException("Item cannot be null!");
		}

		this.registeredIcons.remove(slot);
		this.registeredIcons.put(slot, item);
		this.inventory.setItem(slot, item.getItem());
	}

	public void addItem(Icon item, Integer... slots) {
		for (int slot : slots) {
			this.addItem(slot, item);
		}
	}

	public void addItem(int slot, ItemStack item) {
		this.addItem(slot, new Icon(item));
	}

	public void addItem(ItemStack item) {
		this.addItem(this.inventory.firstEmpty(), new Icon(item));
	}

	public void addItem(Material material) {
		this.addItem(this.inventory.firstEmpty(), new Icon(material));
	}

	public void addItem(int slot, Material material) {
		this.addItem(slot, new Icon(material));
	}

	/**
	 *
	 * Creates a repeat-task that will continue
	 * until the gui has closed.
	 *
	 * @param runDelayInTicks
	 * @param periodInTicks
	 * @param update
	 */
	public void updateTask(int runDelayInTicks, int periodInTicks, final Consumer<BukkitTask> update) {
		final BukkitTask[] bukkitTask = new BukkitTask[]{null};

		if (InventoryAPI.getInstance() != null) {
			bukkitTask[0] = (new BukkitRunnable() {
				public void run() {
					if (!isClosed()) {
						update.accept(bukkitTask[0]);
					} else {
						cancel();
					}
				}
			}).runTaskTimer(getPlugin(), runDelayInTicks, periodInTicks);
		}
	}

	@NotNull
	public Map<Integer, Icon> getItems() {
		return registeredIcons;
	}

	@NotNull
	public String getId() {
		return id;
	}

	@Override
	@NotNull
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
	public void sendTitleUpdate(@NotNull String title) {
		this.title = Objects.requireNonNull(title, "title cannot be null!");
		this.open();
	}

	/**
	 * Automatically updates GUI size and reopens inventory
	 *
	 * @param sizeUpdate new size
	 */
	public void sendSizeUpdate(int sizeUpdate) {
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
	 * Sets size of inventory.
	 *
	 * @param size Size of inventory.
	 */
	public void setSize(int size) {
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

	private boolean checkContainsInt(int i, Integer... ints) {
		for (int n : ints) {
			if (n == i) {
				return true;
			}
		}
		return false;
	}
}