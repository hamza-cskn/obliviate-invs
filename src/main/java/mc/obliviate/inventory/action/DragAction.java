package mc.obliviate.inventory.action;

import org.bukkit.event.inventory.InventoryDragEvent;

@FunctionalInterface
public interface DragAction {

	void drag(InventoryDragEvent event);
}
