package mc.obliviate.inventory;

import mc.obliviate.inventory.util.NMSUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Icon {

    private static final GsonComponentSerializer SERIALIZER = GsonComponentSerializer.gson();
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.builder()
            .hexColors().useUnusualXRepeatedCharacterHexFormat().build();
    private static final Field DISPLAY_NAME_FIELD;
    private static final Field LORE_FIELD;

    static {
        try {
            final Class<?> metaClass = NMSUtil.getCraftBukkitClass("inventory.CraftMetaItem");

            DISPLAY_NAME_FIELD = metaClass.getDeclaredField("displayName");
            DISPLAY_NAME_FIELD.setAccessible(true);

            LORE_FIELD = metaClass.getDeclaredField("lore");
            LORE_FIELD.setAccessible(true);
        } catch (NoSuchFieldException | ClassNotFoundException exception) {
            exception.printStackTrace();
            throw new RuntimeException("Could not initialize Icon.class, please report this to the plugin developer!");
        }
    }

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
    @Nonnull
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
    @Nonnull
    public Icon setDurability(final int newDamage) {
        setDurability((short) newDamage);
        return this;
    }

    /**
     * sets display name of the icon
     *
     * @param name display name
     * @return this
     * @deprecated Use components instead
     */
    @Nonnull
    @Deprecated
    public Icon setName(final String name) {
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) return this;
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return this;
    }

    /**
     * sets display name of the icon
     *
     * @param name display name
     * @return this
     */
    @Nonnull
    public Icon name(final Component name) {
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) return this;

        if (NMSUtil.CAN_USE_COMPONENTS) {
            try {
                DISPLAY_NAME_FIELD.set(meta, SERIALIZER.serialize(name));
            } catch (IllegalAccessException exception) {
                exception.printStackTrace();
                throw new RuntimeException("Could not set displayName, please report this to the plugin developer!");
            }
        }

        meta.setDisplayName(LEGACY.serialize(name));
        item.setItemMeta(meta);
        return this;
    }

    /**
     * sets lore of the icon
     *
     * @param lore lore
     * @return this
     */
    @Nonnull
    public Icon lore(final Component... lore) {
        return lore(new ArrayList<>(Arrays.asList(lore)));
    }

    /**
     * sets lore of the icon
     *
     * @param lore lore
     * @return this
     */
    @Nonnull
    public Icon lore(final List<Component> lore) {
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) return this;

        if (NMSUtil.CAN_USE_COMPONENTS) {
            try {
                final List<String> list = lore.stream().map(SERIALIZER::serialize).collect(Collectors.toList());
                LORE_FIELD.set(meta, list);
            } catch (IllegalAccessException exception) {
                exception.printStackTrace();
                throw new RuntimeException("Could not set lore, please report this to the plugin developer!");
            }
        }

        meta.setLore(lore.stream().map(LEGACY::serialize).collect(Collectors.toList()));
        item.setItemMeta(meta);
        return this;
    }

    /**
     * sets lore of the icon
     *
     * @param lore lore
     * @return this
     * @deprecated Use components instead
     */
    @Nonnull
    @Deprecated
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
     * @deprecated Use components instead
     */
    @Nonnull
    @Deprecated
    public Icon setLore(final String... lore) {
        return setLore(new ArrayList<>(Arrays.asList(lore)));
    }

    /**
     * adds new string lines to end of lore of the icon
     *
     * @param newLines lore lines
     * @return this
     */
    @Nonnull
    public Icon appendLoreComponent(final Component... newLines) {
        return appendLoreComponent(new ArrayList<>(Arrays.asList(newLines)));
    }

    /**
     * adds new lore lines to end of lore of the icon
     *
     * @param lore lore lines
     * @return this
     */
    @Nonnull
    public Icon appendLoreComponent(final List<Component> lore) {
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) return this;

        if (NMSUtil.CAN_USE_COMPONENTS) {
            try {
                final List<String> loreComponents = (List<String>) LORE_FIELD.get(meta);

                List<Component> list = (loreComponents == null) ? new ArrayList<>() : loreComponents.stream()
                        .map(SERIALIZER::deserialize).collect(Collectors.toList());
                list.addAll(lore);

                return lore(list);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException("Could not append lore. Please report this to the plugin developer!");
            }
        }

        List<String> serialized = lore.stream().map(LEGACY::serialize).collect(Collectors.toList());
        List<String> list = meta.getLore();
        if (list != null) list.addAll(serialized);
        else list = serialized;

        // no need to re-call the component based method just call the legacy one
        return setLore(list);
    }

    /**
     * inserts new lines to lore of the icon
     *
     * @param index    line index. entry 0 adds new line as first line.
     * @param newLines lore lines
     * @return this
     */
    @Nonnull
    public Icon insertLoreComponent(final int index, final List<Component> newLines) {
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) return this;

        if (NMSUtil.CAN_USE_COMPONENTS) {
            try {
                final List<String> loreComponents = (List<String>) LORE_FIELD.get(meta);

                List<Component> list = (loreComponents == null) ? new ArrayList<>() : loreComponents.stream()
                        .map(SERIALIZER::deserialize).collect(Collectors.toList());
                list.addAll(index, newLines);

                return lore(list);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException("Could not append lore. Please report this to the plugin developer!");
            }
        }

        List<String> serialized = newLines.stream().map(LEGACY::serialize).collect(Collectors.toList());
        List<String> list = meta.getLore();
        if (list != null) list.addAll(index, serialized);
        else list = serialized;

        // no need to re-call the component based method just call the legacy one
        return setLore(list);
    }

    /**
     * inserts new lines to lore of the icon
     *
     * @param index    line index. entry 0 adds new line as first line.
     * @param newLines lore lines
     * @return this
     */
    @Nonnull
    public Icon insertLoreComponent(final int index, final Component... newLines) {
        return insertLoreComponent(index, new ArrayList<>(Arrays.asList(newLines)));
    }

    /**
     * adds new string lines to end of lore of the icon
     *
     * @param newLines lore lines
     * @return this
     * @deprecated Use components instead
     */
    @Nonnull
    @Deprecated
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
     * @deprecated Use components instead
     */
    @Nonnull
    @Deprecated
    public Icon appendLore(final String... newLines) {
        return appendLore(new ArrayList<>(Arrays.asList(newLines)));
    }

    /**
     * inserts new lines to lore of the icon
     *
     * @param index    line index. entry 0 adds new line as first line.
     * @param newLines lore lines
     * @deprecated Use components instead
     * @return this
     */
    @Nonnull
    @Deprecated
    public Icon insertLore(final int index, final String... newLines) {
        return insertLore(index, new ArrayList<>(Arrays.asList(newLines)));
    }

    /**
     * inserts new lines to lore of the icon
     *
     * @param index    line index. entry 0 adds new line as first line.
     * @param newLines lore lines
     * @return this
     * @deprecated Use components instead
     */
    @Nonnull
    @Deprecated
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
    @Nonnull
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
    @Nonnull
    public Icon hideFlags(final ItemFlag... itemFlag) {
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) return this;
        meta.addItemFlags(itemFlag);
        item.setItemMeta(meta);
        return this;
    }

    /**
     * hides all flags
     *
     * @return this
     */
    @Nonnull
    public Icon hideFlags() {
        hideFlags(ItemFlag.values());
        return this;
    }

    /**
     * enchants the item
     *
     * @param enchantment enchant
     * @return this
     */
    @Nonnull
    public Icon enchant(final Enchantment enchantment) {
        return enchant(enchantment, enchantment.getStartLevel());
    }

    /**
     * enchants the item
     *
     * @param enchantments enchant
     * @return this
     */
    @Nonnull
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
    @Nonnull
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

    @Nonnull
    public Consumer<InventoryClickEvent> getClickAction() {
        return clickAction;
    }

    @Nonnull
    public Icon onClick(Consumer<InventoryClickEvent> clickAction) {
        this.clickAction = clickAction;
        return this;
    }

    @Nonnull
    public Consumer<InventoryDragEvent> getDragAction() {
        return dragAction;

    }

    @Nonnull
    public Icon onDrag(Consumer<InventoryDragEvent> dragAction) {
        this.dragAction = dragAction;
        return this;
    }

    public ItemStack getItem() {
        return item;
    }

}

