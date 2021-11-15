package mc.obliviate.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import mc.obliviate.inventory.advancedslot.AdvancedSlot;
import mc.obliviate.inventory.advancedslot.AdvancedSlotManager;
import mc.obliviate.inventory.pagination.Pagination;

import java.util.HashMap;
import java.util.Map;

public abstract class GUI implements InventoryHolder {

	private final Map<Integer, Icon> items = new HashMap<>();
	private final String id;
	private final Map<Integer, ItemStack> animations = new HashMap<>();
	private final AdvancedSlotManager advancedSlotManager = new AdvancedSlotManager(this);
	public Player player;
	private Pagination pagination = null;
	private Inventory inventory;
	private String title;
	private int size;
	private boolean closed = false;

	public GUI(Player player, String id, String title, int rows) {
		this.player = player;
		this.size = rows * 9;
		this.title = title;
		this.id = id;

	}


	/**
	 * EVENTS
	 */
	public void onClick(InventoryClickEvent e) {

	}

	public void onOpen(InventoryOpenEvent event) {

	}

	public void onClose(InventoryCloseEvent event) {

	}

	/**
	 * METHODS
	 */
	public void open() {
		final GUI gui = InventoryAPI.getInstance().getPlayersCurrentGui(player);
		if (gui != null) {
			Bukkit.getPluginManager().callEvent(new InventoryCloseEvent(player.getOpenInventory()));
		}

		InventoryAPI.getInstance().getPlayers().put(player.getUniqueId(), this);
		inventory = Bukkit.createInventory(null, size, title);

		player.openInventory(inventory);
	}

	public void fillGui(ItemStack item) {
		for (int slot = 0; slot < size; slot++) {
			addItem(slot, item);
		}
	}

	public void fillGui(ItemStack item, Integer... blacklisted_slots) {
		for (int slot = 0; slot < size; slot++) {
			if (!checkContainsInt(slot, blacklisted_slots)) {
				addItem(slot, item);
			}
		}
	}

	public void fillRow(Icon item, int row) {
		for (int i = 0; i < 9; i++) {
			addItem((row * 9 + i), item);
		}
	}

	public void fillColumn(Icon item, int column) {
		for (int i = 0; i < 9; i++) {
			addItem((i * 9 + column), item);
		}
	}

	private boolean checkContainsInt(int i, Integer... ints) {
		for (int j : ints) {
			if (j == i) {
				return true;
			}
		}
		return false;
	}

	public void addItem(int slot, Icon item) {
		if (inventory.getSize() <= slot) {
			throw new IndexOutOfBoundsException("Slot cannot be bigger than inventory size! [ " + slot + " >= " + inventory.getSize() + " ]");
		}
		if (item == null) {
			throw new NullPointerException("Item cannot be null!");
		}

		items.remove(slot);
		items.put(slot, item);
		inventory.setItem(slot, item.getItem());
	}

	public void addItem(Icon item, Integer... slots) {
		for (int slot : slots) {
			addItem(slot, item);
		}
	}

	public void addItem(int slot, ItemStack item) {
		addItem(slot, new Icon(item));
	}

	public void addItem(ItemStack item) {
		addItem(inventory.firstEmpty(), new Icon(item));
	}

	public void addItem(Material material) {
		addItem(inventory.firstEmpty(), new Icon(material));
	}

	public void addItem(int slot, Material material) {
		addItem(slot, new Icon(material));
	}

	public void updateTask(int runLater, int period, final Update update) {
		final BukkitTask[] bukkitTask = new BukkitTask[]{null};

		if (InventoryAPI.getInstance() != null) {
			bukkitTask[0] = (new BukkitRunnable() {
				public void run() {
					if (!isClosed()) {
						update.update(bukkitTask[0]);
					} else {
						cancel();
					}

				}


			}).runTaskTimer(getPlugin(), runLater, period);
		}

	}


	public AdvancedSlot addAdvancedHytem(int slot, Icon hytem) {
		final AdvancedSlot aSlot = new AdvancedSlot(slot, hytem, advancedSlotManager);
		advancedSlotManager.registerSlot(aSlot);
		addItem(slot, aSlot.getDisplayIcon());
		return aSlot;
	}


	/**
	 * GETTERS
	 */
	public Map<Integer, Icon> getItems() {
		return items;
	}

	public String getId() {
		return id;
	}

	public AdvancedSlotManager getAdvancedSlotManager() {
		return advancedSlotManager;
	}

	public Pagination getPagination() {
		if (pagination == null) {
			pagination = new Pagination(this);
		}
		return pagination;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

	public String getTitle() {
		return title;
	}

	/**
	 * Sets title of GUI for GUIs that
	 * will be open later.
	 *
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Plugin getPlugin() {
		return InventoryAPI.getInstance().getPlugin();
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

}
