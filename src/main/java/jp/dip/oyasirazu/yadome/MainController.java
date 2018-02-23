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

import jp.dip.oyasirazu.yadome.plugins.DefaultYadomeTreeCell;

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
    private Menu view;

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

    public void setAvailablePlugins(Iterable<YadomeCellFactory> yadomeCellFactories) {
        for (YadomeCellFactory plugin : yadomeCellFactories) {
            MenuItem menuItem = new MenuItem(plugin.getClass().getName());

            menuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    treeView.setCellFactory((TreeView<YadomeViewData> p) ->
                            plugin.newInstance(application.getYadome()));
                    if (application.isOpendArxml()) {
                        initializeTreeView();
                    }
                }
            });

            view.getItems().add(menuItem);
        }
    }

    private void initializeTreeView() {
        TreeItem<YadomeViewData> treeViewRoot =
                application.buildTreeItem();

        treeView.setEditable(true);

        treeView.setCellFactory((TreeView<YadomeViewData> p) ->
                new DefaultYadomeTreeCell(application.getYadome()));

        treeView.setRoot(treeViewRoot);
    }

    public void setApplication(Main application) {
        this.application = application;

        if (application.isOpendArxml()) {
            initializeTreeView();
        }
    }
}

