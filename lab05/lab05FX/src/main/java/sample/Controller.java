package sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import sample.database.Database;
import sample.database.models.Client;
import sample.database.models.Reservation;
import sample.database.models.Service;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Database db;
    private int selected_hour = 1;
    private int selected_wheel_size = 1;
    private Client selected_client;
    private Service selected_service;
    private List<Service> selected_services = new ArrayList<>();
    private Reservation selected_reservation;
    private List<Reservation> client_reservations;

    @FXML
    private ComboBox<Client> cb_clients;
    @FXML
    private ComboBox<Service> cb_services;
    @FXML
    private ComboBox<Integer> cb_hour;
    @FXML
    private ComboBox<Integer> cb_wheel_size;

    @FXML
    private Button b_login;
    @FXML
    private Button b_logout;
    @FXML
    private Button b_remove_service;
    @FXML
    private Button b_cancel_reservation;
    @FXML
    private Button b_make_reservation;

    @FXML
    private TitledPane tp_operations;
    @FXML
    private ListView<Service> lw_services;
    @FXML
    private ListView<Reservation> lw_reservations;

    @FXML
    private Text t_services_price;

    public void initialize(URL location, ResourceBundle resources) {
        tp_operations.setVisible(false);
        db = new Database("lab05-db");
        initClientsCB(db.getClients());
        initServicesCB(db.getServices(null));
        initHoursCB();
        initServicesLW();
        initWheelSizesCB();
    }

    @FXML
    void login() {
        b_login.setDisable(true);
        b_logout.setDisable(false);
        tp_operations.setVisible(true);
        cb_clients.setDisable(true);
        client_reservations = db.getReservations(selected_client);
        initReservationsLW();
    }

    @FXML
    void logout() {
        b_login.setDisable(false);
        b_logout.setDisable(true);
        cb_clients.setDisable(false);
        tp_operations.setVisible(false);
        lw_services.getItems().clear();
    }

    @FXML
    void addService() {
        selected_service.setWheel_size(selected_wheel_size);
        selected_services.add(selected_service);
        b_make_reservation.setDisable(false);
        updateLW(lw_services, selected_services);
        t_services_price.setText(calculateServicesPrice().toString());
    }

    @FXML
    void removeService() {
        selected_services.remove(lw_services.getSelectionModel().getSelectedItem());
        if (lw_services.getItems().isEmpty()) b_make_reservation.setDisable(true);
        updateLW(lw_services, selected_services);
        t_services_price.setText(calculateServicesPrice().toString());
    }

    @FXML
    void makeReservation() {
        if (!lw_services.getItems().isEmpty()) {
            Reservation reservation = new Reservation();
            reservation.setClient(selected_client);
            reservation.setServices(selected_services);
            reservation.setBookedHour(selected_hour);
            reservation.setStatus(Reservation.STATUS_OPEN);
            reservation.setPrice(calculateServicesPrice());

            db.createReservation(reservation);
            cb_hour.setItems(FXCollections.observableArrayList(filterBookHours()));
            cb_hour.getSelectionModel().selectFirst();
            client_reservations.add(reservation);
            updateLW(lw_reservations, client_reservations);
            lw_services.getItems().clear();
            selected_services = new ArrayList<>();
            b_make_reservation.setDisable(true);
            t_services_price.setText("None");
            System.out.println(reservation);
        }
    }

    @FXML
    void cancelReservation() {
        System.out.println(selected_reservation);
        client_reservations.remove(selected_reservation);
        db.updateReservationStatus(selected_reservation.getId(), Reservation.STATUS_CANCELED);
        updateLW(lw_reservations, client_reservations);
    }

