import java.util.Stack;

public class MyMassiveStackRemovableIterator implements MassiveIterator {

    MyMassiveRemovable abstractParent;
    MassiveStackIterator stackIterator;

    public MyMassiveStackRemovableIterator(Stack<Massive> stack, MyMassiveRemovable abstractParent){
        this.stackIterator = new MassiveStackIterator(stack);
        this.abstractParent = abstractParent;
    }

    private Massive removable;

    @Override
    public void remove(){
        if(this.removable == null) throw new IllegalStateException();

        this.abstractParent.remove(this.removable);
        this.removable = null;
    }

    @Override
    public Massive next(){
        this.removable = this.stackIterator.next();
        return this.removable;
    }

    @Override
    public boolean hasNext(){
        return this.stackIterator.hasNext();
    }
}
