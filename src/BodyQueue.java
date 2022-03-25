import java.util.Arrays;

// A queue of bodies. A collection designed for holding bodies prior to processing.
// The bodies of the queue can be accessed in a FIFO (first-in-first-out) manner,
// i.e., the body that was first inserted by 'add' is retrieved first by 'poll'.
// The number of elements of the queue is not limited.
//
public class BodyQueue {

    /** Array with all bodies of this queue */
    private Body[] queue;

    /** Pointer to the next free index of the queue, starting at 0 */
    private int queuePointer;

    // Initializes this queue with an initial capacity.
    // Precondition: initialCapacity > 0.
    public BodyQueue(int initialCapacity) {
        this.queue = new Body[initialCapacity];
        this.queuePointer = 0;
    }

    // Initializes this queue as an independent copy of the specified queue.
    // Calling methods of this queue will not affect the specified queue
    // and vice versa.
    // Precondition: q != null.
    public BodyQueue(BodyQueue q) {
        this.queue = Arrays.copyOf(q.queue, q.queue.length);
        this.queuePointer = q.queuePointer;
    }

    // Adds the specified body 'b' to this queue.
    public void add(Body b) {

        // is the queue full? extend the array
        if(this.queuePointer >= this.queue.length){
            this.queue = Arrays.copyOf(this.queue, this.queue.length * 2);
        }

        // right-shift elements
        for(int i = this.queuePointer; i > 0; i--) this.queue[i] = this.queue[i-1];
        this.queue[0] = b;
        this.queuePointer++;
    }

    // Retrieves and removes the head of this queue, or returns 'null'
    // if this queue is empty.
    public Body poll() {

        // is queue empty / pointer at 0? return null
        if(this.queuePointer == 0) return null;
        else {

            // get body at last filled index - decrement pointer for this
            Body result = this.queue[--this.queuePointer];

            // free this index and return result
            this.queue[this.queuePointer] = null;
            return result;
        }
    }

    // Returns the number of bodies in this queue.
    public int size() {

        // queue pointer points to the next free slot => equal to count of elements
        return this.queuePointer;
    }
}
