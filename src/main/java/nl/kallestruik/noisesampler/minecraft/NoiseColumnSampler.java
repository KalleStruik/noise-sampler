/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package nl.kallestruik.noisesampler.minecraft;

import nl.kallestruik.noisesampler.minecraft.noise.LazyDoublePerlinNoiseSampler;
import nl.kallestruik.noisesampler.minecraft.util.MathHelper;
import nl.kallestruik.noisesampler.minecraft.util.NoiseValuePoint;
import nl.kallestruik.noisesampler.minecraft.util.TerrainNoisePoint;
import nl.kallestruik.noisesampler.minecraft.util.Util;
import nl.kallestruik.noisesampler.minecraft.noise.InterpolatedNoiseSampler;

public class NoiseColumnSampler {
    public final GenerationShapeConfig config;
    public final InterpolatedNoiseSampler terrainNoise;
    private final LazyDoublePerlinNoiseSampler temperatureNoise;
    private final LazyDoublePerlinNoiseSampler humidityNoise;
    private final LazyDoublePerlinNoiseSampler continentalnessNoise;
    private final LazyDoublePerlinNoiseSampler erosionNoise;
    private final LazyDoublePerlinNoiseSampler weirdnessNoise;
    private final LazyDoublePerlinNoiseSampler jaggedNoise;
    public final LazyDoublePerlinNoiseSampler aquiferBarrierNoise;
    public final LazyDoublePerlinNoiseSampler aquiferFluidLevelFloodednessNoise;
    public final LazyDoublePerlinNoiseSampler aquiferFluidLevelSpreadNoise;
    public final LazyDoublePerlinNoiseSampler aquiferLavaNoise;
    private final LazyDoublePerlinNoiseSampler caveLayerNoise;
    private final LazyDoublePerlinNoiseSampler pillarNoise;
    public final LazyDoublePerlinNoiseSampler pillarRarenessNoise;
    public final LazyDoublePerlinNoiseSampler pillarThicknessNoise;
    private final LazyDoublePerlinNoiseSampler spaghetti2dNoise;
    public final LazyDoublePerlinNoiseSampler spaghetti2dElevationNoise;
    public final LazyDoublePerlinNoiseSampler spaghetti2dModulatorNoise;
    public final LazyDoublePerlinNoiseSampler spaghetti2dThicknessNoise;
    public final LazyDoublePerlinNoiseSampler spaghetti3dFirstNoise;
    public final LazyDoublePerlinNoiseSampler spaghetti3dSecondNoise;
    public final LazyDoublePerlinNoiseSampler spaghetti3dRarityNoise;
    public final LazyDoublePerlinNoiseSampler spaghetti3dThicknessNoise;
    private final LazyDoublePerlinNoiseSampler spaghettiRoughnessNoise;
    public final LazyDoublePerlinNoiseSampler spaghettiRoughnessModulatorNoise;
    private final LazyDoublePerlinNoiseSampler caveEntranceNoise;
    public final LazyDoublePerlinNoiseSampler caveCheeseNoise;
    private final LazyDoublePerlinNoiseSampler shiftNoise;
    public final LazyDoublePerlinNoiseSampler oreGapNoise;

