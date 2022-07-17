package mc.obliviate.inventory.configurable.placeholder;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderUtil {

	private final List<InternalPlaceholder> placeholders = new ArrayList<>();

	public PlaceholderUtil add(final String key, final String value) {
		placeholders.add(new InternalPlaceholder(key, value));
		return this;
	}

	public List<InternalPlaceholder> getPlaceholders() {
		return placeholders;
	}

	public List<String> apply(final List<String> texts) {
		final List<String> result = new ArrayList<>();
		for (final String text : texts) {
			result.add(apply(text));
		}
		return result;
	}
	public String apply(String text) {
		for (final InternalPlaceholder placeholder : placeholders) {
			text = text.replace(placeholder.getPlaceholder(), placeholder.getValue());
		}
		return text;
	}


}
