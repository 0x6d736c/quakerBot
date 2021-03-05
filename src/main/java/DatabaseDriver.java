/**
 * Class for storing data gathered in a PostgreSQL database using credentials (gitignored) provided.
 */

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;
import java.util.Map;
import java.util.Properties;

public class DatabaseDriver {
    private static final String credentialPath = "./credentials.json";
    private static String dbURL = null;
    private static Properties credentials = null;                //Stores DB properties


    /**
     * Reads in credentials from credentialPath and sets the credentials Properties object.
     */
    public static void retrieveCredentials() throws FileNotFoundException {
        //Use Google's GSON library to parse JSON credentials
        Gson gson = new Gson();
        Map<?, ?> map = null;

        //Throw exception if parsing of JSON invalid
        try {
             map = gson.fromJson(new FileReader(credentialPath), Map.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Store credentials in Strings
        String urlFromCredentials = (String) map.get("db");
        String username = (String) map.get("username");
        String password = (String) map.get("password");

        //Ensure all credentials are not null, else throw NullPointerException
        if (urlFromCredentials != null && username != null && password != null) {
            dbURL = urlFromCredentials;

            credentials = new Properties();
            credentials.setProperty("user", username);
            credentials.setProperty("password", password);
        } else {
            throw new NullPointerException("Credentials not found or invalid.");
        }
    }

    public static void connect() {
        if (!isValidCredentials()) throw new NullPointerException("Credentials not found or invalid.");

        Connection dbConnection = null;                                                //Declare DB connection
        try {
             dbConnection = DriverManager.getConnection(dbURL, credentials);   //Attempt to connect using properties
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * Checks validity of class credentials.
     */
    private static boolean isValidCredentials() {
        return (dbURL != null && credentials.get("user") != null && credentials.get("password") != null);
    };

    /**
     * Queries the database using credentials.
     */
    public static void queryDatabase(Connection connection, String query) {
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stores data in the database using credentials.
     */
    public static void storeDatabase() {

    }
}
