/**
 * A node for the linked body list
 */
class MyBodyNode {

    public Body value;
    public MyBodyNode next;
    public MyBodyNode previous;

    /**
     * Create a new instance. Properties are priavte access, since this class can only be accessed within bodylinkedlist.
     * @param value
     * @param next
     * @param previous
     */
    public MyBodyNode(Body value, MyBodyNode previous, MyBodyNode next) {
        this.value = value;
        this.next = next;
        this.previous = previous;
    }
}

// A list of bodies implemented as a linked list.
// The number of elements of the list is not limited.
public class BodyLinkedList {

    private MyBodyNode head;

    // Initializes 'this' as an empty list.
    public BodyLinkedList() {

        // actually useless
        this.head = null;
    }

    // Initializes 'this' as an independent copy of the specified list 'list'.
    // Calling methods of this list will not affect the specified list 'list'
    // and vice versa.
    // Precondition: list != null.
    public BodyLinkedList(BodyLinkedList list) {

        // loop through list and create a new list entry for every existing
        MyBodyNode copy = list.head;
        while(copy != null){
            this.addLast(copy.value);
            copy = copy.next;
        }
    }

    // Inserts the specified element 'body' at the beginning of this list.
    public void addFirst(Body body) {

        // create a new node with current head as next and set as new list head
        this.head = new MyBodyNode(body, null, this.head);

        // update old head's previous node, if present
        if(this.head.next != null) this.head.next.previous = this.head;
    }

    // gets the last node of the list
    private MyBodyNode getlastNode(){

        // traverse to last node (next == null)
        MyBodyNode last = this.head;
        while(last != null && last.next != null) last = last.next;

        return last;
    }

    // Appends the specified element 'body' to the end of this list.
    public void addLast(Body body) {

        // add new node to last
        MyBodyNode last = this.getlastNode();
        if(last != null) last.next = new MyBodyNode(body, last, null);
        else this.head = new MyBodyNode(body, null, null);
    }

    // Returns the last element in this list.
    // Returns 'null' if the list is empty.
    public Body getLast() {

        if (this.head == null) return null;
        else return this.getlastNode().value;
    }

    // Returns the first element in this list.
    // Returns 'null' if the list is empty.
    public Body getFirst() {

        if (this.head == null) return null;
        else return this.head.value;
    }

    // Retrieves and removes the first element in this list.
    // Returns 'null' if the list is empty.
    public Body pollFirst() {

        // get head and skip if not null
        MyBodyNode head = this.head;

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
    public Body pollLast() {

        // get last node
        MyBodyNode last = this.getlastNode();

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

    // Inserts the specified element 'body' at the specified position in this list.
    // Precondition: i >= 0 && i <= size().
    public void add(int i, Body body) {

        // if empty list - insert at 0 because of precondition
        if(this.head == null){
            this.head = new MyBodyNode(body, null, null);
        }

        // else insert at position
        else {

            // find h-th element (index before i)
            MyBodyNode hth = this.head;
            int h = 0;

            while(h < i - 1) {
                hth = hth.next;
                h++;
            }

            // create new node and update pre/next
            MyBodyNode insert = new MyBodyNode(body, hth, hth.next);
            if(insert.next != null) insert.next.previous = insert;
            insert.previous.next = insert;
        }
    }

    // Returns the element at the specified position in this list.
    // Precondition: i >= 0 && i < size().
    public Body get(int i) {

        // find i-th element
        MyBodyNode ith = this.head;
        while(i > 0) {
            ith = ith.next;
            i--;
        }

        return ith.value;
    }

    // Returns the index of the first occurrence of the specified element in this list, or -1 if
    // this list does not contain the element.
    public int indexOf(Body body) {

        MyBodyNode check = this.head;
        int found = -1;
        int index = 0;

        // while there is a next element in the list
        while (check != null) {

            // check if body matches
            if(check.value == body) {
                found = index;
                break;
            }

            // increment index and set nextcheck
            index++;
            check = check.next;
        }

        return found;
    }

    // Removes all bodies of this list, which are colliding with the specified
    // body. Returns a list with all the removed bodies.
    public BodyLinkedList removeCollidingWith(Body body) {

        BodyLinkedList collides = new BodyLinkedList();

        // loop through nodes
        MyBodyNode check = this.head;
        while(check != null){
            boolean collidesWith = body.distanceTo(check.value) < body.radius() + check.value.radius();

            // if collides, remove reference from list and add to collides list
            if(collidesWith){
                collides.addLast(check.value);
                check.previous.next = check.next;
            }

            check = check.next;
        }

        return collides;
    }

    // Returns the number of bodies in this list.
    public int size() {

        // count elements :-)
        int count = 0;
        MyBodyNode next = this.head;

        while(next != null){
            count++;
            next = next.next;
        }

        return count;
    }
}