    public NoiseColumnSampler(GenerationShapeConfig config, long seed, NoiseRegistry noiseRegistry) {
        this.config = config;
        boolean isLargeBiomes = config.largeBiomes();
        Xoroshiro128PlusPlusRandom.RandomDeriver randomDeriver = new Xoroshiro128PlusPlusRandom(seed).createRandomDeriver();

        this.terrainNoise = new InterpolatedNoiseSampler(randomDeriver.createRandom("minecraft:terrain") , config.horizontalBlockSize(), config.verticalBlockSize());
        this.temperatureNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, isLargeBiomes ? NoiseParameterKey.TEMPERATURE_LARGE : NoiseParameterKey.TEMPERATURE);
        this.humidityNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, isLargeBiomes ? NoiseParameterKey.VEGETATION_LARGE : NoiseParameterKey.VEGETATION);
        this.shiftNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.OFFSET);
        this.aquiferBarrierNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.AQUIFER_BARRIER);
        this.aquiferFluidLevelFloodednessNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.AQUIFER_FLUID_LEVEL_FLOODEDNESS);
        this.aquiferLavaNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.AQUIFER_LAVA);
        this.aquiferFluidLevelSpreadNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.AQUIFER_FLUID_LEVEL_SPREAD);
        this.pillarNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.PILLAR);
        this.pillarRarenessNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.PILLAR_RARENESS);
        this.pillarThicknessNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.PILLAR_THICKNESS);
        this.spaghetti2dNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.SPAGHETTI_2D);
        this.spaghetti2dElevationNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.SPAGHETTI_2D_ELEVATION);
        this.spaghetti2dModulatorNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.SPAGHETTI_2D_MODULATOR);
        this.spaghetti2dThicknessNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.SPAGHETTI_2D_THICKNESS);
        this.spaghetti3dFirstNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.SPAGHETTI_3D_1);
        this.spaghetti3dSecondNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.SPAGHETTI_3D_2);
        this.spaghetti3dRarityNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.SPAGHETTI_3D_RARITY);
        this.spaghetti3dThicknessNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.SPAGHETTI_3D_THICKNESS);
        this.spaghettiRoughnessNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.SPAGHETTI_ROUGHNESS);
        this.spaghettiRoughnessModulatorNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.SPAGHETTI_ROUGHNESS_MODULATOR);
        this.caveEntranceNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.CAVE_ENTRANCE);
        this.caveLayerNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.CAVE_LAYER);
        this.caveCheeseNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.CAVE_CHEESE);
        this.continentalnessNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, isLargeBiomes ? NoiseParameterKey.CONTINENTALNESS_LARGE : NoiseParameterKey.CONTINENTALNESS);
        this.erosionNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, isLargeBiomes ? NoiseParameterKey.EROSION_LARGE : NoiseParameterKey.EROSION);
        this.weirdnessNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.RIDGE);
        this.oreGapNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.ORE_GAP);
        this.jaggedNoise = LazyDoublePerlinNoiseSampler.createNoiseSampler(noiseRegistry, randomDeriver, NoiseParameterKey.JAGGED);
    }

    public NoiseValuePoint sample(int x, int y, int z) {
        return this.createNoisePoint(x, y, z, this.createNoiseInfo(x, z));
    }

    public NoiseInfo createNoiseInfo(int x, int z) {
        double d = (double)x + this.sampleShiftNoise(x, 0, z);
        double e = (double)z + this.sampleShiftNoise(z, x, 0);
        double f = this.sampleContinentalnessNoise(d, 0.0, e);
        double g = this.sampleWeirdnessNoise(d, 0.0, e);
        double h = this.sampleErosionNoise(d, 0.0, e);
        TerrainNoisePoint lv = this.createTerrainNoisePoint(Util.toBlock(x), Util.toBlock(z), (float)f, (float)g, (float)h);
        return new NoiseInfo(d, e, f, g, h, lv);
    }

    public NoiseValuePoint createNoisePoint(int x, int y, int z, NoiseInfo arg) {
        double d = arg.shiftedX();
        double e = (double)y + this.sampleShiftNoise(y, z, x);
        double f = arg.shiftedZ();
        double g = this.method_39331(Util.toBlock(y), arg.terrainInfo());
        return NoiseValuePoint.createNoiseValuePoint((float)this.sampleTemperatureNoise(d, e, f), (float)this.sampleHumidityNoise(d, e, f), (float)arg.continentalness(), (float)arg.erosion(), (float)g, (float)arg.weirdness());
    }

    private double sampleHumidityNoise(double x, double y, double z) {
        return this.humidityNoise.sample(x, 0.0, z);
    }

    public double sampleContinentalnessNoise(double x, double y, double z) {
        return this.continentalnessNoise.sample(x, y, z);
    }

    public double sampleErosionNoise(double x, double y, double z) {
        return this.erosionNoise.sample(x, y, z);
    }

    public double sampleWeirdnessNoise(double x, double y, double z) {
        return this.weirdnessNoise.sample(x, y, z);
    }

    private double sampleTemperatureNoise(double x, double y, double z) {
        return this.temperatureNoise.sample(x, 0.0, z);
    }

    private double method_39331(int i, TerrainNoisePoint arg) {
        double d = 1.0 - (double)i / 128.0;
        return d + arg.offset();
    }

    public double sampleJaggedNoise(double d, double e, double f) {
        if (d == 0.0) {
            return 0.0;
        }
        double h = this.jaggedNoise.sample(e * 1500.0, 0.0, f * 1500.0);
        return h > 0.0 ? d * h : d / 2.0 * h;
    }

    public TerrainNoisePoint createTerrainNoisePoint(int x, int z, float continentalness, float weirdness, float erosion) {
        VanillaTerrainParameters parameters = this.config.terrainParameters();
        VanillaTerrainParameters.NoisePoint noisePoint = parameters.createNoisePoint(continentalness, erosion, weirdness);
        float k = parameters.getOffset(noisePoint);
        float l = parameters.getFactor(noisePoint);
        float m = parameters.getPeak(noisePoint);
        return new TerrainNoisePoint(k, l, m);
    }

    public double sampleShiftNoise(int x, int y, int z) {
        return this.shiftNoise.sample(x, y, z) * 4.0;
    }

    public double sampleCaveEntranceNoise(int x, int y, int z) {
        double g = this.caveEntranceNoise.sample((double)x * 0.75, (double)y * 0.5, (double)z * 0.75) + 0.37;
        double h = (double)(y + 10) / 40.0;
        return g + MathHelper.clampedLerp(0.3, 0.0, h);
    }

    public double samplePillarNoise(int x, int y, int z) {
        double f = Util.lerpFromProgress(this.pillarRarenessNoise, x, y, z, 0.0, 2.0);
        double l = Util.lerpFromProgress(this.pillarThicknessNoise, x, y, z, 0.0, 1.1);
        l = Math.pow(l, 3.0);
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
        double h = Util.lerpFromProgress(this.spaghetti3dThicknessNoise, x, y, z, 0.065, 0.088);
        double l = NoiseColumnSampler.sample(this.spaghetti3dFirstNoise, x, y, z, e);
        double m = Math.abs(e * l) - h;
        double n = NoiseColumnSampler.sample(this.spaghetti3dSecondNoise, x, y, z, e);
        double o = Math.abs(e * n) - h;
        return NoiseColumnSampler.clampBetweenNoiseRange(Math.max(m, o));
    }

    public double sampleSpaghetti2dNoise(int x, int y, int z) {
        double d = this.spaghetti2dModulatorNoise.sample(x * 2, y, z * 2);
        double e = CaveScaler.scaleCaves(d);
        double h = Util.lerpFromProgress(this.spaghetti2dThicknessNoise, x * 2, y, z * 2, 0.6, 1.3);
        double l = NoiseColumnSampler.sample(this.spaghetti2dNoise, x, y, z, e);
        double n = Math.abs(e * l) - 0.083 * h;
        int o = this.config.minimumBlockY();
        double q = Util.lerpFromProgress(this.spaghetti2dElevationNoise, x, 0.0, z, o, 8.0);
        double r = Math.abs(q - (double)y / 8.0) - h;
        r = r * r * r;
        return NoiseColumnSampler.clampBetweenNoiseRange(Math.max(r, n));
    }

    public double sampleSpaghettiRoughnessNoise(int x, int y, int z) {
        double d = Util.lerpFromProgress(this.spaghettiRoughnessModulatorNoise, x, y, z, 0.0, 0.1);
        return (0.4 - Math.abs(this.spaghettiRoughnessNoise.sample(x, y, z))) * d;
    }

    private static double clampBetweenNoiseRange(double value) {
        return MathHelper.clamp(value, -1.0, 1.0);
    }

    public static double sample(LazyDoublePerlinNoiseSampler sampler, double x, double y, double z, double invertedScale) {
        return sampler.sample(x / invertedScale, y / invertedScale, z / invertedScale);
    }

    public record NoiseInfo(double shiftedX, double shiftedZ, double continentalness, double weirdness, double erosion, TerrainNoisePoint terrainInfo) {}

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

