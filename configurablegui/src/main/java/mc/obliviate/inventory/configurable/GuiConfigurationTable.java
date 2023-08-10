package mc.obliviate.inventory.configurable;

import com.google.common.base.Preconditions;
import mc.obliviate.util.versiondetection.ServerVersionController;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GuiConfigurationTable {

    protected static GuiConfigurationTable defaultConfigurationTable = new GuiConfigurationTable(null);

    private String titleSectionName = "title";
    private String sizeSectionName = "row";
    private String iconsSectionName = "icons";

    private String materialSectionName = "material";
    private String loreSectionName = "lore";
    private String displayNameSectionName = "display-name";
    private String amountSectionName = "amount";
    private String slotSectionName = "slot";
    private String glowSectionName = "glow";
    private String durabilitySectionName = "durability";
    private String unbreakableSectionName = "unbreakable";
    private String itemFlagsSectionName = "item-flags";
    private String enchantmentsSectionName = "enchantments";
    private String customModelDataSectionName = "custom-model-data";

    private final ConfigurationSection menuConfiguration;

    public GuiConfigurationTable(@Nullable ConfigurationSection menuConfiguration) {
        this.menuConfiguration = menuConfiguration;
        ServerVersionController.calculateServerVersion(Bukkit.getServer());
    }

    public static GuiConfigurationTable getDefaultConfigurationTable() {
        return defaultConfigurationTable;
    }

    public static void setDefaultConfigurationTable(@Nonnull GuiConfigurationTable defaultConfigurationTable) {
        GuiConfigurationTable.defaultConfigurationTable = defaultConfigurationTable;
    }

    public ConfigurationSection getMenusSection(@Nonnull String section) {
        Preconditions.checkNotNull(this.menuConfiguration, "No GUI configuration specified. If you're the developer, visit Wiki of obliviate-invs.");
        return this.menuConfiguration.getConfigurationSection(section);
    }

    public String getTitleSectionName() {
        return this.titleSectionName;
    }

    public void setTitleSectionName(@Nonnull String titleSectionName) {
        this.titleSectionName = titleSectionName;
    }

    public String getSizeSectionName() {
        return this.sizeSectionName;
    }

    public void setSizeSectionName(@Nonnull String sizeSectionName) {
        this.sizeSectionName = sizeSectionName;
    }

    public String getIconsSectionName() {
        return this.iconsSectionName;
    }

    public void setIconsSectionName(@Nonnull String iconsSectionName) {
        this.iconsSectionName = iconsSectionName;
    }

    public String getMaterialSectionName() {
        return this.materialSectionName;
    }

    public void setMaterialSectionName(@Nonnull String materialSectionName) {
        this.materialSectionName = materialSectionName;
    }

    public String getAmountSectionName() {
        return this.amountSectionName;
    }

    public void setAmountSectionName(@Nonnull String amountSectionName) {
        this.amountSectionName = amountSectionName;
    }

    public String getDisplayNameSectionName() {
        return this.displayNameSectionName;
    }

    public void setDisplayNameSectionName(@Nonnull String displayNameSectionName) {
        this.displayNameSectionName = displayNameSectionName;
    }

    public String getLoreSectionName() {
        return this.loreSectionName;
    }

    public void setLoreSectionName(@Nonnull String loreSectionName) {
        this.loreSectionName = loreSectionName;
    }

    public String getItemFlagsSectionName() {
        return this.itemFlagsSectionName;
    }

    public void setItemFlagsSectionName(@Nonnull String itemFlagsSectionName) {
        this.itemFlagsSectionName = itemFlagsSectionName;
    }

    public String getEnchantmentsSectionName() {
        return this.enchantmentsSectionName;
    }

    public void setEnchantmentsSectionName(@Nonnull String enchantmentsSectionName) {
        this.enchantmentsSectionName = enchantmentsSectionName;
    }

    public String getCustomModelDataSectionName() {
        return this.customModelDataSectionName;
    }

    public void setCustomModelDataSectionName(@Nonnull String customModelDataSectionName) {
        this.customModelDataSectionName = customModelDataSectionName;
    }

    public ConfigurationSection getMenuConfiguration() {
        return this.menuConfiguration;
    }

    public String getSlotSectionName() {
        return this.slotSectionName;
    }

    public void setSlotSectionName(@Nonnull String slotSectionName) {
        this.slotSectionName = slotSectionName;
    }

    public String getUnbreakableSectionName() {
        return this.unbreakableSectionName;
    }

    public void setUnbreakableSectionName(@Nonnull String unbreakableSectionName) {
        this.unbreakableSectionName = unbreakableSectionName;
    }

    public String getDurabilitySectionName() {
        return this.durabilitySectionName;
    }

    public void setDurabilitySectionName(@Nonnull String durabilitySectionName) {
        this.durabilitySectionName = durabilitySectionName;
    }

    public String getGlowSectionName() {
        return this.glowSectionName;
    }

    public void setGlowSectionName(@Nonnull String glowSectionName) {
        this.glowSectionName = glowSectionName;
    }
}
