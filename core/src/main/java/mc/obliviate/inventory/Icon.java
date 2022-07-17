package mc.obliviate.inventory;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Icon {

	private final ItemStack item;
	private Consumer<InventoryClickEvent> clickAction;
	private Consumer<InventoryDragEvent> dragAction;

	public Icon(final ItemStack item) {
		this.item = item;
		this.dragAction = event -> {
		};
		this.clickAction = event -> {
		};
	}

	public Icon(final Material material) {
		this(new ItemStack(material));
	}


	/**
	 * sets durability of icon
	 *
	 * @param newDamage durability
	 * @return this
	 */
	@SuppressWarnings("deprecation")
	@NotNull
	@Contract("_ -> this")
	public Icon setDurability(final short newDamage) {
		item.setDurability(newDamage);
		return this;
	}

	/**
	 * sets durability of the icon
	 *
	 * @param newDamage durability
	 * @return this
	 */
	@NotNull
	@Contract("_ -> this")
	public Icon setDurability(final int newDamage) {
		setDurability((short) newDamage);
		return this;
	}

	/**
	 * sets display name of the icon
	 *
	 * @param name display name
	 * @return this
	 */
	@NotNull
	@Contract("_ -> this")
	public Icon setName(final String name) {
		final ItemMeta meta = item.getItemMeta();
		if (meta == null) return this;
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return this;
	}

	/**
	 * sets lore of the icon
	 *
	 * @param lore lore
	 * @return this
	 */
	@NotNull
	@Contract("_ -> this")
	public Icon setLore(final List<String> lore) {
		final ItemMeta meta = item.getItemMeta();
		if (meta == null) return this;
		meta.setLore(lore);
		item.setItemMeta(meta);
		return this;
	}

	/**
	 * sets lore of icon
	 *
	 * @param lore lore
	 * @return this
	 */
	@NotNull
	@Contract("_ -> this")
	public Icon setLore(final String... lore) {
		return setLore(new ArrayList<>(Arrays.asList(lore)));
	}

	/**
	 * adds new string lines to end of lore of the icon
	 *
	 * @param newLines lore lines
	 * @return this
	 */
	@NotNull
	@Contract("_ -> this")
	public Icon appendLore(final List<String> newLines) {
		final ItemMeta meta = item.getItemMeta();
		if (meta == null) return this;
		List<String> lore = meta.getLore();
		if (lore != null) lore.addAll(newLines);
		else lore = newLines;
		return setLore(lore);
	}

	/**
	 * adds new string lines to end of lore of the icon
	 *
	 * @param newLines lore lines
	 * @return this
	 */
	@NotNull
	@Contract("_ -> this")
	public Icon appendLore(final String... newLines) {
		return appendLore(new ArrayList<>(Arrays.asList(newLines)));
	}

	/**
	 * inserts new lines to lore of the icon
	 *
	 * @param index line index. entry 0 adds new line as first line.
	 * @param newLines lore lines
	 * @return this
	 */
	@NotNull
	@Contract("_,_ -> this")
	public Icon insertLore(final int index, final String... newLines) {
		return insertLore(index, new ArrayList<>(Arrays.asList(newLines)));
	}

	/**
	 * inserts new lines to lore of the icon
	 *
	 * @param index line index. entry 0 adds new line as first line.
	 * @param newLines lore lines
	 * @return this
	 */
	@NotNull
	@Contract("_,_ -> this")
	public Icon insertLore(final int index, final List<String> newLines) {
		final ItemMeta meta = item.getItemMeta();
		if (meta == null) return this;
		List<String> lore = meta.getLore();
		if (lore != null) lore.addAll(index, newLines);
		else lore = newLines;
		return setLore(lore);
	}

	/**
	 * sets item amount of the icon
	 *
	 * @param amount new amount
	 * @return this
	 */
	@NotNull
	@Contract("_ -> this")
	public Icon setAmount(final int amount) {
		item.setAmount(amount);
		return this;
	}

	/**
	 * hides a flag of the icon
	 *
	 * @param itemFlag item flag on meta
	 * @return this
	 */
	@NotNull
	@Contract("_ -> this")
	public Icon hideFlags(final ItemFlag itemFlag) {
		final ItemMeta meta = item.getItemMeta();
		if (meta == null) return this;
		meta.addItemFlags(itemFlag);
		item.setItemMeta(meta);
		return this;
	}

	/**
	 * hides all flags (except dye)
	 *
	 * @return this
	 */
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

	/**
	 * enchants the item
	 *
	 * @param enchantment enchant
	 * @return this
	 */
	@NotNull
	@Contract("_ -> this")
	public Icon enchant(final Enchantment enchantment) {
		return enchant(enchantment, enchantment.getStartLevel());
	}

	/**
	 * enchants the item
	 *
	 * @param enchantments enchant
	 * @return this
	 */
	@NotNull
	@Contract("_ -> this")
	public Icon enchant(final Map<Enchantment, Integer> enchantments) {
		for (Map.Entry<Enchantment, Integer> enchant : enchantments.entrySet()) {
			enchant(enchant.getKey(), enchant.getValue());
		}
		return this;
	}

	/**
	 * enchants the item
	 *
	 * @param enchantment enchant
	 * @param level       enchantment level
	 * @return this
	 */
	@NotNull
	@Contract("_,_ -> this")
	public Icon enchant(final Enchantment enchantment, final int level) {
		if (item.getType().equals(Material.ENCHANTED_BOOK)) {
			final EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();

			if (meta == null) return this;

			meta.addStoredEnchant(enchantment, level, true);

			item.setItemMeta(meta);
		} else {
			item.addUnsafeEnchantment(enchantment, level);
		}
		return this;
	}

	@NotNull
	public Consumer<InventoryClickEvent> getClickAction() {
		return clickAction;
	}

	@NotNull
	@Contract("_ -> this")
	public Icon onClick(Consumer<InventoryClickEvent> clickAction) {
		this.clickAction = clickAction;
		return this;
	}

	@NotNull
	public Consumer<InventoryDragEvent> getDragAction() {
		return dragAction;

	}

	@NotNull
	@Contract("_ -> this")
	public Icon onDrag(Consumer<InventoryDragEvent> dragAction) {
		this.dragAction = dragAction;
		return this;
	}

	public ItemStack getItem() {
		return item;
	}

}

