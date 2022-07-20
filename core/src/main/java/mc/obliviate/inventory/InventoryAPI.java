package mc.obliviate.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

public class InventoryAPI {

	private final JavaPlugin plugin;
	private static InventoryAPI instance;
	private final HashMap<UUID, Gui> players = new HashMap<>();
	private final Listener listener = new InvListener(this);
	private boolean initialized = false;

	@Contract("null -> fail")
	public InventoryAPI(JavaPlugin plugin) {
		if (plugin == null) throw new IllegalArgumentException("Java plugin cannot be null!");
		this.plugin = plugin;
		instance = this;
	}

	public static InventoryAPI getInstance() {
		return instance;
	}

	public void init() {
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
		initialized = true;
	}

	@NotNull
	public HashMap<UUID, Gui> getPlayers() {
		return players;
	}

	@Nullable
	@Contract("null -> null")
	public Gui getPlayersCurrentGui(Player player) {
		if (player == null) return null;
		if (!initialized)
			throw new IllegalStateException("Inventory API instance created but is not initialized! Please use init() method to init.");
		return players.get(player.getUniqueId());
	}

	public JavaPlugin getPlugin() {
		return plugin;
	}

	public Listener getListener() {
		return listener;
	}

}
