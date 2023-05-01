package cs1302.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */
public class ApiApp extends Application {
    Stage stage;
    Scene scene;
    VBox root;
    HBox cityHBox;

    Label instructions;
    Label cityLabel;
    TextField cityText;

    Label stateLabel;
    TextField stateText;
    HBox top;
    Button findOutfit;
    Button next;
    ImageView imgView;
    /** A default image which loads when the application starts. */
    private static final String DEFAULT_IMG = "resources/default.png";

    /** Default height and width for Images. */
    private static final int DEF_HEIGHT = 500;
    private static final int DEF_WIDTH = 500;
    private static int FIT_HEIGHT = 300;


    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        root = new VBox();

        instructions = new Label("Enter a city in the US to get an outfit based on the weather.");
        this.cityLabel = new Label("City: ");
        this.cityText = new TextField("Atlanta");
        this.top = new HBox(10);
        this.stateLabel = new Label("State: ");
        this.stateText = new TextField("Georgia");

        this.findOutfit = new Button("Find Outfit");
        this.next = new Button("Next");
        this.imgView = new ImageView();
    } // ApiApp

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {

        this.stage = stage;
        top.getChildren().addAll(cityLabel, cityText, stateLabel, stateText, findOutfit);
//        stateHBox.getChildren().addAll(stateLabel, stateText);

        top.setAlignment(Pos.CENTER);
        top.setSpacing(10);

        imgView.setPreserveRatio(true);
        imgView.setFitWidth(300);
        imgView.setFitHeight(300);

        // load the default image
        Image defaultImage = new Image("file:" + DEFAULT_IMG);
        // add the image to the imageview
        imgView.setImage(defaultImage);

        /*
        // demonstrate how to load local asset using "file:resources/"
        Image bannerImage = new Image("file:resources/readme-banner.png");
        ImageView banner = new ImageView(bannerImage);
        banner.setPreserveRatio(true);
        banner.setFitWidth(640);

        // some labels to display information
        Label notice = new Label("Modify the starter code to suit your needs.");

        // setup scene
        root.getChildren().addAll(banner, notice); */
        root.getChildren().addAll(instructions,top,imgView,next);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        scene = new Scene(root);

        // setup stage
        stage.setMaxWidth(1280);
        stage.setMaxHeight(720);
        stage.setTitle("ApiApp!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();

    } // start

    /*
    private String getWeather(String city, String state) {

    } //getWeather */

    /*
    private String getImage(String weather) {

    } //getImage */
} // ApiApp
