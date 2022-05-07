import codedraw.CodeDraw;

public class NamedBody implements Massive
{

    Body body;
    String name;

    // Initializes this with name, mass, current position and movement. The associated force
    // is initialized with a zero vector.
    public NamedBody(String name, double mass, Vector3 massCenter, Vector3 currentMovement) {
        this.body = new Body(
                mass,
                massCenter,
                currentMovement
        );
        this.name = name;
    }

    // Returns the name of the body.
    public String getName() {
        return this.name;
    }

    public void move(Vector3 force){
        this.body.move(force);
    }

    // Compares `this` with the specified object. Returns `true` if the specified `o` is not
    // `null` and is of type `NamedBody` and both `this` and `o` have equal names.
    // Otherwise `false` is returned.
    public boolean equals(Object o) {
        return o instanceof NamedBody ? ((NamedBody) o).name == this.name : false;
    }

    // Returns the hashCode of `this`.
    public int hashCode() {
        return this.name.hashCode();
    }

    // Returns a readable representation including the name of this body.
    public String toString() {
        return this.name + "(" + this.hashCode() + "): " + this.body.toString();
    }

    public void draw(CodeDraw cd){
        this.body.draw(cd);
    }

    public double getMass(){return this.body.getMass(); }

    public Vector3 getMassCenter(){return this.body.getMassCenter(); }
}
