package com.example.demo1;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class imageController implements Initializable {
    private final int nRows =3;
    private final int nCols =6;
    private static final double ELEMENT_SIZE = 100;
    private static final double GAP = 30;
    @FXML
    private TilePane tilePane;
    File[] filesJpg;

    @FXML
    private void addButtonAction() {

        Stage parent = new Stage();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(parent);

        if (selectedDirectory != null) {
            FilenameFilter filterJpg = (dir, name) -> name.toLowerCase().endsWith(".png");

            filesJpg = selectedDirectory.listFiles(filterJpg);

        }

        createElements();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tilePane.setPrefColumns(nCols);
        tilePane.setPrefRows(nRows);
        tilePane.setHgap(GAP);
        tilePane.setVgap(GAP);
    }

    private void createElements() {
        int count = 0;
        tilePane.getChildren().clear();
            for (int i = 0; i < nCols; i++) {
                for (int j = 0; j < nRows; j++) {
                    if(count<filesJpg.length){
                        tilePane.getChildren().add(createPage(count));
                        count++;
                    }

                }
            }
    }

    String imageToString;

    public VBox createPage(int index){

        ImageView imageView = new ImageView();

        File file = filesJpg[index];
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
            imageView.setFitWidth(ELEMENT_SIZE);
            imageView.setFitHeight(ELEMENT_SIZE);

            imageView.setSmooth(true);
            imageView.setCache(true);
        } catch (IOException ignored) {
        }

        VBox pageBox = new VBox();
        Button button = new Button();
        button.setGraphic(imageView);

        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                imageToString = file.toURI().toString();
                System.out.println(file.toURI());
                try {
                   setImageToMain(e);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        button.setOnAction(event);

        pageBox.getChildren().add(button);

        pageBox.setStyle("-fx-border-color: orange;");

        return pageBox;
    }

    public void setImageToMain(ActionEvent e) {
        Boxes.image = imageToString;
    }

    @FXML
    public void clearImages(ActionEvent e) {
        tilePane.getChildren().clear();
    }

}


