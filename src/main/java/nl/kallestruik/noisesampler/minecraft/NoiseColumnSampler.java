/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package nl.kallestruik.noisesampler.minecraft;

import java.util.stream.Stream;
import nl.kallestruik.noisesampler.minecraft.noise.DoublePerlinNoiseSampler;
import nl.kallestruik.noisesampler.minecraft.noise.InterpolatedNoiseSampler;
import nl.kallestruik.noisesampler.minecraft.noise.SimplexNoiseSampler;

public class NoiseColumnSampler {
    public final GenerationShapeConfig config;
    private final boolean hasNoiseCaves;
    public final InterpolatedNoiseSampler terrainNoise;
    public final SimplexNoiseSampler islandNoise;
    private final DoublePerlinNoiseSampler temperatureNoise;
    private final DoublePerlinNoiseSampler humidityNoise;
    private final DoublePerlinNoiseSampler continentalnessNoise;
    private final DoublePerlinNoiseSampler erosionNoise;
    private final DoublePerlinNoiseSampler weirdnessNoise;
    private final DoublePerlinNoiseSampler jaggedNoise;
    public final DoublePerlinNoiseSampler aquiferBarrierNoise;
    public final DoublePerlinNoiseSampler aquiferFluidLevelFloodednessNoise;
    public final DoublePerlinNoiseSampler aquiferFluidLevelSpreadNoise;
    public final DoublePerlinNoiseSampler aquiferLavaNoise;
    private final DoublePerlinNoiseSampler caveLayerNoise;
    private final DoublePerlinNoiseSampler pillarNoise;
    public final DoublePerlinNoiseSampler pillarRarenessNoise;
    public final DoublePerlinNoiseSampler pillarThicknessNoise;
    private final DoublePerlinNoiseSampler spaghetti2dNoise;
    public final DoublePerlinNoiseSampler spaghetti2dElevationNoise;
    public final DoublePerlinNoiseSampler spaghetti2dModulatorNoise;
    public final DoublePerlinNoiseSampler spaghetti2dThicknessNoise;
    public final DoublePerlinNoiseSampler spaghetti3dFirstNoise;
    public final DoublePerlinNoiseSampler spaghetti3dSecondNoise;
    public final DoublePerlinNoiseSampler spaghetti3dRarityNoise;
    public final DoublePerlinNoiseSampler spaghetti3dThicknessNoise;
    private final DoublePerlinNoiseSampler spaghettiRoughnessNoise;
    public final DoublePerlinNoiseSampler spaghettiRoughnessModulatorNoise;
    private final DoublePerlinNoiseSampler caveEntranceNoise;
    public final DoublePerlinNoiseSampler caveCheeseNoise;
    private final DoublePerlinNoiseSampler shiftNoise;
    public final DoublePerlinNoiseSampler oreGapNoise;

