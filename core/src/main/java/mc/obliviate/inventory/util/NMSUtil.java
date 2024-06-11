package mc.obliviate.inventory.util;

import mc.obliviate.util.versiondetection.ServerVersionController;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;

public class NMSUtil {

    public static final boolean IS_PAPER = isPaper();
    public static final boolean CAN_USE_COMPONENTS = ServerVersionController.isServerVersionAtLeast(ServerVersionController.V1_16) && IS_PAPER;
    public static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.builder()
            .hexColors().useUnusualXRepeatedCharacterHexFormat().build();

    private static boolean isPaper() {
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    private static final String CRAFTBUKKIT_PACKAGE = Bukkit.getServer().getClass().getPackage().getName();
    public static String cbClass(String clazz) {
        return (CRAFTBUKKIT_PACKAGE + "." + clazz);
    }

    public static Class<?> getCraftBukkitClass(final String name) throws ClassNotFoundException {
        return Class.forName(cbClass(name));
    }

}
