package jp.dip.oyasirazu.yadome;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
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

    public TreeItem<YadomeViewData> buildTreeItem() {

        // ノードから TreeItem への紐づけを記憶する Map を作成
        Map<Node, TreeItem<YadomeViewData>> map = new HashMap<>();
        DisplayBuilder displayBuilder = new DisplayBuilder(){};

        yadome.walkTree(new NodeVisitor() {
            @Override
            public NodeVisitResult visitNode(Node node) {
                if (node.getNodeType() == Node.TEXT_NODE
                        && node.getTextContent().trim().isEmpty()) {
                    return NodeVisitResult.CONTINUE;
                }

                TreeItem<YadomeViewData> target =
                        new TreeItem<YadomeViewData>(new YadomeViewData(node, displayBuilder));
                target.setExpanded(true);
                map.put(node, target);

                TreeItem<YadomeViewData> parent =
                        map.get(node.getParentNode());
                if (parent != null) {
                    parent.getChildren().add(target);
                }

                return NodeVisitResult.CONTINUE;
            }
        });

        return map.get(yadome.getDocument().getDocumentElement());
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

