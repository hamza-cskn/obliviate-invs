package mc.obliviate.inventory.event;

import mc.obliviate.inventory.Gui;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GuiPreCloseEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private final InventoryCloseEvent event;
	private final Gui gui;
	private boolean cancelled;

	public GuiPreCloseEvent(InventoryCloseEvent event, Gui gui) {
		this.event = event;
		this.gui = gui;
	}

	public InventoryCloseEvent getEvent() {
		return event;
	}

	public Gui getGui() {
		return gui;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
