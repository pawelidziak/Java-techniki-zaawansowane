package sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import sample.annotations.*;
import sample.helpers.Configuration;
import sample.helpers.LEVEL_ENUM;
import sample.helpers.Pair;
import sample.helpers.STRATEGY_ENUM;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private String[][] board;
    private List<String> packageWithClassList = new ArrayList<>();
    private List<Class> loadedClasses = new ArrayList<>();
    private LEVEL_ENUM LEVEL = LEVEL_ENUM.EASY;
    private STRATEGY_ENUM STRATEGY = STRATEGY_ENUM.DEFENSE;

    private final int STEPS_TO_WIN = 5;
    private int BOARD_SIZE = 5;

    @FXML
    private GridPane gp_board;
    @FXML
    private ComboBox cb_level;
    @FXML
    private ComboBox cb_size;
    @FXML
    private ComboBox cb_strategy;
    @FXML
    private Text t_winner;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createBoard();
        findPackagesWithClasses(Configuration.PATH_TO_FOLDER);
        loadClasses();
        initSizeCB();
        initLevelCB();
        initStrategyCB();
    }


    @FXML
    void reset() {
        gp_board.setDisable(false);
        createBoard();
        t_winner.setText("");
    }

    private void createBoard() {
        moves = 0;
        gp_board.getChildren().clear();
        board = null;
        board = new String[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Button button = new Button();
                int finalI = i;
                int finalJ = j;
                button.setOnAction(e -> movePlayer(new Pair(finalI, finalJ)));
                button.setMinWidth(30.0);
                gp_board.add(button, j, i);
                board[j][i] = Configuration.EMPTY_BOARD_FIELD;
            }
        }
    }

    private void movePlayer(Pair pair) {
        if (checkWin(pair, Configuration.PLAYER_MOVE)) gp_board.setDisable(true);
        else moveComputer();

    }

    private void moveComputer() {
        Pair pair = getResponseFromComputer();
        if (checkWin(pair, Configuration.COMPUTER_MOVE)) gp_board.setDisable(true);
    }

    // metoda wysyla plansze i dostaje w odpowiedzi "strzał" komutera
    private Pair getResponseFromComputer() {

        // szukam klasy z dana adnotacja (strategia)
        Class c = findClassWithAnnos();
        // jesli znalazlem klase
        if (c != null) {
            // to szukam w niej metodny z dana adnotacja (poziom)
            Method method = findMethodWithAnnos(c);
            try {
                // jesli znalazłem metode
                if (method != null) {
                    // to wywołuje ją przekazujac w argumencie plansze, a zwracam "strzał" komputera
                    return (Pair) method.invoke(c.newInstance(), (Object) board);
                }
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private int moves = 0;

    private boolean checkWin(Pair pair, String s) {
        if (pair == null) {
            return false;
        }
        int x = pair.getRow();
        int y = pair.getColumn();

        int row = 0, col = 0, diag = 0, diag2 = 0;
        if (Objects.equals(board[x][y], Configuration.EMPTY_BOARD_FIELD)) {
            board[x][y] = s;
            Button button = (Button) getNodeFromGridPane(gp_board, pair.getColumn(), pair.getRow());
            button.setText(s);
            button.setDisable(true);
        }

        moves++;

        if (moves == (Math.pow(BOARD_SIZE, 2) - 1)) {
            System.out.println("DRAW");
            t_winner.setText("DRAW");
            gp_board.setDisable(true);
            return false;
        }

        //check row
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (Objects.equals(board[x][i], s))
                row++;
            else row = 0;
            if (row == STEPS_TO_WIN) {
                System.out.println(s + " WON! row");
                t_winner.setText(s + " WON!");
                return true;
            }
        }

        //check col
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (Objects.equals(board[i][y], s))
                col++;
            else col = 0;
            if (col == STEPS_TO_WIN) {
                System.out.println(s + " WON! col");
                t_winner.setText(s + " WON!");
                return true;
            }
        }

        // powyzej glownej przekatnej
        if (y > x) {
            int topX = y - x;
            if (topX < 0) topX = 0;
            int topY = 0;
            // diagonal
            for (int i = topX; i < BOARD_SIZE; i++) {
                if (Objects.equals(board[topY][i], s))
                    diag++;
                else diag = 0;
                if (diag == STEPS_TO_WIN) {
                    System.out.println(s + " WON! diag Y");
                    t_winner.setText(s + " WON!");
                    return true;
                }
                topY++;
            }

            //ponizej glownej przekatnej
        } else {
            int topX = 0;
            int topY = x - y;
            if (topY < 0) topY = 0;
            // diagonal
            for (int i = topY; i < BOARD_SIZE; i++) {
                if (Objects.equals(board[i][topX], s))
                    diag++;
                else diag = 0;
                if (diag == STEPS_TO_WIN) {
                    System.out.println(s + " WON! diag X");
                    t_winner.setText(s + " WON!");
                    return true;
                }
                topX++;
            }

        }

        int topX2 = x - (BOARD_SIZE - 1) + y;
        if (topX2 < 0) topX2 = 0;
        int topY2 = x + y;
        if (topY2 > BOARD_SIZE - 1) topY2 = BOARD_SIZE - 1;

        for (int i = topX2; i < BOARD_SIZE; i++) {
            if (Objects.equals(board[i][topY2], s)) {
                diag2++;
            } else diag2 = 0;
            if (diag2 == STEPS_TO_WIN) {
                System.out.println(s + " WON! diag X");
                t_winner.setText(s + " WON!");
                return true;
            }
            if (topY2 > 0) {
                topY2--;
            }
        }
        return false;
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    private Class findClassWithAnnos() {
        Class returnClass = null;

        for (Class c : loadedClasses) {
            Annotation annos = c.getDeclaredAnnotation(Strategy.class);
            if (annos != null) {
                Strategy strategy = (Strategy) annos;
                if(strategy.name().equals(STRATEGY.toString())){
                    returnClass = c;
                }
            }
        }
        return returnClass;
    }

    // metoda znajduje odpowiednie metody z daną adnotacją
    private Method findMethodWithAnnos(Class c) {
        Method returnMethod = null;
        Method[] methods = c.getMethods();
        for (Method method : methods) {
            // sprawdzam adnotacje, jesli jest zgodna z LEVEL to zwracam metodę

            Annotation annos = method.getDeclaredAnnotation(Level.class);
            if (annos != null) {
                Level level = (Level) annos;
                if(level.name().equals(LEVEL.toString())){
                    returnMethod = method;
                }
            }
        }
        return returnMethod;
    }

    // METHODS FOR CLASS LOADER

    private void loadClasses() {
        try {
            File customElementsDir = new File(Configuration.PATH_TO_FOLDER);
            URL url = customElementsDir.toURI().toURL();
            URL[] urls = new URL[]{url};
            URLClassLoader myClassLoader = new URLClassLoader(urls);

            for (String s : packageWithClassList) {
                Class c = myClassLoader.loadClass(s);
                loadedClasses.add(c);
            }

        } catch (MalformedURLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void findPackagesWithClasses(String directoryName) {
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                String packageWithClass = file.toString().replace(Configuration.PATH_TO_FOLDER, "").replace(".class", "").replace("\\", ".");
                packageWithClassList.add(packageWithClass);
            } else if (file.isDirectory()) {
                findPackagesWithClasses(file.getAbsolutePath());
            }
        }
    }


    // INITIALIZE COMPONENT METHODS

    private void initSizeCB() {
        cb_size.setItems(FXCollections.observableArrayList(
                5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15
        ));
        cb_size.getSelectionModel().selectFirst();
        cb_size.valueProperty().addListener((ov, t, t1) -> {
            BOARD_SIZE = (int) t1;
            createBoard();
        });
    }

    private void initLevelCB() {
        cb_level.setItems(FXCollections.observableArrayList(
                LEVEL_ENUM.EASY,
                LEVEL_ENUM.MID,
                LEVEL_ENUM.HARD
        ));
        cb_level.getSelectionModel().selectFirst();
        cb_level.valueProperty().addListener((ov, t, t1) -> {
            LEVEL = (LEVEL_ENUM) t1;
        });
    }


    private void initStrategyCB() {
        cb_strategy.setItems(FXCollections.observableArrayList(
                STRATEGY_ENUM.DEFENSE,
                STRATEGY_ENUM.ATTACK
        ));
        cb_strategy.getSelectionModel().selectFirst();
        cb_strategy.valueProperty().addListener((ov, t, t1) -> {
            STRATEGY = (STRATEGY_ENUM) t1;
        });
    }
}
