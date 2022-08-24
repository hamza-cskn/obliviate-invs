package mc.obliviate.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.UUID;

public class InventoryAPI {

	private final JavaPlugin plugin;
	private static InventoryAPI instance;
	private final HashMap<UUID, Gui> players = new HashMap<>();
	private final Listener listener = new InvListener(this);
	private boolean initialized = false;

	public InventoryAPI(JavaPlugin plugin) {
		if (plugin == null) throw new IllegalArgumentException("Java plugin cannot be null!");
		this.plugin = plugin;
		InventoryAPI.instance = this;
	}

	public static InventoryAPI getInstance() {
		return InventoryAPI.instance;
	}

	public void init() {
		this.plugin.getServer().getPluginManager().registerEvents(this.listener, this.plugin);
		this.initialized = true;
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
		for (Gui gui : this.players.values()) {
			if (gui.getInventory().equals(inventory)) {
				return gui;
			}
		}
		return null;
	}

	public JavaPlugin getPlugin() {
		return this.plugin;
	}

	public Listener getListener() {
		return this.listener;
	}

}
