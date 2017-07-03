package cz.cuni.mff.d3s.trupple.language.nodes.utils;

/**
 * Representation of read only binary tuple with heterogeneous values.
 */
class Tuple<X, Y> {

    private X first;
    private Y second;

    Tuple(X first, Y second) {
        this.first = first;
        this.second = second;
    }

    X getFirst() {
        return this.first;
    }

    Y getSecond() {
        return this.second;
    }

}
