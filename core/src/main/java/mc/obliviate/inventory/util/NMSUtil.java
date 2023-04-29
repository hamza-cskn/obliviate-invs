package mc.obliviate.inventory.util;

import org.bukkit.Bukkit;

public class NMSUtil {

    public static final String NMS = getNMS();
    public static final boolean IS_PAPER = Version.isPaper();
    public static final Version VERSION = Version.getServerVersion(Bukkit.getServer());
    public static final boolean CAN_USE_COMPONENTS = VERSION.isNewerOrSameThan(Version.v1_16_R3) && IS_PAPER;

    private static String getNMS() {
        final String version = Bukkit.getServer().getClass().getPackage().getName();
        return version.substring(version.lastIndexOf('.') + 1);
    }

    public static Class<?> getCraftBukkitClass(final String name) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + NMS + "." + name);
    }

}
