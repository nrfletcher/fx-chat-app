package com.example.socketfx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class Builders extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // FileInputStream input = new FileInputStream("src/main/resources/com/example/chatapp/iconbright.png");
        // Image image = new Image(input);
        // stage.getIcons().add(image);

        MenuBar menuBar = new MenuBar();
        VBox vBox = new VBox(menuBar);
        Scene scene = new Scene(vBox, 960, 600);

        getFontFamilies();
        getFontNames();
        scene.setFill(Color.WHITE);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    public ImageView createImageView() throws FileNotFoundException {
        FileInputStream input = new FileInputStream("src/main/resources/com/example/chatapp/img.png");
        Image image = new Image(input);
        return new ImageView(image);
    }

    public TextField createTextField(Label fieldChanged) {
        TextField textField = new TextField();
        textField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String str = textField.getText();
                fieldChanged.setText(str);
            }
        });
        return textField;
    }

    public Text createText(String textBox) {
        Text text = new Text(textBox);
        text.setFill(Color.CADETBLUE);
        text.setFont(Font.font("Sans Serif"));
        text.setStroke(Color.CRIMSON);
        text.setStrikethrough(false);
        text.setUnderline(true);
        // For multiline instead of run on
        text.setWrappingWidth(40);
        text.setStyle("-fx-font: 24 SansSerif;");
        return text;
    }

    public Font createFont() {
        String fontFamily = "Sans Serif";
        double fontSize = 15;
        FontWeight fontWeight = FontWeight.BLACK;
        FontPosture fontPosture = FontPosture.ITALIC;
        return Font.font(fontFamily, fontWeight, fontPosture, fontSize);
    }

    public void getFontFamilies() {
        List<String> fontNames = Font.getFontNames();
        for(String item : fontNames) {
            System.out.println(item);
        }
    }

    public void getFontNames() {
        List<String> fontFamilies = Font.getFamilies();
        for(String item : fontFamilies) {
            System.out.println(item);
        }
    }

    public Hyperlink createHyperLink(String URL, String text) {
        Hyperlink link = new Hyperlink(URL);
        link.setText(text);
        link.setFont(Font.font("Courier New", FontWeight.BOLD, 36));
        link.setOnAction(e -> {
            System.out.println("Link was clicked");
        });
        return link;
    }

    public Label createLabel(String text) throws FileNotFoundException {
        // We can add images to labels
        FileInputStream input = new FileInputStream("src/main/resources/com/example/chatapp/img.png");
        Image image = new Image(input);
        ImageView img = new ImageView(image);
        Label label = new Label("nothing", img);
        label.setFont(new Font("Arial", 24));
        label.setText(text);
        return label;
    }

    public Button createButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-text-size: 20;");
        button.setWrapText(true);
        button.setFont(new Font("Times New Roman", 12));
        button.setDefaultButton(true);
        button.setCancelButton(true);
        // you can also add an image to a button -> new Button("Text", imageView)
        return button;
    }

    public HBox buttonChangesLabelAsHBox() {
        Label label = new Label("Example text");
        Button button = new Button("Press me!");
        button.setStyle("-fx-border-color: #ff0000;" +
                "-fx-background-color: #00ff00;" +
                "-fx-font-size: 2em;" +
                "-fx-text-fill: #0000ff");
        button.setMnemonicParsing(true); // lets us use an ALT key conjunction
        button.setOnAction(evt -> {
            label.setText("This is different now!");
        });
        return new HBox(label, button);
    }

    public MenuButton createNormalMenu() {
        MenuItem item1 = new MenuItem("Option 1");
        MenuItem item2 = new MenuItem("Option 2");
        MenuItem item3 = new MenuItem("Option 3");
        // Second param is for optional ImageView
        return new MenuButton("Options", null, item1, item2, item3);
    }

    public Slider createSlider(int val1, int val2, int val3) {
        Slider slider = new Slider(val1, val2, val3);
        slider.setMajorTickUnit(5);
        slider.setMinorTickCount(1);
        slider.setSnapToTicks(false);
        slider.setShowTickMarks(false);
        slider.setShowTickLabels(false);
        return slider;
    }

    // this may be more relevant than TextField
    public TextArea createTextArea(String s) {
        return new TextArea(s);
    }

    public VBox createTabPanes() {
        TabPane tabPane = new TabPane();

        Tab tab1 = new Tab("Planes", new Label("Show all planes available"));
        Tab tab2 = new Tab("Cars"  , new Label("Show all cars available"));
        Tab tab3 = new Tab("Boats" , new Label("Show all boats available"));

        tabPane.getTabs().add(tab1);
        tabPane.getTabs().add(tab2);
        tabPane.getTabs().add(tab3);

        return new VBox(tabPane);
    }

    public Background backgroundGenerator() {
        BackgroundFill backgroundFill1 =
                new BackgroundFill(
                        Color.valueOf("#ff00ff"),
                        new CornerRadii(0),
                        new Insets(0)
                );

        BackgroundFill backgroundFill2 =
                new BackgroundFill(
                        Color.valueOf("#00ff00"),
                        new CornerRadii(10),
                        new Insets(10)
                );

        return new Background(backgroundFill1, backgroundFill2);
    }
}
