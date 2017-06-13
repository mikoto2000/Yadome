package jp.dip.oyasirazu.yadome.plugins;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import jp.dip.oyasirazu.yadome.DisplayBuilder;
import jp.dip.oyasirazu.yadome.YadomeViewData;

import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * DisplayBuilderUsingCustomCellFactory
 */
public class DisplayBuilderUsingCustomCellFactory implements DisplayBuilder {
    @Override
    public String buildString(Node node) {
        StringBuilder sb = new StringBuilder();
        sb.append(DisplayBuilder.super.buildString(node));

        // 直下のテキストノード収集
        NodeList nodes = node.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeType() == Node.TEXT_NODE) {
                sb.append(" ");

                // TODO: replace をどうにかしたい
                sb.append(nodes.item(i).getTextContent().replace(" ", "").replace("　", "").replace("\t", "").replace("\r", "").replace("\n", ""));
            }
        }

        return sb.toString();
    }

    public String buildStringWithoutAttr(Node node) {
        StringBuilder sb = new StringBuilder();

        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE:
                sb.append("<" + node.getNodeName() + ">");
                // 直下のテキストノード収集
                NodeList nodes = node.getChildNodes();
                for (int i = 0; i < nodes.getLength(); i++) {
                    if (nodes.item(i).getNodeType() == Node.TEXT_NODE) {
                        sb.append(" ");

                        // TODO: replace をどうにかしたい
                        sb.append(nodes.item(i).getTextContent().replace(" ", "").replace("　", "").replace("\t", "").replace("\r", "").replace("\n", ""));
                    }
                }
                break;
            case Node.COMMENT_NODE:
                sb.append("<!--" + node.getTextContent() + "-->");
                break;
            case Node.CDATA_SECTION_NODE:
                sb.append("<![CDATA[" + node.getTextContent() + "]]>");
                break;
            default:
                sb.append("[ " + node.getNodeName() + " : "
                        + node.getTextContent() + " ]");
        }


        return sb.toString();
    }

    @Override
    public boolean isExclude(Node node) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            return true;
        }

        return false;
    }

    @Override
    public TreeCell<YadomeViewData> getCellFactory() {
        return new MyTreeViewCell();
    }

    /**
     * MyTreeViewCell
     */
    class MyTreeViewCell extends TreeCell<YadomeViewData> {

        private boolean isTableVisible = false;

        @Override
        public void updateItem(YadomeViewData item, boolean isEmpty) {
            super.updateItem(item, isEmpty);
            if (isEmpty || item == null) {
                Pane p = new Pane();
                setGraphic(p);
                return;
            }

            this.setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent e) {
                    if (!isEmpty()) {
                        isTableVisible = !isTableVisible;
                        updateItem(item, isEmpty);
                    }
                }
            });

            try {
                Pane root;
                Node node = item.getNode();
                NamedNodeMap attrs = node.getAttributes();
                int attrCount;
                if (attrs == null) {
                    attrCount = 0;
                } else {
                    attrCount = attrs.getLength();
                }

                if (attrCount == 0 || !isSelected() || !isTableVisible) {
                    root = new Pane();
                    Label label = new Label(buildString(node));
                    root.getChildren().add(label);
                } else {
                    root = new VBox();
                    Label label = new Label(buildStringWithoutAttr(node));
                    root.getChildren().add(label);

                    TableView<TableData> tableView = new TableView<>();

                    // 列の定義
                    TableColumn<TableData, String> attributeNameCol =
                            new TableColumn<>("name");
                    attributeNameCol.setCellValueFactory(
                            new PropertyValueFactory<>("attributeName"));
                    TableColumn<TableData, String> attributeValueCol =
                            new TableColumn<>("value");
                    attributeValueCol.setCellValueFactory(
                            new PropertyValueFactory<>("attributeValue"));
                    ObservableList<TableColumn<TableData, ?>> columns =
                            tableView.getColumns();
                    columns.add(attributeNameCol);
                    columns.add(attributeValueCol);

                    // テーブルレコードの作成
                    ObservableList<TableData> tableDatas =
                            FXCollections.observableArrayList();
                    for (int i = 0; i < attrCount; i++) {
                        Attr attr = (Attr) attrs.item(i);
                        tableDatas.add(new TableData(attr.getName(), attr.getValue()));
                    }
                    tableView.setItems(tableDatas);

                    // TODO: 高さの定数をなんとか
                    tableView.setFixedCellSize(25);
                    tableView.prefHeightProperty().bind(Bindings.size(tableView.getItems()).multiply(tableView.getFixedCellSize()).add(25));

                    root.getChildren().add(tableView);
                }
                setGraphic(root);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("erroe!");
            }

            return;
        }

        /** テーブルレコードにするクラス */
        public class TableData {
            private String attributeName;
            private String attributeValue;

            public TableData(String attributeName, String attributeValue) {
                this.attributeName = attributeName;
                this.attributeValue = attributeValue;
            }

            public String getAttributeName() {
                return this.attributeName;
            }

            public String getAttributeValue() {
                return this.attributeValue;
            }
        }
    }
}


