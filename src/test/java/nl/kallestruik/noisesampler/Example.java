package nl.kallestruik.noisesampler;

import java.util.Map;
import org.junit.jupiter.api.Test;

public class Example {

    @Test
    void example() {
        NoiseSampler noiseSampler = new NoiseSampler();
        noiseSampler.init(1L);
        System.out.println(noiseSampler.sampleNoise(NoiseType.TEMPERATURE, 0, 0, 0));
    }

    @Test
    void example2() {
        NoiseSampler noiseSampler = new NoiseSampler();
        noiseSampler.init(1L);

        Map<NoiseType, Double> noise = noiseSampler.queryNoise(0, 0, 0, NoiseType.TEMPERATURE, NoiseType.HUMIDITY, NoiseType.EROSION, NoiseType.WEIRDNESS);

        for (Map.Entry<NoiseType, Double> entry : noise.entrySet()) {
            System.out.println(entry.getKey().name() + ": " + entry.getValue());
        }
    }
}
