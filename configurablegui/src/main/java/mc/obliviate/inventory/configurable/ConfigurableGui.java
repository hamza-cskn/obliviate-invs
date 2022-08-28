package mc.obliviate.inventory.configurable;

import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.configurable.util.GuiSerializer;
import mc.obliviate.util.placeholder.PlaceholderUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConfigurableGui extends Gui {

    private final ConfigurableGuiCache guiCache = ConfigurableGuiCache.getCache(getId());
    private final GuiConfigurationTable guiConfigurationTable;

    public ConfigurableGui(@Nonnull Player player, @Nonnull String id) {
        this(player, id, GuiConfigurationTable.getDefaultConfigurationTable());
    }

    public ConfigurableGui(@Nonnull Player player, @Nonnull String id, @Nonnull GuiConfigurationTable guiConfigurationTable) {
        super(player, id, "No title found", 0);
        this.guiConfigurationTable = guiConfigurationTable;
        setTitle(guiConfigurationTable.getMenusSection(getSectionPath()).getString(guiConfigurationTable.getTitleSectionName(), "No Title Found"));
        setSize(guiConfigurationTable.getMenusSection(getSectionPath()).getInt(guiConfigurationTable.getSizeSectionName(), 0) * 9);
    }

    public ConfigurableGuiCache getGuiCache() {
        return guiCache;
    }

    public GuiConfigurationTable getGuiConfigurationTable() {
        return guiConfigurationTable;
    }

    public String getSectionPath() {
        return getId();
    }

    public ConfigurationSection getSection() {
        return guiConfigurationTable.getMenusSection(getSectionPath());
    }

    public String getIconsSectionPath() {
        return getSectionPath() + "." + guiConfigurationTable.getIconsSectionName();
    }

    public ConfigurationSection getIconsSection(@Nonnull String sectionName) {
        return guiConfigurationTable.getMenusSection(getIconsSectionPath() + "." + sectionName);
    }

    public int getConfigSlot(@Nonnull String sectionName) {
        return guiCache.getConfigSlot(getIconsSection(sectionName), guiConfigurationTable);
    }

    public ItemStack getConfigItem(@Nonnull String sectionName) {
        return guiCache.getConfigItem((getIconsSection(sectionName)), guiConfigurationTable);
    }

    public ItemStack getConfigItem(@Nonnull String sectionName, @Nullable PlaceholderUtil placeholderUtil) {
        return guiCache.getConfigItem(getIconsSection(sectionName), placeholderUtil, guiConfigurationTable);
    }

    public void putDysfunctionalIcons() {
        GuiSerializer.putDysfunctionalIcons(this, guiConfigurationTable, guiConfigurationTable.getMenusSection(getIconsSectionPath()), null, new ArrayList<>());
    }

    public void putDysfunctionalIcons(@Nullable PlaceholderUtil placeholderUtil) {
        GuiSerializer.putDysfunctionalIcons(this, guiConfigurationTable, guiConfigurationTable.getMenusSection(getIconsSectionPath()), placeholderUtil, new ArrayList<>());
    }

    public void putDysfunctionalIcons(@Nonnull List<String> functionalSlots) {
        GuiSerializer.putDysfunctionalIcons(this, guiConfigurationTable, guiConfigurationTable.getMenusSection(getIconsSectionPath()), null, functionalSlots);
    }

    public void putDysfunctionalIcons(@Nullable PlaceholderUtil placeholderUtil, @Nonnull List<String> functionalSlots) {
        GuiSerializer.putDysfunctionalIcons(this, guiConfigurationTable, guiConfigurationTable.getMenusSection(getIconsSectionPath()), placeholderUtil, functionalSlots);
    }

    public void putIcon(@Nonnull String configName, @Nonnull Consumer<InventoryClickEvent> click) {
        addItem(getConfigSlot(configName), new Icon(getConfigItem(configName)).onClick(click));
    }

    public void putIcon(@Nonnull String configName, @Nullable PlaceholderUtil placeholderUtil, @Nonnull Consumer<InventoryClickEvent> click) {
        addItem(getConfigSlot(configName), new Icon(getConfigItem(configName, placeholderUtil)).onClick(click));
    }
}