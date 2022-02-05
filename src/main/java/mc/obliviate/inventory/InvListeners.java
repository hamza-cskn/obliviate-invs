package mc.obliviate.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class InvListeners implements Listener {

	private final InventoryAPI inventoryAPI;

	public InvListeners(final InventoryAPI inventoryAPI) {
		this.inventoryAPI = inventoryAPI;
	}

	@EventHandler
	public void onClick(final InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) return;

		final GUI openGui = inventoryAPI.getPlayersCurrentGui((Player) event.getWhoClicked());
		if (openGui == null) return;
		if (event.getClickedInventory() == null) return;

		openGui.getAdvancedSlotManager().onClick(event);
		final boolean forceUncancel = openGui.onClick(event);
		final int index = event.getRawSlot();
		if (!forceUncancel) {
			if (event.getSlot() == index) {
				event.setCancelled(true);
			} else {
				switch (event.getAction()) {
					//SHIFT CLICK etc.
					case MOVE_TO_OTHER_INVENTORY:
						//DOUBLE CLICK WITH CURSOR
					case COLLECT_TO_CURSOR:
						//SOMETIMES HACKED CLIENT CLICK etc.
					case UNKNOWN:
						event.setCancelled(true);
				}
			}
		} else {
			event.setCancelled(false);
		}

		final Icon item = openGui.getItems().get(index);

		if (item == null) return;
		if (item.getClickAction() == null) return;


		item.getClickAction().click(event);


	}

	@EventHandler
	public void onClose(final InventoryCloseEvent event) {
		if (!(event.getPlayer() instanceof Player)) return;
		final Player player = (Player) event.getPlayer();
		final GUI openGui = inventoryAPI.getPlayersCurrentGui(player);
		if (openGui == null) return;
		if (!event.getInventory().equals(openGui.getInventory())) return;

		openGui.onClose(event);
		openGui.getAdvancedSlotManager().onClose(event);
		openGui.setClosed(true);
		inventoryAPI.getPlayers().remove(player.getUniqueId());
	}

	@EventHandler
	public void onDrag(final InventoryDragEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) return;
		final Player player = (Player) event.getWhoClicked();
		final GUI openGui = inventoryAPI.getPlayersCurrentGui(player);
		if (openGui == null) return;

		//if forced to uncancel, uncancel. else cancel.
		event.setCancelled(!openGui.onDrag(event));
		for (int index : event.getRawSlots()) {
			final Icon item = openGui.getItems().get(index);

			if (item == null) return;
			if (item.getClickAction() == null) {
				return;
			}
			item.getDragAction().drag(event);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onOpen(final InventoryOpenEvent event) {
		if (!(event.getPlayer() instanceof Player)) return;

		final Player player = (Player) event.getPlayer();
		final GUI openGui = inventoryAPI.getPlayersCurrentGui(player);
		if (openGui == null) return;
		if (event.isCancelled()) return;

		openGui.onOpen(event);
	}


}
