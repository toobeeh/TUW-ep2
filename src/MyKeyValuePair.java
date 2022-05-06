/**
 * Class that contains a key and a value
 * @param <TKey> the key datatype
 * @param <TValue> the value datatype
 */
public class MyKeyValuePair<TKey, TValue>{
    private TKey _key;
    private TValue _value;

    public MyKeyValuePair(TKey key, TValue value){
        this._key = key;
        this._value = value;
    }

    public TKey key(){ return this._key; }
    public TValue value() { return this._value; }

    public String toString(){
        return " (" + this.key().toString() + ":" + this.value().toString() + ") ";
    }

    public int hashCode(){
        return this.key().hashCode() + this.value().hashCode();
    }

    public boolean equals(Object o){
        if(!(o instanceof MyKeyValuePair)) return false;

        MyKeyValuePair kvp = (MyKeyValuePair) o;

        return this._value.equals(kvp._value) && this._key.equals(kvp._key);
    }
}