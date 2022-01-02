/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package nl.kallestruik.noisesampler.minecraft.noise;

import java.util.stream.IntStream;
import nl.kallestruik.noisesampler.minecraft.AbstractRandom;
import nl.kallestruik.noisesampler.minecraft.MathHelper;
import nl.kallestruik.noisesampler.minecraft.NoiseSamplingConfig;

public class InterpolatedNoiseSampler {
    private final OctavePerlinNoiseSampler lowerInterpolatedNoise;
    private final OctavePerlinNoiseSampler upperInterpolatedNoise;
    private final OctavePerlinNoiseSampler interpolationNoise;
    private final double xzScale;
    private final double yScale;
    private final double xzMainScale;
    private final double yMainScale;
    private final int cellWidth;
    private final int cellHeight;

    public InterpolatedNoiseSampler(OctavePerlinNoiseSampler lowerInterpolatedNoise, OctavePerlinNoiseSampler upperInterpolatedNoise, OctavePerlinNoiseSampler interpolationNoise, NoiseSamplingConfig config, int cellWidth, int cellHeight) {
        this.lowerInterpolatedNoise = lowerInterpolatedNoise;
        this.upperInterpolatedNoise = upperInterpolatedNoise;
        this.interpolationNoise = interpolationNoise;
        this.xzScale = 684.412 * config.getXZScale();
        this.yScale = 684.412 * config.getYScale();
        this.xzMainScale = this.xzScale / config.getXZFactor();
        this.yMainScale = this.yScale / config.getYFactor();
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
    }

    public InterpolatedNoiseSampler(AbstractRandom random, NoiseSamplingConfig config, int cellWidth, int cellHeight) {
        this(OctavePerlinNoiseSampler.createLegacy(random, IntStream.rangeClosed(-15, 0)), OctavePerlinNoiseSampler.createLegacy(random, IntStream.rangeClosed(-15, 0)), OctavePerlinNoiseSampler.createLegacy(random, IntStream.rangeClosed(-7, 0)), config, cellWidth, cellHeight);
    }

    public double calculateNoise(int i, int j, int k) {
        int l = Math.floorDiv(i, this.cellWidth);
        int m = Math.floorDiv(j, this.cellHeight);
        int n = Math.floorDiv(k, this.cellWidth);
        double d = 0.0;
        double e = 0.0;
        double f = 0.0;
        boolean bl = true;
        double g = 1.0;
        for (int o = 0; o < 8; ++o) {
            PerlinNoiseSampler lv = this.interpolationNoise.getOctave(o);
            if (lv != null) {
                f += lv.sample(OctavePerlinNoiseSampler.maintainPrecision((double)l * this.xzMainScale * g), OctavePerlinNoiseSampler.maintainPrecision((double)m * this.yMainScale * g), OctavePerlinNoiseSampler.maintainPrecision((double)n * this.xzMainScale * g), this.yMainScale * g, (double)m * this.yMainScale * g) / g;
            }
            g /= 2.0;
        }
        double o = (f / 10.0 + 1.0) / 2.0;
        boolean bl2 = o >= 1.0;
        boolean bl3 = o <= 0.0;
        g = 1.0;
        for (int p = 0; p < 16; ++p) {
            PerlinNoiseSampler lv2;
            double h = OctavePerlinNoiseSampler.maintainPrecision((double)l * this.xzScale * g);
            double q = OctavePerlinNoiseSampler.maintainPrecision((double)m * this.yScale * g);
            double r = OctavePerlinNoiseSampler.maintainPrecision((double)n * this.xzScale * g);
            double s = this.yScale * g;
            if (!bl2 && (lv2 = this.lowerInterpolatedNoise.getOctave(p)) != null) {
                d += lv2.sample(h, q, r, s, (double)m * s) / g;
            }
            if (!bl3 && (lv2 = this.upperInterpolatedNoise.getOctave(p)) != null) {
                e += lv2.sample(h, q, r, s, (double)m * s) / g;
            }
            g /= 2.0;
        }
        return MathHelper.clampedLerp(d / 512.0, e / 512.0, o) / 128.0;
    }
}

