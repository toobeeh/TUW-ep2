import codedraw.CodeDraw;

import java.util.Iterator;

public class MyMassiveKeySet implements MassiveSet {

    private MassiveForceTreeMap tree;

    public MyMassiveKeySet(MassiveForceTreeMap tree){
        this.tree = tree;
    }

    public boolean contains(Massive key){
        return this.tree.containsKey(key);
    }

    public MassiveIterator iterator(){
        return this.tree.iterator();
    }

    public void remove(Massive element){
        this.tree.remove(element);
    }

    public void clear(){
        this.tree.clear();
    }

    public int size(){
        return this.tree.count();
    }

    public MassiveLinkedList toList(){

        MassiveLinkedList list = new MassiveLinkedList();

        for(Massive m : this.tree) list.addLast(m);
        return list;
    }

    public void draw(CodeDraw cd){

        for(Massive m : this.tree) m.draw(cd);
    }

}
