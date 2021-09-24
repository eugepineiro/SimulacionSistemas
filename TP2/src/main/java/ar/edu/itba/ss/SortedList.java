package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.Comparator;

public class SortedList<T> extends ArrayList<T> {

    final private Comparator<T> comparator;

    public SortedList(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    @Override
    public boolean add(T t) {
        int idx = 0;
        while ( idx < size() && comparator.compare(t, get(idx)) >= 0) {
            idx++;
        }
        super.add(idx, t);
        return true;
    }
}
