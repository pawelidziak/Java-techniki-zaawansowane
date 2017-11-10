import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.Customizer;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
/**
 *
 * @author pawelidziak
 */
public class AddressBookCustomizer extends Panel implements Customizer, KeyListener {

    private AddressBookBean target;
    private TextField maxRowsNumberField;
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    @Override
    public void setObject(Object bean) {
        target = (AddressBookBean) bean;
        Label label = new Label("Max rows number: ");
        add(label);
        maxRowsNumberField = new TextField(String.valueOf(target.getMaxRows()));
        add(maxRowsNumberField);
        maxRowsNumberField.addKeyListener(this);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(225, 50);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Object source = e.getSource();
        if (source == maxRowsNumberField) {
            String txt = maxRowsNumberField.getText();
            try {
                target.setMaxRows(Integer.valueOf(txt));
            } catch (NumberFormatException ex) {
                maxRowsNumberField.setText(String.valueOf(target.getMaxRows()));
            }
            support.firePropertyChange("", null, null);
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        support.removePropertyChangeListener(l);
    }
}
