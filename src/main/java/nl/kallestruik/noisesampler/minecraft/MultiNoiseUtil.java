/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package nl.kallestruik.noisesampler.minecraft;

public class MultiNoiseUtil {
    private static final boolean field_34477 = false;
    private static final float field_35359 = 10000.0f;
    protected static final int HYPERCUBE_DIMENSION = 7;

    public static NoiseValuePoint createNoiseValuePoint(float temperatureNoise, float humidityNoise, float continentalnessNoise, float erosionNoise, float depth, float weirdnessNoise) {
        return new NoiseValuePoint(MultiNoiseUtil.method_38665(temperatureNoise), MultiNoiseUtil.method_38665(humidityNoise), MultiNoiseUtil.method_38665(continentalnessNoise), MultiNoiseUtil.method_38665(erosionNoise), MultiNoiseUtil.method_38665(depth), MultiNoiseUtil.method_38665(weirdnessNoise));
    }

    public static NoiseHypercube createNoiseHypercube(float temperature, float humidity, float continentalness, float erosion, float depth, float weirdness, float offset) {
        return new NoiseHypercube(ParameterRange.of(temperature), ParameterRange.of(humidity), ParameterRange.of(continentalness), ParameterRange.of(erosion), ParameterRange.of(depth), ParameterRange.of(weirdness), MultiNoiseUtil.method_38665(offset));
    }

    public static NoiseHypercube createNoiseHypercube(ParameterRange temperature, ParameterRange humidity, ParameterRange continentalness, ParameterRange erosion, ParameterRange depth, ParameterRange weirdness, float offset) {
        return new NoiseHypercube(temperature, humidity, continentalness, erosion, depth, weirdness, MultiNoiseUtil.method_38665(offset));
    }

    public static long method_38665(float f) {
        return (long)(f * 10000.0f);
    }

    public static float method_38666(long l) {
        return (float)l / 10000.0f;
    }

    public record NoiseValuePoint(long temperatureNoise, long humidityNoise, long continentalnessNoise, long erosionNoise, long depth, long weirdnessNoise) {
        protected long[] getNoiseValueList() {
            return new long[]{this.temperatureNoise, this.humidityNoise, this.continentalnessNoise, this.erosionNoise, this.depth, this.weirdnessNoise, 0L};
        }
    }

    public record NoiseHypercube(ParameterRange temperature, ParameterRange humidity, ParameterRange continentalness, ParameterRange erosion, ParameterRange depth, ParameterRange weirdness, long offset) {

       long getSquaredDistance(NoiseValuePoint point) {
            return MathHelper.square(this.temperature.getDistance(point.temperatureNoise)) + MathHelper.square(this.humidity.getDistance(point.humidityNoise)) + MathHelper.square(this.continentalness.getDistance(point.continentalnessNoise)) + MathHelper.square(this.erosion.getDistance(point.erosionNoise)) + MathHelper.square(this.depth.getDistance(point.depth)) + MathHelper.square(this.weirdness.getDistance(point.weirdnessNoise)) + MathHelper.square(this.offset);
        }
    }

    public record ParameterRange(long min, long max) {
        public static ParameterRange of(float point) {
            return ParameterRange.of(point, point);
        }

        public static ParameterRange of(float min, float max) {
            if (min > max) {
                throw new IllegalArgumentException("min > max: " + min + " " + max);
            }
            return new ParameterRange(MultiNoiseUtil.method_38665(min), MultiNoiseUtil.method_38665(max));
        }

        public static ParameterRange combine(ParameterRange min, ParameterRange max) {
            if (min.min() > max.max()) {
                throw new IllegalArgumentException("min > max: " + min + " " + max);
            }
            return new ParameterRange(min.min(), max.max());
        }

        @Override
        public String toString() {
            return this.min == this.max ? String.format("%d", this.min) : String.format("[%d-%d]", this.min, this.max);
        }

        public long getDistance(long noise) {
            long m = noise - this.max;
            long n = this.min - noise;
            if (m > 0L) {
                return m;
            }
            return Math.max(n, 0L);
        }

        public long getDistance(ParameterRange other) {
            long l = other.min() - this.max;
            long m = this.min - other.max();
            if (l > 0L) {
                return l;
            }
            return Math.max(m, 0L);
        }

        public ParameterRange combine(ParameterRange other) {
            return other == null ? this : new ParameterRange(Math.min(this.min, other.min()), Math.max(this.max, other.max()));
        }
    }

    public static interface MultiNoiseSampler {
        public NoiseValuePoint sample(int var1, int var2, int var3);

