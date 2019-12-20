import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class WorkWithDB {
    static Connection dbConnection = null;
    static Statement statement = null;

    private static Connection DBConnection() {
        final String DB_DRIVER = "org.postgresql.Driver";
//        final String DB_ADDRESS = "jdbc:postgresql://localhost:5432/HomeLibrary";
//        final String DB_ADDRESS = "jdbc:postgresql://localhost/?user=postgres&password=postgres";
        final String DB_ADDRESS = "jdbc:postgresql://localhost:5432/AuthorBook";
        final String DB_USER = "admin";
        final String DB_PASSWORD = "admin";
        Connection dbConnection = null;
        try {
            Class.forName(DB_DRIVER);
            dbConnection = DriverManager.getConnection(DB_ADDRESS, DB_USER, DB_PASSWORD);
            return dbConnection;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return dbConnection;
    }

   /* static void createDB() throws SQLException {
        String catalogs = "";
        String DBName = "HomeLibrary";
        String createDB = "CREATE DATABASE HomeLibrary;";
        try {
            dbConnection = DBConnection();
            statement = dbConnection.createStatement();
            ResultSet rs = dbConnection.getMetaData().getCatalogs();
            while (rs.next())
                catalogs = rs.getString(1);
            if (!DBName.equals(catalogs))
                statement.executeUpdate(createDB);
            workWithDB();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            if (statement != null) statement.close();
            if (dbConnection != null) dbConnection.close();
        }
    }*/

    static void workWithDB() throws SQLException {
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
    }
}

 /* try {
          String catalogs = "";
          dbConnection = DBConnection();
          DatabaseMetaData dbmd= dbConnection.getMetaData();
          statement = dbConnection.createStatement();
          ResultSet rs = dbConnection.getMetaData().getTables("mydb");
          while(rs.next())
          catalogs = rs.getString(1);
          if(DBName.equals(catalogs))
          System.out.println("Database " + DBName + " already exists!");
          else {
          statement.executeUpdate(createDB);
          }
            *//*while (rs.next()) {
                String id = rs.getString("login");
                System.out.println(id);*//*
          } catch (SQLException e) {
          e.printStackTrace();
          System.out.println(e.getClass().getName() + ": " + e.getMessage());
          } finally {
          if (statement != null) statement.close();
          if (dbConnection != null) dbConnection.close();
          }*/
