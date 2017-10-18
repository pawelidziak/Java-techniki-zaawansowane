package manager;

import billboard.BillboardImpl;
import billboard.IBillboard;
import others.Configuration;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.rmi.RemoteException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ManagerGUI extends JFrame {

    private final DefaultListModel<String> billboardsStrings = new DefaultListModel<>();
    private final List<IBillboard> billboardsInterfaces = new ArrayList<>();

    private ManagerImpl manager;
    private IBillboard billboard;

    private JLabel jl_totalSlots;
    private JLabel jl_freeSlots;
    private JLabel jl_display;
    private JLabel jl_status;

    ManagerGUI(ManagerImpl manager) {
        this.manager = manager;
        System.out.println("ManagerGUI started");

        createGUI();
    }

    private void createGUI() {
        setTitle(ManagerImpl.class.getSimpleName());
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton b_unbind = new JButton("Unbind");
        JButton b_start = new JButton("Start");
        JButton b_stop = new JButton("Stop");
        JButton b_setInterval = new JButton("Set interval");

        JTextField tf_display = new JTextField();

        jl_totalSlots = new JLabel("none", SwingConstants.CENTER);
        jl_freeSlots = new JLabel("none", SwingConstants.CENTER);
        jl_display = new JLabel("none", SwingConstants.CENTER);
        jl_status = new JLabel("none", SwingConstants.CENTER);

        final JList<String> jl_billboards = new JList<>(billboardsStrings);
        jl_billboards.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jl_billboards.setSelectedIndex(0);
        jl_billboards.setVisibleRowCount(10);

        jl_billboards.addListSelectionListener(arg0 -> {
            if (!arg0.getValueIsAdjusting() && jl_billboards.getSelectedIndex() != -1) {
                try {
                    int totalSlots = billboardsInterfaces.get(jl_billboards.getSelectedIndex()).getCapacity()[0];
                    int freeSlots = billboardsInterfaces.get(jl_billboards.getSelectedIndex()).getCapacity()[1];
                    jl_totalSlots.setText(String.valueOf(totalSlots));
                    jl_freeSlots.setText(String.valueOf(freeSlots));
                    jl_display.setText(Configuration.DISPLAY_INTERVAL.toString());
                } catch (RemoteException e) {
                    System.out.println("Get selected billboard error: " + e.toString());
                }
            }
        });

        JScrollPane jsp_billboards = new JScrollPane(jl_billboards);
        jsp_billboards.setPreferredSize(new Dimension(250, 150));

        b_unbind.addActionListener(e -> {
            if (jl_billboards.getSelectedIndex() != -1) {
                try {
                    int id = jl_billboards.getSelectedIndex();
                    jl_billboards.clearSelection();
                    manager.unbindBillboard(id);
                    jl_totalSlots.setText("none");
                    jl_freeSlots.setText("none");
                } catch (RemoteException e1) {
                    System.out.println("Unbind billboard error: " + e1.toString());
                }
            }
        });

        b_start.addActionListener(e -> {
            if (jl_billboards.getSelectedIndex() != -1) {
                try {
                    System.out.println(billboardsInterfaces.get(jl_billboards.getSelectedIndex()));
                    if(billboardsInterfaces.get(jl_billboards.getSelectedIndex()).start())
                        jl_status.setText("ON");
                } catch (RemoteException ex) {
                    System.out.println("Start billboard error: " + ex.toString());
                }
            }
        });

        b_stop.addActionListener(e -> {
            if (jl_billboards.getSelectedIndex() != -1) {
                try {
                    if(billboardsInterfaces.get(jl_billboards.getSelectedIndex()).stop())
                        jl_status.setText("OFF");
                } catch (RemoteException ex) {
                    System.out.println("Stop billboard error: " + ex.toString());
                }
            }
        });

        b_setInterval.addActionListener(e -> {
            if (jl_billboards.getSelectedIndex() != -1 && !tf_display.getText().equals("")) {
                try {
                    billboardsInterfaces.get(jl_billboards.getSelectedIndex()).setDisplayInterval(Duration.ofSeconds(Long.parseLong(tf_display.getText())));
                    jl_display.setText(Duration.ofSeconds(Long.parseLong(tf_display.getText())).toString());
                } catch (RemoteException ex) {
                    System.out.println("Set interval error: " + ex.toString());
                }
            }
        });

        add(jsp_billboards);

        JPanel panel = new JPanel(new GridLayout(5, 3, 10, 10));

        panel.add(b_unbind);
        panel.add(b_start);
        panel.add(b_stop);
        panel.add(new JLabel(""));
        panel.add(tf_display);
        panel.add(b_setInterval);
        panel.add(new JLabel("Total / free slots: "));
        panel.add(jl_totalSlots);
        panel.add(jl_freeSlots);
        panel.add(new JLabel("Display interval: "));
        panel.add(jl_display);
        panel.add(new JLabel(""));
        panel.add(new JLabel("Status: "));
        panel.add(jl_status);
        panel.add(new JLabel(""));
        add(panel);
    }

    void updateBillboardsList(List<IBillboard> billboards) {
        billboardsStrings.clear();
        billboardsInterfaces.clear();
        for (IBillboard billboard : billboards) {
            billboardsStrings.addElement("Billboard ID: " + billboards.indexOf(billboard));
            billboardsInterfaces.add(billboard);
        }
    }



}
