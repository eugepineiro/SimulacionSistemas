import java.util.List;

public class MultipleNResult<T> {
    final private long N;
    final private List<T> results;

    public MultipleNResult(long n, List<T> results) {
        N = n;
        this.results = results;
    }

    public long getN() {
        return N;
    }

    public List<T> getResults() {
        return results;
    }
}
