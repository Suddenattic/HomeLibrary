import java.sql.*;

public class WorkWithDB {
    private static Connection dbConnection = null;
    private static Statement statement = null;

    private static Connection DBConnection() {
        final String SERVER_ADDRESS = "jdbc:postgresql://localhost/?user=postgres&password=postgres";
        Connection dbConnection = null;
        try {
            Class.forName("org.postgresql.Driver");
            dbConnection = DriverManager.getConnection(SERVER_ADDRESS, "admin", "admin");
            return dbConnection;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return dbConnection;
    }


    static void createDB() throws SQLException {
        final String DB_ADDRESS = "jdbc:postgresql://localhost/homelibrary";
        final String createDB = "CREATE DATABASE homelibrary";
        final String createAuthorsTable = "CREATE TABLE IF NOT EXISTS authors (ID SERIAL, Author CHAR(100))";
        final String createBooksTable = "CREATE TABLE IF NOT EXISTS books (ID SERIAL, Title CHAR(100), Genre CHAR(50)," +
                " № INT, Size INT, Format CHAR(5), Date DATE, Language char(3));";
        try {
            dbConnection = DBConnection();
            statement = dbConnection.createStatement();
            statement.executeUpdate(createDB);
        } catch (SQLException e) {
            dbConnection = DriverManager.getConnection(DB_ADDRESS, "admin", "admin");
            statement = dbConnection.createStatement();
            statement.executeUpdate(createAuthorsTable);
            statement.executeUpdate(createBooksTable);
        } finally {
            if (statement != null) statement.close();
            if (dbConnection != null) dbConnection.close();
        }
    }

  /*  static void workWithDB() throws SQLException {
        String createTable = "CREATE TABLE IF NOT EXISTS HomeLibrary (ID SERIAL, Author CHAR(100), Genre CHAR(50), Title CHAR(100)," +
                " № INT, Size INT, Format CHAR(5), Date DATE, Language char(3));";
        String updateTable = "INSERT INTO HomeLibrary (ID, Author, Genre, Title, №, Size, Format, Date, Language)" +
                "VALUES (" + ")";

        try {
            dbConnection = DBConnection();
            statement = dbConnection.createStatement();
            statement.executeUpdate(createTable);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            if (statement != null) statement.close();
            if (dbConnection != null) dbConnection.close();
        }
    }

    static void insertIntoDB(String[] data) throws SQLException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String createTable = "CREATE TABLE IF NOT EXISTS HomeLibrary (ID SERIAL, Author CHAR(100), Genre CHAR(50), Title CHAR(100)," +
                " № INT, Size INT, Format CHAR(5), Date DATE, Language char(3));";
        String updateTable = "INSERT INTO HomeLibTest (name, genre, title, №, size, format, date, language) " +
                "VALUES (" + data[0] + ", " + data[1] + ", " + data[2] + ", " + Integer.parseInt(data[3]) + ", " +  Integer.parseInt(data[4]) + ", " + data[7] + ", " + format.parse(data[8]) + ", " + data[9] + ")";

        try {
            dbConnection = DBConnection();
            statement = dbConnection.createStatement();
            statement.executeUpdate(updateTable);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            if (statement != null) statement.close();
            if (dbConnection != null) dbConnection.close();
        }
    }*/
}
