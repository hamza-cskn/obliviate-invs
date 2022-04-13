package mc.obliviate.inventory.advancedslot.action;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface PrePutClickAction {

	boolean onPrePutClick(InventoryClickEvent e, ItemStack clickedItem);

}
