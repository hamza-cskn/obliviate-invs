package mc.obliviate.inventory.action;

import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface ClickAction {
	void click(InventoryClickEvent event);

}
