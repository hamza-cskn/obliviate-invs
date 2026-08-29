package mc.obliviate.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.util.HashMap;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InvListenerTest {

    @Test
    public void closeAlwaysRemovesTheGuiRegistration() {
        InventoryAPI api = mock(InventoryAPI.class);
        Player player = mock(Player.class);
        Gui gui = mock(Gui.class);
        Inventory inventory = mock(Inventory.class);
        InventoryCloseEvent event = mock(InventoryCloseEvent.class);
        PluginManager pluginManager = mock(PluginManager.class);
        UUID playerId = UUID.randomUUID();
        HashMap<UUID, Gui> players = new HashMap<>();
        players.put(playerId, gui);

        when(player.getUniqueId()).thenReturn(playerId);
        when(event.getPlayer()).thenReturn(player);
        when(event.getInventory()).thenReturn(inventory);
        when(gui.getInventory()).thenReturn(inventory);
        when(api.getPlayersCurrentGui(player)).thenReturn(gui);
        when(api.getPlayers()).thenReturn(players);
        org.mockito.Mockito.doThrow(new IllegalStateException("callback failed"))
                .when(gui).onClose(event);

        try (MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
            bukkit.when(Bukkit::getPluginManager).thenReturn(pluginManager);

            assertThrows(IllegalStateException.class, () -> new InvListener(api).onClose(event));
        }

        verify(gui).setClosed(true);
        assertFalse(players.containsKey(playerId));
    }

    @Test
    public void closingAnOldGuiDoesNotRemoveItsReplacement() {
        InventoryAPI api = mock(InventoryAPI.class);
        Player player = mock(Player.class);
        Gui oldGui = mock(Gui.class);
        Gui replacementGui = mock(Gui.class);
        Inventory inventory = mock(Inventory.class);
        InventoryCloseEvent event = mock(InventoryCloseEvent.class);
        PluginManager pluginManager = mock(PluginManager.class);
        UUID playerId = UUID.randomUUID();
        HashMap<UUID, Gui> players = new HashMap<>();
        players.put(playerId, oldGui);

        when(player.getUniqueId()).thenReturn(playerId);
        when(event.getPlayer()).thenReturn(player);
        when(event.getInventory()).thenReturn(inventory);
        when(oldGui.getInventory()).thenReturn(inventory);
        when(api.getPlayersCurrentGui(player)).thenReturn(oldGui);
        when(api.getPlayers()).thenReturn(players);
        doAnswer(invocation -> {
            players.put(playerId, replacementGui);
            return null;
        }).when(oldGui).onClose(event);

        try (MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class)) {
            bukkit.when(Bukkit::getPluginManager).thenReturn(pluginManager);
            new InvListener(api).onClose(event);
        }

        verify(oldGui).setClosed(true);
        org.junit.Assert.assertSame(replacementGui, players.get(playerId));
    }
}
