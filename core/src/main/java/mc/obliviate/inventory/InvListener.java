package mc.obliviate.inventory;

import mc.obliviate.inventory.event.GuiPreClickEvent;
import mc.obliviate.inventory.event.GuiPreCloseEvent;
import mc.obliviate.inventory.event.GuiPreDragEvent;
import mc.obliviate.inventory.event.GuiPreOpenEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class InvListener implements Listener {

	private final InventoryAPI inventoryAPI;

	protected InvListener(final InventoryAPI inventoryAPI) {
		this.inventoryAPI = inventoryAPI;
	}

	@EventHandler
	public void onClick(final InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) return;

		final Gui openGui = inventoryAPI.getPlayersCurrentGui((Player) event.getWhoClicked());
		if (openGui == null) return;

		if (callAndCheckCancel(new GuiPreClickEvent(event, openGui))) return;
		final boolean doNotProtect = openGui.onClick(event);
		final int index = event.getRawSlot();

		if (!doNotProtect) {
			//default click
			if (event.getSlot() == index) {
				event.setCancelled(true);
			} else {
				switch (event.getAction()) {
					case MOVE_TO_OTHER_INVENTORY:
						//SHIFT CLICK etc.
					case COLLECT_TO_CURSOR:
						//DOUBLE CLICK WITH CURSOR
					case UNKNOWN:
						//SOMETIMES HACKED CLIENT CLICK etc.
						event.setCancelled(true);
				}
			}
		} else {
			event.setCancelled(false);
		}

		final Icon item = openGui.getItems().get(index);
		if (item == null) return;

		item.getClickAction().accept(event);
	}

	@EventHandler
	public void onClose(final InventoryCloseEvent event) {
		if (!(event.getPlayer() instanceof Player)) return;
		final Player player = (Player) event.getPlayer();
		final Gui openGui = inventoryAPI.getPlayersCurrentGui(player);
		if (openGui == null) return;
		if (!event.getInventory().equals(openGui.getInventory())) return;

		if (callAndCheckCancel(new GuiPreCloseEvent(event, openGui))) return;
		openGui.onClose(event);
		openGui.setClosed(true);
		inventoryAPI.getPlayers().remove(player.getUniqueId());
	}

	@EventHandler
	public void onDrag(final InventoryDragEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) return;
		final Player player = (Player) event.getWhoClicked();
		final Gui openGui = inventoryAPI.getPlayersCurrentGui(player);
		if (openGui == null) return;
		if (callAndCheckCancel(new GuiPreDragEvent(event, openGui))) return;

		//if forced to uncancel, uncancel. else cancel.
		event.setCancelled(!openGui.onDrag(event));
		for (int index : event.getRawSlots()) {
			final Icon item = openGui.getItems().get(index);

			if (item == null) return;
			item.getDragAction().accept(event);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onOpen(final InventoryOpenEvent event) {
		if (!(event.getPlayer() instanceof Player)) return;

		final Player player = (Player) event.getPlayer();
		final Gui openGui = inventoryAPI.getPlayersCurrentGui(player);
		if (openGui == null) return;
		if (callAndCheckCancel(new GuiPreOpenEvent(event, openGui))) return;

		if (event.isCancelled()) return;
		openGui.onOpen(event);
	}

	private static boolean callAndCheckCancel(Event event) {
		Bukkit.getPluginManager().callEvent(event);
		return event instanceof Cancellable && ((Cancellable) event).isCancelled();
	}
}