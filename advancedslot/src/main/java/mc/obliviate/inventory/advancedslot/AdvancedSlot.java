package mc.obliviate.inventory.advancedslot;

import mc.obliviate.inventory.Icon;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class AdvancedSlot {

	private static final Consumer<InventoryClickEvent> EMPTY_CLICK_ACTION = e -> {
	};

	private final int slot;
	private final Icon displayIcon;
	private final AdvancedSlotManager advancedSlotManager;
	private BiPredicate<InventoryClickEvent, ItemStack> prePutClickAction;
	private Consumer<InventoryClickEvent> pickupAction = EMPTY_CLICK_ACTION;
	private Consumer<InventoryClickEvent> putAction = EMPTY_CLICK_ACTION;

	public AdvancedSlot(int slot, Icon displayIcon, AdvancedSlotManager advancedSlotManager) {
		this.slot = slot;
		this.displayIcon = displayIcon;
		this.advancedSlotManager = advancedSlotManager;
		this.prePutClickAction = (e, item) -> false;
	}

	/**
	 * @return defined action of PrePutClick event
	 */
	@NotNull
	public BiPredicate<InventoryClickEvent, ItemStack> getPrePutClickAction() {
		return this.prePutClickAction;
	}

	/**
	 * @return defined action of Pickup event
	 */
	@NotNull
	public Consumer<InventoryClickEvent> getPickupAction() {
		return this.pickupAction;
	}

	/**
	 * @return defined action of Put event
	 */
	@NotNull
	public Consumer<InventoryClickEvent> getPutAction() {
		return this.putAction;
	}

	/**
	 *
	 * Defines action of on Pickup event. It called when
	 * a player picked back an item.
	 *
	 * @param pickupAction the action
	 * @return same instance
	 */
	@Contract("_ -> this")
	public AdvancedSlot onPickup(Consumer<InventoryClickEvent> pickupAction) {
		this.pickupAction = Objects.requireNonNull(pickupAction, "pickup action cannot be null");
		return this;
	}

	/**
	 *
	 * Defines action of on Put event. It called when
	 * a player put an item.
	 *
	 * @param putAction the action
	 * @return same instance
	 */
	@Contract("_ -> this")
	public AdvancedSlot onPut(Consumer<InventoryClickEvent> putAction) {
		this.putAction = Objects.requireNonNull(putAction, "put action cannot be null");
		return this;
	}

	/**
	 *
	 * Defines action of on PrePut event. It called when
	 * a player put an item. This event is able to cancel
	 * putting perform. Also it called before the Put event.
	 *
	 * Generics of action: <The Click Event, Clicked ItemStack>
	 *
	 * @param prePutClickAction the action
	 * @return same instance
	 */
	@Contract("_ -> this")
	public AdvancedSlot onPreClick(BiPredicate<InventoryClickEvent, ItemStack> prePutClickAction) {
		this.prePutClickAction = Objects.requireNonNull(prePutClickAction, "prePut action cannot be null");
		return this;
	}

	/**
	 *
	 * If you don't want go into deep. You don't need this method.
	 *
	 * @return default display icon of the advanced slot
	 */
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
				advancedSlotManager.putIconToAdvancedSlot(this, cursor, e);
			}
		});
	}

	/**
	 * Remove putted icon. Replace display icon.
	 */
	public void reset() {
		advancedSlotManager.getGui().addItem(slot, getDisplayIcon());
	}

	/**
	 * @return inventory slot no of the icon
	 */
	public int getSlot() {
		return slot;
	}
}
