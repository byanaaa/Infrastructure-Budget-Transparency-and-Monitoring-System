
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.io.File;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();

        URL page = getClass().getResource("/index.html");

        if (page == null) {
            System.out.println("Error: index.html not found!");
            return;
        }

        engine.getLoadWorker().stateProperty().addListener(
            (observable, oldState, newState) -> {
                if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                    JSObject window = (JSObject) engine.executeScript("window");
                    window.setMember("javaApp", new JavaBridge());
                }
            }
        );

        engine.setOnAlert(event -> {
    System.out.println("JAVASCRIPT ALERT: " + event.getData());
});

engine.load(page.toExternalForm());

        Scene scene = new Scene(webView, 1200, 750);

        stage.setTitle("Infrastructure Budget Transparency and Monitoring System");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // JavaScript-to-Java connection
    public static class JavaBridge {

        public String getSystemName() {
            return "Infrastructure Budget Transparency and Monitoring System";
        }

        public String getStatus() {
            return "System is running successfully.";
        }
    }
}
