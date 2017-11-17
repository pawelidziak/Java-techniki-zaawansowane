package sample.database;

import java.sql.*;

public class Database {

    private static Connection c = null;
    private static Statement stmt = null;

    public Database(String dbName){
        openConnection(dbName);
    }

    private void openConnection(String dbName) {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

        } catch (Exception e) {
            System.err.println("openConnection ERROR: " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void closeConnection(){
        try {
            stmt.close();
            c.close();
            System.out.println("Connection closed.");
        } catch (SQLException e) {
            System.err.println("closeConnection ERROR: " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void createTables() {

        String sql = "CREATE TABLE COMPANY " +
                "(ID INT PRIMARY KEY     NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " AGE            INT     NOT NULL, " +
                " ADDRESS        CHAR(50), " +
                " SALARY         REAL)";

        try {
            stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
        } catch (SQLException e) {
            System.err.println("createTables ERROR: " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void insertOperation() {
        try {
            stmt = c.createStatement();
            String sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                    "VALUES (1, 'Paul', 32, 'California', 20000.00 );";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                    "VALUES (2, 'Allen', 25, 'Texas', 15000.00 );";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                    "VALUES (3, 'Teddy', 23, 'Norway', 20000.00 );";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                    "VALUES (4, 'Mark', 25, 'Rich-Mond ', 65000.00 );";
            stmt.executeUpdate(sql);

            stmt.close();
            c.commit();
        } catch (Exception e) {
            System.err.println("insertOperation ERROR: " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void selectOperation() {
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM COMPANY;");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String address = rs.getString("address");
                float salary = rs.getFloat("salary");

                System.out.println("ID = " + id);
                System.out.println("NAME = " + name);
                System.out.println("AGE = " + age);
                System.out.println("ADDRESS = " + address);
                System.out.println("SALARY = " + salary);
                System.out.println();
            }
            rs.close();
        } catch (Exception e) {
            System.err.println("selectOperation ERROR: " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

}
