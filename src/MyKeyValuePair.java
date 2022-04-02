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
}