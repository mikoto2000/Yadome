package jp.dip.oyasirazu.yadome;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Main
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main.fxml"));;
        Pane root = loader.load();

        MainController controller = (MainController) loader.getController();
        controller.setStage(stage);

        stage.setScene(new Scene(root));
        stage.setTitle("Yadome");
        stage.show();
    }
}

