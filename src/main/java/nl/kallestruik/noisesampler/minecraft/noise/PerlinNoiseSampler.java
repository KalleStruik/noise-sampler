/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package nl.kallestruik.noisesampler.minecraft.noise;

import nl.kallestruik.noisesampler.minecraft.MathHelper;
import nl.kallestruik.noisesampler.minecraft.AbstractRandom;

public final class PerlinNoiseSampler {
    private static final float field_31701 = 1.0E-7f;
    private final byte[] permutations;
    public final double originX;
    public final double originY;
    public final double originZ;

    public PerlinNoiseSampler(AbstractRandom random) {
        int i;
        this.originX = random.nextDouble() * 256.0;
        this.originY = random.nextDouble() * 256.0;
        this.originZ = random.nextDouble() * 256.0;
        this.permutations = new byte[256];
        for (i = 0; i < 256; ++i) {
            this.permutations[i] = (byte)i;
        }
        for (i = 0; i < 256; ++i) {
            int j = random.nextInt(256 - i);
            byte b = this.permutations[i];
            this.permutations[i] = this.permutations[i + j];
            this.permutations[i + j] = b;
        }
    }

    public double sample(double x, double y, double z) {
        return this.sample(x, y, z, 0.0, 0.0);
    }

    @Deprecated
    public double sample(double x, double y, double z, double yScale, double yMax) {
        double s;
        double i = x + this.originX;
        double j = y + this.originY;
        double k = z + this.originZ;
        int l = MathHelper.floor(i);
        int m = MathHelper.floor(j);
        int n = MathHelper.floor(k);
        double o = i - (double)l;
        double p = j - (double)m;
        double q = k - (double)n;
        if (yScale != 0.0) {
            double r = yMax >= 0.0 && yMax < p ? yMax : p;
            s = (double)MathHelper.floor(r / yScale + (double)1.0E-7f) * yScale;
        } else {
            s = 0.0;
        }
        return this.sample(l, m, n, o, p - s, q, p);
    }

    public double sampleDerivative(double x, double y, double z, double[] ds) {
        double g = x + this.originX;
        double h = y + this.originY;
        double i = z + this.originZ;
        int j = MathHelper.floor(g);
        int k = MathHelper.floor(h);
        int l = MathHelper.floor(i);
        double m = g - (double)j;
        double n = h - (double)k;
        double o = i - (double)l;
        return this.sampleDerivative(j, k, l, m, n, o, ds);
    }

    private static double grad(int hash, double x, double y, double z) {
        return SimplexNoiseSampler.dot(SimplexNoiseSampler.GRADIENTS[hash & 0xF], x, y, z);
    }

    private int getGradient(int hash) {
        return this.permutations[hash & 0xFF] & 0xFF;
    }

    private double sample(int sectionX, int sectionY, int sectionZ, double localX, double localY, double localZ, double fadeLocalX) {
        int l = this.getGradient(sectionX);
        int m = this.getGradient(sectionX + 1);
        int n = this.getGradient(l + sectionY);
        int o = this.getGradient(l + sectionY + 1);
        int p = this.getGradient(m + sectionY);
        int q = this.getGradient(m + sectionY + 1);
        double h = PerlinNoiseSampler.grad(this.getGradient(n + sectionZ), localX, localY, localZ);
        double r = PerlinNoiseSampler.grad(this.getGradient(p + sectionZ), localX - 1.0, localY, localZ);
        double s = PerlinNoiseSampler.grad(this.getGradient(o + sectionZ), localX, localY - 1.0, localZ);
        double t = PerlinNoiseSampler.grad(this.getGradient(q + sectionZ), localX - 1.0, localY - 1.0, localZ);
        double u = PerlinNoiseSampler.grad(this.getGradient(n + sectionZ + 1), localX, localY, localZ - 1.0);
        double v = PerlinNoiseSampler.grad(this.getGradient(p + sectionZ + 1), localX - 1.0, localY, localZ - 1.0);
        double w = PerlinNoiseSampler.grad(this.getGradient(o + sectionZ + 1), localX, localY - 1.0, localZ - 1.0);
        double x = PerlinNoiseSampler.grad(this.getGradient(q + sectionZ + 1), localX - 1.0, localY - 1.0, localZ - 1.0);
        double y = MathHelper.perlinFade(localX);
        double z = MathHelper.perlinFade(fadeLocalX);
        double aa = MathHelper.perlinFade(localZ);
        return MathHelper.lerp3(y, z, aa, h, r, s, t, u, v, w, x);
    }

