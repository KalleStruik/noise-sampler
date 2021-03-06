/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package nl.kallestruik.noisesampler.minecraft.noise;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import nl.kallestruik.noisesampler.minecraft.util.MathHelper;
import nl.kallestruik.noisesampler.minecraft.util.Pair;
import nl.kallestruik.noisesampler.minecraft.Xoroshiro128PlusPlusRandom;

public class OctavePerlinNoiseSampler {
    private final PerlinNoiseSampler[] octaveSamplers;
    private final int firstOctave;
    private final List<Double> amplitudes;
    private final double persistence;
    private final double lacunarity;

    @Deprecated
    public static OctavePerlinNoiseSampler createLegacy(Xoroshiro128PlusPlusRandom random, IntStream intStream) {
        return new OctavePerlinNoiseSampler(random, OctavePerlinNoiseSampler.calculateAmplitudes(new TreeSet<>(intStream.boxed().collect(Collectors.toList()))), false);
    }

    public static OctavePerlinNoiseSampler create(Xoroshiro128PlusPlusRandom random, int offset, List<Double> amplitudes) {
        return new OctavePerlinNoiseSampler(random, Pair.of(offset, amplitudes), true);
    }

    private static Pair<Integer, List<Double>> calculateAmplitudes(SortedSet<Integer> octaves) {
        int j;
        if (octaves.isEmpty()) {
            throw new IllegalArgumentException("Need some octaves!");
        }
        int i = -octaves.first();
        int k = i + (j = octaves.last()) + 1;
        if (k < 1) {
            throw new IllegalArgumentException("Total number of octaves needs to be >= 1");
        }
        List<Double> doubleList = DoubleStream.of(new double[k]).boxed().collect(Collectors.toList());
        Iterator<Integer> intBidirectionalIterator = octaves.iterator();
        while (intBidirectionalIterator.hasNext()) {
            int l = intBidirectionalIterator.next();
            doubleList.set(l + i, 1.0);
        }
        return Pair.of(-i, doubleList);
    }

    protected OctavePerlinNoiseSampler(Xoroshiro128PlusPlusRandom random, Pair<Integer, List<Double>> pair, boolean xoroshiro) {
        this.firstOctave = pair.getLeft();
        this.amplitudes = pair.getRight();
        int i = this.amplitudes.size();
        int j = -this.firstOctave;
        this.octaveSamplers = new PerlinNoiseSampler[i];
        if (xoroshiro) {
            Xoroshiro128PlusPlusRandom.RandomDeriver lv = random.createRandomDeriver();
            for (int k = 0; k < i; ++k) {
                if (this.amplitudes.get(k) == 0.0) continue;
                int l = this.firstOctave + k;
                this.octaveSamplers[k] = new PerlinNoiseSampler(lv.createRandom("octave_" + l));
            }
        } else {
            double k;
            PerlinNoiseSampler lv = new PerlinNoiseSampler(random);
            if (j >= 0 && j < i && (k = this.amplitudes.get(j)) != 0.0) {
                this.octaveSamplers[j] = lv;
            }
            for (int k2 = j - 1; k2 >= 0; --k2) {
                if (k2 < i) {
                    double l = this.amplitudes.get(k2);
                    if (l != 0.0) {
                        this.octaveSamplers[k2] = new PerlinNoiseSampler(random);
                        continue;
                    }
                    OctavePerlinNoiseSampler.skipCalls(random);
                    continue;
                }
                OctavePerlinNoiseSampler.skipCalls(random);
            }
            if (Arrays.stream(this.octaveSamplers).filter(Objects::nonNull).count() != this.amplitudes.stream().filter(double_ -> double_ != 0.0).count()) {
                throw new IllegalStateException("Failed to create correct number of noise levels for given non-zero amplitudes");
            }
            if (j < i - 1) {
                throw new IllegalArgumentException("Positive octaves are temporarily disabled");
            }
        }
        this.lacunarity = Math.pow(2.0, -j);
        this.persistence = Math.pow(2.0, i - 1) / (Math.pow(2.0, i) - 1.0);
    }

    private static void skipCalls(Xoroshiro128PlusPlusRandom random) {
        random.skip(262);
    }

    public double sample(double x, double y, double z) {
        return this.sample(x, y, z, 0.0, 0.0, false);
    }

    @Deprecated
    public double sample(double x, double y, double z, double yScale, double yMax, boolean useOrigin) {
        double i = 0.0;
        double j = this.lacunarity;
        double k = this.persistence;
        for (int l = 0; l < this.octaveSamplers.length; ++l) {
            PerlinNoiseSampler lv = this.octaveSamplers[l];
            if (lv != null) {
                double m = lv.sample(OctavePerlinNoiseSampler.maintainPrecision(x * j), useOrigin ? -lv.originY : OctavePerlinNoiseSampler.maintainPrecision(y * j), OctavePerlinNoiseSampler.maintainPrecision(z * j), yScale * j, yMax * j);
                i += this.amplitudes.get(l) * m * k;
            }
            j *= 2.0;
            k /= 2.0;
        }
        return i;
    }

    public PerlinNoiseSampler getOctave(int octave) {
        return this.octaveSamplers[this.octaveSamplers.length - 1 - octave];
    }

    public static double maintainPrecision(double value) {
        return value - (double)MathHelper.lfloor(value / 3.3554432E7 + 0.5) * 3.3554432E7;
    }
}

