package mc.obliviate.inventory.configurable;

import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.configurable.util.GuiSerializer;
import mc.obliviate.util.placeholder.PlaceholderUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConfigurableGui extends Gui {

    private final ConfigurableGuiCache guiCache = ConfigurableGuiCache.getCache(getId());
    private final GuiConfigurationTable guiConfigurationTable;

    public ConfigurableGui(Player player, String id) {
        this(player, id, GuiConfigurationTable.getDefaultConfigurationTable());
    }

    public ConfigurableGui(Player player, String id, GuiConfigurationTable guiConfigurationTable) {
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

    public ConfigurationSection getIconsSection(String sectionName) {
        return guiConfigurationTable.getMenusSection(getIconsSectionPath() + "." + sectionName);
    }

    public int getConfigSlot(String sectionName) {
        return guiCache.getConfigSlot(getIconsSection(sectionName), guiConfigurationTable);
    }

    public ItemStack getConfigItem(String sectionName) {
        return guiCache.getConfigItem((getIconsSection(sectionName)), guiConfigurationTable);
    }

    public ItemStack getConfigItem(String sectionName, PlaceholderUtil placeholderUtil) {
        return guiCache.getConfigItem(getIconsSection(sectionName), placeholderUtil, guiConfigurationTable);
    }

    public void putDysfunctionalIcons() {
        GuiSerializer.putDysfunctionalIcons(this, guiConfigurationTable, guiConfigurationTable.getMenusSection(getIconsSectionPath()), null, new ArrayList<>());
    }

    public void putDysfunctionalIcons(PlaceholderUtil placeholderUtil) {
        GuiSerializer.putDysfunctionalIcons(this, guiConfigurationTable, guiConfigurationTable.getMenusSection(getIconsSectionPath()), placeholderUtil, new ArrayList<>());
    }

    public void putDysfunctionalIcons(List<String> functionalSlots) {
        GuiSerializer.putDysfunctionalIcons(this, guiConfigurationTable, guiConfigurationTable.getMenusSection(getIconsSectionPath()), null, functionalSlots);
    }

    public void putDysfunctionalIcons(PlaceholderUtil placeholderUtil, List<String> functionalSlots) {
        GuiSerializer.putDysfunctionalIcons(this, guiConfigurationTable, guiConfigurationTable.getMenusSection(getIconsSectionPath()), placeholderUtil, functionalSlots);
    }

    public void putIcon(String configName, Consumer<InventoryClickEvent> click) {
        addItem(getConfigSlot(configName), new Icon(getConfigItem(configName)).onClick(click));
    }

    public void putIcon(String configName, PlaceholderUtil placeholderUtil, Consumer<InventoryClickEvent> click) {
        addItem(getConfigSlot(configName), new Icon(getConfigItem(configName, placeholderUtil)).onClick(click));
    }
}