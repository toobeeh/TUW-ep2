import codedraw.CodeDraw;

import java.awt.*;
import java.util.Random;

/*
    Zusatzfragen:
        Datenkapselung: Daten zu einer Einheit zusammenfassen - in dem Fall des Beispiels zB die Vektorenkomponenten und deren Methoden als Klasse zusammenzufassen
        Data Hiding: Daten vor dem Zugriff von anderen "Orten" zu schützen - in dem FAlle des Beispiels die Vektorenkomponenten *private* zu setzen und diese nur durch Operationen zu modifizieren
        Die Instanz der Klasse bzw. die Klasse, falls es sich um statische Attribute/Methoden handelt. Objektmethoden sollte man im best-practice-Fall an den kleingeschriebenen Instanzen erkennen.
 */

// Simulates the formation of a massive solar system.
public class Simulation {

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
        //Body[] bodies = new Body[NUMBER_OF_BODIES];
        BodyQueue bodies = new BodyQueue(NUMBER_OF_BODIES);
        BodyForceMap forceOnBody = new BodyForceMap(NUMBER_OF_BODIES);

        Random random = new Random(2022);

        for (int i = 0; i < NUMBER_OF_BODIES; i++) {
            bodies.add(new Body(
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

            // for each body (with index i): compute the total force exerted on it.
            /*for (int i = 0; i < bodies.length; i++) {
                forceOnBody[i] = new Vector3(0,0,0); // begin with zero
                for (int j = 0; j < bodies.length; j++) {
                    if (i != j) {
                        Vector3 forceToAdd = bodies[i].gravitationalForce(bodies[j]);
                        forceOnBody[i] = forceOnBody[i].plus(forceToAdd);
                    }
                }
            }*/
            BodyQueue bodiesCopy = new BodyQueue(bodies);
            while(bodiesCopy.size() > 0){

                Body target = bodiesCopy.poll();
                Vector3 gravF = new Vector3(0,0,0);
                BodyQueue queueToAddForce = new BodyQueue(bodies);
                while(queueToAddForce.size() > 0) {
                    Vector3 addForceOf = target.gravitationalForce(queueToAddForce.poll());
                    gravF.plus(addForceOf);
                }

                forceOnBody.put(target, gravF);
            }
            // now forceOnBody[i] holds the force vector exerted on body with index i.

            // for each body (with index i): move it according to the total force exerted on it.
            BodyQueue moveQueue = new BodyQueue(bodies);
            while(moveQueue.size() > 0){
                Body target = moveQueue.poll();
                target.move(forceOnBody.get(target));
            }
            /*for (int i = 0; i < bodies.length; i++) {
                bodies[i].move(forceOnBody[i]);
            }*/

            // show all movements in the canvas only every hour (to speed up the simulation)
            if (seconds % (3600) == 0) {
                // clear old positions (exclude the following line if you want to draw orbits).
                cd.clear(Color.BLACK);

                // draw new positions
                BodyQueue drawQueue = new BodyQueue(bodies);
                while (drawQueue.size() > 0) drawQueue.poll().draw(cd);

                // show new positions
                cd.show();
            }

        }

    }

}
