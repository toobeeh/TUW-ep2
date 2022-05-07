// A list of massive objects implemented as a linked list.

import java.util.Iterator;

/**
 * A node for the linked body list
 */
class MyMassiveNode {

    public Massive value;
    public MyMassiveNode next;
    public MyMassiveNode previous;

    public MyMassiveNode(Massive value, MyMassiveNode previous, MyMassiveNode next) {
        this.value = value;
        this.next = next;
        this.previous = previous;
    }
}

/*
    Yes this is almost only copy-pasted
 */

// The number of elements of the list is not limited.
public class MassiveLinkedList implements Iterable<Massive>{

    public MyMassiveNode head;

    // Initializes 'this' as an empty list.
    public MassiveLinkedList() {

        // useless
        this.head = null;
    }

    // inner class for iterator of this list
    class MLLIterator implements Iterator<Massive> {
        MyMassiveNode nextNode = null;

        public MLLIterator(MyMassiveNode startNode){
            this.nextNode = startNode;
        }

        public Massive next() {
            Massive next = this.nextNode.value;
            this.nextNode = this.nextNode.next;
            return next;
        }

        public boolean hasNext() {
            return this.nextNode != null;
        }
    }

    public Iterator<Massive> iterator(){
        return new MassiveLinkedList.MLLIterator(this.head);
    }

    // Initializes 'this' as an independent copy of the specified list 'list'.
    // Calling methods of this list will not affect the specified list 'list'
    // and vice versa.
    // Precondition: list != null.
    public MassiveLinkedList(BodyLinkedList list) {

        // loop through list and create a new list entry for every existing
        for(Massive entry : list){
            this.addLast(entry);
        }
    }

    // Inserts the specified element 'body' at the beginning of this list.
    public void addFirst(Massive body) {

        // create a new node with current head as next and set as new list head
        this.head = new MyMassiveNode(body, null, this.head);

        // update old head's previous node, if present
        if(this.head.next != null) this.head.next.previous = this.head;
    }
    // gets the last node of the list
    private MyMassiveNode getlastNode(){

        // traverse to last node (next == null)
        MyMassiveNode last = this.head;
        while(last != null && last.next != null) last = last.next;

        return last;
    }

    // Appends the specified element 'body' to the end of this list.
    public void addLast(Massive body) {

        // add new node to last
        MyMassiveNode last = this.getlastNode();
        if(last != null) last.next = new MyMassiveNode(body, last, null);
        else this.head = new MyMassiveNode(body, null, null);
    }

    // Returns the last element in this list.
    // Returns 'null' if the list is empty.
    public Massive getLast() {

        if (this.head == null) return null;
        else return this.getlastNode().value;
    }

    // Returns the first element in this list.
    // Returns 'null' if the list is empty.
    public Massive getFirst() {

        if (this.head == null) return null;
        else return this.head.value;
    }

    // Retrieves and removes the first element in this list.
    // Returns 'null' if the list is empty.
    public Massive pollFirst() {

        // get head and skip if not null
        MyMassiveNode head = this.head;

        // if list has a head
        if (head != null) {
            this.head = this.head.next;
            if (this.head != null) this.head.previous = null;
            return head.value;
        }

        // if list empty
        else return null;
    }

    // Retrieves and removes the last element in this list.
    // Returns 'null' if the list is empty.
    public Massive pollLast() {

        // get last node
        MyMassiveNode last = this.getlastNode();

        // if list empty
        if(last == null) return null;

        else {

            //if head is last, set list to empty
            if(last.previous == null) this.head = null;

                // else remove ref from previous
            else last.previous.next = null;

            return last.value;
        }
    }

    // Inserts the specified element at the specified position in this list.
    // Precondition: i >= 0 && i <= size().
    public void add(int i, Massive body) {

        // if empty list - insert at 0 because of precondition
        if(this.head == null){
            this.head = new MyMassiveNode(body, null, null);
        }

        // else insert at position
        else {

            // find h-th element (index before i)
            MyMassiveNode hth = this.head;
            int h = 0;

            while(h < i - 1) {
                hth = hth.next;
                h++;
            }

            // create new node and update pre/next
            MyMassiveNode insert = new MyMassiveNode(body, hth, hth.next);
            if(insert.next != null) insert.next.previous = insert;
            insert.previous.next = insert;
        }
    }

    // Returns the element at the specified position in this list.
    // Precondition: i >= 0 && i < size().
    public Massive get(int i) {

        // find i-th element
        MyMassiveNode ith = this.head;
        while(i > 0) {
            ith = ith.next;
            i--;
        }

        return ith.value;
    }

    // Returns the index of the first occurrence of the specified element in this list, or -1 if
    // this list does not contain the element.
    public int indexOf(Massive body) {

        MyMassiveNode check = this.head;
        int found = -1;
        int index = 0;

        // while there is a next element in the list
        while (check != null) {

            // check if body matches
            if(check.value.equals(body)) {
                found = index;
                break;
            }

            // increment index and set nextcheck
            index++;
            check = check.next;
        }

        return found;
    }

    // Returns the number of elements in this list.
    public int size() {

        // count elements :-)
        int count = 0;
        MyMassiveNode next = this.head;

        while(next != null){
            count++;
            next = next.next;
        }

        return count;
    }
}
