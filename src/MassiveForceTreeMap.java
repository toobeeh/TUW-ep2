import java.util.Iterator;
import java.util.Stack;

// A map that associates an object of 'Massive' with a Vector3. The number of key-value pairs
// is not limited.
//
// TODO: define further classes and methods for the binary search tree and the implementation
//  of MassiveSet, if needed.
//
public class MassiveForceTreeMap implements MassiveIterable, MyMassiveRemovable {

    // TODO: define missing parts of this class.
    private MyMassiveForceTreeNode root;

    // Adds a new key-value association to this map. If the key already exists in this map,
    // the value is replaced and the old value is returned. Otherwise 'null' is returned.
    // Precondition: key != null.
    public Vector3 put(Massive key, Vector3 value) {

        if(root != null) return root.add(key, value);
        else {
            root = new MyMassiveForceTreeNode(key, value, null);
            return null;
        }
    }

    // Returns the value associated with the specified key, i.e. the method returns the force vector
    // associated with the specified key. Returns 'null' if the key is not contained in this map.
    // Precondition: key != null.
    public Vector3 get(Massive key) {

        // TODO: implement method.
        return this.root == null ? null : this.root.get(key).value;
    }

    // Returns 'true' if this map contains a mapping for the specified key.
    //Precondition: key != null
    public boolean containsKey(Massive key) {

        // TODO: implement method.
        return this.root != null && this.root.has(key);
    }

    // Returns a readable representation of this map, in which key-value pairs are ordered
    // descending according to 'key.getMass()'.
    public String toString() {

        return this.root == null ? "Empty Tree" : this.root.toString();
    }

    // Returns a `MassiveSet` view of the keys contained in this tree map. Changing the
    // elements of the returned `MassiveSet` object also affects the keys in this tree map.
    public MassiveSet getKeys() {

        return new MyMassiveKeySet(this);
    }

    public MassiveIterator iterator(){
        Stack<Massive> massiveStack = new Stack<>();
        this.root.addToStack(massiveStack);
        return new MyMassiveStackRemovableIterator(massiveStack, this);
    }

    public void remove(Massive element){

        if(this.root == null) return;

        MyMassiveForceTreeNode node = this.root.get(element);

        this.root = node.remove(this.root);
    }

    public void clear(){
        if(this.root != null) this.root = null;
    }

    public int count(){
        return this.root == null ? 0 : this.root.count();
    }

}

/**
 * Tree node containing a left and right descendant, public accessible.
 */
class MyMassiveForceTreeNode {

    public MyMassiveForceTreeNode left, right, parent;
    public Massive key;
    public Vector3 value;

    public MyMassiveForceTreeNode(Massive key, Vector3 value, MyMassiveForceTreeNode parent) {
        this.key = key;
        this.value = value;
        this.parent = parent;
    }

    /**
     * deletes this node if it has no children or deletes & moves the next in-order node if it has children
     * @param originalRootNode the original root node of this tree
     * @return the new root node after removing
     */
    public MyMassiveForceTreeNode remove(MyMassiveForceTreeNode originalRootNode){

        // if it has no parent- it's the root node
        if(this.parent == null) {

            // if both children are unset, return null as new root node
            if(this.right == null && this.left == null) return null;

            // if only right, return right as new root node
            else if(this.right != null && this.left == null) {
                this.right.parent = null;
                return this.right;
            }

            // if only left, return left as new root node
            else if(this.left != null && this.right == null) {
                this.left.parent = null;
                return this.left;
            }

            // else if both children are set, move left branch to next in order of right
            else {
                MyMassiveForceTreeNode nextInOrder = this.right;
                while(nextInOrder.left != null) nextInOrder = nextInOrder.left;

                nextInOrder.left = this.left;
                this.left.parent = nextInOrder;

                return this.right;
            }

        }

        // else if it has a parent
        else {
            // if no children, clear reference from parent
            if(this.left == null && this.right == null){
                if(this.parent.left == this) this.parent.left = null;
                else this.parent.right = null;
            }

            // if only right child, skip this node
            else if(this.left == null){
                if(this.parent.left == this) this.parent.left = this.right;
                else this.parent.right = this.right;
                this.right.parent = this.parent;
            }

            // if only left child, skip this node
            else if(this.right == null){
                if(this.parent.left == this) this.parent.left = this.left;
                else this.parent.right = this.left;
                this.left.parent = this.parent;
            }

            // else need to find next in-order and replace with
            else {
                MyMassiveForceTreeNode nextInOrder = this.parent.right;
                while(nextInOrder.left != null) nextInOrder = nextInOrder.left;

                // if there's no next in order, move right branch to parent right
                if(nextInOrder == null) {
                    this.parent.right = this.right;
                    this.right.parent = this.parent;
                }

                // else delete inorder from original place and move it here
                else {
                    nextInOrder.remove(originalRootNode);
                    this.key = nextInOrder.key;
                    this.value = nextInOrder.value;
                }
            }

            return originalRootNode;
        }
    }

    /**
     * recursively counts all elements
     * @return
     */
    public int count(){
        return 1 + (this.left != null ? this.left.count() : 0) + (this.right != null ? this.right.count() : 0);
    }

    /**
     * Adds the node and its successors to a stack, inorder
     * @param stack the stack to add to
     */
    public void addToStack(Stack<Massive> stack){
        if(this.left != null) left.addToStack(stack);
        stack.add(this.key);
        if(this.right != null) right.addToStack(stack);
    }

    /**
     * Checks if the node or its descendants contain a massive key
     * @param key the massive to search for
     * @return boolean that indicates whether this body is part of the subtree
     */
    public boolean has(Massive key){

        // check this value, or right descendants, or left descendants
        return this.key.equals(key)
                || this.right != null && this.right.has(key)
                || this.left != null && this.left.has(key);
    }

    /**
     * Gets a string of the key-value pair, sorted descending
     * @return string in the format [mass ~ vector] ..
     */
    public String toString(){
        return (this.right != null ? this.right.toString() : "")
                + " [massive: " + this.key.toString() + " ~ vector: " + this.value.length() + "] "
                + (this.left != null ? this.left.toString() : "");
    }

    /**
     * searches for a key and returns the mathing node
     * @param key the massive key to search for
     * @return the found node or null if not found
     */
    public MyMassiveForceTreeNode get(Massive key){

        // return this value if key matches
        if(key == this.key) return this;

        // else look in descendants, if they exist
        else {
            if(key.mass() > this.key.mass()) return this.right == null ? null : this.right.get(key);
            else return this.left == null ? null : this.left.get(key);
        }
    }

    /**
     * adds a key-value pair to the subtree. if the key exists, the value is applied and the old value returned.
     * @param key the key to apply the value on
     * @param value new value
     * @return the old value, if replaced
     */
    public Vector3 add(Massive key, Vector3 value){

        Vector3 found = null;

        // is it the key? update
        if(key == this.key){
            found = this.value;
            this.value = value;
        }

        // look in descendants
        else {

            // if mass is bigger, set value of right node
            if(key.mass() > this.key.mass()){

                // does the node exist? if yes, add there
                if(this.right != null) found = this.right.add(key, value);

                    // else create new node with value
                else {
                    this.right = new MyMassiveForceTreeNode(key, value, this);
                }
            }

            // if mass smaller or equal, set value of left node
            else {

                // does the node exist? if yes, add there
                if(this.left != null) found = this.left.add(key, value);

                    // else create new node with value
                else {
                    this.left = new MyMassiveForceTreeNode(key, value, this);
                }
            }
        }

        return found;
    }
}

