package mc.obliviate.inventory.advancedslot.action;

import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface PutAction {

	void put(InventoryClickEvent e);
}
