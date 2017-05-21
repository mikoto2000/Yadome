package jp.dip.oyasirazu.yadome;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * MainController
 */
public class MainController {

    private static final String CHARSET_DEFAULT = "UTF-8";

    private Main application;

    @FXML
    private TreeView<Node> treeView;

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
        Document document = application.getDocument();
        Element documentRoot = document.getDocumentElement();

        TreeItem<Node> treeViewRoot = walkTree(documentRoot);
        treeView.setRoot(treeViewRoot);
    }

    private TreeItem<Node> walkTree(Node target) {
        System.out.println(target);
        TreeItem<Node> targetTreeItem = new TreeItem<>(target);
        ObservableList<TreeItem<Node>> targetTreeItemChildren =
                targetTreeItem.getChildren();

        NodeList children = target.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            targetTreeItemChildren.add(walkTree(children.item(i)));
        }

        targetTreeItem.setExpanded(true);
        return targetTreeItem;
    }

    public void setApplication(Main application) {
        this.application = application;
    }

}
