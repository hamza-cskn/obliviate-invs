package mc.obliviate.inventory.configurable.util;

import mc.obliviate.inventory.configurable.placeholder.InternalPlaceholder;
import mc.obliviate.inventory.configurable.placeholder.PlaceholderUtil;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	private static final Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F\\d]{6}");

	@Contract("null,_ -> null")
	public static String applyPlaceholders(String message, final PlaceholderUtil placeholderUtil) {
		if (message == null) return null;
		if (placeholderUtil == null) return message;
		for (final InternalPlaceholder placeholder : placeholderUtil.getPlaceholders()) {
			message = message.replace(placeholder.getPlaceholder(), placeholder.getValue());
		}
		return message;
	}

	@Contract("null,_ -> null")
	public static List<String> applyPlaceholders(List<String> list, final PlaceholderUtil placeholderUtil) {
		if (placeholderUtil == null) return list;
		for (final InternalPlaceholder placeholder : placeholderUtil.getPlaceholders()) {
			list = listReplace(list, placeholder.getPlaceholder(), placeholder.getValue());
		}
		return list;
	}

	@Contract("null,_,_ -> null")
	public static List<String> listReplace(final List<String> stringList, final String search, final String replace) {
		if (stringList == null) return null;
		final List<String> result = new ArrayList<>();
		for (String str : stringList) {
			result.add(str.replace(search, replace));
		}
		return result;
	}

	@Contract("null -> null")
	public static String parseColor(String string) {
		if (string == null) return null;
		if (XMaterial.supports(16)) {
			Matcher matcher = HEX_PATTERN.matcher(string);
			while (matcher.find()) {
				String color = string.substring(matcher.start(), matcher.end());
				string = string.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
				matcher = HEX_PATTERN.matcher(string);
			}
		}
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	@Contract("null -> null")
	public static List<String> parseColor(final List<String> stringList) {
		if (stringList == null) return null;
		final List<String> result = new ArrayList<>();
		for (String str : stringList) {
			result.add(parseColor(str));
		}
		return result;
	}
}
