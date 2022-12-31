package mc.obliviate.inventory.configurable;

import mc.obliviate.inventory.Icon;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class ConfigIcon extends Icon {

    private final ConfigurationSection section;

    public ConfigIcon(ItemStack item, ConfigurationSection section) {
        super(item);
        this.section = section;
    }

    public ConfigIcon(Material material, ConfigurationSection section) {
        super(material);
        this.section = section;
    }

    public ConfigurationSection getSection() {
        return section;
    }
}
