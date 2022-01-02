/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package nl.kallestruik.noisesampler.minecraft;

public class NoiseSamplingConfig {
    private final double xzScale;
    private final double yScale;
    private final double xzFactor;
    private final double yFactor;

    public NoiseSamplingConfig(double xzScale, double yScale, double xzFactor, double yFactor) {
        this.xzScale = xzScale;
        this.yScale = yScale;
        this.xzFactor = xzFactor;
        this.yFactor = yFactor;
    }

    public double getXZScale() {
        return this.xzScale;
    }

    public double getYScale() {
        return this.yScale;
    }

    public double getXZFactor() {
        return this.xzFactor;
    }

    public double getYFactor() {
        return this.yFactor;
    }
}

