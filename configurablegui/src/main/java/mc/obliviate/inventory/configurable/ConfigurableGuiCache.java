package mc.obliviate.inventory.configurable;

import mc.obliviate.inventory.configurable.placeholder.PlaceholderUtil;
import mc.obliviate.inventory.configurable.util.ItemStackSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class ConfigurableGuiCache {

	private static final HashMap<String, ConfigurableGuiCache> CONFIGURABLE_GUI_CACHES = new HashMap<>();
	private final HashMap<String, ItemStack> itemStackCache = new HashMap<>();

	public static ConfigurableGuiCache getCache(String guiId) {
		ConfigurableGuiCache cache = CONFIGURABLE_GUI_CACHES.get(guiId);
		if (cache != null) return cache;

		ConfigurableGuiCache newCache = new ConfigurableGuiCache();
		CONFIGURABLE_GUI_CACHES.put(guiId, newCache);
		return newCache;
	}

	public static void resetCaches() {
		for (ConfigurableGuiCache cache : CONFIGURABLE_GUI_CACHES.values()) {
			cache.itemStackCache.clear();
		}
	}

	private ConfigurableGuiCache() {

	}

	public ItemStack getConfigItem(ConfigurationSection section, GuiConfigurationTable table) {
		return getConfigItem(section, null, table);
	}

	public ItemStack getConfigItem(ConfigurationSection section, PlaceholderUtil placeholderUtil, GuiConfigurationTable table) {
		ItemStack item = findItemStack(section, table).clone();
		ItemStackSerializer.applyPlaceholdersToItemStack(item, placeholderUtil);
		return item;
	}

	public int getConfigSlot(ConfigurationSection section, GuiConfigurationTable table) {
		return section.getInt(table.getSlotSectionName());
	}

	private ItemStack findItemStack(ConfigurationSection section, GuiConfigurationTable table) {
		ItemStack item = itemStackCache.get(section.getName());
		if (item != null) return item;

		ItemStack serializedItemStack = ItemStackSerializer.deserializeItemStack(section, table);
		itemStackCache.put(section.getName(), serializedItemStack);
		return serializedItemStack;
	}
}