    public NoiseColumnSampler(GenerationShapeConfig config, boolean hasNoiseCaves, long seed, NoiseRegistry noiseRegistry, ChunkRandom.RandomProvider randomProvider) {
        this.config = config;
        this.hasNoiseCaves = hasNoiseCaves;
        if (config.islandNoiseOverride()) {
            AbstractRandom lv = randomProvider.create(seed);
            lv.skip(17292);
            this.islandNoise = new SimplexNoiseSampler(lv);
        } else {
            this.islandNoise = null;
        }
        int lv = config.minimumY();
        int i = Stream.of(VeinType.values()).mapToInt(veinType -> veinType.minY).min().orElse(lv);
        int j = Stream.of(VeinType.values()).mapToInt(veinType -> veinType.maxY).max().orElse(lv);
        float f = 4.0f;
        double d = 2.6666666666666665;
        int k = lv + 4;
        int m = lv + config.height();
        boolean bl2 = config.largeBiomes();
        RandomDeriver randomDeriver = randomProvider.create(seed).createRandomDeriver();

        this.terrainNoise = new InterpolatedNoiseSampler(randomDeriver.createRandom(new Identifier("terrain")), config.sampling(), config.horizontalBlockSize(), config.verticalBlockSize());
        temperatureNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, bl2 ? NoiseParameterKey.TEMPERATURE_LARGE : NoiseParameterKey.TEMPERATURE);
        humidityNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, bl2 ? NoiseParameterKey.VEGETATION_LARGE : NoiseParameterKey.VEGETATION);
        this.shiftNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.OFFSET);


        RandomDeriver aquiferRandomDeriver = randomDeriver.createRandom(new Identifier("aquifer")).createRandomDeriver();
        RandomDeriver oreRandomDeriver = randomDeriver.createRandom(new Identifier("ore")).createRandomDeriver();
        RandomDeriver depthBasedLayerRandomDeriver = randomDeriver.createRandom(new Identifier("depth_based_layer")).createRandomDeriver();
        this.aquiferBarrierNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.AQUIFER_BARRIER);
        this.aquiferFluidLevelFloodednessNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.AQUIFER_FLUID_LEVEL_FLOODEDNESS);
        this.aquiferLavaNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.AQUIFER_LAVA);
        this.aquiferFluidLevelSpreadNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.AQUIFER_FLUID_LEVEL_SPREAD);
        this.pillarNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.PILLAR);
        this.pillarRarenessNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.PILLAR_RARENESS);
        this.pillarThicknessNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.PILLAR_THICKNESS);
        this.spaghetti2dNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.SPAGHETTI_2D);
        this.spaghetti2dElevationNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.SPAGHETTI_2D_ELEVATION);
        this.spaghetti2dModulatorNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.SPAGHETTI_2D_MODULATOR);
        this.spaghetti2dThicknessNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.SPAGHETTI_2D_THICKNESS);
        this.spaghetti3dFirstNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.SPAGHETTI_3D_1);
        this.spaghetti3dSecondNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.SPAGHETTI_3D_2);
        this.spaghetti3dRarityNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.SPAGHETTI_3D_RARITY);
        this.spaghetti3dThicknessNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.SPAGHETTI_3D_THICKNESS);
        this.spaghettiRoughnessNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.SPAGHETTI_ROUGHNESS);
        this.spaghettiRoughnessModulatorNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.SPAGHETTI_ROUGHNESS_MODULATOR);
        this.caveEntranceNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.CAVE_ENTRANCE);
        this.caveLayerNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.CAVE_LAYER);
        this.caveCheeseNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.CAVE_CHEESE);
        this.continentalnessNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, bl2 ? NoiseParameterKey.CONTINENTALNESS_LARGE : NoiseParameterKey.CONTINENTALNESS);
        this.erosionNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, bl2 ? NoiseParameterKey.EROSION_LARGE : NoiseParameterKey.EROSION);
        this.weirdnessNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.RIDGE);
        this.oreGapNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.ORE_GAP);
        this.jaggedNoise = NoiseParameterKey.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.JAGGED);
    }

    public MultiNoiseUtil.NoiseValuePoint sample(int i, int j, int k) {
        return this.method_39329(i, j, k, this.method_39330(i, k, new Blender()));
    }

    public class_6747 method_39330(int i, int j, Blender arg) {
        double d = (double)i + this.sampleShiftNoise(i, 0, j);
        double e = (double)j + this.sampleShiftNoise(j, i, 0);
        double f = this.sampleContinentalnessNoise(d, 0.0, e);
        double g = this.sampleWeirdnessNoise(d, 0.0, e);
        double h = this.sampleErosionNoise(d, 0.0, e);
        TerrainNoisePoint lv = this.createTerrainNoisePoint(BiomeCoords.toBlock(i), BiomeCoords.toBlock(j), (float)f, (float)g, (float)h, arg);
        return new class_6747(d, e, f, g, h, lv);
    }

    public MultiNoiseUtil.NoiseValuePoint method_39329(int i, int j, int k, class_6747 arg) {
        double d = arg.shiftedX();
        double e = (double)j + this.sampleShiftNoise(j, k, i);
        double f = arg.shiftedZ();
        double g = this.method_39331(BiomeCoords.toBlock(j), arg.terrainInfo());
        return MultiNoiseUtil.createNoiseValuePoint((float)this.sampleTemperatureNoise(d, e, f), (float)this.sampleHumidityNoise(d, e, f), (float)arg.continentalness(), (float)arg.erosion(), (float)g, (float)arg.weirdness());
    }

    private double sampleHumidityNoise(double x, double y, double z) {
        return this.humidityNoise.sample(x, 0.0, z);
    }


    public double sampleContinentalnessNoise(double x, double y, double z) {
        if (SharedConstants.DEBUG_BIOME_SOURCE) {
            double g;
            if (SharedConstants.method_37896(new ChunkPos(BiomeCoords.toChunk(MathHelper.floor(x)), BiomeCoords.toChunk(MathHelper.floor(z))))) {
                return -1.0;
            }
            g = MathHelper.fractionalPart(x / 2048.0);
            return g * g * (double)((g  * 2.0 - 1.0) < 0.0 ? -1 : 1);
        }
        if (SharedConstants.DEBUG_NOISE) {
            double g = x * 0.005;
            return Math.sin(g + 0.5 * Math.sin(g));
        }
        return this.continentalnessNoise.sample(x, y, z);
    }

    public double sampleErosionNoise(double x, double y, double z) {
        if (SharedConstants.DEBUG_BIOME_SOURCE) {
            double g;
            if (SharedConstants.method_37896(new ChunkPos(BiomeCoords.toChunk(MathHelper.floor(x)), BiomeCoords.toChunk(MathHelper.floor(z))))) {
                return -1.0;
            }
            g = MathHelper.fractionalPart(z / 256.0);

            return g * g * (double)((g * 2.0 - 1.0) < 0.0 ? -1 : 1);
        }
        if (SharedConstants.DEBUG_NOISE) {
            double g = z * 0.005;
            return Math.sin(g + 0.5 * Math.sin(g));
        }
        return this.erosionNoise.sample(x, y, z);
    }

    public double sampleWeirdnessNoise(double x, double y, double z) {
        return this.weirdnessNoise.sample(x, y, z);
    }

    private double sampleTemperatureNoise(double x, double y, double z) {
        return this.temperatureNoise.sample(x, 0.0, z);
    }

    public static float getEndNoiseAt(SimplexNoiseSampler arg, int i, int j) {
        int k = i / 2;
        int l = j / 2;
        int m = i % 2;
        int n = j % 2;
        float f = 100.0f - MathHelper.sqrt(i * i + j * j) * 8.0f;
        f = MathHelper.clamp(f, -100.0f, 80.0f);
        for (int o = -12; o <= 12; ++o) {
            for (int p = -12; p <= 12; ++p) {
                long q = k + o;
                long r = l + p;
                if (q * q + r * r <= 4096L || !(arg.sample(q, r) < (double)-0.9f)) continue;
                float g = (MathHelper.abs(q) * 3439.0f + MathHelper.abs(r) * 147.0f) % 13.0f + 9.0f;
                float h = m - o * 2;
                float s = n - p * 2;
                float t = 100.0f - MathHelper.sqrt(h * h + s * s) * g;
                t = MathHelper.clamp(t, -100.0f, 80.0f);
                f = Math.max(f, t);
            }
        }
        return f;
    }

    private double sampleNoiseColumn(int x, int y, int z, TerrainNoisePoint point, double noise, boolean hasNoNoiseCaves, boolean bl2, Blender blender) {
        double n;
        double m;
        double l;
        double h;
        double g;
        double f;
        double e;
        if (this.islandNoise != null) {
            e = ((double)getEndNoiseAt(this.islandNoise, x / 8, z / 8) - 8.0) / 128.0;
        } else {
            f = bl2 ? this.method_38409(point.peaks(), x, z) : 0.0;
            g = (this.method_39331(y, point) + f) * point.factor();
            e = g * (double)(g > 0.0 ? 4 : 1);
        }
        f = e + noise;
        g = 1.5625;
        if (hasNoNoiseCaves || f < -64.0) {
            h = f;
            l = 64.0;
            m = -64.0;
        } else {
            n = f - 1.5625;
            boolean bl3 = n < 0.0;
            double o = this.sampleCaveEntranceNoise(x, y, z);
            double p = this.sampleSpaghettiRoughnessNoise(x, y, z);
            double q = this.sampleSpaghetti3dNoise(x, y, z);
            double r = Math.min(o, q + p);
            if (bl3) {
                h = f;
                l = r * 5.0;
                m = -64.0;
            } else {
                double t;
                double s = this.sampleCaveLayerNoise(x, y, z);
                if (s > 64.0) {
                    h = 64.0;
                } else {
                    t = this.caveCheeseNoise.sample(x, (double)y / 1.5, z);
                    double u = MathHelper.clamp(t + 0.27, -1.0, 1.0);
                    double v = n * 1.28;
                    double w = u + MathHelper.clampedLerp(0.5, 0.0, v);
                    h = w + s;
                }
                t = this.sampleSpaghetti2dNoise(x, y, z);
                l = Math.min(r, t + p);
                m = this.samplePillarNoise(x, y, z);
            }
        }
        n = Math.max(Math.min(h, l), m);
        n = this.applySlides(n, y / this.config.verticalBlockSize());
        n = blender.method_39338(x, y, z, n);
        n = MathHelper.clamp(n, -64.0, 64.0);
        return n;
    }

    private double method_39331(int i, TerrainNoisePoint arg) {
        double d = 1.0 - (double)i / 128.0;
        return d + arg.offset();
    }

    public double method_38409(double d, double e, double f) {
        if (d == 0.0) {
            return 0.0;
        }
        float g = 1500.0f;
        double h = this.jaggedNoise.sample(e * 1500.0, 0.0, f * 1500.0);
        return h > 0.0 ? d * h : d / 2.0 * h;
    }

    private double applySlides(double noise, int y) {
        int j = y - this.config.minimumBlockY();
        noise = this.config.topSlide().method_38414(noise, this.config.verticalBlockCount() - j);
        noise = this.config.bottomSlide().method_38414(noise, j);
        return noise;
    }

    public TerrainNoisePoint createTerrainNoisePoint(int x, int z, float continentalness, float weirdness, float erosion, Blender arg) {
        VanillaTerrainParameters lv = this.config.terrainParameters();
        VanillaTerrainParameters.NoisePoint lv2 = lv.createNoisePoint(continentalness, erosion, weirdness);
        float k = lv.getOffset(lv2);
        float l = lv.getFactor(lv2);
        float m = lv.getPeak(lv2);
        TerrainNoisePoint lv3 = new TerrainNoisePoint(k, l, m);
        return arg.method_39340(x, z, lv3);
    }

    public double sampleShiftNoise(int x, int y, int z) {
        return this.shiftNoise.sample(x, y, z) * 4.0;
    }

    public double sampleCaveEntranceNoise(int x, int y, int z) {
        double d = 0.75;
        double e = 0.5;
        double f = 0.37;
        double g = this.caveEntranceNoise.sample((double)x * 0.75, (double)y * 0.5, (double)z * 0.75) + 0.37;
        int l = -10;
        double h = (double)(y - -10) / 40.0;
        double m = 0.3;
        return g + MathHelper.clampedLerp(0.3, 0.0, h);
    }

    public double samplePillarNoise(int x, int y, int z) {
        double d = 0.0;
        double e = 2.0;
        double f = NoiseHelper.lerpFromProgress(this.pillarRarenessNoise, x, y, z, 0.0, 2.0);
        double g = 0.0;
        double h = 1.1;
        double l = NoiseHelper.lerpFromProgress(this.pillarThicknessNoise, x, y, z, 0.0, 1.1);
        l = Math.pow(l, 3.0);
        double m = 25.0;
        double n = 0.3;
        double o = this.pillarNoise.sample((double)x * 25.0, (double)y * 0.3, (double)z * 25.0);
        if ((o = l * (o * 2.0 - f)) > 0.03) {
            return o;
        }
        return Double.NEGATIVE_INFINITY;
    }

    public double sampleCaveLayerNoise(int x, int y, int z) {
        double d = this.caveLayerNoise.sample(x, y * 8, z);
        return MathHelper.square(d) * 4.0;
    }

    public double sampleSpaghetti3dNoise(int x, int y, int z) {
        double d = this.spaghetti3dRarityNoise.sample(x * 2, y, z * 2);
        double e = CaveScaler.scaleTunnels(d);
        double f = 0.065;
        double g = 0.088;
        double h = NoiseHelper.lerpFromProgress(this.spaghetti3dThicknessNoise, x, y, z, 0.065, 0.088);
        double l = NoiseColumnSampler.sample(this.spaghetti3dFirstNoise, x, y, z, e);
        double m = Math.abs(e * l) - h;
        double n = NoiseColumnSampler.sample(this.spaghetti3dSecondNoise, x, y, z, e);
        double o = Math.abs(e * n) - h;
        return NoiseColumnSampler.clampBetweenNoiseRange(Math.max(m, o));
    }

    public double sampleSpaghetti2dNoise(int x, int y, int z) {
        double d = this.spaghetti2dModulatorNoise.sample(x * 2, y, z * 2);
        double e = CaveScaler.scaleCaves(d);
        double f = 0.6;
        double g = 1.3;
        double h = NoiseHelper.lerpFromProgress(this.spaghetti2dThicknessNoise, x * 2, y, z * 2, 0.6, 1.3);
        double l = NoiseColumnSampler.sample(this.spaghetti2dNoise, x, y, z, e);
        double m = 0.083;
        double n = Math.abs(e * l) - 0.083 * h;
        int o = this.config.minimumBlockY();
        int p = 8;
        double q = NoiseHelper.lerpFromProgress(this.spaghetti2dElevationNoise, x, 0.0, z, o, 8.0);
        double r = Math.abs(q - (double)y / 8.0) - 1.0 * h;
        r = r * r * r;
        return NoiseColumnSampler.clampBetweenNoiseRange(Math.max(r, n));
    }

    public double sampleSpaghettiRoughnessNoise(int x, int y, int z) {
        double d = NoiseHelper.lerpFromProgress(this.spaghettiRoughnessModulatorNoise, x, y, z, 0.0, 0.1);
        return (0.4 - Math.abs(this.spaghettiRoughnessNoise.sample(x, y, z))) * d;
    }

    private static double clampBetweenNoiseRange(double value) {
        return MathHelper.clamp(value, -1.0, 1.0);
    }

    public static double sample(DoublePerlinNoiseSampler sampler, double x, double y, double z, double invertedScale) {
        return sampler.sample(x / invertedScale, y / invertedScale, z / invertedScale);
    }

    public record class_6747(double shiftedX, double shiftedZ, double continentalness, double weirdness, double erosion, TerrainNoisePoint terrainInfo) {
    }

    enum VeinType {
        COPPER(0, 50),
        IRON(-60, -8);

        final int minY;
        final int maxY;

        VeinType(int minY, int maxY) {
            this.minY = minY;
            this.maxY = maxY;
        }
    }

    public static final class CaveScaler {
        private CaveScaler() {
        }

        static double scaleCaves(double value) {
            if (value < -0.75) {
                return 0.5;
            }
            if (value < -0.5) {
                return 0.75;
            }
            if (value < 0.5) {
                return 1.0;
            }
            if (value < 0.75) {
                return 2.0;
            }
            return 3.0;
        }

        public static double scaleTunnels(double value) {
            if (value < -0.5) {
                return 0.75;
            }
            if (value < 0.0) {
                return 1.0;
            }
            if (value < 0.5) {
                return 1.5;
            }
            return 2.0;
        }
    }
}

