/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package nl.kallestruik.noisesampler.minecraft;

public class SharedConstants {
    @Deprecated
    public static final boolean IS_DEVELOPMENT_VERSION = false;
    @Deprecated
    public static final int WORLD_VERSION = 2865;
    @Deprecated
    public static final String CURRENT_SERIES = "main";
    @Deprecated
    public static final String VERSION_NAME = "1.18.1";
    @Deprecated
    public static final String RELEASE_TARGET = "1.18.1";
    @Deprecated
    public static final int RELEASE_TARGET_PROTOCOL_VERSION = 757;
    @Deprecated
    public static final int field_29736 = 64;
    @Deprecated
    public static final int RESOURCE_PACK_VERSION = 8;
    @Deprecated
    public static final int DATA_PACK_VERSION = 8;
    public static boolean DEBUG_BIOME_SOURCE = false;
    public static boolean DEBUG_NOISE = false;
    public static boolean useChoiceTypeRegistrations = true;
    public static boolean isDevelopment;
    public static final char[] INVALID_CHARS_LEVEL_NAME;

    public static int getProtocolVersion() {
        return 757;
    }

    public static boolean method_37896(ChunkPos arg) {
        int i = arg.getStartX();
        int j = arg.getStartZ();
        if (DEBUG_BIOME_SOURCE) {
            return i > 8192 || i < 0 || j > 1024 || j < 0;
        }
        return false;
    }

    static {
        INVALID_CHARS_LEVEL_NAME = new char[]{'/', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':'};
    }
}

