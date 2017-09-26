package jp.dip.oyasirazu.yadome;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
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

import jp.dip.oyasirazu.yadome.plugins.DefaultNodeVisitor;

import lombok.Data;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
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

    public TreeItem<YadomeViewData> buildTreeItem() {

        DefaultNodeVisitor nodeVisitor = new DefaultNodeVisitor();
        yadome.walkTree(nodeVisitor);

        return nodeVisitor.get(yadome.getDocument().getDocumentElement());
    }

    @Override
    public void start(Stage stage) throws ParserConfigurationException, SAXException, IOException {

        if (options.getTargetFile() != null
                && options.getTargetFile().size() > 0) {
            setTarget(Paths.get(options.getTargetFile().get(0)));
        }

        this.stage = stage;

        loadStatus();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main.fxml"));;
        Pane root = loader.load();

        MainController controller = (MainController) loader.getController();
        controller.setApplication(this);

        // プラグイン一覧取得
        ServiceLoader<YadomeCellFactory> yadomeCellFactories = ServiceLoader.load(
                YadomeCellFactory.class,
                Thread.currentThread().getContextClassLoader());
        controller.setAvailablePlugins(yadomeCellFactories);

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

    public Yadome getYadome() {
        return yadome;
    }

    // オプションオブジェクト
    private static Options options = new Options();

    public static void main(String[] args) {

        // パーサー準備
        CmdLineParser optionParser = new CmdLineParser(options);

        try {
            // パース
            optionParser.parseArgument(args);

        } catch (CmdLineException E) {
            // Useage を表示
            System.out.println("Useage:\n"
                    + "  Main [options] [FILE]\n"
                    + "\n"
                    + "Options:");
            optionParser.printUsage(System.out);
            System.exit(1);
        }

        Application.launch(Main.class, args);
    }

    @Data
    static class Options {
        @Argument
        private List<String> targetFile;
    }

}

