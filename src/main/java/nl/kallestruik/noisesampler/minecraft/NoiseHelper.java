/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package nl.kallestruik.noisesampler.minecraft;

import nl.kallestruik.noisesampler.minecraft.noise.DoublePerlinNoiseSampler;

public class NoiseHelper {
    public static double lerpFromProgress(DoublePerlinNoiseSampler sampler, double x, double y, double z, double start, double end) {
        double i = sampler.sample(x, y, z);
        return MathHelper.lerpFromProgress(i, -1.0, 1.0, start, end);
    }

    public static double method_35479(double d, double e) {
        return d + Math.sin(Math.PI * d) * e / Math.PI;
    }

    public static void appendDebugInfo(StringBuilder builder, double originX, double originY, double originZ, byte[] permutations) {
        builder.append(String.format("xo=%.3f, yo=%.3f, zo=%.3f, p0=%d, p255=%d", Float.valueOf((float)originX), Float.valueOf((float)originY), Float.valueOf((float)originZ), permutations[0], permutations[255]));
    }

    public static void appendDebugInfo(StringBuilder builder, double originX, double originY, double originZ, int[] permutations) {
        builder.append(String.format("xo=%.3f, yo=%.3f, zo=%.3f, p0=%d, p255=%d", Float.valueOf((float)originX), Float.valueOf((float)originY), Float.valueOf((float)originZ), permutations[0], permutations[255]));
    }
}

