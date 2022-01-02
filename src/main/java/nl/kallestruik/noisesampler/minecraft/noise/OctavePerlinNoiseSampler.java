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
import nl.kallestruik.noisesampler.minecraft.MathHelper;
import nl.kallestruik.noisesampler.minecraft.AbstractRandom;
import nl.kallestruik.noisesampler.minecraft.Pair;
import nl.kallestruik.noisesampler.minecraft.RandomDeriver;

public class OctavePerlinNoiseSampler {
    private static final int field_31704 = 0x2000000;
    private final PerlinNoiseSampler[] octaveSamplers;
    private final int firstOctave;
    private final List<Double> amplitudes;
    private final double persistence;
    private final double lacunarity;


    @Deprecated
    public static OctavePerlinNoiseSampler createLegacy(AbstractRandom random, IntStream intStream) {
        return new OctavePerlinNoiseSampler(random, OctavePerlinNoiseSampler.calculateAmplitudes(new TreeSet<Integer>(intStream.boxed().collect(Collectors.toList()))), false);
    }

    @Deprecated
    public static OctavePerlinNoiseSampler createLegacy(AbstractRandom random, int offset, List<Double> amplitudes) {
        return new OctavePerlinNoiseSampler(random, Pair.of(offset, amplitudes), false);
    }

    public static OctavePerlinNoiseSampler create(AbstractRandom random, IntStream intStream) {
        return OctavePerlinNoiseSampler.create(random, intStream.boxed().collect(Collectors.toList()));
    }

    public static OctavePerlinNoiseSampler create(AbstractRandom random, List<Integer> list) {
        return new OctavePerlinNoiseSampler(random, OctavePerlinNoiseSampler.calculateAmplitudes(new TreeSet<Integer>(list)), true);
    }

    public static OctavePerlinNoiseSampler create(AbstractRandom random, int offset, double firstAmplitude, double ... amplitudes) {
        List<Double> doubleArrayList = DoubleStream.of(amplitudes).boxed().collect(Collectors.toList());
        doubleArrayList.add(0, firstAmplitude);
        return new OctavePerlinNoiseSampler(random, Pair.of(offset, doubleArrayList), true);
    }

    public static OctavePerlinNoiseSampler create(AbstractRandom random, int offset, List<Double> amplitudes) {
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

    protected OctavePerlinNoiseSampler(AbstractRandom random, Pair<Integer, List<Double>> pair, boolean xoroshiro) {
        this.firstOctave = pair.getLeft();
        this.amplitudes = pair.getRight();
        int i = this.amplitudes.size();
        int j = -this.firstOctave;
        this.octaveSamplers = new PerlinNoiseSampler[i];
        if (xoroshiro) {
            RandomDeriver lv = random.createRandomDeriver();
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

    private static void skipCalls(AbstractRandom random) {
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

    protected int getFirstOctave() {
        return this.firstOctave;
    }

    protected List<Double> getAmplitudes() {
        return this.amplitudes;
    }

    public void addDebugInfo(StringBuilder info) {
        info.append("PerlinNoise{");
        List<String> list = this.amplitudes.stream().map(double_ -> String.format("%.2f", double_)).toList();
        info.append("first octave: ").append(this.firstOctave).append(", amplitudes: ").append(list).append(", noise levels: [");
        for (int i = 0; i < this.octaveSamplers.length; ++i) {
            info.append(i).append(": ");
            PerlinNoiseSampler lv = this.octaveSamplers[i];
            if (lv == null) {
                info.append("null");
            }
            info.append(", ");
        }
        info.append("]");
        info.append("}");
    }
}

