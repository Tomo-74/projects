import java.sql.*;


/**
 * The Database class contains helper functions to connect and test the connection with the database.
 */
public class Database {

    /**
     * Creates a connection to the database using environment variables. The following environment
     * variables must be set prior to running the program:
     *
     * <ul>
     *    <li>LMS_PORT - The database management system port
     *    <li>LMS_HOST - The database management system host
     *    <li>LMS_USERNAME - The database management system username
     *    <li>LMS_PASSWORD - The database management system user's password
     *    <li>LMS_DATABASE - The name of the database in the database management system
     * </ul>
     *
     * For more information on environment variables see:
     * <a href="https://docs.oracle.com/javase/tutorial/essential/environment/env.html">
     *  https://docs.oracle.com/javase/tutorial/essential/environment/env.html
     * </a>
     * @return java.sql.Connection
     * @throws SQLException
     */
    public static Connection getDatabaseConnection() throws SQLException {
        int databasePort = Integer.parseInt(System.getenv("LMS_PORT"));
        String databaseHost = System.getenv("LMS_HOST");
        String databaseUsername = System.getenv("LMS_USERNAME");
        String databasePassword = System.getenv("LMS_PASSWORD");
        String databaseName = System.getenv("LMS_DATABASE");
        String databaseURL = String.format(
                "jdbc:mysql://%s:%s/%s?verifyServerCertificate=false&useSSL=false&serverTimezone=UTC",
                databaseHost,
                databasePort,
                databaseName);

        try {

            return DriverManager.getConnection(databaseURL, databaseUsername, databasePassword);
        } catch (SQLException sqlException) {
            System.out.printf("SQLException was thrown while trying to connection to database: %s%n", databaseURL);
            System.out.println(sqlException.getMessage());
            throw sqlException;
        }

    }

    /**
     * Tests the connection to the database. If the connection fails, please check:
     * <ul>
     *    <li>Database is running
     *    <li>Environment variables are set and being read in properly
     *    <li>Database Driver is in the CLASSPATH.
     *    <li>SSH port forwarding is properly setup
     * </ul>
     */
    public static void testConnection() {
        System.out.println("Attempting to connect to MySQL database using:");
        System.out.printf("LMS_HOST: %s%n", System.getenv("LMS_HOST"));
        System.out.printf("LMS_PORT: %s%n", System.getenv("LMS_PORT"));
        System.out.printf("LMS_USERNAME: %s%n", System.getenv("LMS_USERNAME"));
        System.out.printf("LMS_PASSWORD: %s%n", System.getenv("LMS_PASSWORD"));
        System.out.printf("LMS_DATABASE: %s%n", System.getenv("LMS_DATABASE"));

        Connection connection = null;
        ResultSet resultSet = null;

        try{
            connection = getDatabaseConnection();
            Statement sqlStatement = connection.createStatement();
            String sql = "SELECT VERSION();";
            resultSet = sqlStatement.executeQuery(sql);
            resultSet.next();
            System.out.printf("Connected SUCCESS! Database Version: %s%n", resultSet.getString(1));
        } catch (SQLException sql){
            System.out.println("Failed to connect to database! Please make sure your Environment variables are set!");
        } finally {
            try { resultSet.close(); } catch (Exception e) { /* ignored */ }
            try { connection.close(); } catch (Exception e) { /* ignored */ }
        }
    }
}