package cz.cuni.mff.d3s.trupple.language.customvalues;

public abstract class PascalOrdinal {
    public abstract int getRealIndex(Object index);
    public abstract int getSize();

    public static class RangePascalOrdinal extends PascalOrdinal {
        private final int offset;
        private final int size;

        public RangePascalOrdinal(int size, int offset) {
            this.offset = offset;
            this.size = size;
        }

        @Override
        public int getRealIndex(Object index) {
            // TODO: the problem here is that index is java.lang.Long
            return  (int)(long)index - this.offset;
        }

        @Override
        public int getSize() {
            return this.size;
        }
    }

    public static PascalOrdinal booleanPascalOrdinal = new BooleanPascalOrdinal();
    public static PascalOrdinal charPascalOrdinal = new CharPascalOrdinal();

    private static class BooleanPascalOrdinal extends PascalOrdinal {
        @Override
        public int getRealIndex(Object index) {
            return  ((boolean)index)? 1 : 0;
        }

        @Override
        public int getSize() {
            return 2;
        }
    }

    private static class CharPascalOrdinal extends PascalOrdinal {
        @Override
        public int getRealIndex(Object index) {
            return  (char)index;
        }

        @Override
        public int getSize() {
            return 256;
        }
    }

}
