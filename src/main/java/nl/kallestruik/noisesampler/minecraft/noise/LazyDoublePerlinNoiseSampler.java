package nl.kallestruik.noisesampler.minecraft.noise;

import nl.kallestruik.noisesampler.minecraft.NoiseParameterKey;
import nl.kallestruik.noisesampler.minecraft.NoiseRegistry;
import nl.kallestruik.noisesampler.minecraft.Xoroshiro128PlusPlusRandom;

public class LazyDoublePerlinNoiseSampler {
    private boolean initialized = false;
    private DoublePerlinNoiseSampler sampler = null;

    private Xoroshiro128PlusPlusRandom.RandomDeriver randomDeriver;
    private String identifier;
    private DoublePerlinNoiseSampler.NoiseParameters parameters;

    public static LazyDoublePerlinNoiseSampler create(Xoroshiro128PlusPlusRandom.RandomDeriver randomDeriver, String identifier, DoublePerlinNoiseSampler.NoiseParameters parameters) {
        LazyDoublePerlinNoiseSampler sampler = new LazyDoublePerlinNoiseSampler();
        sampler.randomDeriver = randomDeriver;
        sampler.identifier = identifier;
        sampler.parameters = parameters;
        return sampler;
    }

    public double sample(double x, double y, double z) {
        if (!initialized) {
            sampler = DoublePerlinNoiseSampler.create(randomDeriver.createRandom(identifier), parameters);
            initialized = true;
        }
        return sampler.sample(x, y, z);
    }

    public static LazyDoublePerlinNoiseSampler createNoiseSampler(NoiseRegistry noiseRegistry, Xoroshiro128PlusPlusRandom.RandomDeriver randomDeriver, NoiseParameterKey noise) {
        return LazyDoublePerlinNoiseSampler.create(randomDeriver, "minecraft:" + noise.value, noiseRegistry.registry.get(noise));
    }
}
