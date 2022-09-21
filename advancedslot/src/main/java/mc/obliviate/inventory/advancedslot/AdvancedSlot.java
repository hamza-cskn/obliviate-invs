package mc.obliviate.inventory.advancedslot;

import mc.obliviate.inventory.Icon;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class AdvancedSlot {


    private boolean refundOnClose = true;
    private final int slot;
    private final Icon displayIcon;
    private final AdvancedSlotManager advancedSlotManager;
    private BiPredicate<InventoryClickEvent, ItemStack> prePutClickAction = (e, item) -> false;
    private BiPredicate<InventoryClickEvent, ItemStack> prePickupClickAction = (e, item) -> false;
    private BiConsumer<InventoryClickEvent, ItemStack> pickupAction = (e, item) -> {
    };
    private BiConsumer<InventoryClickEvent, ItemStack> putAction = (e, item) -> {
    };

    public AdvancedSlot(int slot, Icon displayIcon, AdvancedSlotManager advancedSlotManager) {
        this.slot = slot;
        this.advancedSlotManager = advancedSlotManager;
        this.displayIcon = displayIcon.onClick(e -> {
            if (this.prePutClickAction.test(e, e.getCursor())) return;
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
                this.advancedSlotManager.putIconToAdvancedSlot(this, cursor, e);
            } else if (e.getAction().equals(InventoryAction.HOTBAR_MOVE_AND_READD)) {
                ItemStack hotbarItem = Objects.requireNonNull(AdvancedSlotManager.getItemStackFromHotkeyClick(e));
                if (this.prePutClickAction.test(e, hotbarItem)) return;
                this.advancedSlotManager.putIconToAdvancedSlot(this, hotbarItem, e);
                e.getWhoClicked().getInventory().setItem(e.getHotbarButton(), null);
            }
        });
    }

    /**
     * @return defined action of PrePutClick event
     */
    @Nonnull
    public BiPredicate<InventoryClickEvent, ItemStack> getPrePutClickAction() {
        return this.prePutClickAction;
    }

    /**
     * @return defined action of Pickup event
     */
    @Nonnull
    public BiConsumer<InventoryClickEvent, ItemStack> getPickupAction() {
        return this.pickupAction;
    }

    /**
     * @return defined action of Put event
     */
    @Nonnull
    public BiConsumer<InventoryClickEvent, ItemStack> getPutAction() {
        return this.putAction;
    }

    /**
     * @return defined action of PrePickupClick event
     */
    @Nonnull
    public BiPredicate<InventoryClickEvent, ItemStack> getPrePickupClickAction() {
        return prePickupClickAction;
    }

    /**
     * Defines action of on Pickup event. It called when
     * a player picked back an item.
     *
     * @param pickupAction the action
     * @return same instance
     */
    public AdvancedSlot onPickup(BiConsumer<InventoryClickEvent, ItemStack> pickupAction) {
        this.pickupAction = Objects.requireNonNull(pickupAction, "pickup action cannot be null");
        return this;
    }

    /**
     * Defines action of on Put event. It called when
     * a player put an item.
     *
     * @param putAction the action
     * @return same instance
     */
    public AdvancedSlot onPut(BiConsumer<InventoryClickEvent, ItemStack> putAction) {
        this.putAction = Objects.requireNonNull(putAction, "put action cannot be null");
        return this;
    }

    /**
     * Defines action of on PrePut event. It called when
     * a player put an item. This event is able to cancel
     * putting perform. Also, it called before the Put event.
     * <p>
     * Generics of action: <The Click Event, Clicked ItemStack>
     *
     * @param prePutClickAction the action
     * @return same instance
     */
    public AdvancedSlot onPrePutClick(BiPredicate<InventoryClickEvent, ItemStack> prePutClickAction) {
        this.prePutClickAction = Objects.requireNonNull(prePutClickAction, "prePut action cannot be null");
        return this;
    }

    /**
     * Defines action of on PrePut event. It called when
     * a player put an item. This event is able to cancel
     * putting perform. Also, it called before the Put event.
     * <p>
     * Generics of action: <The Click Event, Clicked ItemStack>
     *
     * @param prePickupClick the action
     * @return same instance
     */
    public AdvancedSlot onPrePickupClick(BiPredicate<InventoryClickEvent, ItemStack> prePickupClick) {
        this.prePickupClickAction = Objects.requireNonNull(prePickupClick, "prePickup action cannot be null");
        return this;
    }

    /**
     * If you don't want go into deep. You don't need this method.
     *
     * @return default display icon of the advanced slot
     */
    public Icon getDisplayIcon() {
        return displayIcon;
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

    public ItemStack getPuttedItem() {
        ItemStack itemOnSlot = this.advancedSlotManager.getGui().getInventory().getItem(this.getSlot());
        if (Objects.equals(getDisplayIcon().getItem(), itemOnSlot)) {
            return null;
        }
        return itemOnSlot;
    }

    public boolean isRefundOnClose() {
        return refundOnClose;
    }

    /**
     * when the gui closed, advanced slot
     * gives back putted item to player.
     * false parameter disables this feature
     * for that advanced slot.
     * <p>
     * default is true.
     *
     * @param refundOnClose
     */
    public void setRefundOnClose(boolean refundOnClose) {
        this.refundOnClose = refundOnClose;
    }
}
