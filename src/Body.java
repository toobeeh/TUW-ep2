import codedraw.CodeDraw;

import java.awt.*;

// This class represents celestial bodies like stars, planets, asteroids, etc..
public class Body implements Massive {

    private double mass;
    private Vector3 massCenter; // position of the mass center.
    private Vector3 currentMovement;

    public Body(double mass, Vector3 massCenter, Vector3 currentMovement){
        this.mass = mass;
        this.massCenter = massCenter;
        this.currentMovement = currentMovement;
    }

    // Returns the distance between the mass centers of this body and the specified body 'b'.
    public double distanceTo(Body b) {

        return this.massCenter.distanceTo(b.massCenter);
    }

    public double getMinimalDistance(BodyQueue bodies){
        double min = this.distanceTo(bodies.poll());
        while (bodies.size() > 0) {

            Body body = bodies.poll();

            double dist = this.distanceTo(body);
            if (dist < min && this != body) min = dist;
        }
        return min;
    }

    // Returns a vector representing the gravitational force exerted by 'b' on this body.
    // The gravitational Force F is calculated by F = G*(m1*m2)/(r*r), with m1 and m2 being the
    // masses of the objects interacting, r being the distance between the centers of the masses
    // and G being the gravitational constant.
    // Hint: see simulation loop in Simulation.java to find out how this is done.
    public Vector3 gravitationalForce(Body b) {

        Vector3 direction = b.massCenter.minus(this.massCenter);
        double distance = direction.length();
        direction.normalize();
        double force = Simulation.G * this.mass * b.mass / (distance * distance);
        return direction.times((force));
    }

    // Moves this body to a new position, according to the specified force vector 'force' exerted
    // on it, and updates the current movement accordingly.
    // (Movement depends on the mass of this body, its current movement and the exerted force.)
    // Hint: see simulation loop in Simulation.java to find out how this is done.
    public void move(Vector3 force) {

        Vector3 newPosition = currentMovement.plus(
                massCenter.plus(
                        force.times(1 / mass)
                        // F = m*a -> a = F/m
                )
        );

        // new minus old position.
        Vector3 newMovement = newPosition.minus(massCenter);

        // update body state
        this.massCenter = newPosition;
        this.currentMovement = newMovement;
    }

    // Returns the approximate radius of this body.
    // (It is assumed that the radius r is related to the mass m of the body by r = m ^ 0.5,
    // where m and r measured in solar units.)
    public double radius() {

        return SpaceDraw.massToRadius(this.mass);
    }

    /**
     * Get the mass center of this body
     * @return vector3 which is the mass center
     */
    public Vector3 massCenter() {
        return this.massCenter;
    }

    // Returns a new body that is formed by the collision of this body and 'b'. The impulse
    // of the returned body is the sum of the impulses of 'this' and 'b'.
    public Body merge(Body b) {

        double mass = this.mass + b.mass;
        Vector3 massCenter = b.massCenter.times(b.mass)
                    .plus(this.massCenter.times(this.mass))
                    .times(1/mass);
        Vector3 currentMovement = b.currentMovement.times(b.mass)
                .plus(this.currentMovement.times(this.mass))
                .times(1.0/mass);

        return new Body(mass, massCenter, currentMovement);
    }

    // Draws the body to the specified canvas as a filled circle.
    // The radius of the circle corresponds to the radius of the body
    // (use a conversion of the real scale to the scale of the canvas as
    // in 'Simulation.java').
    // Hint: call the method 'drawAsFilledCircle' implemented in 'Vector3'.
    public void draw(CodeDraw cd) {

        cd.setColor(SpaceDraw.massToColor(this.mass));
        this.massCenter.drawAsFilledCircle(cd, SpaceDraw.massToRadius(this.mass));
    }

    // Returns a string with the information about this body including
    // mass, position (mass center) and current movement. Example:
    // "5.972E24 kg, position: [1.48E11,0.0,0.0] m, movement: [0.0,29290.0,0.0] m/s."
    public String toString() {

        return String.format("%e kg, position: %s m, movement: %s m/s", this.mass, this.massCenter.toString(), this.currentMovement.toString());
    }

    /**
     * Gets the mass of this body
     * @return mass as double
     */
    public double mass() {
        return this.mass;
    }


}

