package nl.kallestruik.noisesampler;

import java.util.Map;
import org.junit.jupiter.api.Test;

public class Example {

    @Test
    void example() {
        NoiseSampler noiseSampler = new NoiseSampler(1L);
        long start = System.nanoTime();
        for (int i = 0; i < 8000000; i++) {
            noiseSampler.sampleNoise(NoiseType.TEMPERATURE, 0, 0, 0);
        }
        System.out.println((System.nanoTime() - start) / 1000000000.0);
    }

    @Test
    void example2() {
        NoiseSampler noiseSampler = new NoiseSampler(1L);

        Map<NoiseType, Double> noise = noiseSampler.queryNoise(0, 0, 0, NoiseType.TEMPERATURE, NoiseType.HUMIDITY, NoiseType.EROSION, NoiseType.WEIRDNESS);

        for (Map.Entry<NoiseType, Double> entry : noise.entrySet()) {
            System.out.println(entry.getKey().name() + ": " + entry.getValue());
        }
    }
}
