import codedraw.CodeDraw;

// A body with a name and an associated force. The leaf node of
// a hierarchical cosmic system. This class implements 'CosmicSystem'.
//
public class NamedBodyForcePair implements CosmicSystem {

    private String name;
    private Body body;
    private Vector3 force;

    // Initializes this with name, mass, current position and movement. The associated force
    // is initialized with a zero vector.
    public NamedBodyForcePair(String name, double mass, Vector3 massCenter, Vector3 currentMovement) {
        this.name = name;
        this.body = new Body(mass, massCenter, currentMovement);
        this.force = new Vector3(0,0,0);
    }

    // Returns the name of the body.
    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }

    public double getMass(){
        return this.body.mass();
    }

    public Vector3 getMassCenter(){
        return this.body.massCenter();
    }

    public int numberOfBodies(){
        return 1;
    }

    public double distanceTo(CosmicSystem cs){

        return this.body.massCenter().distanceTo(cs.getMassCenter());
    }

    // adds force that is exerted from another body on this body
    public void addForceFrom(Body b){
        if(b != this.body) this.force = this.force.plus(this.body.gravitationalForce(b));
    }

    // add force to a cosmic system that is exerted from this body
    public void addForceTo(CosmicSystem cs){
        cs.addForceFrom(this.body);
    }

    public BodyLinkedList getBodies(){
        BodyLinkedList list = new BodyLinkedList();
        list.addFirst(this.body);

        return list;
    }

    public void update(){
        this.body.move(this.force);
        this.force = new Vector3(0,0,0);
    }

    public void draw(CodeDraw cd){

        this.body.draw(cd);
        this.body.massCenter().drawLabel(cd, this.name);
    }
}
