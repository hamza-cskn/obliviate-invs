package mc.obliviate.inventory.advancedslot;

import mc.obliviate.inventory.InventoryAPI;
import mc.obliviate.inventory.event.GuiPreClickEvent;
import mc.obliviate.inventory.event.GuiPreCloseEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AdvancedSlotListener implements Listener {

    public static final AdvancedSlotListener ADVANCED_SLOT_LISTENER = new AdvancedSlotListener();
    private static boolean registered = false;

    private AdvancedSlotListener() {

    }

    public static void poke() {
        if (!registered) {
            Bukkit.getPluginManager().registerEvents(ADVANCED_SLOT_LISTENER, InventoryAPI.getInstance().getPlugin());
            registered = true;
        }
    }

    @EventHandler
    public void onClick(final GuiPreClickEvent event) {
        final AdvancedSlotManager advancedSlotManager = AdvancedSlotManager.ADVANCED_SLOT_MANAGERS.get(event.getGui());
        if (advancedSlotManager == null) return;
        advancedSlotManager.onClick(event.getEvent());
    }

    @EventHandler
    public void onClose(final GuiPreCloseEvent event) {
        final AdvancedSlotManager advancedSlotManager = AdvancedSlotManager.ADVANCED_SLOT_MANAGERS.get(event.getGui());
        if (advancedSlotManager == null) return;
        advancedSlotManager.onClose(event.getEvent());
    }
}
