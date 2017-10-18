package client;

import manager.IManager;
import others.Configuration;
import others.Order;
import others.Server;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;

public class ClientGUI extends JFrame {

    private ClientImpl client;
    private IManager manager;

    ClientGUI(ClientImpl client) {
        this.client = client;
        manager = (IManager) Server.getService(Configuration.REMOTE_MANAGER);

        setTitle(ClientImpl.class.getSimpleName());
        setSize(300, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        createGUI();
    }

    private void createGUI() {
        JTextField tf_text = new JTextField();
        JTextField tf_display = new JTextField();
        JTextField tf_delete = new JTextField();

        JButton b_send = new JButton("Send");
        JButton b_delete = new JButton("Delete");

        b_send.addActionListener(e -> {
            if (!tf_text.getText().equals("") && !tf_display.getText().equals("")) {
                System.out.println("order");
                Order order = new Order(tf_text.getText(), Duration.ofSeconds(Long.parseLong(tf_display.getText())), client);
                try {
                    if (!manager.placeOrder(order)) System.out.println("Cannot place order.");
                } catch (Exception ex) {
                    System.out.print(ex.toString());
                }
            }
        });

        b_delete.addActionListener(e -> {
            if (tf_delete.getText() != null) {
                try {
                    manager.withdrawOrder(Integer.parseInt(tf_delete.getText()));
                } catch (Exception ex) {
                    System.out.print(ex.toString());
                }
            }
        });

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.add(new JLabel("Advert text: "));
        panel.add(tf_text);
        panel.add(new JLabel("Display period"));
        panel.add(tf_display);
        panel.add(new JLabel(""));
        panel.add(b_send);
        panel.add(new JLabel("ID to delete"));
        panel.add(tf_delete);
        panel.add(new JLabel(""));
        panel.add(b_delete);
        add(panel);
    }

}