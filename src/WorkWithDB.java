import java.sql.*;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

public class WorkWithDB {
    private static Connection dbConnection = null;
    private static Statement statement = null;

    private static Connection DBConnection() {
        final String SERVER_ADDRESS = "jdbc:postgresql://localhost/?user=postgres&password=postgres";
        final String DB_ADDRESS = "jdbc:postgresql://localhost/homelibrary";
        final String createDB = "CREATE DATABASE homelibrary";
        final String createAuthorsTable = "CREATE TABLE IF NOT EXISTS authors (ID SERIAL, Name CHAR(100))";
        final String createBooksTable = "CREATE TABLE IF NOT EXISTS books (ID SERIAL, Title CHAR(200), Genre CHAR(200)," +
                " Size INT, Format CHAR(5), Date DATE, Language char(5));";
        /*final String createAuthorBookTable = "CREATE TABLE IF NOT EXISTS books (ID SERIAL, Title CHAR(200), Genre CHAR(200)," +
                " Size INT, Format CHAR(5), Date DATE, Language char(5));";
        final String createGenreTable = "CREATE TABLE IF NOT EXISTS books (ID SERIAL, Title CHAR(200), Genre CHAR(200)," +
                " Size INT, Format CHAR(5), Date DATE, Language char(5));";
        final String createGenreBookTable = "CREATE TABLE IF NOT EXISTS books (ID SERIAL, Title CHAR(200), Genre CHAR(200)," +
                " Size INT, Format CHAR(5), Date DATE, Language char(5));";
        final String createLanguageTable = "CREATE TABLE IF NOT EXISTS books (ID SERIAL, Title CHAR(200), Genre CHAR(200)," +
                " Size INT, Format CHAR(5), Date DATE, Language char(5));";
        final String createLanguageBookTable = "CREATE TABLE IF NOT EXISTS books (ID SERIAL, Title CHAR(200), Genre CHAR(200)," +
                " Size INT, Format CHAR(5), Date DATE, Language char(5));";*/
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

    static void updateTables(Set<String> authorsSet, List<String> booksList) throws SQLException {
        String[] booksBuf;
        Calendar today = Calendar.getInstance();
        long time = today.getTimeInMillis();
        Date date = new Date(time);

        try {
            dbConnection = DBConnection();
           /* PreparedStatement authorsUpdate = dbConnection.prepareStatement(
                    "INSERT INTO public.authors(author) VALUES (?)");
            for (String i : authors) {
                authorsUpdate.setString(1, i);
                authorsUpdate.executeUpdate();
            }
            PreparedStatement booksUpdate = dbConnection.prepareStatement(
                    "INSERT INTO public.books(title, genre, size, format, date, language) VALUES (?, ?, ?, ?, ?, ?)");*/
            /*for (String i : books) {
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
            }*/

            PreparedStatement authorsUpdate = dbConnection.prepareStatement(
                    "INSERT INTO public.authors(name) VALUES (?)");
            PreparedStatement booksUpdate = dbConnection.prepareStatement(
                    "INSERT INTO public.books(title, genre, size, format, date, language) VALUES (?, ?, ?, ?, ?, ?)");

            for(String i : authorsSet) {
                authorsUpdate.setString(1, i);
                authorsUpdate.executeUpdate();
            }

            for(String i : booksList) {
                try {
                    booksBuf = i.split("\u0004");
                    if (booksBuf[0].length() > 200 || booksBuf[1].length() > 200) {
                        System.out.println(i);
                        continue;
                    }
                    booksUpdate.setString(1, booksBuf[1]);
                    booksUpdate.setString(2, booksBuf[0]);
                    booksUpdate.setInt(3, Integer.parseInt(booksBuf[5]));
                    booksUpdate.setString(4, booksBuf[8]);
                    booksUpdate.setDate(5, date);
                    booksUpdate.setString(6, booksBuf[10]);
                    booksUpdate.executeUpdate();
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println(i);
                }
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


