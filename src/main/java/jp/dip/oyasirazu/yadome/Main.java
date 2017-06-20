package jp.dip.oyasirazu.yadome;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.prefs.Preferences;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Main
 */
public class Main extends Application {

    private static final String KEY_STAGE_X = "stageX";
    private static final String KEY_STAGE_Y = "stageY";
    private static final String KEY_STAGE_WIDTH = "stageWidth";
    private static final String KEY_STAGE_HEIGHT = "stageHeight";

    private Preferences prefs = Preferences.userNodeForPackage(this.getClass());

    private Yadome yadome;

    private Stage stage;

    public boolean isOpendArxml() {
        if (yadome != null) {
            return true;
        }

        return false;
    }

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

    public TreeItem<YadomeViewData> buildTreeItem(DisplayBuilder displayBuilder) {

        // ノードから TreeItem への紐づけを記憶する Map を作成
        Map<Node, TreeItem<YadomeViewData>> map = new HashMap<>();

        yadome.walkTree(new NodeVisitor() {

            // 空白文字しかないテキストノードは無視する。
            // displayBuilder で exclude 指定されているノードは無視する。
            @Override
            public NodeVisitResult visitNode(Node node) {
                if ((node.getNodeType() == Node.TEXT_NODE
                        && node.getTextContent().trim().isEmpty())
                        || displayBuilder.isExclude(node)) {
                    return NodeVisitResult.CONTINUE;
                }

                TreeItem<YadomeViewData> target =
                        new TreeItem<YadomeViewData>(new YadomeViewData(node, displayBuilder));
                target.setExpanded(true);
                map.put(node, target);

                if (node.getNodeType() != Node.ATTRIBUTE_NODE) {
                    TreeItem<YadomeViewData> parent =
                            map.get(node.getParentNode());
                    if (parent != null) {
                        parent.getChildren().add(target);
                    }
                } else {
                    Attr attr = (Attr)node;
                    TreeItem<YadomeViewData> owner =
                            map.get(attr.getOwnerElement());
                    if (owner != null) {
                        owner.getChildren().add(target);
                    }
                }

                return NodeVisitResult.CONTINUE;
            }
        });

        return map.get(yadome.getDocument().getDocumentElement());
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;

        loadStatus();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main.fxml"));;
        Pane root = loader.load();

        MainController controller = (MainController) loader.getController();
        controller.setApplication(this);

        // プラグイン一覧取得
        ServiceLoader<DisplayBuilder> displayBuilders = ServiceLoader.load(
                DisplayBuilder.class,
                Thread.currentThread().getContextClassLoader());
        controller.setAvailablePlugins(displayBuilders);

        stage.setScene(new Scene(root));
        stage.setTitle("Yadome");

        stage.setOnCloseRequest(event -> saveStatus());

        stage.show();
    }

    private void saveStatus() {
        prefs.putInt(KEY_STAGE_X, (int) stage.getX());
        prefs.putInt(KEY_STAGE_Y, (int) stage.getY());
        prefs.putInt(KEY_STAGE_WIDTH, (int) stage.getWidth());
        prefs.putInt(KEY_STAGE_HEIGHT, (int) stage.getHeight());
    }

    private void loadStatus() {
        int x = prefs.getInt(KEY_STAGE_X, -1);
        int y = prefs.getInt(KEY_STAGE_Y, -1);
        int width = prefs.getInt(KEY_STAGE_WIDTH, -1);
        int height = prefs.getInt(KEY_STAGE_HEIGHT, -1);

        if (x >= 0) {
            stage.setX(x);
        }

        if (y >= 0) {
            stage.setY(y);
        }

        if (width >= 0) {
            stage.setWidth(width);
        }

        if (height >= 0) {
            stage.setHeight(height);
        }
    }
}

