package mc.obliviate.inventory.advancedslot.action;

import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface PickupAction {

	void pickup(InventoryClickEvent e);

}
