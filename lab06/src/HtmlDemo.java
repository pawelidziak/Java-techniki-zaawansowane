import helpers.Configuration;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class HtmlDemo extends JPanel {
    private static JTextArea jta_input;
    private static JEditorPane jep_output;
    private static JFrame frame;

    /**
     * Method creates all elements witch will be in frame
     *
     * @param initialText - text witch will be formatted
     */
    private HtmlDemo(String initialText) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // ---------------------------------------------------------------------------------------
        // -------------------------------------------
        // -------------------------------------------          HEADER
        // -------------------------------------------
        // ---------------------------------------------------------------------------------------

        // creating combo box with options
        JComboBox jcb_options = new JComboBox<>(loadJSFiles());
        jcb_options.setMaximumSize(new Dimension(500, 50));

        JButton b_style = new JButton("STYLE!");
        b_style.addActionListener(event -> {
            invokeJSMethod(jcb_options.getSelectedItem().toString());
        });

        JButton b_reset = new JButton("RESET");
        b_reset.addActionListener(event -> resetStyles());

        // creating header (with combo box with options)
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        "Select the edit option, then click the button"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        headerPanel.add(jcb_options);
        headerPanel.add(b_style);
        headerPanel.add(b_reset);

        // ---------------------------------------------------------------------------------------
        // -------------------------------------------
        // -------------------------------------------          MIDDLE
        // -------------------------------------------
        // ---------------------------------------------------------------------------------------

        // creating JTextArea with input html
        jta_input = new JTextArea(20, 40);
        jta_input.setText(initialText);
        jta_input.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                jep_output.setText(jta_input.getText());
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                jep_output.setText(jta_input.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                jep_output.setText(jta_input.getText());
            }
        });

        // add JTextArea to Scroll Pane
        JScrollPane scrollPane = new JScrollPane(jta_input);

        // creating middle panel (with scrollPane with JTextArea with html)
        JPanel middlePanel = new JPanel();
        middlePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        "Edit the HTML, then click the button"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        middlePanel.add(scrollPane);
        middlePanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // ---------------------------------------------------------------------------------------
        // -------------------------------------------
        // -------------------------------------------          FOOTER
        // -------------------------------------------
        // ---------------------------------------------------------------------------------------

        // creating JTextArea with output (edited html)
        jep_output = new JEditorPane();
        jep_output.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        jep_output.setContentType("text/html");
        jep_output.setText(jta_input.getText());
        jep_output.setEditable(false);

        // creating footer panel (with JEditorPane with edited html)
        JPanel footerPanel = new JPanel();
        footerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("A label with HTML"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        footerPanel.add(jep_output);


        // add all panels to frame
        add(headerPanel);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(middlePanel);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(footerPanel);

    }


    /**
     * Method creates JFrame with elements from HtmlDemo()
     *
     * @param initialText - text witch will be formatted
     */
    public static void createAndShowGUI(String initialText) {
        // create and set up the window.
        frame = new JFrame("Paweł Idziak 208766 - lab06");
        frame.setSize(500, 700);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // add content to the window.
        frame.add(new HtmlDemo(initialText));
        frame.setVisible(true);
    }

    /**
     * Method invokes JS method from js_functions package
     *
     * !!!!!! FUNCTION INSIDE FILE HAS BE NAMED THE SAME AS THE FILE !!!!!!!!
     *
     * @param scriptFileName - JS method name which is in js_functions.js
     */
    private static void invokeJSMethod(String scriptFileName) {

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            engine.eval(new FileReader(Configuration.DECORATORS_FOLDER + scriptFileName));

            Invocable invocable = (Invocable) engine;

            Object result = invocable.invokeFunction(scriptFileName.replace(".js", ""), jep_output);

            if (result != null) {
                jep_output = (JEditorPane) result;
                jta_input.setText(jep_output.getText());
            }
        } catch (ScriptException | FileNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method resets styles (close opened frame and open new)
     */
    private static void resetStyles() {
        frame.setVisible(false); //you can't see me!
        frame.dispose(); //Destroy the JFrame object
        createAndShowGUI(Configuration.INIT_TEXT);
    }

    /**
     * Method loads name of JS scripts (string). It returned tab of Strings,
     * because JComboBox doesn't take List
     *
     * @return String[] with names
     */
    private static String[] loadJSFiles() {
        ArrayList<String> listWithFilesName = new ArrayList<>();
        getMethodsNames(Configuration.DECORATORS_FOLDER, listWithFilesName);
        return listWithFilesName.toArray(new String[listWithFilesName.size()]);
    }

    /**
     * Method gets all files from directory and add their name to list
     * @param directoryName - name of directory
     * @param listWithFilesName - list where names will be saved
     */
    private static void getMethodsNames(String directoryName, ArrayList<String> listWithFilesName) {
        File directory = new File(directoryName);
        File[] fileList = directory.listFiles();
        for (File file : fileList) {
            if (file.isFile()) {
                listWithFilesName.add(file.getName());
            } else if (file.isDirectory()) {
                getMethodsNames(file.getAbsolutePath(), listWithFilesName);
            }
        }
    }
}