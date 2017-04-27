package cz.cuni.mff.d3s.trupple.language.runtime.customvalues;

import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.EnumTypeDescriptor;

import java.util.List;

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
            int intvalue = 0;

            if (index instanceof Long) {
                intvalue = ((Long)index).intValue();
            } else if (index instanceof Character) {
                intvalue = (int)((Character)index);
            } else if (index instanceof Boolean) {
                intvalue = ((Boolean) index)? 1 : 0;
            } else if (index instanceof EnumValue) {
                intvalue = ((EnumValue)index).getIntValue();
            }

            return intvalue - this.offset;
        }

        @Override
        public int getSize() {
            return this.size;
        }
    }

    public static class EnumPascalOrdinal extends PascalOrdinal {

        private final List<String> identifiers;

        public EnumPascalOrdinal(EnumTypeDescriptor enumTypeDescriptor) {
            this.identifiers = enumTypeDescriptor.getIdentifiers();
        }

        @Override
        public int getSize() {
            return this.identifiers.size();
        }

        @Override
        public int getRealIndex(Object index) {
            return this.identifiers.indexOf(((EnumValue)index).getValue());
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
