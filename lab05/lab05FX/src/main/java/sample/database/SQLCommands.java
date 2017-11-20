package sample.database;

class SQLCommands {

    static final String CREATE_CLIENT_TABLE =
            "CREATE TABLE client (client_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " name VARCHAR(255))";

    static final String CREATE_WHEEL_SIZE_TABLE =
            "CREATE TABLE wheel_size (wheel_size_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " size INTEGER)";

    static final String CREATE_SERVICE_TABLE =
            "CREATE TABLE service (service_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " name VARCHAR(255)," +
                    " price DOUBLE)";

    static final String CREATE_ORDER_TABLE =
            "CREATE TABLE order_table (reservation_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " client_id INTEGER," +
                    " booked_hour INTEGER," +
                    " status VARCHAR(255)," +
                    " total_price DOUBLE," +
                    " FOREIGN KEY(client_id) REFERENCES client(id) ON DELETE CASCADE)";

    static final String CREATE_ORDER_SERVICE_TABLE =
            "CREATE TABLE order_with_service (service_id INTEGER," +
                    " reservation_id INTEGER," +
                    " FOREIGN KEY(service_id) REFERENCES service(service_id) ON DELETE CASCADE," +
                    " FOREIGN KEY(reservation_id) REFERENCES reservation(reservation_id) ON DELETE CASCADE," +
                    " PRIMARY KEY(service_id, reservation_id))";

}
