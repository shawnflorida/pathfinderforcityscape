package com.example.demo1;
import Algorithm.AlgoTooltip;
import Strategy.Techniques.Tactics;
import Strategy.Explorer.AStarStrategy;
import Strategy.Explorer.Algo;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class mainController implements Initializable {
    @FXML
    Button pickImageButton;
    @FXML
    private ComboBox<Boxes.Type> toolChoicebox = new ComboBox<>(FXCollections.observableArrayList(Boxes.Type.values()));
    @FXML
    private ComboBox<String> projectChoicebox;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Button startButton;
    @FXML
    private Pane tilePane;
    @FXML
    private Slider tileXSize;
    @FXML
    private Slider tileYSize;
    @FXML
    private Slider tileSize;
    @FXML
    private TextField objectName;
    @FXML
    private TextField titleBox;
    private BoxGroupGrid model;
    @FXML
    private  TextField txtStatsElapsedTimeValue;
    @FXML
    private  TextField txtIsPathFound;
    @FXML
    private ComboBox<Algo.Algorithms> algorithmsComboBox = new ComboBox<>(FXCollections.observableArrayList(Algo.Algorithms.values()));

    @FXML
    private  ComboBox<AStarStrategy.Heuristic> heuristicComboBox = new ComboBox<>(FXCollections.observableArrayList(AStarStrategy.Heuristic.values()));

    @FXML
    public void generateTiles() throws FileNotFoundException {
        Boxes.getColor(colorPicker.getValue());
        tilePane.getChildren().clear();
        BoxGroupGrid model = new BoxGroupGrid();
        model.gridInit((int) tileXSize.getValue(), (int) tileYSize.getValue(), (int) tileSize.getValue());
        this.fillGrid(model.getGrid());

        this.model = model;
    }
    private Pane gridPane;

    private void fillGrid(Boxes[][] gridboxes)
    {
        this.gridPane = new Pane();
        for(Boxes[] row : gridboxes)
        {
            for(Boxes boxes : row)
            {
                tilePane.getChildren().add(boxes.getStackPane());
            }
        }
        this.tilePane.getChildren().add(gridPane);
    }
    @FXML
    private Rectangle imageViewer;

    @FXML
    private void callImageViewer() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("image-file.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);

        stage.getIcons().add(new Image("file:src/main/resources/assets/mapme.png"));
        stage.setTitle("Image Viewer");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void clearTiles(){
        tilePane.getChildren().clear();
    }
    public void doToggleTileCoords(boolean toAdd)
    {
        this.model.toggleCoords(toAdd);
    }
    @FXML
    private CheckBox isCheckCoords;
    @FXML
    private Button generateAgain;
    @FXML
    private void findPathButton(){
        startButton.setOnAction((event) ->
        {
            Algo.Algorithms algorithm = null;
            AStarStrategy.Heuristic heuristic = null;

            for(Algo.Algorithms algo : FXCollections.observableArrayList(Algo.Algorithms.values()))
            {
                if(algo == algorithmsComboBox.getValue())
                {
                    algorithm = algo;
                }
            }

            for(AStarStrategy.Heuristic heur : FXCollections.observableArrayList(AStarStrategy.Heuristic.values()))
            {
                if(heur == heuristicComboBox.getValue())
                {
                    heuristic = heur;
                }
            }

            if(algorithm != null && heuristic != null)
            {
                try
                {
                    doShortestPathAlgorithm(algorithm, heuristic);
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    public void doChangeClickType(Boxes.Type type)
    {
        this.model.changeClickType(type);
    }

    public void setTriggers()
    {
        toolChoicebox.setOnAction((event) ->
        {
            FXCollections.observableArrayList(Boxes.Type.values()).stream().filter((item) -> (toolChoicebox.getValue().toString().equals(item.toString()))).forEachOrdered(this::doChangeClickType);
        });

        algorithmsComboBox.setOnAction((event) ->
        {
            heuristicComboBox.setDisable(!(algorithmsComboBox.getSelectionModel().getSelectedItem().toString().contains("AStar")));
        });

        generateAgain.setOnAction((event) ->
        {
            clearTiles();
            int x = (int) tileXSize.getValue();
            x = (x % 2 == 0) ? x - 1 : x;
            int y = (int) tileYSize.getValue();
            y = (y % 2 == 0) ? y - 1 : y;
            int size = (int) tileSize.getValue();

            tilePane.getChildren().remove(gridPane);

            try {
                model.gridInit(x, y, size);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            this.fillGrid(model.getGrid());
        });
        startButton.setOnAction((event) ->
        {
            Algo.Algorithms algorithm = null;
            AStarStrategy.Heuristic heuristic = null;

            for(Algo.Algorithms algo : FXCollections.observableArrayList(Algo.Algorithms.values()))
            {
                if(algo == algorithmsComboBox.getValue())
                {
                    algorithm = algo;
                }
            }

            for(AStarStrategy.Heuristic heur : FXCollections.observableArrayList(AStarStrategy.Heuristic.values()))
            {
                if(heur == heuristicComboBox.getValue())
                {
                    heuristic = heur;
                }
            }

            if(algorithm != null && heuristic != null)
            {
                try
                {
                    doShortestPathAlgorithm(algorithm, heuristic);
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            txtStatsElapsedTimeValue.setText(String.format("%.4f Milliseconds", Boxes.getElapsedTime() * Math.pow(10, -6)));
            txtIsPathFound.setText(String.format(Boxes.getPathFound()));
            String type = projectChoicebox.getValue();
            String name = objectName.getText();
            titleBox.setText(type + " - " + name);
            Boxes.getColor(colorPicker.getValue());
            Image img = new Image(Boxes.getImage());
            imageViewer.setFill(new ImagePattern(img));
        });
    }  public void doAddRandomWalls()
    {
        this.model.addRandomWalls();
    }


    public void doShortestPathAlgorithm(Algo.Algorithms algorithm, AStarStrategy.Heuristic heuristic) throws InterruptedException
    {
        Tactics tactics = AlgoTooltip.getTactics(heuristic);
        Algo algo = AlgoTooltip.getPathAlgo(algorithm, tactics);
        this.model.executePathfinding(algo);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setTriggers();
        tileXSize.valueProperty().addListener(
                (ChangeListener<Number>) (observable, oldValue, newValue) -> tileXSize.setValue((Double) newValue));
        tileYSize.valueProperty().addListener(
                (ChangeListener<Number>) (observable, oldValue, newValue) -> tileYSize.setValue((Double) newValue));
        tileSize.valueProperty().addListener(
                (ChangeListener<Number>) (observable, oldValue, newValue) -> tileSize.setValue((Double) newValue));
        Image img = new Image(Boxes.getImage());

        imageViewer.setFill(new ImagePattern(img));
        try {
            colorPicker.setOnAction((ActionEvent e) -> {
                Boxes.getColor(colorPicker.getValue());
            });
            generateTiles();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        heuristicComboBox.getItems().setAll(AStarStrategy.Heuristic.values());
        algorithmsComboBox.getItems().setAll(Algo.Algorithms.values());
        heuristicComboBox.setValue(AStarStrategy.Heuristic.valueOf("Manhattan"));
        algorithmsComboBox.setValue(Algo.Algorithms.valueOf("Dijkstra"));

        toolChoicebox.getItems().setAll(Boxes.Type.values());
        toolChoicebox.setValue(Boxes.Type.valueOf("START"));
        projectChoicebox.getItems().addAll("Interior Design", "Compound", "Cityscape");
        projectChoicebox.setValue("Interior Design");
        isCheckCoords.setOnAction((event) ->
        {
            doToggleTileCoords(isCheckCoords.isSelected());
        });
        toolChoicebox.getItems().remove(Boxes.Type.VISITED);
        toolChoicebox.getItems().remove(Boxes.Type.PATH);
        toolChoicebox.getItems().remove(Boxes.Type.HIGHLIGHT);
    }

}


