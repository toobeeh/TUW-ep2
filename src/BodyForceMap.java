import java.util.Arrays;

// A map that associates a body with a force exerted on it. The number of
// key-value pairs is not limited.
//
public class BodyForceMap {

    private MyKeyValuePair<Body, Vector3>[] map;

    // Initializes this map with an initial capacity.
    // Precondition: initialCapacity > 0.
    public BodyForceMap(int initialCapacity) {

        this.map = new MyKeyValuePair[initialCapacity];
    }

    // Adds a new key-value association to this map. If the key already exists in this map,
    // the value is replaced and the old value is returned. Otherwise 'null' is returned.
    // Precondition: key != null.
    public Vector3 put(Body key, Vector3 force) {

        // get first index of elem whose mass isnt bigger - if array empty, -1 returned!
        int massIndex = MyBodyForceMapHelper.firstNotBigger(key.mass(), this.map);

        // try go right as long as right element has same mass - maybe it's the key?
        int sameMass = massIndex;
        while(sameMass < this.map.length && this.map[sameMass] != null && this.map[sameMass].key().mass() == key.mass()){

            if(this.map[sameMass].key() == key) {
                Vector3 old = this.map[sameMass].value();
                this.map[sameMass] = new MyKeyValuePair<>(key, force);
                return old;
            }
            sameMass++;
        }

        // massindex is now either -1 if empty array or at the first non-bigger mass index
        // => massindex is now the insert index

        // if array was empty, insert at 0
        if(massIndex < 0) massIndex = 0;

        // if array is full, double size
        if(this.map[this.map.length - 1] != null) {
            this.map = Arrays.copyOf(this.map, this.map.length * 2);
        }

        // right-shift everything including insert index
        for(int i = this.map.length - 1; i > massIndex; i--){
            this.map[i] = this.map[i-1];
        }

        // insert new keyvaluepair at free index
        this.map[massIndex] = new MyKeyValuePair<Body, Vector3>(key, force);

        return null;
    }

    // Returns the value associated with the specified key, i.e. the returns the force vector
    // associated with the specified body. Returns 'null' if the key is not contained in this map.
    // Precondition: key != null.
    public Vector3 get(Body key) {
        int massIndex = MyBodyForceMapHelper.firstNotBigger(key.mass(), this.map);

        // loop through all key-value-pairs with same mass (higher indexes) and see if any is the key
        while(massIndex <= map.length - 1 && this.map[massIndex] != null && this.map[massIndex].key().mass() == key.mass()){

            // check if entry with same mass is the searched-for key
            if(this.map[massIndex].key() == key)
                return this.map[massIndex].value();
            else massIndex++;
        }
        return null;
    }
}

/**
 * Provides a help function for finding the first not-bigger index
 */
class MyBodyForceMapHelper{

    /**
     * Searches for the first index that isn't bigger as the given mass
     * Necessary to take care of multiple keys with the same mass
     * @param mass the mass to search for
     * @return first index of the smaller or equal mass, or -1 if empty array
     */
    static public int firstNotBigger(double mass, MyKeyValuePair<Body, Vector3>[] map){
        int left = 0;
        int right = map.length - 1;

        // find an index that is not bigger
        while(left >= 0 && left <= right && right <= map.length - 1){
            int middle = left + (right - left) / 2;

            var middleKVP = map[middle];

            // if the middle is not set or smaller / equal, narrow down to left
            if(middleKVP == null || middleKVP.key().mass() <= mass)
                right = middle - 1;
                // else narrow down right until left gets bigger than right
            else left = middle + 1;
        }

        // move index as far left as possible to get the first not-bigger
        // not 100% efficient but makes the function more reusable
        while(left > 0 && map[left - 1].key().mass() == mass) left--;

        // return the guaranteed index of the first smaller or equal mass
        return left;
    }
}
