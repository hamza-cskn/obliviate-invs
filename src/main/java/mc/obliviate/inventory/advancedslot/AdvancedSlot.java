package mc.obliviate.inventory.advancedslot;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.advancedslot.action.PickupAction;
import mc.obliviate.inventory.advancedslot.action.PutAction;

public class AdvancedSlot {

	private final int slot;
	private final Icon displayIcon;
	private final AdvancedSlotManager asm;
	private PickupAction pickupAction;
	private PutAction putAction;

	public AdvancedSlot(int slot, Icon displayIcon, AdvancedSlotManager asm) {
		this.slot = slot;
		this.displayIcon = displayIcon;
		this.asm = asm;
		pickupAction = e -> {
		};
		putAction = e -> {
		};
	}


	public PickupAction getPickupAction() {
		return pickupAction;
	}

	public PutAction getPutAction() {
		return putAction;
	}

	public AdvancedSlot onPickup(PickupAction pickupAction) {
		this.pickupAction = pickupAction;
		return this;
	}

	public AdvancedSlot onPut(PutAction putAction) {
		this.putAction = putAction;
		return this;
	}

	public Icon getDisplayIcon() {
		return displayIcon.onClick(e -> {
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
