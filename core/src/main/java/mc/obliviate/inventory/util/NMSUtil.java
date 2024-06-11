package mc.obliviate.inventory.util;

import mc.obliviate.util.versiondetection.ServerVersionController;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;

public class NMSUtil {

    public static final String NMS = getNMS();
    public static final boolean IS_PAPER = isPaper();
    public static final boolean CAN_USE_COMPONENTS = ServerVersionController.isServerVersionAtLeast(ServerVersionController.V1_16) && IS_PAPER;
    public static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.builder()
            .hexColors().useUnusualXRepeatedCharacterHexFormat().build();

    private static boolean isPaper() {
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            Bukkit.getLogger().info("Paper detected");
            return true;
        } catch (ClassNotFoundException ignored) {
            Bukkit.getLogger().info("Paper detected");
            return false;
        }
    }

    private static String getNMS() {
        final String version = Bukkit.getServer().getClass().getPackage().getName();
        Bukkit.getLogger().info("version" + version);
        return version.substring(version.lastIndexOf('.') + 1);
    }

    public static Class<?> getCraftBukkitClass(final String name) throws ClassNotFoundException {
        Class cl = Class.forName("org.bukkit.craftbukkit." + NMS + "." + name);
        Bukkit.getLogger().info(Bukkit.getServer().getClass().getPackage().getName());
        Bukkit.getLogger().info("clye bak: " + cl);
        Bukkit.getLogger().info("versiona bak: " + NMS);
        return Class.forName("org.bukkit.craftbukkit." + NMS + "." + name);
    }

}
