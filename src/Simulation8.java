import codedraw.CodeDraw;

import javax.naming.Name;
import java.awt.*;
import java.io.IOException;
import java.util.regex.Pattern;

// Simulates the solar system.
//
public class Simulation8 {

    public static Pattern DATE = Pattern.compile("\\d{4}-\\w{3}-\\d{2}");

    // gravitational constant
    public static final double G = 6.6743e-11;

    // one astronomical unit (AU) is the average distance of earth to the sun.
    public static final double AU = 150e9; // meters

    // set some system parameters
    public static final double SECTION_SIZE = 10 * AU; // the size of the square region in space

    // all quantities are based on units of kilogram respectively second and meter.

    // The main simulation method using instances of other classes.
    public static void main(String[] args) {

        if(args.length < 1){
            System.out.println("No path argument provided");
            System.exit(64);
        }

        if(args.length < 2 || !DATE.matcher(args[1]).matches()){
            System.out.println("No valid date argument provided");
            System.exit(64);
        }

        // simulation
        CodeDraw cd = new CodeDraw();

        // create solar system with 13 bodies
        MassiveForceTreeMap map = new MassiveForceTreeMap();

        NamedBody earth = new NamedBody("Earth", 5.972E24, new Vector3(0, 0, 0), new Vector3(0, 0, 0));
        map.put(earth,
                new Vector3(0, 0, 0));

        NamedBody moon = new NamedBody("Moon", 7.349E22, new Vector3(0, 0, 0), new Vector3(0, 0, 0));
        map.put(moon,
                new Vector3(0, 0, 0));

        NamedBody mars = new NamedBody("Mars", 6.41712E23, new Vector3(0, 0, 0), new Vector3(0, 0, 0));
        map.put(mars,
                new Vector3(0, 0, 0));

        NamedBody mercury = new NamedBody("Mercury", 3.301E23, new Vector3(0, 0, 0), new Vector3(0, 0, 0));
        map.put(mercury,
                new Vector3(0, 0, 0));

        NamedBody venus = new NamedBody("Venus", 4.86747E24, new Vector3(0, 0, 0), new Vector3(0, 0, 0));
        map.put(venus,
                new Vector3(0, 0, 0));


        // read values from txt
        applyConfig(map, earth, args[0], args[1]);
        applyConfig(map, moon, args[0], args[1]);
        applyConfig(map, venus, args[0], args[1]);
        applyConfig(map, mercury, args[0], args[1]);
        applyConfig(map, mars, args[0], args[1]);

        // add sun after states have been read from files.
        map.put(new NamedBody("Sun", 1.989E30, new Vector3(0, 0, 0), new Vector3(0, 0, 0)),
                new Vector3(0, 0, 0));

        // simulate
        for(int seconds = 0; true; seconds++){

            // calculate forces
            MassiveForceTreeMap massiveMap = new MassiveForceTreeMap();

            for(Massive massive : map){
                Vector3 force = new Vector3(0,0,0);

                for(Massive forceOf : map){
                    if(!massive.equals(forceOf)) force = force.plus(massive.gravitationalForce(forceOf));
                }
                massiveMap.put(massive, force);
            }

            map = massiveMap;

            // move massives
            for(Massive massive : massiveMap.getKeys()){
                massive.move(massiveMap.get(massive));
            }

            // draw massives
            if(seconds % 3600 == 0){

                cd.clear(Color.BLACK);

                for(Massive massive : massiveMap.getKeys()){
                    massive.draw(cd);
                }

                cd.show();
            }
        }

    }

    static void applyConfig(MassiveForceTreeMap map, NamedBody body, String path, String day){
        try{
            if(!ReadDataUtil.readConfiguration(body,path +"/" + body.name + ".txt", day)){
                System.out.println("No config of this day for body : " + body.name);
            }
        }
        catch(StateFileNotFoundException e){
            System.out.println("Config for body not found: " + body.name);
            map.remove(body);
        }
        catch(StateFileFormatException e){
            System.out.println("Config for body has errors: " + body.name);
            map.remove(body);
        }
        catch (IOException ex){

        }
    }
}
