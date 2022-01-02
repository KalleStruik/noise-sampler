/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package nl.kallestruik.noisesampler.minecraft.noise;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import nl.kallestruik.noisesampler.minecraft.AbstractRandom;
import nl.kallestruik.noisesampler.minecraft.AtomicSimpleRandom;
import nl.kallestruik.noisesampler.minecraft.ChunkRandom;

public class OctaveSimplexNoiseSampler {
    private final SimplexNoiseSampler[] octaveSamplers;
    private final double persistence;
    private final double lacunarity;

    public OctaveSimplexNoiseSampler(AbstractRandom random, List<Integer> octaves) {
        this(random, new TreeSet<>(octaves));
    }

    private OctaveSimplexNoiseSampler(AbstractRandom random, SortedSet<Integer> octaves) {
        int j;
        if (octaves.isEmpty()) {
            throw new IllegalArgumentException("Need some octaves!");
        }
        int i = -octaves.first();
        int k = i + (j = octaves.last()) + 1;
        if (k < 1) {
            throw new IllegalArgumentException("Total number of octaves needs to be >= 1");
        }
        SimplexNoiseSampler lv = new SimplexNoiseSampler(random);
        int l = j;
        this.octaveSamplers = new SimplexNoiseSampler[k];
        if (l >= 0 && l < k && octaves.contains(0)) {
            this.octaveSamplers[l] = lv;
        }
        for (int m = l + 1; m < k; ++m) {
            if (m >= 0 && octaves.contains(l - m)) {
                this.octaveSamplers[m] = new SimplexNoiseSampler(random);
                continue;
            }
            random.skip(262);
        }
        if (j > 0) {
            long m = (long)(lv.sample(lv.originX, lv.originY, lv.originZ) * 9.223372036854776E18);
            ChunkRandom lv2 = new ChunkRandom(new AtomicSimpleRandom(m));
            for (int n = l - 1; n >= 0; --n) {
                if (n < k && octaves.contains(l - n)) {
                    this.octaveSamplers[n] = new SimplexNoiseSampler(lv2);
                    continue;
                }
                lv2.skip(262);
            }
        }
        this.lacunarity = Math.pow(2.0, j);
        this.persistence = 1.0 / (Math.pow(2.0, k) - 1.0);
    }

    public double sample(double x, double y, boolean useOrigin) {
        double f = 0.0;
        double g = this.lacunarity;
        double h = this.persistence;
        for (SimplexNoiseSampler lv : this.octaveSamplers) {
            if (lv != null) {
                f += lv.sample(x * g + (useOrigin ? lv.originX : 0.0), y * g + (useOrigin ? lv.originY : 0.0)) * h;
            }
            g /= 2.0;
            h *= 2.0;
        }
        return f;
    }
}