        default public BlockPos findBestSpawnPosition() {
            return BlockPos.ORIGIN;
        }
    }

//    public static class Entries<T> {
//        private final List<Pair<NoiseHypercube, T>> entries;
//        private final SearchTree<T> tree;
//
//        public Entries(List<Pair<NoiseHypercube, T>> entries) {
//            this.entries = entries;
//            this.tree = SearchTree.create(entries);
//        }
//
//        public List<Pair<NoiseHypercube, T>> getEntries() {
//            return this.entries;
//        }
//
//        public T method_39529(NoiseValuePoint arg, T object) {
//            return this.method_39527(arg);
//        }
//
//        public T method_39530(NoiseValuePoint arg, T object) {
//            long l = Long.MAX_VALUE;
//            T object2 = object;
//            for (Pair<NoiseHypercube, T> pair : this.getEntries()) {
//                long m = pair.getFirst().getSquaredDistance(arg);
//                if (m >= l) continue;
//                l = m;
//                object2 = pair.getSecond();
//            }
//            return object2;
//        }
//
//        public T method_39527(NoiseValuePoint arg) {
//            return this.method_39528(arg, SearchTree.TreeNode::getSquaredDistance);
//        }
//
//        protected T method_39528(NoiseValuePoint arg, NodeDistanceFunction<T> arg2) {
//            return this.tree.get(arg, arg2);
//        }
//    }
//
//    protected static final class SearchTree<T> {
//        private static final int MAX_NODES_FOR_SIMPLE_TREE = 10;
//        private final MultiNoiseSampler.SearchTree.TreeNode<T> firstNode;
//        private final ThreadLocal<TreeLeafNode<T>> previousResultNode = new ThreadLocal();
//
//        private SearchTree(TreeNode<T> firstNode) {
//            this.firstNode = firstNode;
//        }
//
//        public static <T> SearchTree<T> create(List<Pair<NoiseHypercube, T>> entries) {
//            if (entries.isEmpty()) {
//                throw new IllegalArgumentException("Need at least one value to build the search tree.");
//            }
//            int i = entries.get(0).getFirst().getParameters().size();
//            if (i != 7) {
//                throw new IllegalStateException("Expecting parameter space to be 7, got " + i);
//            }
//            List list2 = entries.stream().map(entry -> new TreeLeafNode((NoiseHypercube)entry.getFirst(), entry.getSecond())).collect(Collectors.toCollection(ArrayList::new));
//            return new SearchTree<T>(SearchTree.createNode(i, list2));
//        }
//
//        public static <T> TreeNode<T> createNode(int parameterNumber, List<? extends TreeNode<T>> subTree) {
//            if (subTree.isEmpty()) {
//                throw new IllegalStateException("Need at least one child to build a node");
//            }
//            if (subTree.size() == 1) {
//                return subTree.get(0);
//            }
//            if (subTree.size() <= 10) {
//                subTree.sort(Comparator.comparingLong(node -> {
//                    long l = 0L;
//                    for (int j = 0; j < parameterNumber; ++j) {
//                        ParameterRange lv = node.parameters[j];
//                        l += Math.abs((lv.min() + lv.max()) / 2L);
//                    }
//                    return l;
//                }));
//                return new TreeBranchNode(subTree);
//            }
//            long l = Long.MAX_VALUE;
//            int j = -1;
//            List<TreeBranchNode<T>> list2 = null;
//            for (int k = 0; k < parameterNumber; ++k) {
//                SearchTree.sortTree(subTree, parameterNumber, k, false);
//                List<TreeBranchNode<T>> list3 = SearchTree.getBatchedTree(subTree);
//                long m = 0L;
//                for (TreeBranchNode<T> lv : list3) {
//                    m += SearchTree.getRangeLengthSum(lv.parameters);
//                }
//                if (l <= m) continue;
//                l = m;
//                j = k;
//                list2 = list3;
//            }
//            SearchTree.sortTree(list2, parameterNumber, j, true);
//            return new TreeBranchNode(list2.stream().map(node -> SearchTree.createNode(parameterNumber, Arrays.asList(node.subTree))).collect(Collectors.toList()));
//        }
//
//        private static <T> void sortTree(List<? extends TreeNode<T>> subTree, int parameterNumber, int currentParameter, boolean abs) {
//            Comparator<TreeNode<TreeNode<T>>> comparator = SearchTree.createNodeComparator(currentParameter, abs);
//            for (int k = 1; k < parameterNumber; ++k) {
//                comparator = comparator.thenComparing(SearchTree.createNodeComparator((currentParameter + k) % parameterNumber, abs));
//            }
//            subTree.sort(comparator);
//        }
//
//        private static <T> Comparator<TreeNode<T>> createNodeComparator(int currentParameter, boolean abs) {
//            return Comparator.comparingLong(arg -> {
//                ParameterRange lv = arg.parameters[currentParameter];
//                long l = (lv.min() + lv.max()) / 2L;
//                return abs ? Math.abs(l) : l;
//            });
//        }
//
//        private static <T> List<TreeBranchNode<T>> getBatchedTree(List<? extends TreeNode<T>> nodes) {
//            ArrayList<TreeBranchNode<T>> list2 = Lists.newArrayList();
//            ArrayList<TreeNode<T>> list3 = Lists.newArrayList();
//            int i = (int)Math.pow(10.0, Math.floor(Math.log((double)nodes.size() - 0.01) / Math.log(10.0)));
//            for (TreeNode<T> lv : nodes) {
//                list3.add(lv);
//                if (list3.size() < i) continue;
//                list2.add(new TreeBranchNode(list3));
//                list3 = Lists.newArrayList();
//            }
//            if (!list3.isEmpty()) {
//                list2.add(new TreeBranchNode(list3));
//            }
//            return list2;
//        }
//
//        private static long getRangeLengthSum(ParameterRange[] parameters) {
//            long l = 0L;
//            for (ParameterRange lv : parameters) {
//                l += Math.abs(lv.max() - lv.min());
//            }
//            return l;
//        }
//
//        static <T> List<ParameterRange> getEnclosingParameters(List<? extends TreeNode<T>> subTree) {
//            if (subTree.isEmpty()) {
//                throw new IllegalArgumentException("SubTree needs at least one child");
//            }
//            int i = 7;
//            ArrayList<ParameterRange> list2 = Lists.newArrayList();
//            for (int j = 0; j < 7; ++j) {
//                list2.add(null);
//            }
//            for (TreeNode<T> lv : subTree) {
//                for (int k = 0; k < 7; ++k) {
//                    list2.set(k, lv.parameters[k].combine((ParameterRange)list2.get(k)));
//                }
//            }
//            return list2;
//        }
//
//        public T get(NoiseValuePoint point, NodeDistanceFunction<T> distanceFunction) {
//            long[] ls = point.getNoiseValueList();
//            TreeLeafNode<T> lv = this.firstNode.getResultingNode(ls, this.previousResultNode.get(), distanceFunction);
//            this.previousResultNode.set(lv);
//            return lv.value;
//        }
//
//        static abstract class SearchTree.TreeNode<T> {
//            protected final ParameterRange[] parameters;
//
//            protected TreeNode(List<ParameterRange> parameters) {
//                this.parameters = parameters.toArray(new ParameterRange[0]);
//            }
//
//            protected abstract TreeLeafNode<T> getResultingNode(long[] var1, @Nullable TreeLeafNode<T> var2, NodeDistanceFunction<T> var3);
//
//            protected long getSquaredDistance(long[] otherParameters) {
//                long l = 0L;
//                for (int i = 0; i < 7; ++i) {
//                    l += MathHelper.square(this.parameters[i].getDistance(otherParameters[i]));
//                }
//                return l;
//            }
//
//            public String toString() {
//                return Arrays.toString(this.parameters);
//            }
//        }
//
//        static final class SearchTree.TreeBranchNode<T>
//        extends TreeNode<T> {
//            final TreeNode<T>[] subTree;
//
//            protected TreeBranchNode(List<? extends TreeNode<T>> list) {
//                this(SearchTree.getEnclosingParameters(list), list);
//            }
//
//            protected TreeBranchNode(List<ParameterRange> parameters, List<? extends TreeNode<T>> subTree) {
//                super(parameters);
//                this.subTree = subTree.toArray(new TreeNode[0]);
//            }
//
//            @Override
//            protected TreeLeafNode<T> getResultingNode(long[] otherParameters, @Nullable TreeLeafNode<T> alternative, NodeDistanceFunction<T> distanceFunction) {
//                long l = alternative == null ? Long.MAX_VALUE : distanceFunction.getDistance(alternative, otherParameters);
//                TreeLeafNode<T> lv = alternative;
//                for (TreeNode<T> lv2 : this.subTree) {
//                    long n;
//                    long m = distanceFunction.getDistance(lv2, otherParameters);
//                    if (l <= m) continue;
//                    TreeLeafNode<T> lv3 = lv2.getResultingNode(otherParameters, lv, distanceFunction);
//                    long l2 = n = lv2 == lv3 ? m : distanceFunction.getDistance(lv3, otherParameters);
//                    if (l <= n) continue;
//                    l = n;
//                    lv = lv3;
//                }
//                return lv;
//            }
//        }
//
//        static final class SearchTree.TreeLeafNode<T>
//        extends TreeNode<T> {
//            final T value;
//
//            TreeLeafNode(NoiseHypercube parameters, T value) {
//                super(parameters.getParameters());
//                this.value = value;
//            }
//
//            @Override
//            protected TreeLeafNode<T> getResultingNode(long[] otherParameters, @Nullable TreeLeafNode<T> alternative, NodeDistanceFunction<T> distanceFunction) {
//                return this;
//            }
//        }
//    }
}

