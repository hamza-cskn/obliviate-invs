package mc.obliviate.inventory.configurable.util;

import com.google.common.base.Preconditions;
import mc.obliviate.inventory.configurable.GuiConfigurationTable;
import mc.obliviate.inventory.configurable.placeholder.PlaceholderUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ItemStackSerializer {

	@NotNull
	public static ItemStack deserializeMaterial(@NotNull ConfigurationSection section, GuiConfigurationTable table) {
		final String materialName = section.getString(table.getMaterialSectionName());
		if (materialName == null) throw new IllegalArgumentException("material section could not find");

		final Optional<XMaterial> xmaterial = XMaterial.matchXMaterial(materialName);
		if (!xmaterial.isPresent()) {
			throw new IllegalArgumentException("Material could not found: " + materialName);
		}

		final ItemStack item = xmaterial.get().parseItem();
		if (item == null) {
			throw new IllegalArgumentException("Material could not parsed as itemstack: " + materialName);
		}
		return item;
	}

	@NotNull
	public static ItemStack deserializeItemStack(@NotNull ConfigurationSection section, GuiConfigurationTable table) {
		final ItemStack item = deserializeMaterial(section, table);
		final ItemMeta meta = item.getItemMeta();
		Preconditions.checkNotNull(meta, "item meta cannot be null");

		meta.setDisplayName(StringUtils.parseColor(section.getString(table.getDisplayNameSectionName())));
		meta.setLore(StringUtils.parseColor(section.getStringList(table.getLoreSectionName())));
		item.setItemMeta(meta);
		applyEnchantmentsToItemStack(item, deserializeEnchantments(section, table));

		if (section.isSet(table.getCustomModelDataSectionName()))
			meta.setCustomModelData(section.getInt(table.getCustomModelDataSectionName()));

		if (section.getBoolean(table.getUnbreakableSectionName()))
			meta.setUnbreakable(true);

		applyItemFlagsToItemStacks(item, deserializeItemFlags(section, table));
		item.setAmount(section.getInt(table.getAmountSectionName(), 1));


		return item;
	}

	public static void applyItemFlagsToItemStacks(@NotNull ItemStack item, ItemFlag[] itemFlags) {
		ItemMeta meta = item.getItemMeta();
		Preconditions.checkNotNull(meta, "item meta cannot be null");

		if (itemFlags.length == 0) return;

		for (ItemFlag itemFlag : itemFlags) {
			if (itemFlag == null) continue;
			meta.addItemFlags(itemFlag);
		}
		item.setItemMeta(meta);
	}

	public static ItemFlag[] deserializeItemFlags(@NotNull ConfigurationSection section, GuiConfigurationTable table) {
		ItemFlag[] itemFlags = new ItemFlag[ItemFlag.values().length];

		List<String> serializedItemFlags = section.getStringList(table.getItemFlagsSectionName());
		if (serializedItemFlags.isEmpty()) return itemFlags;
		if (serializedItemFlags.contains("*")) return ItemFlag.values();

		int index = 0;
		for (String serializedItemFlag : serializedItemFlags) {
			try {
				ItemFlag itemFlag = ItemFlag.valueOf(serializedItemFlag);
				Preconditions.checkNotNull(itemFlag);
				itemFlags[index++] = itemFlag;
			} catch (Exception e) {
				throw new IllegalArgumentException("item flag could not find: " + serializedItemFlag);
			}
		}

		return itemFlags;
	}

	public static void applyEnchantmentsToItemStack(ItemStack item, Map<Enchantment, Integer> enchantments) {
		if (enchantments.isEmpty()) return;
		if (item.getType().equals(XMaterial.ENCHANTED_BOOK.parseMaterial())) {
			final EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
			if (meta == null) return;

			for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
				meta.addStoredEnchant(entry.getKey(), entry.getValue(), true);
			}

			item.setItemMeta(meta);
		} else {
			item.addUnsafeEnchantments(enchantments);
		}
	}

	private static Map<Enchantment, Integer> deserializeEnchantments(@NotNull ConfigurationSection section, GuiConfigurationTable table) {
		if (!section.isSet(table.getEnchantmentsSectionName())) return new HashMap<>();
		Map<Enchantment, Integer> map = new HashMap<>();
		for (final String serializedEnchantment : section.getStringList(table.getEnchantmentsSectionName())) {
			final Pair<Enchantment, Integer> enchantmentValue = deserializeEnchantment(serializedEnchantment);
			map.put(enchantmentValue.key, enchantmentValue.value);
		}
		return map;
	}

	private static Pair<Enchantment, Integer> deserializeEnchantment(String serializedEnchantment) {
		Preconditions.checkNotNull(serializedEnchantment, "serialized enchantment cannot be null");
		String[] datas = serializedEnchantment.split(":");
		Preconditions.checkArgument(datas.length == 2, "Enchantment could not deserialized: " + serializedEnchantment);
		Enchantment enchantment;
		int value;
		try {
			enchantment = Enchantment.getByName(datas[0]);
			value = Integer.parseInt(datas[1]);
		} catch (Exception e) {
			throw new IllegalArgumentException("Enchantment or its Value could not resolved: " + datas[0]);
		}
		Preconditions.checkArgument(enchantment != null, "Enchantment could not find: " + datas[0]);
		return new Pair<>(enchantment, value);
	}

	public static void applyPlaceholdersToItemStack(ItemStack item, PlaceholderUtil placeholderUtil) {
		if (item == null) return;
		final ItemMeta meta = item.getItemMeta();
		Preconditions.checkNotNull(meta, "item meta cannot be null");
		meta.setDisplayName(StringUtils.applyPlaceholders(meta.getDisplayName(), placeholderUtil));
		meta.setLore(StringUtils.applyPlaceholders(meta.getLore(), placeholderUtil));
		item.setItemMeta(meta);
	}

	private static class Pair<K, V> {

		private final K key;
		private final V value;

		private Pair(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}

}
