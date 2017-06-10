package jp.dip.oyasirazu.yadome;

import java.io.IOException;
import java.lang.Thread;
import java.nio.file.Path;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import jp.dip.oyasirazu.yadome.plugins.DisplayBuilderDefault;


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
    private Menu view;

    // ツリー描画時に使われる DisplayBuilder
    private DisplayBuilder usingDisplayBuilder = new DisplayBuilderDefault();

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

    public void setAvailablePlugins(Iterable<DisplayBuilder> displayBuilders) {
        for (DisplayBuilder plugin : displayBuilders) {
            MenuItem menuItem = new MenuItem(plugin.getClass().getName());

            menuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    usingDisplayBuilder = plugin;
                    initializeTreeView();
                }
            });

            view.getItems().add(menuItem);
        }
    }

    private void initializeTreeView() {
        TreeItem<YadomeViewData> treeViewRoot =
                application.buildTreeItem(usingDisplayBuilder);
        treeView.setRoot(treeViewRoot);
    }

    public void setApplication(Main application) {
        this.application = application;
    }

}
