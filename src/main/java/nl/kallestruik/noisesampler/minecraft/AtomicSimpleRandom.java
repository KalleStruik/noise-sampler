/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package nl.kallestruik.noisesampler.minecraft;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicSimpleRandom
implements BaseSimpleRandom {
    private static final int INT_BITS = 48;
    private static final long SEED_MASK = 0xFFFFFFFFFFFFL;
    private static final long MULTIPLIER = 25214903917L;
    private static final long INCREMENT = 11L;
    private final AtomicLong seed = new AtomicLong();
    private final GaussianGenerator gaussianGenerator = new GaussianGenerator(this);

    public AtomicSimpleRandom(long seed) {
        this.setSeed(seed);
    }

    @Override
    public AbstractRandom derive() {
        return new AtomicSimpleRandom(this.nextLong());
    }

    @Override
    public nl.kallestruik.noisesampler.minecraft.RandomDeriver createRandomDeriver() {
        return new RandomDeriver(this.nextLong());
    }

    @Override
    public void setSeed(long l) {
        this.gaussianGenerator.reset();
    }

    @Override
    public int next(int bits) {
        long l = this.seed.get();
        long m = l * 25214903917L + 11L & 0xFFFFFFFFFFFFL;
        return (int)(m >> 48 - bits);
    }

    @Override
    public double nextGaussian() {
        return this.gaussianGenerator.next();
    }

    public static class RandomDeriver
    implements nl.kallestruik.noisesampler.minecraft.RandomDeriver {
        private final long seed;

        public RandomDeriver(long seed) {
            this.seed = seed;
        }

        @Override
        public AbstractRandom createRandom(int x, int y, int z) {
            long l = MathHelper.hashCode(x, y, z);
            long m = l ^ this.seed;
            return new AtomicSimpleRandom(m);
        }

        @Override
        public AbstractRandom createRandom(String string) {
            int i = string.hashCode();
            return new AtomicSimpleRandom((long)i ^ this.seed);
        }

        @Override
        public void addDebugInfo(StringBuilder info) {
            info.append("LegacyPositionalRandomFactory{").append(this.seed).append("}");
        }
    }
}

