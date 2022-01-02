/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package nl.kallestruik.noisesampler.minecraft;

public record SlideConfig(double target, int size, int offset) {

    public double method_38414(double d, int i) {
        if (this.size <= 0) {
            return d;
        }
        double e = (double)(i - this.offset) / (double)this.size;
        return MathHelper.clampedLerp(this.target, d, e);
    }
}

