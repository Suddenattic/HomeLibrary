import java.sql.*;
import java.sql.Date;
import java.util.*;

public class WorkWithDB {
    private static Connection dbConnection = null;
    private static Statement statement = null;
    private static List<String> booksList = new ArrayList<>();
    private static Set<String> authorsSet = new TreeSet<>();
    private static Set<String> genresSet = new TreeSet<>();
    private static Set<String> languagesSet = new TreeSet<>();

    private static Connection DBConnection() {
        final String SERVER_ADDRESS = "jdbc:postgresql://localhost/?user=postgres&password=postgres";
        final String DB_ADDRESS = "jdbc:postgresql://localhost/homelibrary";
        final String createDB = "CREATE DATABASE homelibrary";

        //Операторы создания таблиц
        final String createAuthorsTable = "CREATE TABLE IF NOT EXISTS authors (ID SERIAL PRIMARY KEY, Name CHAR(100))";
        final String createBooksTable = "CREATE TABLE IF NOT EXISTS books (ID SERIAL PRIMARY KEY, Title CHAR(200)," +
                " Size INT, Format CHAR(5), Date DATE);";
        final String createGenreTable = "CREATE TABLE IF NOT EXISTS genre (ID SERIAL PRIMARY KEY, genre CHAR(200) NOT NULL)";
        final String createLanguageTable = "CREATE TABLE IF NOT EXISTS language (ID SERIAL PRIMARY KEY, language CHAR(50) NOT NULL)";

        //Операторы создания связующих таблиц
        final String createAuthorBookTable = "CREATE TABLE IF NOT EXISTS author_book (AuthorID INT NOT NULL, BookID INT NOT NULL," +
                " FOREIGN KEY (AuthorID) REFERENCES authors(id), FOREIGN KEY (BookID) REFERENCES books(id))";
        final String createGenreBookTable = "CREATE TABLE IF NOT EXISTS genre_book (GenreID INT NOT NULL, BookID INT NOT NULL," +
                " FOREIGN KEY (GenreID) REFERENCES genre(id), FOREIGN KEY (BookID) REFERENCES books(id))";

        //Подключение к БД (при отсутствии создание БД и таблиц)
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
            statement.executeUpdate(createGenreTable);
            statement.executeUpdate(createLanguageTable);
            statement.executeUpdate(createAuthorBookTable);
            statement.executeUpdate(createGenreBookTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbConnection;
    }

    //Заполнение/обновление данных таблиц
    private static void updateTables() throws SQLException {
        String[] booksBuf;
        Calendar today = Calendar.getInstance();
        long time = today.getTimeInMillis();
        Date date = new Date(time);

        try {
            dbConnection = DBConnection();
            PreparedStatement authorsUpdate = dbConnection.prepareStatement(
                    "INSERT INTO public.authors(name) VALUES (?)");
            PreparedStatement booksUpdate = dbConnection.prepareStatement(
                    "INSERT INTO public.books(title, size, format, date) VALUES (?, ?, ?, ?)");
            PreparedStatement genreUpdate = dbConnection.prepareStatement(
                    "INSERT INTO public.genre(genre) VALUES (?)");
            PreparedStatement languageUpdate = dbConnection.prepareStatement(
                    "INSERT INTO public.language(language) VALUES (?)");

            for(String i : authorsSet) {
                authorsUpdate.setString(1, i);
                authorsUpdate.executeUpdate();
            }

            for(String i : genresSet) {
                genreUpdate.setString(1, i);
                genreUpdate.executeUpdate();
            }

            for(String i : languagesSet) {
                languageUpdate.setString(1, i);
                languageUpdate.executeUpdate();
            }

            for(String i : booksList) {
                try {
                    booksBuf = i.split("\u0004");
                    if (booksBuf[0].length() > 200 || booksBuf[1].length() > 200) {
                        System.out.println(i);
                        continue;
                    }
                    booksUpdate.setString(1, booksBuf[0]);
                    booksUpdate.setInt(2, Integer.parseInt(booksBuf[4]));
                    booksUpdate.setString(3, booksBuf[7]);
                    booksUpdate.setDate(4, date);
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

    //Редактирование строк перед заполением
    static void lineEditor(List<String> list) throws SQLException {
        String[] buf;
        String[] authorsBuf;
        String[] genreBuf;
        for (String i : list) {
            buf = i.split("\u0004",3);
            authorsBuf = buf[0].split(":");
            for (String s : authorsBuf) {
                authorsSet.add(s.replaceAll("[.,]", " ").
                        replaceAll("\\s+", " ").trim());
            }
            genreBuf = buf[1].split(":");
            for (String s : genreBuf) {
                genresSet.add(s.trim());
            }
            booksList.add(buf[2]);
            languagesSet.add(i.split("\u0004")[11].toLowerCase()
                    .replaceAll("en-gb", "en").replaceAll("ru-ru", "ru"));
        }
        updateTables();
    }

    static void bindingTable(String tabName, Map<String, String> map, List<String> list) {

        }
    }
}


