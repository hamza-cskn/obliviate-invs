package mc.obliviate.inventory.guiserializer;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;
import mc.obliviate.inventory.GUI;
import mc.obliviate.inventory.Icon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class YAMLSerializer {

	public static void serializeItemStack(final ConfigurationSection serializedSection, final int slot, final Icon item) {
		final ItemMeta meta = item.getItem().getItemMeta();

		setSafe(serializedSection, "type", item.getItem().getType());
		setSafe(serializedSection, "data", item.getItem().getDurability());
		setSafe(serializedSection, "slot", slot);
		if (meta != null) {
			setSafe(serializedSection, "display-name", meta.getDisplayName().replace("§","&"));
			if (meta.getLore() != null && meta.getLore().size() > 0) {
				final List<String> list = new ArrayList<>();
				meta.getLore().forEach(line -> list.add(line.replace("§", "&")));
				setSafe(serializedSection, "lore", list);
			}
			setSafe(serializedSection, "custom-model-data", meta.getCustomModelData());
			setSafe(serializedSection, "item-flags", Collections.singletonList(meta.getItemFlags()));
		}
		setSafe(serializedSection, "amount", item.getItem().getAmount());

		for (final Map.Entry<Enchantment, Integer> enchant : item.getItem().getEnchantments().entrySet()) {
			setSafe(serializedSection, "enchantments." + enchant.getKey(), enchant.getValue());
		}

	}

	private static void setSafe(ConfigurationSection section, String key, Object value) {
		if (value != null) {
			if (value instanceof List && ((List) value).size() == 0) {
				return;
			}
			section.set(key, value);
		}
	}

}

