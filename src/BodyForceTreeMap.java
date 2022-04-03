// A map that associates a Body with a Vector3 (typically this is the force exerted on the body).
// The number of key-value pairs is not limited.
public class BodyForceTreeMap {

    private MyForceTreeNode root;

    // Adds a new key-value association to this map. If the key already exists in this map,
    // the value is replaced and the old value is returned. Otherwise 'null' is returned.
    // Precondition: key != null.
    public Vector3 put(Body key, Vector3 value) {

        if(root != null) return root.add(key, value);
        else {
            root = new MyForceTreeNode(key, value);
            return null;
        }
    }

    // Returns the value associated with the specified key, i.e. the method returns the force vector
    // associated with the specified key. Returns 'null' if the key is not contained in this map.
    // Precondition: key != null.
    public Vector3 get(Body key) {

        return this.root == null ? null : this.root.get(key);
    }

    // Returns 'true' if this map contains a mapping for the specified key.
    public boolean containsKey(Body key) {

        return this.root != null && this.root.has(key);
    }

    // Returns a readable representation of this map, in which key-value pairs are ordered
    // descending according to the mass of the bodies.
    public String toString() {

        return this.root == null ? "Empty Tree" : this.root.toString();
    }
}

/**
 * Tree node containing a left and right descendant, public accessible.
 */
class MyForceTreeNode {

    public MyForceTreeNode left, right;
    public Body key;
    public Vector3 value;

    public MyForceTreeNode(Body key, Vector3 value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Checks if the node or its descendants contain a body key
     * @param key the body to search for
     * @return boolean that indicates whether this body is part of the subtree
     */
    public boolean has(Body key){

        // check this value, or right descendants, or left descendants
        return this.key == key
                || this.right != null && this.right.has(key)
                || this.left != null && this.left.has(key);
    }

    /**
     * Gets a string of the key-value pair, sorted descending
     * @return string in the format [mass ~ vector] ..
     */
    public String toString(){
        return (this.right != null ? this.right.toString() : "")
                + " [mass: " + this.key.mass() + " ~ vector: " + this.value.length() + "] "
                + (this.left != null ? this.left.toString() : "");
    }

    /**
     * searches for a key and returns its value
     * @param key the body key to search for
     * @return the found value or null if not found
     */
    public Vector3 get(Body key){

        // return this value if key matches
        if(key == this.key) return this.value;

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
    public Vector3 add(Body key, Vector3 value){

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
                    this.right = new MyForceTreeNode(key, value);
                }
            }

            // if mass smaller or equal, set value of left node
            else {

                // does the node exist? if yes, add there
                if(this.left != null) found = this.left.add(key, value);

                // else create new node with value
                else {
                    this.left = new MyForceTreeNode(key, value);
                }
            }
        }

        return found;
    }
}
