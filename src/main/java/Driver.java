import org.jsoup.nodes.Element;

import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * The brain of the Discord Quaker bot. Executes main functionality of the program.
 */
public class Driver {
    public static void main(String[] args) {
        try {
            DatabaseDriver.retrieveCredentials();
            DatabaseDriver.connect();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
