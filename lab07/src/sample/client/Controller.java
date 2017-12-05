package sample.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.util.Callback;
import sample.agent.AgentMXBean;
import sample.agent.MyCategory;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.net.URL;
import java.util.*;

//            String[] agent = (String[]) server.getAttribute(mbeanName, "Categories");
//            for(String s : agent) System.out.println(s);

public class Controller implements Initializable {

    private AgentMXBean mbeanProxy;
    private String SELECTED_CATEGORY;
    private QueueLogic queueLogic;

    @FXML
    private ComboBox<MyCategory> cb_categories;
    @FXML
    private Button b_getTicket;
    @FXML
    private Button b_returnTicket;
    @FXML
    private Text t_notifications;
    @FXML
    private Text t_current_queue;
    @FXML
    private Text t_current_ticket;

    public Controller() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (connect()) {
            queueLogic = new QueueLogic();
            initServicesCB();
        } else System.out.println("No connection.");
    }

    /**
     * Method connects to the JMX Service and gets objects with methods
     */
    private boolean connect() {
        try {
            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:9999/jmxrmi");
            JMXConnector jmxc = JMXConnectorFactory.connect(url);
            MBeanServerConnection server = jmxc.getMBeanServerConnection();
            ObjectName mbeanName = new ObjectName("sample.agent:type=JMX - lab07");
            mbeanProxy = JMX.newMXBeanProxy(server, mbeanName, AgentMXBean.class, true);
            ClientListener listener = new ClientListener();
            server.addNotificationListener(mbeanName, listener, null, null);
//            server.addNotificationListener(mbeanName, this, null, null);
            return true;
        } catch (IOException | MalformedObjectNameException | InstanceNotFoundException e) {
            System.err.println("Connect ERROR: " + e.toString());
            return false;
        }
    }

    /**
     * Methods generates current ticket
     */
    @FXML
    void getTicket() {
        int priority = mbeanProxy.getPriority(SELECTED_CATEGORY);
        queueLogic.getTicket(SELECTED_CATEGORY, priority);
        updateCurrentQueue();
    }

    /**
     * Method returns tickets
     */
    @FXML
    void returnTicket() {
        if (queueLogic.returnTicket()) updateCurrentQueue();
    }

    /**
     * Listener which we add to server.
     * It takes information what we need and set it to the t_notifications
     */
    class ClientListener implements NotificationListener {
        public void handleNotification(Notification notification, Object handback) {
            String result = "";
            result += "\nClassName:\t\t" + notification.getClass().getName();
            result += "\nSource:\t\t\t" + notification.getSource();
            result += "\nType:\t\t\t" + notification.getType();
//            result += "\nUser data:\t\t" + notification.getUserData();
            result += "\nMessage:\t\t\t" + notification.getMessage();
            if (notification instanceof AttributeChangeNotification) {
                AttributeChangeNotification acn = (AttributeChangeNotification) notification;
                result += "\nAttributeName:\t" + acn.getAttributeName();
                result += "\nAttributeType:\t\t" + acn.getAttributeType();
                if (Objects.equals(acn.getAttributeType(), "String[]")) {
                    result += "\nOldValue:\t\t\t";
                    if (acn.getOldValue() != null) for (String s : (String[]) acn.getOldValue()) result += s + " ";
                    else result += "null";
                    result += "\nNewValue:\t\t";
                    for (String s : (String[]) acn.getNewValue()) result += s + " ";
                } else {
                    result += "\nOldValue:\t\t\t" + acn.getOldValue();
                    result += "\nNewValue:\t\t" + acn.getNewValue();
                }
            }
            t_notifications.setText(result);
            updateCB();
        }
    }


    //    auxiliary methods

    private void updateCurrentQueue() {
        List<Ticket> queue = queueLogic.getQueue();
        if (queue.size() > 0) {
            StringBuilder result = new StringBuilder();
            for (int i = 1; i < queue.size(); i++) {
                result.append(queue.get(i)).append(", ");
            }
            t_current_queue.setText(result.toString());
            t_current_ticket.setText(queue.get(0).toString());
        } else {
            t_current_queue.setText("None");
            t_current_ticket.setText("None");
        }

    }

    private void initServicesCB() {
        Callback<ListView<MyCategory>, ListCell<MyCategory>> factory = lv -> new ListCell<MyCategory>() {
            @Override
            protected void updateItem(MyCategory item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName() + " p: " + item.getPriority());
            }
        };

        cb_categories.setItems(FXCollections.observableArrayList(
                mbeanProxy.getCategories()
        ));
        cb_categories.setCellFactory(factory);
        cb_categories.setButtonCell(factory.call(null));

        cb_categories.getSelectionModel().selectFirst();
        SELECTED_CATEGORY = cb_categories.getSelectionModel().getSelectedItem().getName();
        cb_categories.valueProperty().addListener((observable, oldValue, newValue) -> {
            SELECTED_CATEGORY = newValue.getName();
        });
    }

    private void updateCB() {
//        cb_categories.getSelectionModel().select(null);

        cb_categories.getSelectionModel().selectFirst();
        cb_categories.setItems(FXCollections.observableArrayList(
                mbeanProxy.getCategories()
        ));
    }
}

