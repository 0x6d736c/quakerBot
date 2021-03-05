import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;

/**
 * Retrieves requisite data from MCITOfficial and stores it in database to
 * prevent multiple queries or overloading.
 */
public class RetrieveMCITOfficial {
    private static final String MCITOfficial = "https://onlinelearning.seas.upenn.edu/mcit";
    private static final String admissions = "/admissions";              //application deadlines, tuitions, fees
    private static final String courseList = "/mcit-online-course-list"; //course listings

    private static Instant lastUpdate = Instant.EPOCH;


    /**
     * Retrieve admissions information: deadlines, tuition fees, etc.
     */
    public static boolean connectAdmissions() {
        return false;
    }

    /**
     * Retrieve course listings from official website.
     */
    public static boolean connectCourseList() {
        return false;
    }


}
