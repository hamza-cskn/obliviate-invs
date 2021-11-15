package mc.obliviate.inventory.advancedslot;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import mc.obliviate.inventory.GUI;
import mc.obliviate.inventory.Icon;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AdvancedSlotManager {

	private final Map<Integer, AdvancedSlot> slots = new HashMap<>();
	private final GUI gui;

	public AdvancedSlotManager(GUI gui) {
		this.gui = gui;
	}

	public Collection<AdvancedSlot> getSlots() {
		return slots.values();
	}

	public void registerSlot(AdvancedSlot slot) {
		slots.put(slot.getSlot(), slot);
	}

	public void putIcon(AdvancedSlot aSlot, ItemStack item, InventoryClickEvent e) {
		gui.addItem(aSlot.getSlot(), new Icon(item)
				.onClick(event -> {
					event.setCancelled(false);
					switch (event.getAction()) {
						case PICKUP_ALL:
						case DROP_ALL_SLOT:
						case HOTBAR_SWAP:
						case MOVE_TO_OTHER_INVENTORY:
						case COLLECT_TO_CURSOR:
							aSlot.getPickupAction().pickup(e);
							break;
						default:
							aSlot.getPutAction().put(e);
					}

					Bukkit.getScheduler().runTaskLater(gui.getPlugin(), () -> {
						if (gui.getInventory().getItem(aSlot.getSlot()) == null) {
							gui.addItem(aSlot.getSlot(), aSlot.getDisplayIcon());
						}
					}, 1);
				}));
		aSlot.getPutAction().put(e);
	}

	public void onClick(InventoryClickEvent e) {
		if (e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
			if (e.getRawSlot() == e.getSlot()) return;
			for (final AdvancedSlot aSlot : getSlots()) {

				final ItemStack itemOnSlot = gui.getInventory().getItem(aSlot.getSlot());
				ItemStack clickedItem = e.getCurrentItem();

				//if aSlot is empty, just put the clicked item.
				if ((isNullOrAir(itemOnSlot) && isNullOrAir(aSlot.getDisplayIcon().getItem())) || aSlot.getDisplayIcon().getItem().equals(itemOnSlot)) {
					putIcon(aSlot, clickedItem, e);
					e.setCurrentItem(new ItemStack(Material.AIR));

					//else, compare aSlot item and clicked item then merge item amount.
				} else if (clickedItem != null && itemOnSlot != null && compareSimilar(clickedItem, itemOnSlot) && itemOnSlot.getAmount() < itemOnSlot.getType().getMaxStackSize()) {
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
					putIcon(aSlot, itemOnSlot, e);

				} else {
					continue;
				}
				return;
			}
		}
	}

	public void onClose(InventoryCloseEvent e) {
		for (int slot : slots.keySet()) {
			final ItemStack itemOnSlot = e.getInventory().getItem(slot);
			if (itemOnSlot == null) return;
			if (!compareSimilar(itemOnSlot, slots.get(slot).getDisplayIcon().getItem())) {
				if (hasSpace(e.getInventory())) {
					e.getPlayer().getInventory().addItem(itemOnSlot);
				} else {
					e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation(), itemOnSlot);
				}
			}
		}
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

}
