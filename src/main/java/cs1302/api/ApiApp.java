
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.URI;
import java.net.http.HttpClient;

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
import cs1302.api.PhotoResult;
import cs1302.api.PhotoResponse;

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
    Label details;
    Label credit;

    private static HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)           // uses HTTP protocol version 2 where possible
        .followRedirects(HttpClient.Redirect.NORMAL)  // always redirects, except from HTTPS to HTTP
        .build();                                     // builds and returns an HttpClient

    private static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    /** A default image which loads when the application starts. */
    private static final String DEFAULT_IMG = "resources/default.png";


    private static final String WEATHER_KEY = "3VUANMh12DeqhHRY1ZlZMdKXNuCFLc2Tya2qgQVb";
    private static final String WEATHER_API = "https://api.api-ninjas.com/v1/weather?city=";

    private static final String PHOTO_API = "https://api.unsplash.com/photos/random";
    private static final String PHOTO_KEY = "xntOH7mSke1Zlr8sb8c2Hj55kGGNPCr4YTTuJmUInyY";
    private static final String CLIENT_ID = "";
    private static final String CLIENT_SECRET = "";


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

        instructions = new Label("Enter a city to get an outfit based on the current temperature.");
        this.cityLabel = new Label("City: ");
        this.cityText = new TextField("Atlanta");
        this.top = new HBox(10);
        this.stateLabel = new Label("State: ");
        this.stateText = new TextField("Georgia");

        this.findOutfit = new Button("Find Outfit");
        this.next = new Button("Next");
        this.imgView = new ImageView();
        this.details = new Label("test");
        this.credit = new Label("Images provided by Unsplash API.");
    } // ApiApp

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {

        EventHandler<ActionEvent> getImageAction = event -> {
            next.setDisable(true);
            String city = cityText.getText();
            runOnNewThread(() -> getPhoto(getWeather(city)));
        };

        this.stage = stage;
        top.getChildren().addAll(cityLabel, cityText, findOutfit);
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
v        Image bannerImage = new Image("file:resources/readme-banner.png");
        ImageView banner = new ImageView(bannerImage);
        banner.setPreserveRatio(true);
        banner.setFitWidth(640);

        // some labels to display information
        Label notice = new Label("Modify the starter code to suit your needs.");

        // setup scene
        root.getChildren().addAll(banner, notice); */
        root.getChildren().addAll(instructions, top, imgView, credit);
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
                instructions.setText("It is rainy in" + city + ". here is an outfit idea!");
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
                        String text =
                            "It's hot outside in " + city + ". Here's an outfit idea!";
                        Platform.runLater(() -> instructions.setText(text));

                        return "hot";
                    } else if (temp > 10.0) {
                        String text =
                            "It's moderate outside in " + city + ". Here's an outfit idea!";
                        Platform.runLater(() -> instructions.setText(text));
                        return "moderate";
                    } else {
                        String text =
                            "It's cold outside in " + city + ". Here's an outfit idea!";
                        Platform.runLater(() -> instructions.setText(text));
                        return "cold";
                    } //else
                } else {
                    System.out.println("Temperature not found in response.");
                    return "";
                } //else
            }
        } catch (IOException e) {
            e.printStackTrace();
            Platform.runLater(() -> alertError(e));
            Platform.runLater(() -> instructions.setText("Last attempt to get images failed..."));
            findOutfit.setDisable(false);
            imgView.setImage(null);
            return "";
        } //catch

    } //getWeather

    /**
     * Gets a link to an outfit from the unsplash API based on the weather condition.
     * @param condition from getWeather.
//     * @return PhotoResult with link.
     */
    public void getPhoto(String condition) {
        try {
            String term;
            if (condition.equals("hot")) {
                term = "shorts";

//                details.setText("Since it's so hot outside, I suggest wearing shorts!");
            } else if (condition.equals("cold")) {
                term = "coat";
            } else {
                term = "longsleeve";
            }
//            String term = "sweater";
            String word = URLEncoder.encode(term, StandardCharsets.UTF_8);
            String query = String.format("?query=%s&client_id=%s", word, PHOTO_KEY);
            String uri = PHOTO_API + query;

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();
            // send request / receive response in the form of a String
            HttpResponse<String> response = HTTP_CLIENT
                .send(request, BodyHandlers.ofString());
            // ensure the request is okay
            if (response.statusCode() != 200) {
                throw new IOException(response.toString());
            } // if
            String jsonString = response.body();
//            System.out.println(jsonString.trim());

            PhotoResponse photoResponse = GSON
                .fromJson(jsonString, PhotoResponse.class);
            System.out.println(GSON.toJson(photoResponse));

            PhotoResult result = GSON.fromJson(jsonString, PhotoResult.class);
            String link = result.getUrl().getRaw();
//            String link = "https://images.unsplash.com/photo-1417325384643-aac51acc9e5d?q=75&fm=jpg&w=1080&fit=max";
            Image image = getImage(link);
            imgView.setImage(image);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Platform.runLater(() -> alertError(e));
            Platform.runLater(() -> instructions.setText("Last attempt to get images failed..."));
            findOutfit.setDisable(false);
            Image nullImg = getImage("");
            imgView.setImage(nullImg);
//            return null;
        } //catch

    }

    /**
     * This method converts the url into an image.
     * @param url is an url to an image
     * @return Image to add to the imageview,
     */
    private Image getImage(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            Image img = new Image(connection.getInputStream());
            return img;

        } catch (IOException e) {
            e.printStackTrace();
            Platform.runLater(() -> alertError(e));
            Platform.runLater(() -> instructions.setText("Last attempt to get images failed..."));
            findOutfit.setDisable(false);
            return null;
        } //catch
    } //getImage

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