//
//    /**
//     * Method gets client (ONE) from database
//     *
//     * @param clientId - client id (int)
//     * @return client (object)
//     */
//    private Client getClient(int clientId) {
//        try {
//            Statement stmt = db.getConnection().createStatement();
//            ResultSet rs = stmt.executeQuery("SELECT * FROM client WHERE id = " + clientId);
//
//            if (!rs.next()) {
//                throw new IllegalArgumentException("Client with given Id does not exist!");
//            }
//            Client client = new Client(rs.getInt("client_id"), rs.getString("name"));
//            rs.close();
//            return client;
//        } catch (Exception e) {
//            System.err.println("getClient ERROR: " + e.getClass().getName() + ": " + e.getMessage());
//        }
//        return null;
//    }


    /**
     * Method initializes services combo box
     *
     * @param clients - array with services string formatted like: ID: id, NAME: name
     */
    private void initClientsCB(List<Client> clients) {
        cb_clients.setItems(FXCollections.observableArrayList(
                clients
        ));
        cb_clients.getSelectionModel().selectFirst();
        selected_client = cb_clients.getSelectionModel().getSelectedItem();
        cb_clients.valueProperty().addListener((ov, t, t1) -> {
            selected_client = t1;
        });
    }

    private void initServicesLW() {
        if (lw_services.getSelectionModel().isEmpty()) b_remove_service.setDisable(true);
        else b_remove_service.setDisable(false);
        lw_services.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (lw_services.getSelectionModel().isEmpty()) b_remove_service.setDisable(true);
            else b_remove_service.setDisable(false);
        });
    }

    private void initReservationsLW() {
        if (lw_reservations.getSelectionModel().isEmpty()) b_cancel_reservation.setDisable(true);
        else b_cancel_reservation.setDisable(false);
        lw_reservations.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (lw_reservations.getSelectionModel().isEmpty()) b_cancel_reservation.setDisable(true);
            else b_cancel_reservation.setDisable(false);
            selected_reservation = newValue;
        });
        if(client_reservations != null) {
            lw_reservations.setItems(FXCollections.observableArrayList(client_reservations));
        }
    }


    /**
     * Method initializes services combo box
     *
     * @param services - array with services string formatted like: ID: id, NAME: name, PRICE: price
     */
    private void initServicesCB(List<Service> services) {
        cb_services.setItems(FXCollections.observableArrayList(
                services
        ));
        cb_services.getSelectionModel().selectFirst();
        selected_service = cb_services.getSelectionModel().getSelectedItem();
        cb_services.valueProperty().addListener((observable, oldValue, newValue) -> {
            selected_service = newValue;
        });
    }

    /**
     * Method initializes booking hour combo box
     */
    private void initHoursCB() {
        cb_hour.setItems(FXCollections.observableArrayList(filterBookHours()));
        cb_hour.getSelectionModel().selectFirst();
        String tmp = cb_hour.getSelectionModel().getSelectedItem().toString();
        selected_hour = Integer.valueOf(tmp);
        cb_hour.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selected_hour = Integer.valueOf(newValue.toString());
            }
        });
    }

    /**
     * Method initializes wheel sizes combo box
     */
    private void initWheelSizesCB() {
        cb_wheel_size.setItems(FXCollections.observableArrayList(db.getWheelSizes()));
        cb_wheel_size.getSelectionModel().selectFirst();
        String tmp = cb_wheel_size.getSelectionModel().getSelectedItem().toString();
        selected_wheel_size = Integer.valueOf(tmp);
        cb_wheel_size.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selected_wheel_size = Integer.valueOf(newValue.toString());
            }
        });
    }

    // Auxiliary methods
    private Double calculateServicesPrice(){
        double price = 0.0;
        for(Service service: selected_services){
            price += service.getPrice();
        }
        return price;
    }
//    private void calculatePrice(){
//        Double price = 0.0;
//        for(Reservation client_reservation: client_reservations){
//            price += client_reservation.getPrice();
//        }
//        t_total_price.setText(price.toString());
//    }

    private List<Integer> filterBookHours() {
        List<Integer> result = new ArrayList<>();
        for (int i = 1; i <= 24; i++) result.add(i);

        List<Integer> bookedHours = db.getBookedHours();

        if (bookedHours == null) {
            return result;
        } else {
            result.removeAll(bookedHours);
            return result;
        }
    }

    private void updateLW(ListView<?> listView, List list){
        listView.getItems().clear();
        listView.setItems(FXCollections.observableArrayList(list));
    }

    void exitApplication() {
        db.closeConnection();
        System.exit(0);
    }
}
