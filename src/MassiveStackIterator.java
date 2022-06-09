import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class MassiveStackIterator implements MassiveIterator {

    private Iterator<Massive> stackIterator;

    public MassiveStackIterator(Stack<Massive> stack){
        this.stackIterator = stack.iterator();
    }

    public boolean hasNext(){
        return this.stackIterator.hasNext();
    }

    public Massive next(){
        if(!this.stackIterator.hasNext()) throw new NoSuchElementException();
        return this.stackIterator.next();
    }
}
