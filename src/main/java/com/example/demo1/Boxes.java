package com.example.demo1;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import static javafx.scene.paint.Color.RED;
public class Boxes extends Observable {
    static String image = "C:\\Users\\lorda\\IdeaProjects\\demo1\\src\\main\\resources\\assets\\city\\1.png";

    public static enum Type
    {
        SELECT,
        START,
        END,
        ADD_IMAGE,
        ROAD,
        PATH,
        HIGHLIGHT,
        VISITED,
    }

    private final Map<Type, Color> enumType;
    private final Map<Integer, Color> wType;
    private final Map<Integer, Color> visitorType;
    private final int x;
    private final int y;
    private final StackPane pane;
    private final int defaultWeight = 3;
    private int weight;
    private Type type;
    private final int size;
    private final Rectangle rectangle;

    static Color pathcolor = RED;

    public static void getColor(Color color){
        pathcolor = color;
    }

    public Boxes(int x, int y, int size) throws FileNotFoundException {
            pane = new StackPane();
            enumType = new HashMap<>();
            enumType.put(Type.START, Color.TEAL);
            enumType.put(Type.END, Color.SKYBLUE);
            enumType.put(Type.ROAD, Color.YELLOW);
            enumType.put(Type.ADD_IMAGE, Color.BLACK);
            enumType.put(Type.PATH, pathcolor);
            enumType.put(Type.HIGHLIGHT, Color.VIOLET);
            enumType.put(Type.VISITED, Color.LIGHTGREEN);
            visitorType = new HashMap<>();
            int[] WEIGHTS = {this.getDefaultWeight(),5,12,22,19};
            visitorType.put(WEIGHTS[0], Color.PALEGREEN);
            visitorType.put(WEIGHTS[1], Color.LIGHTGREEN);
            visitorType.put(WEIGHTS[2], Color.SPRINGGREEN);
            visitorType.put(WEIGHTS[3], Color.GREENYELLOW);
            visitorType.put(WEIGHTS[4], Color.GREEN);
            wType = new HashMap<>();
            wType.put(WEIGHTS[0], Color.WHITE);
            wType.put(WEIGHTS[1], Color.LIGHTCYAN);
            wType.put(WEIGHTS[2], Color.AQUA);
            wType.put(WEIGHTS[3], Color.DEEPSKYBLUE);
            wType.put(WEIGHTS[4], Color.CORNFLOWERBLUE);
            this.x = x;
            this.y = y;
            this.weight = defaultWeight;
            this.type = Type.ROAD;
            this.size = size;
            double tileGap =3;

            this.rectangle = new Rectangle(size - tileGap, size - tileGap);
            this.rectangle.setFill(Color.BEIGE);

            pane.getChildren().add(this.rectangle);
            ImageView imageView = new ImageView();
            pane.setMinSize(size - tileGap, size - tileGap);
            imageView.setFitWidth(size);
            imageView.setFitHeight(size);
            imageView.setPreserveRatio(false);
            pane.getChildren().add(imageView);
            this.setTileStroke(true);
            pane.setTranslateX(x * size);
            pane.setTranslateY(y * size);
            tooltip(null);
            setEvents();
        }
    public StackPane getStackPane()
    {
        return this.pane;
    }

    public void setTileStroke(boolean setStroke)
    {
        this.rectangle.setStroke((setStroke) ? Color.BLACK : null);
    }
    private void tooltip(String text)
    {
        text = (text == null) ? this.toString() : text;
        Tooltip.install(pane, new Tooltip(text));
    }
    private void setEvents()
    {
        pane.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent me) ->
        {
            setChanged();
            notifyObservers();
        });
    }
    public void coordinates(boolean toAdd)
    {
        if(pane.getChildren().size() > 1) pane.getChildren().remove(1);
        if(toAdd)
        {
            Text coords = new Text(String.format("%d,%d", this.x, this.y));
            coords.setStyle(String.format("-fx-font: %d arial;", (size * 6) / 20));
            Color color = (this.getType() == Boxes.Type.ADD_IMAGE) ? Color.WHITE : Color.BLACK;
            coords.setFill(color);
            pane.getChildren().add(coords);
        }
    }
    public void clearTile()
    {
        this.changeBoxAttributes(Type.ROAD, defaultWeight);
    }

    public void changeBoxAttributes(Type type, int weight)
    {
        tooltip(null);
        Color color;
        color = switch (type) {
            case VISITED -> this.visitorType.get(this.getWeight());
            case ROAD -> this.wType.get(weight);
            default -> this.enumType.get(type);
        };
        this.rectangle.setFill(color);
        this.type = type;
        this.weight = weight;
        tooltip(null);
        if(type == Type.ADD_IMAGE){
            Image img = new Image(image);
            this.rectangle.setFill(new ImagePattern(img));
        }else{
            rectangle.setFill(color);
        }
        this.type = type;
        this.weight = weight;

        tooltip(null);
    }
    public static String getImage(){
        return image;
    }
    public int getDefaultWeight()
    {
        return this.defaultWeight;
    }
    public boolean isWall()
    {
        return (this.type == Type.ADD_IMAGE);
    }
    public Type getType()
    {
        return this.type;
    }
    public int getWeight()
    {
        return this.weight;
    }
    public int getX()
    {
        return this.x;
    }
    private static long elapsedTime;
    public static double getElapsedTime()
    {
        return elapsedTime;
    }
    public static void setElapsedTime(long l)
    {
        elapsedTime = l;
    }
    private static boolean isPathFound = false;

    public static void setPathFound(boolean pathFound)
    {
        isPathFound = pathFound;
    }
    public static boolean isDone = false;
    public static String getPathFound()
    {
        if(!isPathFound)   {
        return "No";
        }
        else {
            return "Yes";
        }
    }
    public int getY()
    {
        return this.y;
    }

}

