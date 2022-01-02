/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package nl.kallestruik.noisesampler.minecraft;

import java.util.ArrayList;
import java.util.List;

public interface Spline<C>
extends ToFloatFunction<C> {
    public String getDebugString();

    public static <C> Spline<C> fixedFloatFunction(float value) {
        return new FixedFloatFunction(value);
    }

    public static <C> Builder<C> builder(ToFloatFunction<C> locationFunction) {
        return new Builder<C>(locationFunction);
    }

    public static <C> Builder<C> builder(ToFloatFunction<C> locationFunction, ToFloatFunction<Float> arg2) {
        return new Builder<C>(locationFunction, arg2);
    }

    public record FixedFloatFunction<C>(float value) implements Spline<C>
    {
        @Override
        public float apply(C object) {
            return this.value;
        }

        @Override
        public String getDebugString() {
            return String.format("k=%.3f", Float.valueOf(this.value));
        }
    }

    public static final class Builder<C> {
        private final ToFloatFunction<C> locationFunction;
        private final ToFloatFunction<Float> field_35661;
        private final List<Float> locations = new ArrayList<>();
        private final List<Spline<C>> values = new ArrayList<>();
        private final List<Float> derivatives = new ArrayList<>();

        protected Builder(ToFloatFunction<C> locationFunction) {
            this(locationFunction, float_ -> float_.floatValue());
        }

        protected Builder(ToFloatFunction<C> locationFunction, ToFloatFunction<Float> arg2) {
            this.locationFunction = locationFunction;
            this.field_35661 = arg2;
        }

        public Builder<C> add(float location, float value, float derivative) {
            return this.add(location, new FixedFloatFunction(this.field_35661.apply(Float.valueOf(value))), derivative);
        }

        public Builder<C> add(float location, Spline<C> value, float derivative) {
            if (!this.locations.isEmpty() && location <= this.locations.get(this.locations.size() - 1)) {
                throw new IllegalArgumentException("Please register points in ascending order");
            }
            this.locations.add(location);
            this.values.add(value);
            this.derivatives.add(derivative);
            return this;
        }

        public Spline<C> build() {
            if (this.locations.isEmpty()) {
                throw new IllegalStateException("No elements added");
            }
            return new class_6738<C>(this.locationFunction, this.locations, List.copyOf(this.values), this.derivatives);
        }
    }

    public record class_6738<C>(ToFloatFunction<C> coordinate, List<Float> locations, List<Spline<C>> values, List<Float> derivatives) implements Spline<C>
    {
        @Override
        public float apply(C object) {
            float f = this.coordinate.apply(object);
            int i2 = MathHelper.binarySearch(0, this.locations.size(), i -> f < this.locations.get(i)) - 1;
            int j = this.locations.size() - 1;
            if (i2 < 0) {
                return this.values.get(0).apply(object) + this.derivatives.get(0) * (f - this.locations.get(0));
            }
            if (i2 == j) {
                return this.values.get(j).apply(object) + this.derivatives.get(j) * (f - this.locations.get(j));
            }
            float g = this.locations.get(i2);
            float h = this.locations.get(i2 + 1);
            float k = (f - g) / (h - g);
            ToFloatFunction lv = this.values.get(i2);
            ToFloatFunction lv2 = this.values.get(i2 + 1);
            float l = this.derivatives.get(i2);
            float m = this.derivatives.get(i2 + 1);
            float n = lv.apply(object);
            float o = lv2.apply(object);
            float p = l * (h - g) - (o - n);
            float q = -m * (h - g) + (o - n);
            float r = MathHelper.lerp(k, n, o) + k * (1.0f - k) * MathHelper.lerp(k, p, q);
            return r;
        }

        @Override
        public String getDebugString() {
            return "";
        }


    }
}

