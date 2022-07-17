package mc.obliviate.inventory.configurable.util;

import mc.obliviate.inventory.configurable.ConfigurableGui;
import mc.obliviate.inventory.configurable.GuiConfigurationTable;
import mc.obliviate.inventory.configurable.placeholder.PlaceholderUtil;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class GuiSerializer {

	public static void putDysfunctionalIcons(ConfigurableGui gui, GuiConfigurationTable table, ConfigurationSection iconsSection, PlaceholderUtil placeholderUtil, List<String> functionalSlots) {
		if (gui == null) throw new IllegalArgumentException("dysfunctional icons could not put because gui was null!");
		if (iconsSection == null) throw new IllegalArgumentException("null configuration section given!");
		for (String sectionName : iconsSection.getKeys(false)) {
			final ConfigurationSection section = iconsSection.getConfigurationSection(sectionName);

			if (functionalSlots.contains(sectionName)) continue;
			if (!section.isSet(table.getSlotSectionName())) continue;
			if (!section.isSet(table.getMaterialSectionName())) continue;

			final int slotNo = section.getInt(table.getSlotSectionName(), -1);
			if (slotNo != -1) {
				gui.addItem(slotNo, gui.getGuiCache().getConfigItem(iconsSection.getConfigurationSection(sectionName), placeholderUtil, table));
				continue;
			}

			final String slotString = section.getString(table.getSlotSectionName(), "");
			if (slotString.contains("-")) {
				final String[] slots = slotString.split("-");
				if (slots.length != 2) continue;
				int from, to;
				try {
					from = Integer.parseInt(slots[0]);
					to = Integer.parseInt(slots[1]);
				} catch (NumberFormatException ignore) {
					continue;
				}
				if (from > to) continue;
				for (; from <= to; from++) {
					gui.addItem(from, gui.getGuiCache().getConfigItem(iconsSection.getConfigurationSection(sectionName), placeholderUtil, table));
				}
			} else if (slotString.contains(",")) {
				final String[] slots = slotString.split(",");
				if (slots.length < 2) continue;

				for (final String slotText : slots) {
					try {
						gui.addItem(Integer.parseInt(slotText), gui.getGuiCache().getConfigItem(iconsSection.getConfigurationSection(sectionName), placeholderUtil, table));
					} catch (NumberFormatException ignore) {
					}
				}
			}
		}
	}

}
