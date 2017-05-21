package jp.dip.oyasirazu.yadome;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * MainController
 */
public class MainController {

    private static final String CHARSET_DEFAULT = "UTF-8";

    private Main application;

    @FXML
    private TreeView<YadomeViewData> treeView;

    @FXML
    private Label label;

    @FXML
    private void onFileOpen()
            throws SAXException, IOException, ParserConfigurationException {

        Optional<Path> choosedFilePathOpt = application.openFileChooser();

        if (!choosedFilePathOpt.isPresent()) {
            return;
        }

        Path choosedFilePath = choosedFilePathOpt.get();
        application.setTarget(choosedFilePath);

        initializeTreeView();

        //// TODO: ファイル読み込み失敗時のリカバリ
        //String content = new String(
        //        Files.readAllBytes(choosedFilePath),
        //        Charset.forName(CHARSET_DEFAULT));

        //label.setText(content);
    }

    private void initializeTreeView() {
        TreeItem<YadomeViewData> treeViewRoot =
                application.buildTreeItem();
        treeView.setRoot(treeViewRoot);
    }

    public void setApplication(Main application) {
        this.application = application;
    }

}
