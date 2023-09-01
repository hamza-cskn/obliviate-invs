package mc.obliviate.inventory.event;

import mc.obliviate.inventory.Gui;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GuiPreCloseEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final InventoryCloseEvent event;
    private final Gui gui;

    public GuiPreCloseEvent(InventoryCloseEvent event, Gui gui) {
        this.event = event;
        this.gui = gui;
    }

    public static HandlerList getHandlerList() {
        return handlers;
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

}
