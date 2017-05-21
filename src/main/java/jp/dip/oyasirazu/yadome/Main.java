package jp.dip.oyasirazu.yadome;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Main
 */
public class Main extends Application {

    private Yadome yadome;

    private Stage stage;

    public Optional<Path> openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("File Chooser Dialog");
        File choosedFile = fileChooser.showOpenDialog(stage);

        if (choosedFile == null) {
            return Optional.empty();
        } else {
            return Optional.of(choosedFile.toPath());
        }
    }

    public void setTarget(Path xmlFilePath)
            throws SAXException, IOException, ParserConfigurationException {
        yadome = new Yadome(xmlFilePath);
    }

    public Document getDocument() {
        return yadome.getDocument();
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main.fxml"));;
        Pane root = loader.load();

        MainController controller = (MainController) loader.getController();
        controller.setApplication(this);

        stage.setScene(new Scene(root));
        stage.setTitle("Yadome");
        stage.show();
    }
}

