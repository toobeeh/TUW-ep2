import codedraw.CodeDraw;

import java.awt.*;
import java.util.Random;

// Simulates the formation of a massive solar system.
//
public class Simulation3 {

    // gravitational constant
    public static final double G = 6.6743e-11;

    // one astronomical unit (AU) is the average distance of earth to the sun.
    public static final double AU = 150e9; // meters

    // one light year
    public static final double LY = 9.461e15; // meters

    // some further constants needed in the simulation
    public static final double SUN_MASS = 1.989e30; // kilograms
    public static final double SUN_RADIUS = 696340e3; // meters
    public static final double EARTH_MASS = 5.972e24; // kilograms
    public static final double EARTH_RADIUS = 6371e3; // meters

    // set some system parameters
    public static final double SECTION_SIZE = 2 * AU; // the size of the square region in space
    public static final int NUMBER_OF_BODIES = 22;
    public static final double OVERALL_SYSTEM_MASS = 20 * SUN_MASS; // kilograms

    // all quantities are based on units of kilogram respectively second and meter.

    // The main simulation method using instances of other classes.
    public static void main(String[] args) {

        // simulation
        CodeDraw cd = new CodeDraw();
        BodyLinkedList bodies = new BodyLinkedList();
        BodyForceTreeMap forceOnBody = new BodyForceTreeMap();

        Random random = new Random(2022);

        // generate bodies
        for (int i = 0; i < NUMBER_OF_BODIES; i++) {
            bodies.addLast(new Body(
                    Math.abs(random.nextGaussian()) * OVERALL_SYSTEM_MASS / NUMBER_OF_BODIES, // kg
                    new Vector3(
                            0.2 * random.nextGaussian() * AU,
                            0.2 * random.nextGaussian() * AU,
                            0.2 * random.nextGaussian() * AU
                    ),
                    new Vector3(
                            0 + random.nextGaussian() * 5e3,
                            0 + random.nextGaussian() * 5e3,
                            0 + random.nextGaussian() * 5e3
                    )
            ));
        }

        double seconds = 0;

        // simulation loop
        while (true) {

            seconds++; // each iteration computes the movement of the celestial bodies within one second.

            // merge bodies: poll from bodies and add processed to a new list
            BodyLinkedList bodyMerges = new BodyLinkedList();
            Body checkMerge;
            while((checkMerge = bodies.pollFirst()) != null){

                // get all colliding bodies and merge them
                BodyLinkedList merges = bodies.removeCollidingWith(checkMerge);
                Body mergeWith;
                while((mergeWith = merges.pollFirst()) != null) checkMerge =  checkMerge.merge(mergeWith);

                bodyMerges.addFirst(checkMerge);
            }

            // apply merged bodies
            bodies = bodyMerges;

            // create new linked list which will be consumed
            BodyLinkedList bodiesForceOn = new BodyLinkedList(bodies);
            Body forceOn;

            // poll through list and add calculate force on body
            while((forceOn = bodiesForceOn.pollFirst()) != null){

                // create new vector to accumulate force
                Vector3 force = new Vector3(0,0,0);

                // poll through list and add force
                BodyLinkedList bodiesAddForce = new BodyLinkedList(bodies);
                Body addForce;
                while((addForce = bodiesAddForce.pollFirst()) != null){

                    if(addForce != forceOn) {
                        force = force.plus(forceOn.gravitationalForce(addForce));
                    }
                }

                // set vector / add KVPair
                forceOnBody.put(forceOn, force);
            }

            // now forceOnBody[i] holds the force vector exerted on body with index i.

            // move each body in the list
            BodyLinkedList bodiesMove = new BodyLinkedList(bodies);
            Body moved;
            while((moved = bodiesMove.pollFirst()) != null){

                moved.move(forceOnBody.get(moved));
            }

            // show all movements in the canvas only every hour (to speed up the simulation)
            if (seconds % (3600) == 0) {
                // clear old positions (exclude the following line if you want to draw orbits).
                cd.clear(Color.BLACK);

                BodyLinkedList bodiesDraw = new BodyLinkedList(bodies);
                Body drawn;
                while((drawn = bodiesDraw.pollFirst()) != null){

                    drawn.draw(cd);
                }

                // show new positions
                cd.show();
            }
        }
    }
}
