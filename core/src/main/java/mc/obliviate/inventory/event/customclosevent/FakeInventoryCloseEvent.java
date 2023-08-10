package mc.obliviate.inventory.event.customclosevent;

import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;

public class FakeInventoryCloseEvent extends InventoryCloseEvent {

    public FakeInventoryCloseEvent(InventoryView transaction) {
        super(transaction);
    }
}
