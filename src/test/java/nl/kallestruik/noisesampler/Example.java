package nl.kallestruik.noisesampler;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Example {

    @Test
    void example() {
        long start = System.nanoTime();
        for (int i = 0; i < 8000; i++) {
            NoiseSampler noiseSampler = new NoiseSampler(i);
            for (int j = 0; j < 1000; j++) {
                noiseSampler.queryNoise(j, 0, 0, NoiseType.TEMPERATURE);
            }
        }
        System.out.println((System.nanoTime() - start) / 1000000000.0);
    }

    @Test
    void example2() {
        Map<NoiseType, Double> expected = new HashMap<>();
        expected.put(NoiseType.CAVE_LAYER, 1.51651);
        expected.put(NoiseType.SPAGHETTI_3D_THICKNESS, 0.07613);
        expected.put(NoiseType.WEIRDNESS, 0.25820);
        expected.put(NoiseType.TERRAIN_PEAKS, 0.00000);
        expected.put(NoiseType.SPAGHETTI_3D_SECOND, 0.27466);
        expected.put(NoiseType.SHIFT_Y, 1.17460);
        expected.put(NoiseType.PILLAR, Double.NEGATIVE_INFINITY);
        expected.put(NoiseType.HUMIDITY, -0.28160);
        expected.put(NoiseType.AQUIFER_BARRIER, -0.32249);
        expected.put(NoiseType.TERRAIN_FACTOR, 3.95000);
        expected.put(NoiseType.ORE_GAP, -0.60252);
        expected.put(NoiseType.DEPTH, 0.30490);
        expected.put(NoiseType.AQUIFER_FLUID_LEVEL_FLOODEDNESS, -0.37301);
        expected.put(NoiseType.SPAGHETTI_ROUGHNESS_MODULATOR, 0.05551);
        expected.put(NoiseType.SHIFT_X, 1.17460);
        expected.put(NoiseType.SPAGHETTI_3D_FIRST, 0.26643);
        expected.put(NoiseType.PILLAR_THICKNESS, 0.46733);
        expected.put(NoiseType.TEMPERATURE, -0.04780);
        expected.put(NoiseType.JAGGED, 0.00000);
        expected.put(NoiseType.SPAGHETTI_2D, 0.10346);
        expected.put(NoiseType.SPAGHETTI_2D_ELEVATION, 0.05952);
        expected.put(NoiseType.SPAGHETTI_ROUGHNESS, 0.01566);
        expected.put(NoiseType.EROSION, -0.22580);
        expected.put(NoiseType.SPAGHETTI_3D, 0.33586);
        expected.put(NoiseType.ISLAND, 0.00000);
        expected.put(NoiseType.CAVE_ENTRANCE, 0.58032);
        expected.put(NoiseType.AQUIFER_FLUID_LEVEL_SPREAD, -0.44030);
        expected.put(NoiseType.CONTINENTALNESS, -0.48440);
        expected.put(NoiseType.SPAGHETTI_2D_THICKNESS, 0.75923);
        expected.put(NoiseType.SPAGHETTI_2D_MODULATOR, 0.39480);
        expected.put(NoiseType.SHIFT_Z, 1.17460);
        expected.put(NoiseType.TERRAIN, -0.00427);
        expected.put(NoiseType.PILLAR_RARENESS, 1.22982);
        expected.put(NoiseType.TERRAIN_OFFSET, -0.69494);
        expected.put(NoiseType.AQUIFER_LAVA, -0.27982);
        expected.put(NoiseType.SPAGHETTI_3D_RARITY, 0.27865);
        expected.put(NoiseType.CAVE_CHEESE, 0.52080);

        NoiseSampler noiseSampler = new NoiseSampler(1L);

        Map<NoiseType, Double> noise = noiseSampler.queryNoise(0, 0, 0, NoiseType.values());

        for (Map.Entry<NoiseType, Double> entry : noise.entrySet()) {
            Assertions.assertEquals(expected.get(entry.getKey()), entry.getValue(), 0.00001, "Noise value for " + entry.getKey() + " does not match.");
        }
    }
}
