package mc.obliviate.inventory.event;

import mc.obliviate.inventory.Gui;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GuiPreCloseEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }


    private final InventoryCloseEvent event;
    private final Gui gui;
    private boolean cancelled;

    public GuiPreCloseEvent(InventoryCloseEvent event, Gui gui) {
        this.event = event;
        this.gui = gui;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public InventoryCloseEvent getEvent() {
        return this.event;
    }

    public Gui getGui() {
        return this.gui;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
