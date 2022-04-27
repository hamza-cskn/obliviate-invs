package mc.obliviate.inventory.advancedslot;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import mc.obliviate.inventory.Icon;

import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class AdvancedSlot {

	private final int slot;
	private final Icon displayIcon;
	private final AdvancedSlotManager asm;
	private BiPredicate<InventoryClickEvent, ItemStack> prePutClickAction;
	private Consumer<InventoryClickEvent> pickupAction;
	private Consumer<InventoryClickEvent> putAction;

	public AdvancedSlot(int slot, Icon displayIcon, AdvancedSlotManager asm) {
		this.slot = slot;
		this.displayIcon = displayIcon;
		this.asm = asm;
		pickupAction = e -> {};
		putAction = e -> {};
		prePutClickAction = (e, item) -> false;
	}

	public BiPredicate<InventoryClickEvent, ItemStack> getPrePutClickAction() {
		return prePutClickAction;
	}

	public Consumer<InventoryClickEvent> getPickupAction() {
		return pickupAction;
	}

	public Consumer<InventoryClickEvent> getPutAction() {
		return putAction;
	}

	public AdvancedSlot onPickup(Consumer<InventoryClickEvent> pickupAction) {
		this.pickupAction = pickupAction;
		return this;
	}

	public AdvancedSlot onPut(Consumer<InventoryClickEvent> putAction) {
		this.putAction = putAction;
		return this;
	}

	public AdvancedSlot onPreClick(BiPredicate<InventoryClickEvent, ItemStack> prePutClickAction) {
		this.prePutClickAction = prePutClickAction;
		return this;
	}

	public Icon getDisplayIcon() {
		return displayIcon.onClick(e -> {
			if (prePutClickAction.test(e, e.getCursor())) return;
			if (e.getCursor() != null && !e.getCursor().getType().equals(Material.AIR)) {
				final ItemStack cursor = e.getCursor();
				ItemStack newCursor = null;
				if (e.isRightClick()) {
					if (cursor.getAmount() > 1) {
						newCursor = cursor.clone();
						newCursor.setAmount(newCursor.getAmount() - 1);
					}
					cursor.setAmount(1);
				}

				e.setCursor(newCursor);
				asm.putIcon(this, cursor, e);


			}
		});
	}

	/**
	 * Replace slot to display icon.
	 */
	public void resetSlot() {
		asm.getGui().addItem(slot, getDisplayIcon());
	}

	public int getSlot() {
		return slot;
	}
}
