package mc.obliviate.inventory.advancedslot;

import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AdvancedSlotManager {

	protected static final Map<Gui, AdvancedSlotManager> ADVANCED_SLOT_MANAGERS = new HashMap<>();
	private final Map<Integer, AdvancedSlot> slots = new HashMap<>();
	private final Gui gui;

	public AdvancedSlotManager(Gui gui) {
		this.gui = gui;
		AdvancedSlotListener.poke();
	}

	public Collection<AdvancedSlot> getSlots() {
		return slots.values();
	}

	/**
	 *
	 * Puts new advanced slot icon normally.
	 *
	 * @param slot the slot where icon will put
	 * @param icon the default slot icon (most time, it is barrier or air)
	 * @return new advanced slot instance
	 */
	@Nonnull
	public AdvancedSlot addAdvancedIcon(final int slot, final Icon icon) {
		final AdvancedSlot aSlot = new AdvancedSlot(slot, icon, this);
		registerSlot(aSlot);
		aSlot.reset();
		return aSlot;
	}

	/**
	 * @param aSlot advanced slot where the item will put.
	 * @param item the item which will be putted
	 */
	public void putIconToAdvancedSlot(AdvancedSlot aSlot, ItemStack item) {
		putIconToAdvancedSlot(aSlot, item, null);
	}

	/**
	 * @param aSlot advanced slot where the item will put.
	 * @param item the item which will be putted
	 */
	public void putIconToAdvancedSlot(AdvancedSlot aSlot, ItemStack item, InventoryClickEvent event) {
		gui.addItem(aSlot.getSlot(), new Icon(item)
				.onClick(e -> {
					//pre put action checks
					switch (e.getAction()) {
						case HOTBAR_MOVE_AND_READD:
						case HOTBAR_SWAP: // theoretically it's impossible, but I'll add for guarantee.

							//check is it put action
							if (!isNullOrAir(getItemStackFromHotkeyClick(e))) {
								if (aSlot.getPrePutClickAction().test(e, e.getCursor())) return;
							}
							break;
						case SWAP_WITH_CURSOR:
							//check is it put action
							if (!isNullOrAir(e.getCursor())) {
								if (aSlot.getPrePutClickAction().test(e, e.getCursor())) return;
							}
							break;
					}

					e.setCancelled(false);

					//general checks
					switch (e.getAction()) {
						case PICKUP_ALL:
						case DROP_ALL_SLOT:
						case HOTBAR_SWAP:
						case MOVE_TO_OTHER_INVENTORY:
						case COLLECT_TO_CURSOR:
							aSlot.getPickupAction().accept(e);
							break;
						default:
							aSlot.getPutAction().accept(e);
					}
					Bukkit.getScheduler().runTaskLater(gui.getPlugin(), () -> {
						if (gui.getInventory().getItem(aSlot.getSlot()) == null) {
							aSlot.reset();
						}
					}, 1);
				}));
		aSlot.getPutAction().accept(event);
	}

	public void onClick(InventoryClickEvent e) {
		if (e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
			if (e.getRawSlot() == e.getSlot()) return;
			for (final AdvancedSlot aSlot : getSlots()) {

				final ItemStack itemOnSlot = gui.getInventory().getItem(aSlot.getSlot());
				ItemStack clickedItem = e.getCurrentItem();

				//if aSlot is empty, just put the clicked item.
				if ((isNullOrAir(itemOnSlot) && isNullOrAir(aSlot.getDisplayIcon().getItem())) || aSlot.getDisplayIcon().getItem().equals(itemOnSlot)) {
					if (aSlot.getPrePutClickAction().test(e, clickedItem)) {
						return;
					}
					putIconToAdvancedSlot(aSlot, clickedItem, e);
					e.setCurrentItem(new ItemStack(Material.AIR));

					//else, compare aSlot item and clicked item to merge item amount.
				} else if (clickedItem != null && itemOnSlot != null && compareSimilar(clickedItem, itemOnSlot) && itemOnSlot.getAmount() < itemOnSlot.getType().getMaxStackSize()) {
					if (aSlot.getPrePutClickAction().test(e, clickedItem)) return;

					int maxSize = itemOnSlot.getType().getMaxStackSize();

					int transferSize;
					if (maxSize <= itemOnSlot.getAmount() + clickedItem.getAmount()) {
						transferSize = maxSize - itemOnSlot.getAmount();
					} else {
						transferSize = clickedItem.getAmount();
					}
					itemOnSlot.setAmount(itemOnSlot.getAmount() + transferSize);
					clickedItem.setAmount(clickedItem.getAmount() - transferSize);

					if (clickedItem.getAmount() == 0) {
						clickedItem = new ItemStack(Material.AIR);
					}

					e.setCurrentItem(clickedItem);
					putIconToAdvancedSlot(aSlot, itemOnSlot, e);

				} else {
					continue;
				}
				return;
			}
		}
	}

	public void onClose(InventoryCloseEvent e) {
		for (int slot : this.slots.keySet()) {
			final ItemStack itemOnSlot = e.getInventory().getItem(slot);
			if (itemOnSlot == null) continue;
			AdvancedSlot advancedSlot = this.slots.get(slot);
			if (!advancedSlot.isRefundOnClose()) continue;
			if (!this.compareSimilar(itemOnSlot, advancedSlot.getDisplayIcon().getItem())) {
				if (this.hasSpace(e.getInventory())) {
					e.getPlayer().getInventory().addItem(itemOnSlot);
				} else {
					e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation(), itemOnSlot);
				}
			}
		}
	}

	public void registerSlot(AdvancedSlot slot) {
		this.slots.put(slot.getSlot(), slot);
	}

	private ItemStack getItemStackFromHotkeyClick(InventoryClickEvent event) {
		if (event.getHotbarButton() == -1) return null;
		final Player player = (Player) event.getWhoClicked();
		return player.getInventory().getItem(event.getHotbarButton());
	}

	private boolean isNullOrAir(final ItemStack item) {
		return item == null || item.getType().equals(Material.AIR);
	}

	private boolean compareSimilar(final ItemStack item1, final ItemStack item2) {
		final boolean inoa1 = isNullOrAir(item1);
		final boolean inoa2 = isNullOrAir(item2);
		if (inoa1 && inoa2) return true;
		if (inoa1 || inoa2) return false;
		return item1.isSimilar(item2);
	}

	private boolean hasSpace(Inventory inventory) {
		for (ItemStack item : inventory.getContents()) {
			if (item == null) return true;
		}
		return false;
	}

	public Gui getGui() {
		return this.gui;
	}

	public static Map<Gui, AdvancedSlotManager> getAdvancedSlotManagers() {
		return Collections.unmodifiableMap(ADVANCED_SLOT_MANAGERS);
	}
}
