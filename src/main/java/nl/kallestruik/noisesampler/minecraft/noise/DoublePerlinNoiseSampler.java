/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package nl.kallestruik.noisesampler.minecraft.noise;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import nl.kallestruik.noisesampler.minecraft.AbstractRandom;

public class DoublePerlinNoiseSampler {
    private static final double DOMAIN_SCALE = 1.0181268882175227;
    private static final double field_31703 = 0.3333333333333333;
    private final double amplitude;
    private final OctavePerlinNoiseSampler firstSampler;
    private final OctavePerlinNoiseSampler secondSampler;

    @Deprecated
    public static DoublePerlinNoiseSampler createLegacy(AbstractRandom random, NoiseParameters parameters) {
        return new DoublePerlinNoiseSampler(random, parameters.getFirstOctave(), parameters.getAmplitudes(), false);
    }

    public static DoublePerlinNoiseSampler create(AbstractRandom random, int offset, double ... octaves) {
        return new DoublePerlinNoiseSampler(random, offset, DoubleStream.of(octaves).boxed().collect(Collectors.toList()), true);
    }

    public static DoublePerlinNoiseSampler create(AbstractRandom random, NoiseParameters parameters) {
        return new DoublePerlinNoiseSampler(random, parameters.getFirstOctave(), parameters.getAmplitudes(), true);
    }

    public static DoublePerlinNoiseSampler create(AbstractRandom random, int offset, List<Double> octaves) {
        return new DoublePerlinNoiseSampler(random, offset, octaves, true);
    }

    private DoublePerlinNoiseSampler(AbstractRandom random, int offset, List<Double> octaves, boolean xoroshiro) {
        this.firstSampler = OctavePerlinNoiseSampler.create(random, offset, octaves);
        this.secondSampler = OctavePerlinNoiseSampler.create(random, offset, octaves);
        int j = Integer.MAX_VALUE;
        int k = Integer.MIN_VALUE;
        for (int l = 0; l < octaves.size(); l++) {
            double d = octaves.get(l);
            if (d == 0.0) continue;
            j = Math.min(j, l);
            k = Math.max(k, l);
        }
        this.amplitude = 0.16666666666666666 / DoublePerlinNoiseSampler.createAmplitude(k - j);
    }

    private static double createAmplitude(int octaves) {
        return 0.1 * (1.0 + 1.0 / (double)(octaves + 1));
    }

    public double sample(double x, double y, double z) {
        double g = x * 1.0181268882175227;
        double h = y * 1.0181268882175227;
        double i = z * 1.0181268882175227;
        return (this.firstSampler.sample(x, y, z) + this.secondSampler.sample(g, h, i)) * this.amplitude;
    }

    public NoiseParameters copy() {
        return new NoiseParameters(this.firstSampler.getFirstOctave(), this.firstSampler.getAmplitudes());
    }

    public void addDebugInfo(StringBuilder info) {
        info.append("NormalNoise {");
        info.append("first: ");
        this.firstSampler.addDebugInfo(info);
        info.append(", second: ");
        this.secondSampler.addDebugInfo(info);
        info.append("}");
    }

    public static class NoiseParameters {
        private final int firstOctave;
        private final List<Double> amplitudes;

        public NoiseParameters(int firstOctave, List<Double> amplitudes) {
            this.firstOctave = firstOctave;
            this.amplitudes = new ArrayList<Double>(amplitudes);
        }

        public NoiseParameters(int firstOctave, double firstAmplitude, double ... amplitudes) {
            this.firstOctave = firstOctave;
            this.amplitudes = DoubleStream.of(amplitudes).boxed().collect(Collectors.toList());
            this.amplitudes.add(0, firstAmplitude);
        }

        public int getFirstOctave() {
            return this.firstOctave;
        }

        public List<Double> getAmplitudes() {
            return this.amplitudes;
        }
    }
}

