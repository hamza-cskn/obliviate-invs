package mc.obliviate.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class InventoryAPI<P extends JavaPlugin> {

	private static InventoryAPI instance;
	private final P plugin;
	private final HashMap<UUID, Gui> players = new HashMap<>();
	private final Listener listener = new InvListeners(this);
	private boolean initialized = false;

	public InventoryAPI(P plugin) {
		this.plugin = plugin;
		instance = this;
	}

	public static InventoryAPI getInstance() {
		return instance;
	}

	/**
	 * Registers listeners of the Inventory API
	 */
	public void init() {
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
		initialized = true;
	}

	public P getPlugin() {
		return plugin;
	}

	public HashMap<UUID, Gui> getPlayers() {
		return players;
	}

	public Gui getPlayersCurrentGui(Player player) {
		if (!initialized) throw new IllegalStateException("Inventory API instance created but is not initialized! Please use init() method to init.");
		return players.get(player.getUniqueId());
	}

	public Listener getListener() {
		return listener;
	}
}
