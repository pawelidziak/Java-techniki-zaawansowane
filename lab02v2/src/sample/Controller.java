package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.lang.reflect.*;
import java.util.*;

public class Controller {

    @FXML
    TextField tf_method_name;
    @FXML
    TextField tf_signature;
    @FXML
    ListView<Class> lv_classes = new ListView<>();
    @FXML
    ListView<Method> lv_methods = new ListView<>();
    @FXML
    Button b_run;
    @FXML
    Button b_clear;

    private List<String> packageWithClassList = new ArrayList<>();
    private MyClassLoader myClassLoader = null;

    private Method methodToRun;
    private Class classToRun;

    public void initialize() {

        findPackagesWithClasses(Configuration.PATH_TO_FOLDER);

        lv_classes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!lv_methods.getItems().isEmpty()) lv_methods.getItems().clear();
            if (newValue != null) findMethodsInClass(newValue);
            classToRun = newValue;
        });

        lv_methods.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            methodToRun = newValue;
            b_run.setDisable(false);
            if (newValue != null) System.out.println(getSignature(methodToRun));

        });
    }

    @FXML
    void run() {
        try {
            // private - 2
            // nic - 0
            // public - 1
            if (methodToRun.getModifiers() == 1) {
                List<Object> invokeArgsList = new ArrayList<>();

                for (Class c : methodToRun.getParameterTypes()) {
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Put argument");
                    dialog.setHeaderText("Enter argument of type: " + c);
                    Optional<String> result = dialog.showAndWait();
                    String entered = "none.";
                    if (result.isPresent()) {
                        entered = result.get();
                    }
                    invokeArgsList.add(parseToObject(c.toString(), entered));
                }
                b_clear.setDisable(false);

                Class<?> base = Class.forName(classToRun.getName());
                Method serverMethod = base.getMethod(methodToRun.getName(), methodToRun.getParameterTypes());
                serverMethod.setAccessible(true);
                Object[] invokeArgs = invokeArgsList.toArray(new Object[invokeArgsList.size()]);
                serverMethod.invoke(base.newInstance(), invokeArgs);

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Probably modifier is not public.");
                alert.showAndWait();
            }

        } catch (NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private Object parseToObject(String type, String value) {
        System.out.println(type);
        switch (type) {
            case "int":
                return Integer.parseInt(value);
            case "boolean":
                return Boolean.parseBoolean(value);
            case "char":
                return Byte.parseByte(value);
            case "byte":
                return Byte.parseByte(value);
            case "short":
                return Short.parseShort(value);
            case "long":
                return Long.parseLong(value);
            case "float":
                return Float.parseFloat(value);
            case "double":
                return Double.parseDouble(value);
            case "class java.lang.String":
                return value;
        }
        return null;
    }

    @FXML
    void search() {
        // jesli pola nie sa puste
        if (!tf_method_name.getText().equals("") && !tf_signature.getText().equals("")) {

            if (!lv_classes.getItems().isEmpty()) lv_classes.getItems().clear();
            b_clear.setDisable(true);
            b_run.setDisable(true);
            try {
                searchClassesWithMethodsWithSignature(tf_method_name.getText(), tf_signature.getText());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void clear() {
        if (!lv_classes.getItems().isEmpty()) lv_classes.getItems().clear();
        if (!lv_methods.getItems().isEmpty()) lv_methods.getItems().clear();
        if (myClassLoader != null) myClassLoader = null;
        System.gc();
        System.out.println("\n\nWyładowano wszystkie kalsy.");
    }

    private void searchClassesWithMethodsWithSignature(String methodName, String signature) throws ClassNotFoundException {
        myClassLoader = new MyClassLoader();

        for (String packageWithClass : packageWithClassList) {
            Class myClass = myClassLoader.loadClass(packageWithClass);
            Method[] methods = myClass.getDeclaredMethods();

            for (Method m : methods) {
                if (m.getName().equals(methodName) && getSignature(m).contains(signature)) {
                    lv_classes.getItems().addAll(myClass);
                }
            }
        }

    }

    private void findMethodsInClass(Class clas) {
        Method[] methods = clas.getDeclaredMethods();
        for (Method m : methods) {
            lv_methods.getItems().addAll(m);
        }
    }

    private static String getSignature(Method m) {
        String sig;
        try {
            Field gSig = Method.class.getDeclaredField("signature");
            gSig.setAccessible(true);
            sig = (String) gSig.get(m);
            if (sig != null) return sig;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder("(");
        for (Class<?> c : m.getParameterTypes())
            sb.append((sig = Array.newInstance(c, 0).toString()).substring(1, sig.indexOf('@')));

        return sb.append(')')
                .append(
                        m.getReturnType() == void.class ? "V" : (sig = Array.newInstance(m.getReturnType(), 0).toString()).substring(1, sig.indexOf('@'))
                ).toString();
    }


    private void findPackagesWithClasses(String directoryName) {
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                String packageWithClass = file.toString().replace(Configuration.PATH_TO_FOLDER, "").replace(".class", "").replace("\\", ".");
                packageWithClassList.add(Configuration.NAME_OF_FOLDER + "." + packageWithClass);
            } else if (file.isDirectory()) {
                findPackagesWithClasses(file.getAbsolutePath());
            }
        }
    }
}
