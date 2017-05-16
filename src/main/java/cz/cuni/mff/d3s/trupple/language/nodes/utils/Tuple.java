package cz.cuni.mff.d3s.trupple.language.nodes.utils;

class Tuple<X, Y> {

    X first;
    Y second;

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
