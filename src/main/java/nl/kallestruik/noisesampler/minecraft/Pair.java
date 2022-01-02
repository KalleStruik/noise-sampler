/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package nl.kallestruik.noisesampler.minecraft;

public class Pair<A, B> {
    private A left;
    private B right;

    public Pair(A left, B right) {
        this.left = left;
        this.right = right;
    }

    public static <A, B> Pair<A, B> of(A a, B b) {
        return new Pair<>(a, b);
    }

    public A getLeft() {
        return this.left;
    }

    public void setLeft(A left) {
        this.left = left;
    }

    public B getRight() {
        return this.right;
    }

    public void setRight(B right) {
        this.right = right;
    }
}

