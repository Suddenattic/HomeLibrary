import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;

public class WorkWithDB {
    private static Connection dbConnection = null;
    private static Statement statement = null;

    private static Connection DBConnection() {
        final String SERVER_ADDRESS = "jdbc:postgresql://localhost/?user=postgres&password=postgres";
        final String DB_ADDRESS = "jdbc:postgresql://localhost/homelibrary";
        final String createDB = "CREATE DATABASE homelibrary";
        final String createAuthorsTable = "CREATE TABLE IF NOT EXISTS authors (ID SERIAL, Author CHAR(150))";
        final String createBooksTable = "CREATE TABLE IF NOT EXISTS books (ID SERIAL, Title CHAR(100), Genre CHAR(50)," +
                " № INT, Size INT, Format CHAR(5), Date DATE, Language char(3));";
        Connection dbConnection = null;
        try {
            Class.forName("org.postgresql.Driver");
            dbConnection = DriverManager.getConnection(DB_ADDRESS, "admin", "admin");
        } catch (ClassNotFoundException | SQLException e) {
            if(e.getMessage().contains("база данных \"homelibrary\" не существует") ||
                    e.getMessage().contains("database \"homelibrary\" does not exist")) {
                try {
                    dbConnection = DriverManager.getConnection(SERVER_ADDRESS, "admin", "admin");
                    statement = dbConnection.createStatement();
                    statement.executeUpdate(createDB);
                    dbConnection = DriverManager.getConnection(DB_ADDRESS, "admin", "admin");
                } catch (SQLException ex) {
                    System.err.println(e.getClass().getName() + ": " + ex.getMessage());
                    ex.printStackTrace();
                    System.exit(0);
                }
            } else {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                e.printStackTrace();
                System.exit(0);
            }
        }
        try {
            statement = dbConnection.createStatement();
            statement.executeUpdate(createAuthorsTable);
            statement.executeUpdate(createBooksTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbConnection;
    }

    static void updateTables(Set<String> authors, Set<String> books) throws SQLException, ParseException {
        Calendar today = Calendar.getInstance();
        long time = today.getTimeInMillis();
        Date date = new Date(time);

        String[] booksBuf;
        try {
            dbConnection = DBConnection();
            PreparedStatement authorsUpdate = dbConnection.prepareStatement(
                    "INSERT INTO public.authors(author) VALUES (?)");
            for (String i : authors) {
                authorsUpdate.setString(1, i);
                authorsUpdate.executeUpdate();
            }
            PreparedStatement booksUpdate = dbConnection.prepareStatement(
                    "INSERT INTO public.books(title, genre, size, format, date, language) VALUES (?, ?, ?, ?, ?, ?)");
            for (String i : books) {
                booksBuf = i.split("\u0004");
                int k = 1;
                for(int j = 0; j < booksBuf.length; j++) {
                    switch (k) {
                        case 3: booksUpdate.setInt(k, Integer.parseInt(booksBuf[j]));
                        break;
                        case 5: booksUpdate.setDate(k, date);
                        break;
                        default: booksUpdate.setString(k, booksBuf[j]);
                        break;
                    }
                    k++;
                }
                booksUpdate.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (statement != null) statement.close();
            if (dbConnection != null) dbConnection.close();
        }
    }
}


