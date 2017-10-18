package billboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

class BillboardGUI extends JFrame {

    private JLabel advertLabel;
    private BillboardImpl billboard;

    BillboardGUI(BillboardImpl billboard) {
        this.billboard = billboard;
        createGUI();
    }

    private void createGUI(){
        setTitle(BillboardImpl.class.getSimpleName());
        setSize(450, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(WindowEvent evt) {
                billboard.close();
            }
        });

        advertLabel = new JLabel("No adverts");
        advertLabel.setHorizontalAlignment(SwingConstants.CENTER);
        advertLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        add(advertLabel);
    }


    void updateAdvert(String advertText) {
        if (advertLabel != null) {
            advertLabel.setText(advertText);
        }
        invalidate();
    }

    void clear() {
        if (advertLabel != null) {
            advertLabel.setText("");
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignore) {
            }
        }
        invalidate();
    }

    void exit() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

}