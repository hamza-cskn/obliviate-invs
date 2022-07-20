package mc.obliviate.inventory.configurable;

import org.bukkit.configuration.ConfigurationSection;

public class GuiConfigurationTable {

	protected static GuiConfigurationTable defaultConfigurationTable;

	private String titleSectionName = "title";
	private String sizeSectionName = "row";
	private String iconsSectionName = "icons";

	private String materialSectionName = "material";
	private String amountSectionName = "amount";
	private String displayNameSectionName = "display-name";
	private String slotSectionName = "slot";
	private String durabilitySectionName = "durability";
	private String unbreakableSectionName = "unbreakable";
	private String loreSectionName = "lore";
	private String itemFlagsSectionName = "item-flags";
	private String enchantmentsSectionName = "enchantments";
	private String customModelDataSectionName = "custom-model-data";

	private final ConfigurationSection menuConfiguration;

	public GuiConfigurationTable(ConfigurationSection menuConfiguration) {
		this.menuConfiguration = menuConfiguration;
	}

	public static GuiConfigurationTable getDefaultConfigurationTable() {
		return defaultConfigurationTable;
	}

	public static void setDefaultConfigurationTable(GuiConfigurationTable defaultConfigurationTable) {
		GuiConfigurationTable.defaultConfigurationTable = defaultConfigurationTable;
	}

	public ConfigurationSection getMenusSection(String section) {
		return menuConfiguration.getConfigurationSection(section);
	}

	public String getTitleSectionName() {
		return titleSectionName;
	}

	public void setTitleSectionName(String titleSectionName) {
		this.titleSectionName = titleSectionName;
	}

	public String getSizeSectionName() {
		return sizeSectionName;
	}

	public void setSizeSectionName(String sizeSectionName) {
		this.sizeSectionName = sizeSectionName;
	}

	public String getIconsSectionName() {
		return iconsSectionName;
	}

	public void setIconsSectionName(String iconsSectionName) {
		this.iconsSectionName = iconsSectionName;
	}

	public String getMaterialSectionName() {
		return materialSectionName;
	}

	public void setMaterialSectionName(String materialSectionName) {
		this.materialSectionName = materialSectionName;
	}

	public String getAmountSectionName() {
		return amountSectionName;
	}

	public void setAmountSectionName(String amountSectionName) {
		this.amountSectionName = amountSectionName;
	}

	public String getDisplayNameSectionName() {
		return displayNameSectionName;
	}

	public void setDisplayNameSectionName(String displayNameSectionName) {
		this.displayNameSectionName = displayNameSectionName;
	}

	public String getLoreSectionName() {
		return loreSectionName;
	}

	public void setLoreSectionName(String loreSectionName) {
		this.loreSectionName = loreSectionName;
	}

	public String getItemFlagsSectionName() {
		return itemFlagsSectionName;
	}

	public void setItemFlagsSectionName(String itemFlagsSectionName) {
		this.itemFlagsSectionName = itemFlagsSectionName;
	}

	public String getEnchantmentsSectionName() {
		return enchantmentsSectionName;
	}

	public void setEnchantmentsSectionName(String enchantmentsSectionName) {
		this.enchantmentsSectionName = enchantmentsSectionName;
	}

	public String getCustomModelDataSectionName() {
		return customModelDataSectionName;
	}

	public void setCustomModelDataSectionName(String customModelDataSectionName) {
		this.customModelDataSectionName = customModelDataSectionName;
	}

	public ConfigurationSection getMenuConfiguration() {
		return menuConfiguration;
	}

	public String getSlotSectionName() {
		return slotSectionName;
	}

	public void setSlotSectionName(String slotSectionName) {
		this.slotSectionName = slotSectionName;
	}

	public String getUnbreakableSectionName() {
		return unbreakableSectionName;
	}

	public void setUnbreakableSectionName(String unbreakableSectionName) {
		this.unbreakableSectionName = unbreakableSectionName;
	}

	public String getDurabilitySectionName() {
		return durabilitySectionName;
	}

	public void setDurabilitySectionName(String durabilitySectionName) {
		this.durabilitySectionName = durabilitySectionName;
	}
}
