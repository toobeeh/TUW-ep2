import java.util.Iterator;
import java.util.NoSuchElementException;

// An iterator over elements of 'Massive'.
//
public interface MassiveIterator extends Iterator<Massive> {

    @Override
    // Returns the next element in the iteration.
    // (Returns 'null' if the iteration has no more elements.)
    Massive next() throws NoSuchElementException;

    @Override
    // Returns 'true' if the iteration has more elements.
    boolean hasNext();
}
