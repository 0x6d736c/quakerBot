import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;

/**
 * Retrieves requisite data from MCITCentral (thanks, @wilsonplau on Github) and stores it in database to
 * prevent multiple queries or overloading.
 */
public class RetrieveMCITCentral {
    private static final String MCITCentralURL = "https://mcitcentral.com/";
    private static HashMap<String, HashMap<String, String>> updatedCourseInformation;
    private static Element table;
    private static Instant lastUpdate = Instant.EPOCH;

    /**
     * Connects to MCIT Central and returns the parsed web page. Retyrns false if site retrieval failed or table
     * parsing failed. True if table element found.
     */
    public static boolean connect() {
        Document document = null;

        try {
            document = Jsoup.connect(MCITCentralURL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (document != null) {
            Elements pageTable = document.getElementsByTag("table");
            if (pageTable != null) {
                table = pageTable.get(0);
                return true;
            }
        }

        return false;
    }

    /**
     * Retrieves the relevant data and returns a HashMap of associated classes and their reviews/difficulty/workload/etc.
     *
     * @return HashMap<String, HashMap < String, String>> - a HashMap of classes as keys and a HashMap of Reviews/Difficulty/
     * Workload/Rating as another key/value pair.
     */
    public static HashMap<String, HashMap<String, String>> retrieveCourseInformation() {
        if (table == null) return null;

        Duration timeDelta = Duration.between(lastUpdate, Instant.now());

        if (timeDelta.toHours() < 168) return null;     //Prevents spamming of Wilson's server - weekly update

        HashMap<String, HashMap<String, String>> courseInformation = new HashMap<>();  //Declare HashMap for storage
        Elements rows = parseRows();                    //Parse rows

        for (Element e : rows) {                        //Loop through each row, retrieve class required data
            HashMap<String, HashMap<String, String>> course = parseColumns(e);
            courseInformation.putAll(course);
        }

        updatedCourseInformation = courseInformation;   //Set updated course information
        lastUpdate = Instant.now();                     //Set last update time

        return courseInformation;                       //Return K/V course information pair
    }

    /**
     * Returns the table rows containing each class's info. First row is scrapped as it contains only header information.
     */
    private static Elements parseRows() {
        Elements rows = table.getElementsByTag("tr");
        Elements sanitizedRows = new Elements();
        for (int i = 1; i < rows.size(); i++) {
            sanitizedRows.add(rows.get(i));
        }

        return sanitizedRows;
    }

    /**
     * Returns the columns of a specified row containing the class info.
     * Column 0 :: class code
     * Column 1 :: class title
     * Column 2 :: reviews given (0-*)
     * Column 3 :: difficulty [1..5]
     * Column 4 :: workload (hours, avg)
     * Column 5 :: rating [1..5]
     */
    private static HashMap<String, HashMap<String, String>> parseColumns(Element row) {
        Elements column = row.getElementsByTag("td");

        String courseCode = column.get(0).text();
        String courseName = column.get(1).text();
        String reviewsGiven = column.get(2).text();
        String difficulty = column.get(3).text();
        String workload = column.get(4).text();
        String rating = column.get(5).text();

        HashMap<String, HashMap<String, String>> courseAll = new HashMap<>();
        HashMap<String, String> courseStats = new HashMap<>();

        courseStats.put("Course Name", courseName);
        courseStats.put("Number of Reviews", reviewsGiven);
        courseStats.put("Difficulty Level", difficulty);
        courseStats.put("Workload", workload);
        courseStats.put("Rating", rating);

        courseAll.put(courseCode, courseStats);

        return courseAll;
    }

    /**
     * Getter for updatedCourseInformation
     */
    public static HashMap<String, HashMap<String, String>> getUpdatedCourseInformation() {
        return updatedCourseInformation;
    }
}