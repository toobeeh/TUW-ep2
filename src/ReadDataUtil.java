import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadDataUtil {

    private static final Pattern
        REGEX_DATASET = Pattern.compile("\\$\\$SOE([\\s\\S]+)\\$\\$EOE"),
        REGEX_DATA_ENTRY = Pattern.compile("(-?\\d+(?:\\.\\d+)?(?:E[\\+-]\\d+)?), A.D. (\\d{4}-\\w{3}-\\d{2}) \\d{2}:\\d{2}:\\d{2}.\\d{4},  ?([-]?\\d+(?:\\.\\d+)?(?:E[\\+-]\\d+)?),  ?([-]?\\d+(?:\\.\\d+)?(?:E[\\+-]\\d+)?),  ?([-]?\\d+(?:\\.\\d+)?(?:E[\\+-]\\d+)?),  ?([-]?\\d+(?:\\.\\d+)?(?:E[\\+-]\\d+)?),  ?([-]?\\d+(?:\\.\\d+)?(?:E[\\+-]\\d+)?),  ?([-]?\\d+(?:\\.\\d+)?(?:E[\\+-]\\d+)?),\\n");

    // Reads the position and velocity vector on the specified 'day' from the file with the
    // specified 'path', and sets position and current velocity of 'b' accordingly. If
    // successful the method returns 'true'. If the specified 'day' was not found in the file,
    // 'b' is unchanged and the method returns 'false'.
    // The file format is validated before reading the state.
    // Lines before the line "$$SOE" and after the line "$$EOE" the are ignored. Each line of the
    // file between the line "$$SOE" and the line "$$EOE" is required to have the following format:
    // JDTDB, TIME, X, Y, Z, VX, VY, VZ
    // where JDTDB is interpretable as a 'double' value, TIME is a string and X, Y, Z, VX, VY and
    // VZ are interpretable as 'double' values. JDTDB can be ignored. The character ',' must only
    // be used as field separator. If the file is not found, an exception of the class
    // 'StateFileNotFoundException' is thrown. If it does not comply with the format described
    // above, the method throws an exception of the class 'StateFileFormatException'. Both
    // exceptions are subtypes of 'IOException'.
    // Precondition: b != null, path != null, day != null and has the format YYYY-MM-DD.
    public static boolean readConfiguration(NamedBody b, String path, String day)
            throws IOException {

            String content = "";

            // get file content
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(path));
                String line = "";
                while((line = reader.readLine()) != null) content += line + "\n";
            }
            catch(Exception ex){
                throw new StateFileNotFoundException();
            }
            finally {
                if(reader != null) reader.close();
            }

            // get data content
            Matcher formatMatcher = REGEX_DATASET.matcher(content);
            String data = "";

            if(formatMatcher.find()){
                data = formatMatcher.group(1);
                //if(data == null || data.length() <= 0) throw new StateFileFormatException();
            }
            else throw new StateFileFormatException();

            // find data that matches regex
            Matcher dataEntryMatcher = REGEX_DATA_ENTRY.matcher(data);

            while(dataEntryMatcher.find()){
                String data_date = dataEntryMatcher.group(2);

                // if day matches
                if(data_date.equals(day)){

                    float data_JDTDB = Float.parseFloat(dataEntryMatcher.group(1));
                    float data_X = Float.parseFloat(dataEntryMatcher.group(3));
                    float data_Y = Float.parseFloat(dataEntryMatcher.group(4));
                    float data_Z = Float.parseFloat(dataEntryMatcher.group(5));
                    float data_VX = Float.parseFloat(dataEntryMatcher.group(6));
                    float data_VY = Float.parseFloat(dataEntryMatcher.group(7));
                    float data_VZ = Float.parseFloat(dataEntryMatcher.group(8));

                    Body position = new Body(
                            b.mass(),
                            new Vector3(data_X * 1000,data_Y * 1000,data_Z * 1000),
                            new Vector3(data_VX * 1000,data_VY * 1000,data_VZ * 1000)
                    );
                    b.updateBody(position);

                    return true;
                }
            }

            return false;
        }
    }