    private double sampleDerivative(int sectionX, int sectionY, int sectionZ, double localX, double localY, double localZ, double[] ds) {
        int l = this.getGradient(sectionX);
        int m = this.getGradient(sectionX + 1);
        int n = this.getGradient(l + sectionY);
        int o = this.getGradient(l + sectionY + 1);
        int p = this.getGradient(m + sectionY);
        int q = this.getGradient(m + sectionY + 1);
        int r = this.getGradient(n + sectionZ);
        int s = this.getGradient(p + sectionZ);
        int t = this.getGradient(o + sectionZ);
        int u = this.getGradient(q + sectionZ);
        int v = this.getGradient(n + sectionZ + 1);
        int w = this.getGradient(p + sectionZ + 1);
        int x = this.getGradient(o + sectionZ + 1);
        int y = this.getGradient(q + sectionZ + 1);
        int[] is = SimplexNoiseSampler.GRADIENTS[r & 0xF];
        int[] js = SimplexNoiseSampler.GRADIENTS[s & 0xF];
        int[] ks = SimplexNoiseSampler.GRADIENTS[t & 0xF];
        int[] ls = SimplexNoiseSampler.GRADIENTS[u & 0xF];
        int[] ms = SimplexNoiseSampler.GRADIENTS[v & 0xF];
        int[] ns = SimplexNoiseSampler.GRADIENTS[w & 0xF];
        int[] os = SimplexNoiseSampler.GRADIENTS[x & 0xF];
        int[] ps = SimplexNoiseSampler.GRADIENTS[y & 0xF];
        double g = SimplexNoiseSampler.dot(is, localX, localY, localZ);
        double h = SimplexNoiseSampler.dot(js, localX - 1.0, localY, localZ);
        double z = SimplexNoiseSampler.dot(ks, localX, localY - 1.0, localZ);
        double aa = SimplexNoiseSampler.dot(ls, localX - 1.0, localY - 1.0, localZ);
        double ab = SimplexNoiseSampler.dot(ms, localX, localY, localZ - 1.0);
        double ac = SimplexNoiseSampler.dot(ns, localX - 1.0, localY, localZ - 1.0);
        double ad = SimplexNoiseSampler.dot(os, localX, localY - 1.0, localZ - 1.0);
        double ae = SimplexNoiseSampler.dot(ps, localX - 1.0, localY - 1.0, localZ - 1.0);
        double af = MathHelper.perlinFade(localX);
        double ag = MathHelper.perlinFade(localY);
        double ah = MathHelper.perlinFade(localZ);
        double ai = MathHelper.lerp3(af, ag, ah, is[0], js[0], ks[0], ls[0], ms[0], ns[0], os[0], ps[0]);
        double aj = MathHelper.lerp3(af, ag, ah, is[1], js[1], ks[1], ls[1], ms[1], ns[1], os[1], ps[1]);
        double ak = MathHelper.lerp3(af, ag, ah, is[2], js[2], ks[2], ls[2], ms[2], ns[2], os[2], ps[2]);
        double al = MathHelper.lerp2(ag, ah, h - g, aa - z, ac - ab, ae - ad);
        double am = MathHelper.lerp2(ah, af, z - g, ad - ab, aa - h, ae - ac);
        double an = MathHelper.lerp2(af, ag, ab - g, ac - h, ad - z, ae - aa);
        double ao = MathHelper.perlinFadeDerivative(localX);
        double ap = MathHelper.perlinFadeDerivative(localY);
        double aq = MathHelper.perlinFadeDerivative(localZ);
        double ar = ai + ao * al;
        double as = aj + ap * am;
        double at = ak + aq * an;
        ds[0] = ds[0] + ar;
        ds[1] = ds[1] + as;
        ds[2] = ds[2] + at;
        return MathHelper.lerp3(af, ag, ah, g, h, z, aa, ab, ac, ad, ae);
    }
}

