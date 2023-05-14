package mc.obliviate.inventory;

import mc.obliviate.inventory.util.NMSUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ComponentManager {

	private static final GsonComponentSerializer SERIALIZER = GsonComponentSerializer.gson();
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

	private final Icon icon;

	public static ComponentManager fromIcon(Icon icon) {
		return new ComponentManager(icon);
	}

	private ComponentManager(Icon icon) {
		this.icon = icon;
	}

	public Icon toIcon() {
		return icon;
	}

	/**
	 * sets lore of the icon
	 *
	 * @param lore lore
	 * @return this
	 */
	@Nonnull
	public Icon setLore(final Component... lore) {
		return setLore(new ArrayList<>(Arrays.asList(lore)));
	}

	/**
	 * sets lore of the icon
	 *
	 * @param lore lore
	 * @return this
	 */
	@Nonnull
	public Icon setLore(final List<Component> lore) {
		final ItemMeta meta = icon.getItem().getItemMeta();
		if (meta == null) return icon;

		if (NMSUtil.CAN_USE_COMPONENTS) {
			try {
				final List<String> list = lore.stream().map(SERIALIZER::serialize).collect(Collectors.toList());
				LORE_FIELD.set(meta, list);
			} catch (IllegalAccessException exception) {
				exception.printStackTrace();
				throw new RuntimeException("Could not set lore, please report this to the plugin developer!");
			}
		}

		meta.setLore(lore.stream().map(NMSUtil.LEGACY::serialize).collect(Collectors.toList()));
		icon.getItem().setItemMeta(meta);
		return icon;
	}

	/**
	 * sets display name of the icon
	 *
	 * @param name display name
	 * @return this
	 */
	@Nonnull
	public Icon setName(final Component name) {
		final ItemMeta meta = icon.getItem().getItemMeta();
		if (meta == null) return icon;

		if (NMSUtil.CAN_USE_COMPONENTS) {
			try {
				DISPLAY_NAME_FIELD.set(meta, SERIALIZER.serialize(name));
			} catch (IllegalAccessException exception) {
				exception.printStackTrace();
				throw new RuntimeException("Could not set displayName, please report this to the plugin developer!");
			}
		}

		meta.setDisplayName(NMSUtil.LEGACY.serialize(name));
		icon.getItem().setItemMeta(meta);
		return icon;
	}

	/**
	 * adds new string lines to end of lore of the icon
	 *
	 * @param newLines lore lines
	 * @return this
	 */
	@Nonnull
	public Icon appendLore(final Component... newLines) {
		return appendLore(new ArrayList<>(Arrays.asList(newLines)));
	}

	/**
	 * adds new lore lines to end of lore of the icon
	 *
	 * @param lore lore lines
	 * @return this
	 */
	@Nonnull
	public Icon appendLore(final List<Component> lore) {
		final ItemMeta meta = icon.getItem().getItemMeta();
		if (meta == null) return icon;

		if (NMSUtil.CAN_USE_COMPONENTS) {
			try {
				final List<String> loreComponents = (List<String>) LORE_FIELD.get(meta);

				List<Component> list = (loreComponents == null) ? new ArrayList<>() : loreComponents.stream()
						.map(SERIALIZER::deserialize).collect(Collectors.toList());
				list.addAll(lore);

				return setLore(list);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new RuntimeException("Could not append lore. Please report this to the plugin developer!");
			}
		}

		List<String> serialized = lore.stream().map(NMSUtil.LEGACY::serialize).collect(Collectors.toList());
		List<String> list = meta.getLore();
		if (list != null) list.addAll(serialized);
		else list = serialized;

		// no need to re-call the component based method just call the legacy one
		return icon.setLore(list);
	}

	/**
	 * inserts new lines to lore of the icon
	 *
	 * @param index    line index. entry 0 adds new line as first line.
	 * @param newLines lore lines
	 * @return this
	 */
	@Nonnull
	public Icon insertLore(final int index, final List<Component> newLines) {
		final ItemMeta meta = icon.getItem().getItemMeta();
		if (meta == null) return icon;

		if (NMSUtil.CAN_USE_COMPONENTS) {
			try {
				final List<String> loreComponents = (List<String>) LORE_FIELD.get(meta);

				List<Component> list = (loreComponents == null) ? new ArrayList<>() : loreComponents.stream()
						.map(SERIALIZER::deserialize).collect(Collectors.toList());
				list.addAll(index, newLines);

				return setLore(list);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new RuntimeException("Could not append lore. Please report this to the plugin developer!");
			}
		}

		List<String> serialized = newLines.stream().map(NMSUtil.LEGACY::serialize).collect(Collectors.toList());
		List<String> list = meta.getLore();
		if (list != null) list.addAll(index, serialized);
		else list = serialized;

		// no need to re-call the component based method just call the legacy one
		return icon.setLore(list);
	}

	/**
	 * inserts new lines to lore of the icon
	 *
	 * @param index    line index. entry 0 adds new line as first line.
	 * @param newLines lore lines
	 * @return this
	 */
	@Nonnull
	public Icon insertLore(final int index, final Component... newLines) {
		return insertLore(index, new ArrayList<>(Arrays.asList(newLines)));
	}

}
