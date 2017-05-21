package jp.dip.oyasirazu.yadome;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * MainController
 */
public class MainController {

    private static final String CHARSET_DEFAULT = "UTF-8";

    private Stage stage;

    @FXML
    private Label label;

    @FXML
    private void onFileOpen() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("File Chooser Dialog");
        File choosedFile = fileChooser.showOpenDialog(stage);

        if (choosedFile == null) {
            return;
        }

        // TODO: ファイル読み込み失敗時のリカバリ
        String content = new String(
                Files.readAllBytes(choosedFile.toPath()),
                Charset.forName(CHARSET_DEFAULT));

        label.setText(content);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
