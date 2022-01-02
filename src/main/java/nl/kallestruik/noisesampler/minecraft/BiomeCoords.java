/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package nl.kallestruik.noisesampler.minecraft;

public final class BiomeCoords {
    public static final int field_33089 = 2;
    public static final int field_33090 = 4;
    public static final int field_34830 = 3;
    private static final int field_33091 = 2;

    private BiomeCoords() {
    }

    public static int fromBlock(int blockCoord) {
        return blockCoord >> 2;
    }

    public static int method_39920(int i) {
        return i & 3;
    }

    public static int toBlock(int biomeCoord) {
        return biomeCoord << 2;
    }

    public static int fromChunk(int chunkCoord) {
        return chunkCoord << 2;
    }

    public static int toChunk(int biomeCoord) {
        return biomeCoord >> 2;
    }
}

