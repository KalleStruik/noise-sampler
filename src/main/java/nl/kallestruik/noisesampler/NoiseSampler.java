package nl.kallestruik.noisesampler;

import java.util.HashMap;
import java.util.Map;
import nl.kallestruik.noisesampler.minecraft.BiomeCoords;
import nl.kallestruik.noisesampler.minecraft.MultiNoiseUtil;
import nl.kallestruik.noisesampler.minecraft.TerrainNoisePoint;
import nl.kallestruik.noisesampler.minecraft.NoiseHelper;
import nl.kallestruik.noisesampler.minecraft.Blender;
import nl.kallestruik.noisesampler.minecraft.NoiseSamplingConfig;
import nl.kallestruik.noisesampler.minecraft.SlideConfig;
import nl.kallestruik.noisesampler.minecraft.ChunkRandom;
import nl.kallestruik.noisesampler.minecraft.GenerationShapeConfig;
import nl.kallestruik.noisesampler.minecraft.NoiseColumnSampler;
import nl.kallestruik.noisesampler.minecraft.NoiseRegistry;
import nl.kallestruik.noisesampler.minecraft.VanillaTerrainParameters;

public class NoiseSampler {
    private NoiseColumnSampler noiseColumnSampler;

    public void init(long seed) {
        NoiseRegistry noiseRegistry = new NoiseRegistry();
        GenerationShapeConfig generationShapeConfig = new GenerationShapeConfig(-64, 384,
                new NoiseSamplingConfig(1.0, 1.0, 80.0, 160.0),
                new SlideConfig(-0.078125, 2, 8),
                new SlideConfig(0.1171875, 3, 0),
                1, 2, false, false, false,
                VanillaTerrainParameters.createSurfaceParameters(false)
        );

        noiseColumnSampler = new NoiseColumnSampler(generationShapeConfig, true, seed, noiseRegistry, ChunkRandom.RandomProvider.XOROSHIRO);
    }

    public Map<NoiseType, Double> queryNoise(int x, int y, int z, NoiseType... noiseTypes) {
        Map<NoiseType, Double> noises = new HashMap<>();
        for (NoiseType noiseType : noiseTypes) {
            noises.put(noiseType, sampleNoise(noiseType, x, y, z));
        }

        return noises;
    }

