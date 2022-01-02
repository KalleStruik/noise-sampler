/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package nl.kallestruik.noisesampler.minecraft;

public record GenerationShapeConfig(int minimumY, int height, NoiseSamplingConfig sampling, SlideConfig topSlide, SlideConfig bottomSlide, int horizontalSize, int verticalSize, boolean islandNoiseOverride, boolean amplified, boolean largeBiomes, VanillaTerrainParameters terrainParameters) {

    public int verticalBlockSize() {
        return BiomeCoords.toBlock(this.verticalSize());
    }

    public int horizontalBlockSize() {
        return BiomeCoords.toBlock(this.horizontalSize());
    }

    public int verticalBlockCount() {
        return this.height() / this.verticalBlockSize();
    }

    public int minimumBlockY() {
        return Math.floorDiv(this.minimumY(), this.verticalBlockSize());
    }
}

