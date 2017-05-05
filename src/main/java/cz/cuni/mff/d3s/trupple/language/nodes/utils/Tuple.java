package cz.cuni.mff.d3s.trupple.language.nodes.utils;

class Tuple<X, Y> {

    X first;
    Y second;

    Tuple(X first, Y second) {
        this.first = first;
        this.second = second;
    }

    X getKey() {
        return this.first;
    }

    Y getValue() {
        return this.second;
    }

}