    public double sampleNoise(NoiseType type, int x, int y, int z) {
        MultiNoiseUtil.NoiseValuePoint noiseValuePoint = noiseColumnSampler.sample(x, y, z);
        TerrainNoisePoint terrainNoisePoint = noiseColumnSampler.createTerrainNoisePoint(
                x,
                z,
                MultiNoiseUtil.method_38666(noiseValuePoint.continentalnessNoise()),
                MultiNoiseUtil.method_38666(noiseValuePoint.weirdnessNoise()),
                MultiNoiseUtil.method_38666(noiseValuePoint.erosionNoise()),
                new Blender()
        );

        switch (type) {
            case TEMPERATURE -> {
                return MultiNoiseUtil.method_38666(noiseValuePoint.temperatureNoise());
            }
            case HUMIDITY -> {
                return MultiNoiseUtil.method_38666(noiseValuePoint.humidityNoise());
            }
            case CONTINENTALNESS -> {
                return MultiNoiseUtil.method_38666(noiseValuePoint.continentalnessNoise());
            }
            case EROSION -> {
                return MultiNoiseUtil.method_38666(noiseValuePoint.erosionNoise());
            }
            case WEIRDNESS -> {
                return MultiNoiseUtil.method_38666(noiseValuePoint.weirdnessNoise());
            }
            case DEPTH -> {
                return MultiNoiseUtil.method_38666(noiseValuePoint.depth());
            }
            case SHIFT_X -> {
                return x + noiseColumnSampler.sampleShiftNoise(x, 0, z);
            }
            case SHIFT_Y -> {
                return y + noiseColumnSampler.sampleShiftNoise(y, z, x);
            }
            case SHIFT_Z -> {
                return z + noiseColumnSampler.sampleShiftNoise(z, x, 0);
            }
            case TERRAIN -> {
                return noiseColumnSampler.terrainNoise.calculateNoise(x, y, z);
            }
            case ISLAND -> {
                return noiseColumnSampler.islandNoise == null ? 0 : noiseColumnSampler.islandNoise.sample(x, y, z);
            }
            case JAGGED -> {
                return noiseColumnSampler.method_38409(terrainNoisePoint.peaks(), x, z);
            }
            case AQUIFER_BARRIER -> {
                return noiseColumnSampler.aquiferBarrierNoise.sample(x, y, z);
            }
            case AQUIFER_FLUID_LEVEL_FLOODEDNESS -> {
                return noiseColumnSampler.aquiferFluidLevelFloodednessNoise.sample(x, y, z);
            }
            case AQUIFER_FLUID_LEVEL_SPREAD -> {
                return noiseColumnSampler.aquiferFluidLevelSpreadNoise.sample(x, y, z);
            }
            case AQUIFER_LAVA -> {
                return noiseColumnSampler.aquiferLavaNoise.sample(x, y, z);
            }
            case PILLAR -> {
                return noiseColumnSampler.samplePillarNoise(x, y, z);
            }
            case PILLAR_RARENESS -> {
                return NoiseHelper.lerpFromProgress(noiseColumnSampler.pillarRarenessNoise, x, y, z, 0.0D, 2.0D);
            }
            case PILLAR_THICKNESS -> {
                return NoiseHelper.lerpFromProgress(noiseColumnSampler.pillarThicknessNoise, x, y, z, 0.0D, 1.1D);
            }
            case SPAGHETTI_2D -> {
                return noiseColumnSampler.sampleSpaghetti2dNoise(x, y, z);
            }
            case SPAGHETTI_2D_ELEVATION -> {
                return NoiseHelper.lerpFromProgress(noiseColumnSampler.spaghetti2dElevationNoise, x, 0.0, z, noiseColumnSampler.config.minimumBlockY(), 8.0);
            }
            case SPAGHETTI_2D_MODULATOR -> {
                return noiseColumnSampler.spaghetti2dModulatorNoise.sample(x * 2, y, z * 2);
            }
            case SPAGHETTI_2D_THICKNESS -> {
                return NoiseHelper.lerpFromProgress(noiseColumnSampler.spaghetti2dThicknessNoise, x * 2, y, z * 2, 0.6, 1.3);
            }
            case SPAGHETTI_3D -> {
                return noiseColumnSampler.sampleSpaghetti3dNoise(x, y, z);
            }
            case SPAGHETTI_3D_FIRST -> {
                double d = noiseColumnSampler.spaghetti3dRarityNoise.sample(x * 2, y, z * 2);
                double e = NoiseColumnSampler.CaveScaler.scaleTunnels(d);
                return NoiseColumnSampler.sample(noiseColumnSampler.spaghetti3dFirstNoise, x, y, z, e);
            }
            case SPAGHETTI_3D_SECOND -> {
                double d = noiseColumnSampler.spaghetti3dRarityNoise.sample(x * 2, y, z * 2);
                double e = NoiseColumnSampler.CaveScaler.scaleTunnels(d);
                return NoiseColumnSampler.sample(noiseColumnSampler.spaghetti3dSecondNoise, x, y, z, e);
            }
            case SPAGHETTI_3D_RARITY -> {
                return noiseColumnSampler.spaghetti3dRarityNoise.sample(x * 2, y, z * 2);
            }
            case SPAGHETTI_3D_THICKNESS -> {
                return NoiseHelper.lerpFromProgress(noiseColumnSampler.spaghetti3dThicknessNoise, x, y, z, 0.065, 0.088);
            }
            case SPAGHETTI_ROUGHNESS -> {
                return noiseColumnSampler.sampleSpaghettiRoughnessNoise(x, y, z);
            }
            case SPAGHETTI_ROUGHNESS_MODULATOR -> {
                return NoiseHelper.lerpFromProgress(noiseColumnSampler.spaghettiRoughnessModulatorNoise, x, y, z, 0.0, 0.1);
            }
            case CAVE_ENTRANCE -> {
                return noiseColumnSampler.sampleCaveEntranceNoise(x, y, z);
            }
            case CAVE_LAYER -> {
                // scaling them by 4 because it's sampled in normal coords
                return noiseColumnSampler.sampleCaveLayerNoise(
                        BiomeCoords.toBlock(x),
                        BiomeCoords.toBlock(y),
                        BiomeCoords.toBlock(z)
                );
            }
            case CAVE_CHEESE -> {
                // same reason as above
                return noiseColumnSampler.caveCheeseNoise.sample(
                        BiomeCoords.toBlock(x),
                        BiomeCoords.toBlock(y) / 1.5,
                        BiomeCoords.toBlock(z)
                );
            }
            case TERRAIN_PEAKS -> {
                return terrainNoisePoint.peaks();
            }
            case TERRAIN_OFFSET -> {
                return terrainNoisePoint.offset();
            }
            case TERRAIN_FACTOR -> {
                return terrainNoisePoint.factor();
            }
            case ORE_GAP -> {
                return noiseColumnSampler.oreGapNoise.sample(x, y, z);
            }
            default -> throw new IllegalArgumentException("Unknown noise type: " + type.name());
        }
    }
}
