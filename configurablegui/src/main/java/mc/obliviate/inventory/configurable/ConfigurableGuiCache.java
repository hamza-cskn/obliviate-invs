package mc.obliviate.inventory.configurable;

import mc.obliviate.inventory.configurable.util.ItemStackSerializer;
import mc.obliviate.util.placeholder.PlaceholderUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class ConfigurableGuiCache {

    private static final HashMap<String, ConfigurableGuiCache> CONFIGURABLE_GUI_CACHES = new HashMap<>();
    private final HashMap<String, ItemStack> itemStackCache = new HashMap<>();

    public static ConfigurableGuiCache getCache(String guiId) {
        ConfigurableGuiCache cache = ConfigurableGuiCache.CONFIGURABLE_GUI_CACHES.get(guiId);
        if (cache != null) return cache;

        ConfigurableGuiCache newCache = new ConfigurableGuiCache();
        ConfigurableGuiCache.CONFIGURABLE_GUI_CACHES.put(guiId, newCache);
        return newCache;
    }

    public static void resetCaches() {
        for (ConfigurableGuiCache cache : ConfigurableGuiCache.CONFIGURABLE_GUI_CACHES.values()) {
            cache.itemStackCache.clear();
        }
    }

    private ConfigurableGuiCache() {

    }

    public ItemStack getConfigItem(@Nonnull ConfigurationSection section, @Nonnull GuiConfigurationTable table) {
        return this.getConfigItem(section, null, table);
    }

    public ItemStack getConfigItem(@Nonnull ConfigurationSection section, PlaceholderUtil placeholderUtil, @Nonnull GuiConfigurationTable table) {
        ItemStack item = this.findItemStack(section, table).clone();
        ItemStackSerializer.applyPlaceholdersToItemStack(item, placeholderUtil);
        return item;
    }

    public int getConfigSlot(@Nonnull ConfigurationSection section, @Nonnull GuiConfigurationTable table) {
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
