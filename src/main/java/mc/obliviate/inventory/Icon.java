package mc.obliviate.inventory;

import mc.obliviate.inventory.action.DragAction;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import mc.obliviate.inventory.action.ClickAction;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Icon {

	private final ItemStack item;
	private ClickAction clickAction;
	private DragAction dragAction;

	public Icon(final ItemStack item) {
		this.item = item;
		this.dragAction = event -> {
		};
		this.clickAction = event -> {
		};
	}

	public Icon(final Material material) {
		this.clickAction = event -> {
		};
		this.dragAction = event -> {
		};
		this.item = new ItemStack(material);
	}


	@NotNull
	@Contract("_ -> this")
	public Icon setDamage(final short dmg) {
		item.setDurability(dmg);
		return this;
	}

	@NotNull
	@Contract("_ -> this")
	public Icon setDamage(final int dmg) {
		setDamage((short) dmg);
		return this;
	}

	@NotNull
	@Contract("_ -> this")
	public Icon setName(final String name) {
		final ItemMeta meta = item.getItemMeta();
		if (meta == null) return this;
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return this;
	}

	@NotNull
	@Contract("_ -> this")
	public Icon setLore(final List<String> lore) {
		final ItemMeta meta = item.getItemMeta();
		if (meta == null) return this;
		meta.setLore(lore);
		item.setItemMeta(meta);
		return this;
	}

	@NotNull
	@Contract("_ -> this")
	public Icon setLore(final String... lore) {
		return setLore(new ArrayList<>(Arrays.asList(lore)));
	}

	@NotNull
	@Contract("_ -> this")
	public Icon appendLore(final List<String> strings) {
		final ItemMeta meta = item.getItemMeta();
		if (meta == null) return this;
		List<String> lore = meta.getLore();
		if (lore != null) lore.addAll(strings);
		else lore = strings;
		return setLore(lore);
	}

	@NotNull
	@Contract("_ -> this")
	public Icon appendLore(final String... strings) {
		return appendLore(new ArrayList<>(Arrays.asList(strings)));
	}

	@NotNull
	@Contract("_,_ -> this")
	public Icon insertLore(final int index, final String... strings) {
		return insertLore(index, new ArrayList<>(Arrays.asList(strings)));
	}

	@NotNull
	@Contract("_,_ -> this")
	public Icon insertLore(final int index, final List<String> strings) {
		final ItemMeta meta = item.getItemMeta();
		if (meta == null) return this;
		List<String> lore = meta.getLore();
		if (lore != null) lore.addAll(index, strings);
		else lore = strings;
		return setLore(lore);
	}

	@NotNull
	@Contract("_ -> this")
	public Icon setAmount(final int amount) {
		item.setAmount(amount);
		return this;
	}

	@NotNull
	@Contract("_ -> this")
	public Icon hideFlags(final ItemFlag itemFlag) {
		final ItemMeta meta = item.getItemMeta();
		if (meta == null) return this;
		meta.addItemFlags(itemFlag);
		item.setItemMeta(meta);
		return this;
	}

	@NotNull
	@Contract("-> this")
	public Icon hideFlags() {
		hideFlags(ItemFlag.HIDE_ATTRIBUTES)
				.hideFlags(ItemFlag.HIDE_DESTROYS)
				.hideFlags(ItemFlag.HIDE_ENCHANTS)
				.hideFlags(ItemFlag.HIDE_PLACED_ON)
				.hideFlags(ItemFlag.HIDE_POTION_EFFECTS)
				.hideFlags(ItemFlag.HIDE_UNBREAKABLE);
		//hideFlags(DYE) not exists because 1.8 servers does not support.
		return this;
	}

	@NotNull
	@Contract("_ -> this")
	public Icon enchant(final Enchantment enchantment) {
		return enchant(enchantment, enchantment.getStartLevel());
	}

	@NotNull
	@Contract("_ -> this")
	public Icon enchant(final Map<Enchantment, Integer> enchantments) {
		for (Map.Entry<Enchantment, Integer> enchant : enchantments.entrySet()) {
			enchant(enchant.getKey(), enchant.getValue());
		}
		return this;
	}

	@NotNull
	@Contract("_,_ -> this")
	public Icon enchant(final Enchantment enchantment, final int value) {
		if (item.getType().equals(Material.ENCHANTED_BOOK)) {
			final EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();

			if (meta == null) return this;

			meta.addStoredEnchant(enchantment, value, true);

			item.setItemMeta(meta);
		} else {
			item.addUnsafeEnchantment(enchantment, value);
		}
		return this;
	}

	@NotNull
	public ClickAction getClickAction() {
		return clickAction;
	}

	@NotNull
	@Contract("_ -> this")
	public Icon onClick(ClickAction clickAction) {
		this.clickAction = clickAction;
		return this;
	}

	@NotNull
	public DragAction getDragAction() {
		return dragAction;

	}

	@NotNull
	@Contract("_ -> this")
	public Icon onDrag(DragAction dragAction) {
		this.dragAction = dragAction;
		return this;
	}

	public ItemStack getItem() {
		return item;
	}

}

