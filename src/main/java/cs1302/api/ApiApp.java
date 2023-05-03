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
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.io.IOException;

/**
 * This app takes in a city, and gets the current weather of that city.
 * It then takes in the weather of that city and inputs it into the pinterest API
 * and outputs an image of an outfit based on the weather.
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
//    private static final String WEATHER_API_URL = "https://api.weather.gov/";
//    private static final String WEATHER_API_ENDPOINT = "points/";
    private static final String WEATHER_KEY = "3VUANMh12DeqhHRY1ZlZMdKXNuCFLc2Tya2qgQVb";
    private static final String WEATHER_API = "https://api.api-ninjas.com/v1/weather?city=";

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

        EventHandler<ActionEvent> getImageAction = event -> {
            next.setDisable(true);
            runOnNewThread(() -> System.out.println(getWeather("Atlanta")));
        };

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
        findOutfit.setOnAction(getImageAction);
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

    /**
     * Based on the user's inputs, returns a weather condition for that location.
     * @param city the user inputs
     * @return a weather condition for the location
     */
    private String getWeather(String city) {
        try {
            String url = WEATHER_API + URLEncoder.encode(city, StandardCharsets.UTF_8.toString());
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Api-Key", WEATHER_KEY);

            int responseCode = con.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String weather = response.toString();
            System.out.println(weather);
            if (weather.contains("rain") || weather.contains("thunderstorm")) {
                System.out.println("rainy");
                return "rainy";
            } else {
                int tempIndex = weather.indexOf("\"temp\":");
                if (tempIndex >= 0) {
                    int endIndex = weather.indexOf(",", tempIndex);
                    if (endIndex < 0) {
                        endIndex = weather.indexOf("}", tempIndex);
                    }
                    String tempString = weather.substring(tempIndex + 7, endIndex);
                    double temp = Double.parseDouble(tempString);
                    if (temp > 25.0) {
                        return "hot";
                    } else if (temp > 10.0) {
                        return "moderate";
                    } else {
                        return "cold";
                    }
                } else {
                    System.out.println("Temperature not found in response.");
                    return "";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Platform.runLater(() -> alertError(e));
            Platform.runLater(() -> instructions.setText("Last attempt to get images failed..."));
            findOutfit.setDisable(false);
            return "";
        } //catch

    } //getWeather

    /*
    private String callAPI(String url) {
        URL apiUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) apiUrl.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }
        conn.disconnect();
        return sb.toString();
    }
    /*
    private String getImage(String weather) {

    } //getImage */

     /**
      * Show a modal error alert based on {@code cause}.
      * @param cause a {@link java.lang.Throwable Throwable} that caused the alert
      */
    public static void alertError(Throwable cause) {
        TextArea text = new TextArea(cause.toString());
        text.setEditable(false);
        Alert alert = new Alert(AlertType.ERROR);
        alert.getDialogPane().setContent(text);
        alert.setResizable(true);
        alert.showAndWait();
    } // alertError

    /**
     * Creates and immediately starts a new dameon thread that executes.
     * @param target the object
     */
    public static void runOnNewThread(Runnable target) {
        Thread t = new Thread(target);
        t.setDaemon(true);
        t.start();
    } //runOnNewThread

} // ApiApp
