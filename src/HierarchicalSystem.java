import codedraw.CodeDraw;

import java.util.Arrays;
import java.util.stream.Collectors;

// A cosmic system that is composed of a central named body (of type 'NamedBodyForcePair')
// and an arbitrary number of subsystems (of type 'HierarchicalSystem') in its orbit.
// This class implements 'CosmicSystem'.
//
public class HierarchicalSystem implements CosmicSystem {

    CosmicSystem[] systemsInOrbit;
    NamedBodyForcePair central;

    // Initializes this system with a name and a central body.
    public HierarchicalSystem(NamedBodyForcePair central, CosmicSystem... inOrbit) {
        this.central = central;
        this.systemsInOrbit = inOrbit;
    }

    public String toString(){

        String subDescription = Arrays.stream(this.systemsInOrbit)
                .map(system -> system.toString())
                .collect(Collectors.joining(", "));

        String description = this.central.getName()
                + (subDescription != "" ? " {" + subDescription + "} " : "");

        return description;
    }

    public int numberOfBodies(){
        int count = 1;

        for(CosmicSystem system : this.systemsInOrbit){
            count += system.numberOfBodies();
        }

        return count;
    }

    public double getMass(){
        double mass = this.central.getMass();

        for(CosmicSystem system : this.systemsInOrbit){
            mass += system.getMass();
        }

        return mass;
    }

    public Vector3 getMassCenter(){

        Vector3 weightedCenters = this.central.getMassCenter().times((this.central.getMass()));

        for(CosmicSystem system : this.systemsInOrbit){
            weightedCenters = weightedCenters.plus(system.getMassCenter().times(system.getMass()));
        }

        weightedCenters = weightedCenters.times(1 / this.getMass());

        return weightedCenters;
    }

    public void addForceFrom(Body b){

        this.central.addForceFrom(b);

        for(CosmicSystem system : this.systemsInOrbit){
            system.addForceFrom(b);
        }
    }

    public void addForceTo(CosmicSystem system){

        BodyLinkedList forceSources = this.getBodies();

        for(Body source : forceSources){
            system.addForceFrom(source);
        }
    }

    public void update(){

        this.central.update();

        for(CosmicSystem system : this.systemsInOrbit){
            system.update();
        }
    }

    public BodyLinkedList getBodies(){
        BodyLinkedList list = new BodyLinkedList();

        for(int i = -1; i < this.systemsInOrbit.length; i++){

            CosmicSystem source = i == -1 ? this.central : this.systemsInOrbit[i];

            for(Body b : source.getBodies()){
                list.addLast(b);
            }
        }

        return list;
    }

    public double distanceTo(CosmicSystem system){
        return this.getMassCenter().distanceTo(system.getMassCenter());
    }

    public void draw(CodeDraw cd){

        this.central.draw(cd);

        for(CosmicSystem system : this.systemsInOrbit){
            system.draw(cd);
        }
    }
}
