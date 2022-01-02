/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package nl.kallestruik.noisesampler.minecraft;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public record VanillaTerrainParameters(Spline<NoisePoint> offsetSpline, Spline<NoisePoint> factorSpline, Spline<NoisePoint> peakSpline) {
    private static final float OFFSET_VALUE_OFFSET = -0.50375f;
    private static final ToFloatFunction<Float> field_35673 = float_ -> float_;

    private static float method_39534(float f) {
        return f < 0.0f ? f : f * 2.0f;
    }

    private static float method_39535(float f) {
        return 1.25f - 6.25f / (f + 5.0f);
    }

    private static float method_39536(float f) {
        return f * 2.0f;
    }

    public static VanillaTerrainParameters createSurfaceParameters(boolean amplified) {
        ToFloatFunction<Float> lv = amplified ? VanillaTerrainParameters::method_39534 : field_35673;
        ToFloatFunction<Float> lv2 = amplified ? VanillaTerrainParameters::method_39535 : field_35673;
        ToFloatFunction<Float> lv3 = amplified ? VanillaTerrainParameters::method_39536 : field_35673;
        Spline<NoisePoint> lv4 = VanillaTerrainParameters.createLandSpline(-0.15f, 0.0f, 0.0f, 0.1f, 0.0f, -0.03f, false, false, lv);
        Spline<NoisePoint> lv5 = VanillaTerrainParameters.createLandSpline(-0.1f, 0.03f, 0.1f, 0.1f, 0.01f, -0.03f, false, false, lv);
        Spline<NoisePoint> lv6 = VanillaTerrainParameters.createLandSpline(-0.1f, 0.03f, 0.1f, 0.7f, 0.01f, -0.03f, true, true, lv);
        Spline<NoisePoint> lv7 = VanillaTerrainParameters.createLandSpline(-0.05f, 0.03f, 0.1f, 1.0f, 0.01f, 0.01f, true, true, lv);
        float f = -0.51f;
        float g = -0.4f;
        float h = 0.1f;
        float i = -0.15f;
        Spline<NoisePoint> lv8 = Spline.builder(LocationFunction.CONTINENTS, lv).add(-1.1f, 0.044f, 0.0f).add(-1.02f, -0.2222f, 0.0f).add(-0.51f, -0.2222f, 0.0f).add(-0.44f, -0.12f, 0.0f).add(-0.18f, -0.12f, 0.0f).add(-0.16f, lv4, 0.0f).add(-0.15f, lv4, 0.0f).add(-0.1f, lv5, 0.0f).add(0.25f, lv6, 0.0f).add(1.0f, lv7, 0.0f).build();
        Spline<NoisePoint> lv9 = Spline.builder(LocationFunction.CONTINENTS, field_35673).add(-0.19f, 3.95f, 0.0f).add(-0.15f, VanillaTerrainParameters.buildErosionFactorSpline(6.25f, true, field_35673), 0.0f).add(-0.1f, VanillaTerrainParameters.buildErosionFactorSpline(5.47f, true, lv2), 0.0f).add(0.03f, VanillaTerrainParameters.buildErosionFactorSpline(5.08f, true, lv2), 0.0f).add(0.06f, VanillaTerrainParameters.buildErosionFactorSpline(4.69f, false, lv2), 0.0f).build();
        float j = 0.65f;
        Spline<NoisePoint> lv10 = Spline.builder(LocationFunction.CONTINENTS, lv3).add(-0.11f, 0.0f, 0.0f).add(0.03f, VanillaTerrainParameters.method_38856(1.0f, 0.5f, 0.0f, 0.0f, lv3), 0.0f).add(0.65f, VanillaTerrainParameters.method_38856(1.0f, 1.0f, 1.0f, 0.0f, lv3), 0.0f).build();
        return new VanillaTerrainParameters(lv8, lv9, lv10);
    }

    private static Spline<NoisePoint> method_38856(float f, float g, float h, float i, ToFloatFunction<Float> arg) {
        float j = -0.5775f;
        Spline<NoisePoint> lv = VanillaTerrainParameters.method_38855(f, h, arg);
        Spline<NoisePoint> lv2 = VanillaTerrainParameters.method_38855(g, i, arg);
        return Spline.builder(LocationFunction.EROSION, arg).add(-1.0f, lv, 0.0f).add(-0.78f, lv2, 0.0f).add(-0.5775f, lv2, 0.0f).add(-0.375f, 0.0f, 0.0f).build();
    }

    private static Spline<NoisePoint> method_38855(float f, float g, ToFloatFunction<Float> arg) {
        float h = VanillaTerrainParameters.getNormalizedWeirdness(0.4f);
        float i = VanillaTerrainParameters.getNormalizedWeirdness(0.56666666f);
        float j = (h + i) / 2.0f;
        Spline.Builder<NoisePoint> lv = Spline.builder(LocationFunction.RIDGES, arg);
        lv.add(h, 0.0f, 0.0f);
        if (g > 0.0f) {
            lv.add(j, VanillaTerrainParameters.method_38857(g, arg), 0.0f);
        } else {
            lv.add(j, 0.0f, 0.0f);
        }
        if (f > 0.0f) {
            lv.add(1.0f, VanillaTerrainParameters.method_38857(f, arg), 0.0f);
        } else {
            lv.add(1.0f, 0.0f, 0.0f);
        }
        return lv.build();
    }

    private static Spline<NoisePoint> method_38857(float f, ToFloatFunction<Float> arg) {
        float g = 0.63f * f;
        float h = 0.3f * f;
        return Spline.builder(LocationFunction.WEIRDNESS, arg).add(-0.01f, g, 0.0f).add(0.01f, h, 0.0f).build();
    }

    private static Spline<NoisePoint> buildErosionFactorSpline(float value, boolean bl, ToFloatFunction<Float> arg) {
        Spline<NoisePoint> lv = Spline.builder(LocationFunction.WEIRDNESS, arg).add(-0.2f, 6.3f, 0.0f).add(0.2f, value, 0.0f).build();
        Spline.Builder<NoisePoint> lv2 = Spline.builder(LocationFunction.EROSION, arg).add(-0.6f, lv, 0.0f).add(-0.5f, Spline.builder(LocationFunction.WEIRDNESS, arg).add(-0.05f, 6.3f, 0.0f).add(0.05f, 2.67f, 0.0f).build(), 0.0f).add(-0.35f, lv, 0.0f).add(-0.25f, lv, 0.0f).add(-0.1f, Spline.builder(LocationFunction.WEIRDNESS, arg).add(-0.05f, 2.67f, 0.0f).add(0.05f, 6.3f, 0.0f).build(), 0.0f).add(0.03f, lv, 0.0f);
        if (bl) {
            Spline<NoisePoint> lv3 = Spline.builder(LocationFunction.WEIRDNESS, arg).add(0.0f, value, 0.0f).add(0.1f, 0.625f, 0.0f).build();
            Spline<NoisePoint> lv4 = Spline.builder(LocationFunction.RIDGES, arg).add(-0.9f, value, 0.0f).add(-0.69f, lv3, 0.0f).build();
            lv2.add(0.35f, value, 0.0f).add(0.45f, lv4, 0.0f).add(0.55f, lv4, 0.0f).add(0.62f, value, 0.0f);
        } else {
            Spline<NoisePoint> lv3 = Spline.builder(LocationFunction.RIDGES, arg).add(-0.7f, lv, 0.0f).add(-0.15f, 1.37f, 0.0f).build();
            Spline<NoisePoint> lv4 = Spline.builder(LocationFunction.RIDGES, arg).add(0.45f, lv, 0.0f).add(0.7f, 1.56f, 0.0f).build();
            lv2.add(0.05f, lv4, 0.0f).add(0.4f, lv4, 0.0f).add(0.45f, lv3, 0.0f).add(0.55f, lv3, 0.0f).add(0.58f, value, 0.0f);
        }
        return lv2.build();
    }

    private static float method_38210(float f, float g, float h, float i) {
        return (g - f) / (i - h);
    }

    private static Spline<NoisePoint> method_38219(float f, boolean bl, ToFloatFunction<Float> arg) {
        Spline.Builder<NoisePoint> lv = Spline.builder(LocationFunction.RIDGES, arg);
        float g = -0.7f;
        float h = -1.0f;
        float i = VanillaTerrainParameters.getOffsetValue(-1.0f, f, -0.7f);
        float j = 1.0f;
        float k = VanillaTerrainParameters.getOffsetValue(1.0f, f, -0.7f);
        float l = VanillaTerrainParameters.method_38217(f);
        float m = -0.65f;
        if (-0.65f < l && l < 1.0f) {
            float n = VanillaTerrainParameters.getOffsetValue(-0.65f, f, -0.7f);
            float o = -0.75f;
            float p = VanillaTerrainParameters.getOffsetValue(-0.75f, f, -0.7f);
            float q = VanillaTerrainParameters.method_38210(i, p, -1.0f, -0.75f);
            lv.add(-1.0f, i, q);
            lv.add(-0.75f, p, 0.0f);
            lv.add(-0.65f, n, 0.0f);
            float r = VanillaTerrainParameters.getOffsetValue(l, f, -0.7f);
            float s = VanillaTerrainParameters.method_38210(r, k, l, 1.0f);
            float t = 0.01f;
            lv.add(l - 0.01f, r, 0.0f);
            lv.add(l, r, s);
            lv.add(1.0f, k, s);
        } else {
            float n = VanillaTerrainParameters.method_38210(i, k, -1.0f, 1.0f);
            if (bl) {
                lv.add(-1.0f, Math.max(0.2f, i), 0.0f);
                lv.add(0.0f, MathHelper.lerp(0.5f, i, k), n);
            } else {
                lv.add(-1.0f, i, n);
            }
            lv.add(1.0f, k, n);
        }
        return lv.build();
    }

    private static float getOffsetValue(float weirdness, float continentalness, float weirdnessThreshold) {
        float i = 1.17f;
        float j = 0.46082947f;
        float k = 1.0f - (1.0f - continentalness) * 0.5f;
        float l = 0.5f * (1.0f - continentalness);
        float m = (weirdness + 1.17f) * 0.46082947f;
        float n = m * k - l;
        if (weirdness < weirdnessThreshold) {
            return Math.max(n, -0.2222f);
        }
        return Math.max(n, 0.0f);
    }

    private static float method_38217(float continentalness) {
        float g = 1.17f;
        float h = 0.46082947f;
        float i = 1.0f - (1.0f - continentalness) * 0.5f;
        float j = 0.5f * (1.0f - continentalness);
        return j / (0.46082947f * i) - 1.17f;
    }

    private static Spline<NoisePoint> createLandSpline(float f, float g, float h, float i, float j, float k, boolean bl, boolean bl2, ToFloatFunction<Float> arg) {
        float l = 0.6f;
        float m = 0.5f;
        float n = 0.5f;
        Spline<NoisePoint> lv = VanillaTerrainParameters.method_38219(MathHelper.lerp(i, 0.6f, 1.5f), bl2, arg);
        Spline<NoisePoint> lv2 = VanillaTerrainParameters.method_38219(MathHelper.lerp(i, 0.6f, 1.0f), bl2, arg);
        Spline<NoisePoint> lv3 = VanillaTerrainParameters.method_38219(i, bl2, arg);
        Spline<NoisePoint> lv4 = VanillaTerrainParameters.createFlatOffsetSpline(f - 0.15f, 0.5f * i, MathHelper.lerp(0.5f, 0.5f, 0.5f) * i, 0.5f * i, 0.6f * i, 0.5f, arg);
        Spline<NoisePoint> lv5 = VanillaTerrainParameters.createFlatOffsetSpline(f, j * i, g * i, 0.5f * i, 0.6f * i, 0.5f, arg);
        Spline<NoisePoint> lv6 = VanillaTerrainParameters.createFlatOffsetSpline(f, j, j, g, h, 0.5f, arg);
        Spline<NoisePoint> lv7 = VanillaTerrainParameters.createFlatOffsetSpline(f, j, j, g, h, 0.5f, arg);
        Spline<NoisePoint> lv8 = Spline.builder(LocationFunction.RIDGES, arg).add(-1.0f, f, 0.0f).add(-0.4f, lv6, 0.0f).add(0.0f, h + 0.07f, 0.0f).build();
        Spline<NoisePoint> lv9 = VanillaTerrainParameters.createFlatOffsetSpline(-0.02f, k, k, g, h, 0.0f, arg);
        Spline.Builder<NoisePoint> lv10 = Spline.builder(LocationFunction.EROSION, arg).add(-0.85f, lv, 0.0f).add(-0.7f, lv2, 0.0f).add(-0.4f, lv3, 0.0f).add(-0.35f, lv4, 0.0f).add(-0.1f, lv5, 0.0f).add(0.2f, lv6, 0.0f);
        if (bl) {
            lv10.add(0.4f, lv7, 0.0f).add(0.45f, lv8, 0.0f).add(0.55f, lv8, 0.0f).add(0.58f, lv7, 0.0f);
        }
        lv10.add(0.7f, lv9, 0.0f);
        return lv10.build();
    }

    private static Spline<NoisePoint> createFlatOffsetSpline(float f, float g, float h, float i, float j, float k, ToFloatFunction<Float> arg) {
        float l = Math.max(0.5f * (g - f), k);
        float m = 5.0f * (h - g);
        return Spline.builder(LocationFunction.RIDGES, arg).add(-1.0f, f, l).add(-0.4f, g, Math.min(l, m)).add(0.0f, h, m).add(0.4f, i, 2.0f * (i - h)).add(1.0f, j, 0.7f * (j - i)).build();
    }

    public Spline<NoisePoint> getOffsetSpline() {
        return this.offsetSpline;
    }

    public Spline<NoisePoint> getFactorSpline() {
        return this.factorSpline;
    }

    public Spline<NoisePoint> getPeakSpline() {
        return this.peakSpline;
    }

    public float getOffset(NoisePoint point) {
        return this.offsetSpline.apply(point) + -0.50375f;
    }

    public float getFactor(NoisePoint point) {
        return this.factorSpline.apply(point);
    }

    public float getPeak(NoisePoint point) {
        return this.peakSpline.apply(point);
    }

    public NoisePoint createNoisePoint(float continentalnessNoise, float erosionNoise, float weirdnessNoise) {
        return new NoisePoint(continentalnessNoise, erosionNoise, VanillaTerrainParameters.getNormalizedWeirdness(weirdnessNoise), weirdnessNoise);
    }

    public static float getNormalizedWeirdness(float weirdness) {
        return -(Math.abs(Math.abs(weirdness) - 0.6666667f) - 0.33333334f) * 3.0f;
    }

    protected static enum LocationFunction implements ToFloatFunction<NoisePoint>
    {
        CONTINENTS(NoisePoint::continentalnessNoise, "continents"),
        EROSION(NoisePoint::erosionNoise, "erosion"),
        WEIRDNESS(NoisePoint::weirdnessNoise, "weirdness"),
        RIDGES(NoisePoint::normalizedWeirdness, "ridges");

        private static final Map<String, LocationFunction> field_35462;
        private final ToFloatFunction<NoisePoint> noiseFunction;
        private final String id;

        LocationFunction(ToFloatFunction<NoisePoint> noiseFunction, String id) {
            this.noiseFunction = noiseFunction;
            this.id = id;
        }

        public String asString() {
            return this.id;
        }

        public String toString() {
            return this.id;
        }

        public float apply(NoisePoint arg) {
            return this.noiseFunction.apply(arg);
        }

        static {
            field_35462 = Arrays.stream(LocationFunction.values()).collect(Collectors.toMap(LocationFunction::asString, arg -> arg));
        }
    }

    public record NoisePoint(float continentalnessNoise, float erosionNoise, float normalizedWeirdness, float weirdnessNoise) {
    }
}

