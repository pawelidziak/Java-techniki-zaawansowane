package sample.database;

import sample.database.models.Client;
import sample.database.models.Reservation;
import sample.database.models.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private Connection connection = null;

    public Database(String dbName) {
        openConnection(dbName);

        if (createTables()) {
            insertInitData();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * Method creates new database and opens connection to it
     *
     * @param dbName - name of database
     */
    private void openConnection(String dbName) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db");
//            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

        } catch (Exception e) {
            System.err.println("openConnection ERROR: " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * Method close connection to the database
     */
    public void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed.");
        } catch (SQLException e) {
            System.err.println("closeConnection ERROR: " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * Method execute the SQL command
     *
     * @param sql - command
     * @return result - fail or success
     */
    private void executeSQL(String sql) throws SQLException {
        Statement statement = getConnection().createStatement();
        statement.execute(sql);
    }

    private boolean createTables() {
        try {
            executeSQL(SQLCommands.CREATE_CLIENT_TABLE);
            executeSQL(SQLCommands.CREATE_WHEEL_SIZE_TABLE);
            executeSQL(SQLCommands.CREATE_SERVICE_TABLE);
            executeSQL(SQLCommands.CREATE_ORDER_TABLE);
            executeSQL(SQLCommands.CREATE_ORDER_SERVICE_TABLE);
            System.out.println("TABLES CREATED.");
        } catch (SQLException e) {
            System.err.println("createTables ERROR: " + e.toString());
            return false;
        }
        return true;
    }


    private void insertInitData() {

        // create and insert two clients
        Client client1 = new Client();
        client1.setName("Paul");
        insertClient(client1);
        Client client2 = new Client();
        client2.setName("Peter");
        insertClient(client2);

        // create and insert three services
        Service changeWheel = new Service();
        changeWheel.setName("Changing wheel");
        changeWheel.setPrice(100.0);
        insertService(changeWheel);

        Service changeTire = new Service();
        changeTire.setName("Changing tire");
        changeTire.setPrice(85.5);
        insertService(changeTire);

        Service PumpTires = new Service();
        PumpTires.setName("Pumping tires");
        PumpTires.setPrice(20);
        insertService(PumpTires);

        // insert wheel sizes
        for (int i = 15; i <= 20; i++) {
            insertWheelSize(i);
        }

    }

    private void insertClient(Client client) {
        try {
            String sql = "INSERT INTO client (name) VALUES ('" + client.getName() + "')";
            executeSQL(sql);
        } catch (SQLException e) {
            System.err.println("insertClient ERROR: " + e.toString());
        }
    }

    private void insertService(Service service) {
        try {
            String sql = "INSERT INTO service (name, price) VALUES ('" + service.getName() + "', " + service.getPrice() + ")";
            executeSQL(sql);
        } catch (SQLException e) {
            System.err.println("insertClient ERROR: " + e.toString());
        }
    }

    private void insertWheelSize(int size) {
        try {
            String sql = "INSERT INTO wheel_size (size) VALUES (" + size + ")";
            executeSQL(sql);
        } catch (SQLException e) {
            System.err.println("insertWheelSize ERROR: " + e.toString());
        }
    }

    public void createReservation(Reservation reservation) {
        try {
            // use PreparedStatement to be able to use getGeneratedKeys()
            PreparedStatement insertReservation = getConnection().prepareStatement("INSERT INTO order_table (client_id, booked_hour, status, total_price) VALUES (" +
                    reservation.getClient().getId() + ", " + reservation.getBookedHour() + ", '" + reservation.getStatus() + "', " + reservation.getPrice() + ")");
            insertReservation.execute();

            try (ResultSet generatedKeys = insertReservation.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reservation.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
            for (Service service : reservation.getServices()) {
                String sql = "INSERT INTO order_with_service (service_id, reservation_id) VALUES (" + service.getId() + ", " + reservation.getId() + ")";
                executeSQL(sql);
            }
        } catch (SQLException e) {
            System.err.println("createReservation ERROR: " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public List<Integer> getBookedHours() {
        List<Integer> bookedHours = new ArrayList<>();
        try {
            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT booked_hour FROM order_table WHERE status = '" + Reservation.STATUS_OPEN + "'");

            while (rs.next()) {
                bookedHours.add(rs.getInt("booked_hour"));
            }
            rs.close();
            return bookedHours;
        } catch (Exception e) {
            System.err.println("getBookedHours ERROR: " + e.getClass().getName() + ": " + e.getMessage());
        }
        return null;
    }

    public List<Integer> getWheelSizes() {
        List<Integer> wheelSizes = new ArrayList<>();
        try {
            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT size FROM wheel_size");

            while (rs.next()) {
                wheelSizes.add(rs.getInt("size"));
            }
            rs.close();
            return wheelSizes;
        } catch (Exception e) {
            System.err.println("getWheelSizes ERROR: " + e.getClass().getName() + ": " + e.getMessage());
        }
        return null;
    }

    public void cancelReservation(int reservationId) {
        try {
            String sql = "DELETE FROM order_table WHERE reservation_id = " + reservationId;
            executeSQL(sql);
        } catch (SQLException e) {
            System.err.println("cancelReservation ERROR: " + e.toString());
        }
    }

    public void updateReservationStatus(int reservationId, String newStatus) {
        try {
            String sql = "UPDATE order_table SET status = '" + newStatus + "' WHERE reservation_id = " + reservationId;
            executeSQL(sql);
        } catch (SQLException e) {
            System.err.println("updateReservationStatus ERROR: " + e.toString());
        }
    }

    /**
     * Method gets client reservations from database and invoke initClientReservationsLV method
     */
    public List<Reservation> getReservations(Client client) {
        List<Reservation> reservations = new ArrayList<>();
        try {
            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT reservation_id, booked_hour, status FROM order_table WHERE client_id = " + client.getId() + " AND status = '" + Reservation.STATUS_OPEN + "';");

            while (rs.next()) {
                int id = rs.getInt("reservation_id");
                int bookedHour = rs.getInt("booked_hour");
                String status = rs.getString("status");
                List<Service> services = getServices(id);
                Reservation reservation = new Reservation(id, client, services, bookedHour, status);
                reservations.add(reservation);
            }
            rs.close();
            return reservations;
        } catch (Exception e) {
            System.err.println("getReservations ERROR: " + e.getClass().getName() + ": " + e.getMessage());
        }
        return null;
    }


    /**
     * Method gets clients from database and invoke initClientsCB method
     */
    public List<Client> getClients() {
        List<Client> clients = new ArrayList<>();
        try {
            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM client;");

            while (rs.next()) {
                int id = rs.getInt("client_id");
                String name = rs.getString("name");
                Client client = new Client(id, name);
                clients.add(client);
            }
            rs.close();
            return clients;
        } catch (Exception e) {
            System.err.println("getClients ERROR: " + e.getClass().getName() + ": " + e.getMessage());
        }
        return null;
    }

    /**
     * Method gets services from database and invoke initServicesCB method
     */
    public List<Service> getServices(Integer reservationId) {
        List<Service> services = new ArrayList<>();
        try {
            Statement stmt = getConnection().createStatement();
            ResultSet rs;
            if (reservationId != null)
                rs = stmt.executeQuery("SELECT * FROM service INNER JOIN order_with_service ON " +
                        "service.service_id = order_with_service.service_id WHERE order_with_service.reservation_id = " + reservationId);
            else rs = stmt.executeQuery("SELECT * FROM service;");

            while (rs.next()) {
                int id = rs.getInt("service_id");
                double price = rs.getDouble("price");
                String name = rs.getString("name");
                Service service = new Service(id, name, price);
                services.add(service);
            }
            rs.close();
            return services;
        } catch (Exception e) {
            System.err.println("getServices ERROR: " + e.getClass().getName() + ": " + e.getMessage());
        }
        return null;
    }
}
