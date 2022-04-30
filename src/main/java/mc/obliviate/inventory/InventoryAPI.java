package mc.obliviate.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class InventoryAPI {

	private static InventoryAPI instance;
	private final Plugin plugin;
	private final HashMap<UUID, Gui> players = new HashMap<>();
	private final Listener listener = new InvListeners(this);
	private boolean inited = false;

	public InventoryAPI(Plugin plugin) {
		this.plugin = plugin;
		instance = this;
	}

	public static InventoryAPI getInstance() {
		return instance;
	}

	public void init() {
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
		inited = true;
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public HashMap<UUID, Gui> getPlayers() {
		return players;
	}

	public Gui getPlayersCurrentGui(Player player) {
		if (!inited) throw new IllegalStateException("Inventory API instance created but is not inited! Please use init() method to init.");
		return players.get(player.getUniqueId());
	}

	public Listener getListener() {
		return listener;
	}
}
