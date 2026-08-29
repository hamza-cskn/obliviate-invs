package mc.obliviate.inventory;

import com.github.Anon8281.universalScheduler.scheduling.tasks.MyScheduledTask;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GuiCloseTest {

    @SuppressWarnings("unchecked")
    @Test
    public void closeStopsTasksWithoutLookingUpTheOpenInventory() throws Exception {
        Gui gui = new Gui(mock(Player.class), "test", "Test", 1) {
        };
        MyScheduledTask task = mock(MyScheduledTask.class);
        Field taskListField = Gui.class.getDeclaredField("taskList");
        taskListField.setAccessible(true);
        ((List<MyScheduledTask>) taskListField.get(gui)).add(task);

        gui.onClose(mock(InventoryCloseEvent.class));

        verify(task).cancel();
        assertTrue(gui.getTaskList().isEmpty());
    }

    @Test
    public void reopeningResetsTheClosedState() {
        Player player = mock(Player.class);
        Inventory inventory = mock(Inventory.class);
        InventoryAPI api = mock(InventoryAPI.class);
        UUID playerId = UUID.randomUUID();
        HashMap<UUID, Gui> players = new HashMap<>();
        Gui gui = new Gui(player, "test", "Test", 1) {
            @Override
            protected Inventory createInventory(org.bukkit.inventory.InventoryHolder owner, int size, String title) {
                return inventory;
            }
        };

        when(player.getUniqueId()).thenReturn(playerId);
        when(api.getPlayersCurrentGui(player)).thenReturn(null);
        when(api.getPlayers()).thenReturn(players);
        gui.setClosed(true);

        try (MockedStatic<InventoryAPI> inventoryApi = mockStatic(InventoryAPI.class)) {
            inventoryApi.when(InventoryAPI::getInstance).thenReturn(api);
            gui.open();
        }

        assertFalse(gui.isClosed());
        verify(player).openInventory(inventory);
    }
}
