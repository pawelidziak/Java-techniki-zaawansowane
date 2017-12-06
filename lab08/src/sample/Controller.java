package sample;

import generated.Offer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.WebView;

import javax.xml.bind.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

// cd src -> xjc offer.xsd
// https://www.mkyong.com/java/jaxb-hello-world-example/

public class Controller implements Initializable {

    @FXML
    private TextField offer_id;
    @FXML
    private TextField equipment_type;
    @FXML
    private TextField equipment_desc;
    @FXML
    private TextField equipment_location;
    @FXML
    private TextField price;
    @FXML
    private TextField expiry_after_days;
    @FXML
    private TextField first_name;
    @FXML
    private TextField last_name;
    @FXML
    private TextField address;
    @FXML
    private TextField telephone;

    @FXML
    public WebView wv_html;
    @FXML
    private ComboBox<String> cb_xml_1;
    @FXML
    private ComboBox<String> cb_xml_2;
    @FXML
    private ComboBox<String> cb_xsl;

    private String SELECTED_XML = "Select...";
    private String SELECTED_XSL = "Select...";

    private Marshaller JAXB_marshaller = JAXBContext.newInstance(Offer.class).createMarshaller();
    private Unmarshaller JAXB_unmarshaller = JAXBContext.newInstance(Offer.class).createUnmarshaller();

    public Controller() throws JAXBException {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initXMLcb();
        initXSLcb();
        myListener();
    }

    /**
     * Method load XML file and get offer from it.
     */
    @FXML
    void loadXML() {
        try {
            Offer offer = (Offer) JAXB_unmarshaller.unmarshal(new File(Configuration.XML_FOLDER + SELECTED_XML));
            updateOfferFields(offer);
        } catch (JAXBException e) {
            System.err.println("loadXML ERROR: " + e.toString());
            displayAlert("XML file not loaded",
                    "ERROR",
                    e.toString(),
                    Alert.AlertType.ERROR);
        }
    }

    /**
     * Method generate new offer and save it in new XML file or updates selected XML file.
     */
    @FXML
    void generateEditXML() {
        Offer offer = new Offer();
        Offer.Seller seller = new Offer.Seller();

        offer.setOfferId(new BigInteger(offer_id.getText()));
        offer.setEquipmentType(equipment_type.getText());
        offer.setEquipmentDesc(equipment_desc.getText());
        offer.setEquipmentLocation(equipment_location.getText());
        offer.setPrice(new BigDecimal(price.getText()));
        offer.setExpiryAfterDays(new BigInteger(expiry_after_days.getText()));

        seller.setFirstName(first_name.getText());
        seller.setLastName(last_name.getText());
        seller.setAddress(address.getText());
        seller.setTelephone(telephone.getText());

        offer.setSeller(seller);

        File file = new File(Configuration.XML_FOLDER + offer.getOfferId() + ".xml");
        try {
            JAXB_marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            JAXB_marshaller.marshal(offer, file);
            displayAlert("XML file saved",
                    "INFO",
                    "The XML file has been saved!",
                    Alert.AlertType.INFORMATION);
        } catch (JAXBException e) {
            System.err.println("generateEditXML ERROR: " + e.toString());
            displayAlert("XML file not saved",
                    "ERROR",
                    e.toString(),
                    Alert.AlertType.ERROR);
        }
    }

    /**
     * Method gets XML and XSL files paths, creates new file 'offer.html' and puts this file into Web View.
     * http://www.java2s.com/Code/Java/JavaFX/UsingWebViewtodisplayHTML.htm
     *
     * @throws TransformerException
     */
    @FXML
    void displayXMLwithXSL() throws TransformerException {
        TransformerFactory tFactory = TransformerFactory.newInstance();

        Source xmlDoc = new StreamSource(Configuration.XML_FOLDER + SELECTED_XML);
        Source xslDoc = new StreamSource(Configuration.XSL_FOLDER + SELECTED_XSL);


        try {
            String outputFileName = "offer.html";
            OutputStream htmlFile = new FileOutputStream(outputFileName);

            Transformer transformer = tFactory.newTransformer(xslDoc);
            transformer.transform(xmlDoc, new StreamResult(htmlFile));
            List<String> lines = Files.readAllLines(Paths.get("offer.html"), StandardCharsets.UTF_8);

            StringBuilder htmlText = new StringBuilder();
            for (String line : lines) htmlText.append(line);

            wv_html.getEngine().loadContent(htmlText.toString());
        } catch (TransformerConfigurationException | IOException e) {
            System.err.println("displayXMLwithXSL ERROR: " + e.toString());
            displayAlert("XML file not generated",
                    "ERROR",
                    e.toString(),
                    Alert.AlertType.ERROR);
        }
    }

