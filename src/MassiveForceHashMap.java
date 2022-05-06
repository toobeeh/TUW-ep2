import java.util.Arrays;

// A hash map that associates a 'Massive'-object with a Vector3 (typically this is the force
// exerted on the object). The number of key-value pairs is not limited.
//
public class MassiveForceHashMap {

    private MyKeyValuePair<Massive, Vector3>[] map;
    private int count = 0;

    // Initializes 'this' as an empty map.
    public MassiveForceHashMap() {

        // init with capacity 32, because.. :)
        this.map = new MyKeyValuePair[65];
    }

    // Adds a new key-value association to this map. If the key already exists in this map,
    // the value is replaced and the old value is returned. Otherwise 'null' is returned.
    // Precondition: key != null.
    public Vector3 put(Massive key, Vector3 value) {

        // check if search returned existing or new index
        int index = this.findIndex(key);

        if(this.map[index] !=  null){

            // update value
            Vector3 old = this.map[index] == null ? null : this.map[index].value();
            this.map[index] = new MyKeyValuePair<>(key, value);
            return old;
        }

        // else add new KVP and resize if necessary
        else {
            this.map[index] = new MyKeyValuePair<>(key, value);
            this.count++;

            // check if map has gotten too full, resize to avoid frequent collisions
            if(this.count >= 0.75 * this.map.length){

                // double size
                MyKeyValuePair<Massive, Vector3>[] old = this.map;
                this.map = new MyKeyValuePair[this.map.length * 2];

                // re-place entries
                for(MyKeyValuePair<Massive, Vector3> entry : old){
                    this.map[findIndex(entry.key())] = entry;
                }
            }

            return null;
        }

    }

    // Returns the value associated with the specified key, i.e. the method returns the force vector
    // associated with the specified key. Returns 'null' if the key is not contained in this map.
    // Precondition: key != null.
    public Vector3 get(Massive key) {

        int index = findIndex(key);
        if(this.map[index] != null && this.map[index].key().equals(key)) return this.map[index].value();
        else return  null;
    }

    /**
     * finds either the index for this hash key or the first free index for it
     * @param key the has key
     * @return index..
     */
    private int findIndex(Massive key){

        // get initially mapped index
        int index = key.hashCode() & (this.map.length - 2);

        // search linear through collisions with function from scriptum
        while(this.map[index] != null && !this.map[index].key().equals(key)){
            index = (index + 1) & (this.map.length - 2);
        }

        return index;
    }

    // Returns 'true' if this map contains a mapping for the specified key.
    public boolean containsKey(Massive key) {

        int index = findIndex(key);
        return this.map[index] != null && this.map[index].key().equals(key);
    }

    // Returns a readable representation of this map, with all key-value pairs. Their order is not
    // defined.
    public String toString() {

        String value = "[" + this.map.length + "] : ";
        for(MyKeyValuePair<Massive,Vector3> pair : this.map){
            if(pair != null){
                value += pair.toString();
            }
        }
        return value;
    }

    // Compares `this` with the specified object for equality. Returns `true` if the specified
    // `o` is not `null` and is of type `MassiveForceHashMap` and both `this` and `o` have equal
    // key-value pairs, i.e. the number of key-value pairs is the same in both maps and every
    // key-value pair in `this` equals one key-value pair in `o`. Two key-value pairs are
    // equal if the two keys are equal and the two values are equal. Otherwise `false` is returned.
    public boolean equals(Object o) {

        if(!(o instanceof MassiveForceHashMap)) return false;

        MassiveForceHashMap otherMap = (MassiveForceHashMap) o;

        for(int i = 0; i < this.map.length || i < otherMap.map.length; i++){

            // if one of both is out of bounds
            if(i >= this.map.length || i >= otherMap.map.length) return false;

            // if KVP do not equal
            if(!this.map[i].equals(otherMap.map[i])) return false;
        }

        return true;
    }

    // Returns the hashCode of `this`.
    public int hashCode() {

        int hash = 0;
        for(MyKeyValuePair<Massive, Vector3> kvp : this.map){
            if(kvp != null) hash += kvp.hashCode();
        }
        return hash;
    }

    // Returns a list of all the keys in no specified order.
    public MassiveLinkedList keyList() {

        MassiveLinkedList list = new MassiveLinkedList();

        for(MyKeyValuePair<Massive, Vector3> kvp : this.map){
            if(kvp != null) list.addLast(kvp.key());
        }

        return list;
    }


}