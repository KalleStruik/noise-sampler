/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package nl.kallestruik.noisesampler.minecraft;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Longs;

public class Xoroshiro128PlusPlusRandom {
    private Xoroshiro128PlusPlusRandomImpl implementation;

    public Xoroshiro128PlusPlusRandom(long seed) {
        this.implementation = new Xoroshiro128PlusPlusRandomImpl(createXoroshiroSeed(seed));
    }

    public Xoroshiro128PlusPlusRandom(long seedLo, long seedHi) {
        this.implementation = new Xoroshiro128PlusPlusRandomImpl(seedLo, seedHi);
    }

    public RandomDeriver createRandomDeriver() {
        return new RandomDeriver(this.implementation.next(), this.implementation.next());
    }

    public int nextInt() {
        return (int)this.implementation.next();
    }

    public int nextInt(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("Bound must be positive");
        }
        long l = Integer.toUnsignedLong(this.nextInt());
        long m = l * (long)i;
        long n = m & 0xFFFFFFFFL;
        if (n < (long)i) {
            int j = Integer.remainderUnsigned(~i + 1, i);
            while (n < (long)j) {
                l = Integer.toUnsignedLong(this.nextInt());
                m = l * (long)i;
                n = m & 0xFFFFFFFFL;
            }
        }
        long j = m >> 32;
        return (int)j;
    }

    public double nextDouble() {
        return (double)this.next(53) * (double)1.110223E-16f;
    }

    public void skip(int count) {
        for (int j = 0; j < count; ++j) {
            this.implementation.next();
        }
    }

    private long next(int bits) {
        return this.implementation.next() >>> 64 - bits;
    }

    public static class RandomDeriver {
        private static final HashFunction MD5_HASHER = Hashing.md5();
        private final long seedLo;
        private final long seedHi;

        public RandomDeriver(long seedLo, long seedHi) {
            this.seedLo = seedLo;
            this.seedHi = seedHi;
        }

        public Xoroshiro128PlusPlusRandom createRandom(String string) {
            byte[] bs = MD5_HASHER.hashString(string, Charsets.UTF_8).asBytes();
            long l = Longs.fromBytes(bs[0], bs[1], bs[2], bs[3], bs[4], bs[5], bs[6], bs[7]);
            long m = Longs.fromBytes(bs[8], bs[9], bs[10], bs[11], bs[12], bs[13], bs[14], bs[15]);
            return new Xoroshiro128PlusPlusRandom(l ^ this.seedLo, m ^ this.seedHi);
        }
    }

    public static long nextSplitMix64Int(long seed) {
        seed = (seed ^ seed >>> 30) * -4658895280553007687L;
        seed = (seed ^ seed >>> 27) * -7723592293110705685L;
        return seed ^ seed >>> 31;
    }

    public static XoroshiroSeed createXoroshiroSeed(long seed) {
        long m = seed ^ 0x6A09E667F3BCC909L;
        long n = m + -7046029254386353131L;
        return new XoroshiroSeed(nextSplitMix64Int(m), nextSplitMix64Int(n));
    }

    public record XoroshiroSeed(long seedLo, long seedHi) {
    }
}

