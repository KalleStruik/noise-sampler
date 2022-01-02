/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package nl.kallestruik.noisesampler.minecraft;

import java.util.Random;
import java.util.function.LongFunction;

public class ChunkRandom
extends Random
implements AbstractRandom {
    private final AbstractRandom baseRandom;
    private int sampleCount;

    public ChunkRandom(AbstractRandom baseRandom) {
        super(0L);
        this.baseRandom = baseRandom;
    }

    public int getSampleCount() {
        return this.sampleCount;
    }

    @Override
    public AbstractRandom derive() {
        return this.baseRandom.derive();
    }

    @Override
    public RandomDeriver createRandomDeriver() {
        return this.baseRandom.createRandomDeriver();
    }

    @Override
    public int next(int count) {
        ++this.sampleCount;
        AbstractRandom abstractRandom = this.baseRandom;
        if (abstractRandom instanceof AtomicSimpleRandom) {
            AtomicSimpleRandom lv = (AtomicSimpleRandom)abstractRandom;
            return lv.next(count);
        }
        return (int)(this.baseRandom.nextLong() >>> 64 - count);
    }

    @Override
    public synchronized void setSeed(long l) {
        if (this.baseRandom == null) {
            return;
        }
        this.baseRandom.setSeed(l);
    }

    public long setPopulationSeed(long worldSeed, int blockX, int blockZ) {
        this.setSeed(worldSeed);
        long m = this.nextLong() | 1L;
        long n = this.nextLong() | 1L;
        long o = (long)blockX * m + (long)blockZ * n ^ worldSeed;
        this.setSeed(o);
        return o;
    }

    public void setDecoratorSeed(long populationSeed, int index, int step) {
        long m = populationSeed + (long)index + (long)(10000 * step);
        this.setSeed(m);
    }

    public void setCarverSeed(long worldSeed, int chunkX, int chunkZ) {
        this.setSeed(worldSeed);
        long m = this.nextLong();
        long n = this.nextLong();
        long o = (long)chunkX * m ^ (long)chunkZ * n ^ worldSeed;
        this.setSeed(o);
    }

    public void setRegionSeed(long worldSeed, int regionX, int regionZ, int salt) {
        long m = (long)regionX * 341873128712L + (long)regionZ * 132897987541L + worldSeed + (long)salt;
        this.setSeed(m);
    }

    public static Random getSlimeRandom(int chunkX, int chunkZ, long worldSeed, long scrambler) {
        return new Random(worldSeed + (long)(chunkX * chunkX * 4987142) + (long)(chunkX * 5947611) + (long)(chunkZ * chunkZ) * 4392871L + (long)(chunkZ * 389711) ^ scrambler);
    }

    public static enum RandomProvider {
        LEGACY(AtomicSimpleRandom::new),
        XOROSHIRO(Xoroshiro128PlusPlusRandom::new);

        private final LongFunction<AbstractRandom> provider;

        private RandomProvider(LongFunction<AbstractRandom> provider) {
            this.provider = provider;
        }

        public AbstractRandom create(long seed) {
            return this.provider.apply(seed);
        }
    }
}

