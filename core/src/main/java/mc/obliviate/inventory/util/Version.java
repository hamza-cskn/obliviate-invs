package mc.obliviate.inventory.util;

import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;
import java.util.logging.Logger;

/*
Taken from https://github.com/DerFrZocker/Spigot-Utils/
 */
public enum Version implements Comparable<Version> {

    v1_19_R3(20),
    v1_19_R2(19),
    v1_19_R1(18),
    v1_18_R2(17),
    v1_18_R1(16),
    v1_17_R1(15),
    v1_16_R3(14),
    v1_16_R2(13),
    v1_16_R1(12),
    v1_15_R1(11),
    v1_14_R1(10),
    v1_13_R2(9),
    v1_13_R1(8),
    v1_12_R1(7),
    v1_11_R1(6),
    v1_10_R1(5),
    v1_9_R2(4),
    v1_9_R1(3),
    v1_8_R3(2),
    v1_8_R2(1),
    v1_8_R1(0),
    UNKNOWN(-1);

    private final int value;

    Version(int value) {
        this.value = value;
    }

    /**
     * @param server to get the version from
     * @return the version of the server
     * @throws IllegalArgumentException if server is null
     */
    @NotNull
    public static Version getServerVersion(@NotNull Server server) {
        Validate.notNull(server, "Server cannot be null");

        String packageName = server.getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);

        try {
            return valueOf(version.trim());
        } catch (final IllegalArgumentException e) {
            return Version.UNKNOWN;
        }
    }

    /**
     * @return true if the server is Paper or false of not
     * @throws IllegalArgumentException if server is null
     */
    public static boolean isPaper() {
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            return true;
        } catch (ClassNotFoundException exception) {
            return false;
        }
    }

    public static boolean isSupportedVersion(@Nullable Logger logger, @NotNull Version serverVersion, @NotNull Version... supportedVersions) {
        for (Version version : supportedVersions) {
            if (version == serverVersion) {
                return true;
            }
        }

        if (logger == null) {
            return false;
        }

        logger.warning(String.format("The Server version which you are running is unsupported, you are running version '%s'.", serverVersion));
        logger.warning(String.format("The plugin supports following versions %s.", combineVersions(supportedVersions)));

        if (serverVersion == Version.UNKNOWN) {
            logger.warning(String.format("The Version '%s' can indicate, that you are using a newer Minecraft version than currently supported.", serverVersion));
            logger.warning("In this case please update to the newest version of this plugin. If this is the newest Version, than please be patient. It can take some weeks until the plugin is updated.");
        }

        logger.log(Level.WARNING, "No compatible Server version found!", new IllegalStateException("No compatible Server version found!"));

        return false;
    }

    @NotNull
    private static String combineVersions(@NotNull Version... versions) {
        StringBuilder stringBuilder = new StringBuilder();

        boolean first = true;

        for (Version version : versions) {
            if (first) {
                first = false;
            } else {
                stringBuilder.append(" ");
            }

            stringBuilder.append("'");
            stringBuilder.append(version);
            stringBuilder.append("'");
        }

        return stringBuilder.toString();
    }

    /**
     * Checks if the version is newer than the given version
     * <p>
     * If both versions are the same, the method will return false
     *
     * @param version to check against
     * @return true if the version is newer than the given one, otherwise false
     * @throws IllegalArgumentException if version is null
     * @throws IllegalArgumentException if this version or the given version, is the version UNKNOWN
     */
    public boolean isNewerThan(@NotNull Version version) {
        Validate.notNull(version, "Version cannot be null");
        Validate.isTrue(this != UNKNOWN, "Cannot check, if version UNKNOWN is newer");
        Validate.isTrue(version != UNKNOWN, "Cannot check, if version UNKNOWN is newer");

        return value > version.value;
    }

    /**
     * Checks if the version is newer or the same than the given version
     *
     * @param version to check against
     * @return true if the version is newer or the same than the given one, otherwise false
     * @throws IllegalArgumentException if version is null
     * @throws IllegalArgumentException if this version or the given version, is the version UNKNOWN
     */
    public boolean isNewerOrSameThan(@NotNull Version version) {
        Validate.notNull(version, "Version cannot be null");
        Validate.isTrue(this != UNKNOWN, "Cannot check, if version UNKNOWN is newer or same");
        Validate.isTrue(version != UNKNOWN, "Cannot check, if version UNKNOWN is newer or same");

        return value >= version.value;
    }

    /**
     * Checks if the version is older than the given version
     *
     * @param version to check against
     * @return true if the version is older than the given one, otherwise false
     * @throws IllegalArgumentException if version is null
     * @throws IllegalArgumentException if this version or the given version, is the version UNKNOWN
     */
    public boolean isOlderThan(@NotNull Version version) {
        Validate.notNull(version, "Version cannot be null");
        Validate.isTrue(this != UNKNOWN, "Cannot check, if version UNKNOWN is older");
        Validate.isTrue(version != UNKNOWN, "Cannot check, if version UNKNOWN is older");

        return value < version.value;
    }

    /**
     * Checks if the version is older or the same than the given version
     *
     * @param version to check against
     * @return true if the version is older or the same than the given one, otherwise false
     * @throws IllegalArgumentException if version is null
     * @throws IllegalArgumentException if this version or the given version, is the version UNKNOWN
     */
    public boolean isOlderOrSameThan(@NotNull Version version) {
        Validate.notNull(version, "Version cannot be null");
        Validate.isTrue(this != UNKNOWN, "Cannot check, if version UNKNOWN is older or same");
        Validate.isTrue(version != UNKNOWN, "Cannot check, if version UNKNOWN is older or same");

        return value <= version.value;
    }
}