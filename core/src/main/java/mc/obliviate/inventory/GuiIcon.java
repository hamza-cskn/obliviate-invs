package mc.obliviate.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public interface GuiIcon {

	Consumer<InventoryClickEvent> getClickAction();

	Consumer<InventoryDragEvent> getDragAction();

	ItemStack getItem();
}
