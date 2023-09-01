package mc.obliviate.inventory;

import com.github.Anon8281.universalScheduler.UniversalScheduler;
import com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.UUID;

public class InventoryAPI {

    private static InventoryAPI instance;
    private final JavaPlugin plugin;
    private final HashMap<UUID, Gui> players = new HashMap<>();
    private final Listener listener = new InvListener(this);
    private boolean initialized = false;

    public InventoryAPI(JavaPlugin plugin) {
        if (plugin == null) throw new IllegalArgumentException("Java plugin cannot be null!");
        this.plugin = plugin;
        InventoryAPI.instance = this;
    }

    public void init() {
        this.plugin.getServer().getPluginManager().registerEvents(this.listener, this.plugin);
        this.initialized = true;
    }

    /**
     * Unloads the Inventory API.
     * In natural statement, you don't need to implement/call this method.
     */
    public void unload() {
        InventoryAPI.getScheduler().cancelTasks();
        HandlerList.unregisterAll(this.listener);
    }

    @Nonnull
    public HashMap<UUID, Gui> getPlayers() {
        return players;
    }

    @Nullable
    public Gui getPlayersCurrentGui(final Player player) {
        if (player == null) return null;
        if (!this.initialized)
            throw new IllegalStateException("Inventory API instance created but is not initialized! Please use init() method to init.");
        return this.players.get(player.getUniqueId());
    }

    @Nullable
    public Gui getGuiFromInventory(final Inventory inventory) {
        return this.players.values().stream()
                .filter(gui -> gui.getInventory().equals(inventory))
                .findFirst().orElse(null);
    }

    public static TaskScheduler getScheduler() {
        return UniversalScheduler.getScheduler(InventoryAPI.getInstance().getPlugin());
    }

    public static InventoryAPI getInstance() {
        return InventoryAPI.instance;
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    public Listener getListener() {
        return this.listener;
    }

}