    /**
     * Method updates all offer fields. This method is using during loading XML file in TAB 1.
     *
     * @param offer - instance of Offer class
     */
    private void updateOfferFields(Offer offer) {
        offer_id.setText(String.valueOf(offer.getOfferId()));
        equipment_type.setText(offer.getEquipmentType());
        equipment_desc.setText(offer.getEquipmentDesc());
        equipment_location.setText(offer.getEquipmentLocation());
        price.setText(String.valueOf(offer.getPrice()));
        expiry_after_days.setText(String.valueOf(offer.getExpiryAfterDays()));
        first_name.setText(offer.getSeller().getFirstName());
        last_name.setText(offer.getSeller().getLastName());
        address.setText(offer.getSeller().getAddress());
        telephone.setText(offer.getSeller().getTelephone());
    }

    /**
     * Method display alerts to user.
     *
     * @param title       - title of the alert
     * @param headerText  - header text of the alert
     * @param contentText - content text of the alert
     * @param type        - type of the alert
     */
    private void displayAlert(String title, String headerText, String contentText, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    /**
     * Method initialize XML combo boxes.
     */
    private void initXMLcb() {
        cb_xml_1.setItems(FXCollections.observableArrayList(getOffers()));
        cb_xml_1.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                SELECTED_XML = newValue;
            }
        });
        cb_xml_2.setItems(FXCollections.observableArrayList(getOffers()));
        cb_xml_2.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                SELECTED_XML = newValue;
            }
        });
    }

    /**
     * Method initialize XSL combo box.
     */
    private void initXSLcb() {
        cb_xsl.setItems(FXCollections.observableArrayList(getTransformations()));
        cb_xsl.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                SELECTED_XSL = newValue;
            }
        });
    }

    /**
     * Method loads name of XML files (string). It returned tab of Strings
     *
     * @return String[] with names
     */
    private ArrayList<String> getOffers() {
        ArrayList<String> listWithFilesName = new ArrayList<>();
        getFilesNames(Configuration.XML_FOLDER, listWithFilesName);
        return listWithFilesName;
    }

    /**
     * Method loads name of XSL files (string). It returned tab of Strings
     *
     * @return String[] with names
     */
    private ArrayList<String> getTransformations() {
        ArrayList<String> listWithFilesName = new ArrayList<>();
        getFilesNames(Configuration.XSL_FOLDER, listWithFilesName);
        return listWithFilesName;
    }

    /**
     * Method gets all files from directory and add their name to list
     *
     * @param directoryName     - name of directory
     * @param listWithFilesName - list where names will be saved
     */
    private static void getFilesNames(String directoryName, ArrayList<String> listWithFilesName) {
        File directory = new File(directoryName);
        File[] fileList = directory.listFiles();
        for (File file : fileList != null ? fileList : new File[0]) {
//        for (File file : fileList) {
            if (file.isFile()) {
                listWithFilesName.add(file.getName());
            } else if (file.isDirectory()) {
                getFilesNames(file.getAbsolutePath(), listWithFilesName);
            }
        }
    }

    /**
     * Listener which listen to change in 'oferty' and 'transformacje' folder.
     * When it detects changes, it automatically update combo boxes.
     */
    private void myListener() {
        Thread thread = new Thread(() -> {
            Path path1 = Paths.get(Configuration.XML_FOLDER);
            Path path2 = Paths.get(Configuration.XSL_FOLDER);
            try (final WatchService watchService = FileSystems.getDefault().newWatchService()) {
                path1.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
                path2.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
                while (true) {
                    final WatchKey wk = watchService.take();
                    for (WatchEvent<?> event : wk.pollEvents()) {
                        updateXMLlist();
                        updateXSLlist();
                    }
                    // reset the key
                    boolean valid = wk.reset();
                    if (!valid) {
                        System.out.println("Key has been unregisterede");
                    }
                }
            } catch (IOException | InterruptedException e) {
                System.out.println();
            }
        });

        thread.start();
    }

    /**
     * Method updates XML combo boxes lists.
     */
    private void updateXMLlist() {
        Platform.runLater(() -> {
            cb_xml_1.setItems(FXCollections.observableArrayList(getOffers()));
            cb_xml_2.setItems(FXCollections.observableArrayList(getOffers()));
        });
    }

    /**
     * Method updates XSL combo box list.
     */
    private void updateXSLlist() {
        Platform.runLater(() -> cb_xsl.setItems(FXCollections.observableArrayList(getTransformations())));
    }
}
