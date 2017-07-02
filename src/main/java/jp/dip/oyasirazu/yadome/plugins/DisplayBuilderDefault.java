package jp.dip.oyasirazu.yadome.plugins;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.util.StringConverter;

import jp.dip.oyasirazu.yadome.DisplayBuilder;
import jp.dip.oyasirazu.yadome.YadomeViewData;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * DisplayBuilderDefault
 */
public class DisplayBuilderDefault implements DisplayBuilder {

    @Override
    public TreeCell<YadomeViewData> getCellFactory() {
        return new YadomeTreeCell();
    }

    public class YadomeTreeCell extends TextFieldTreeCell<YadomeViewData> {
        public YadomeTreeCell() {
            super();

            setConverter(new StringConverter<YadomeViewData>() {
                @Override
                public String toString(YadomeViewData data) {
                    return data.toString();
                }

                @Override
                public YadomeViewData fromString(String string) {
                    Node node = getItem().getNode();

                    switch (node.getNodeType()) {
                        case Node.ELEMENT_NODE:
                        case Node.ATTRIBUTE_NODE:
                            break;
                        default:
                            node.setTextContent(string);
                    }
                    return getItem();
                }
            });
        }

        @Override
        public void updateItem(YadomeViewData data, boolean empty) {
            super.updateItem(data, empty);
            setContextMenu(null);

            if (data == null || empty == true) {
                return;
            }

            updateHeader();

            ContextMenu cm = new ContextMenu();
            List<MenuItem> items = cm.getItems();

            Menu insertBefore = new Menu("前に挿入");

            if (data.getNode().getParentNode() != null
                    && (data.getNode().getParentNode().getNodeType() == Node.ELEMENT_NODE
                    || data.getNode().getParentNode().getNodeType() == Node.TEXT_NODE
                    || data.getNode().getParentNode().getNodeType() == Node.COMMENT_NODE
                    || data.getNode().getParentNode().getNodeType() == Node.CDATA_SECTION_NODE
            )) {
                MenuItem insertBeforeElement = new MenuItem("Element");
                insertBeforeElement.setOnAction((ActionEvent e) -> {
                    Element elem = data.getNode().getOwnerDocument().createElement("element");
                    System.out.println(data.getNode());
                    Node parentNode = data.getNode().getParentNode();
                    if (parentNode == null) {
                        return;
                    }

                    parentNode.insertBefore(elem, data.getNode());
                    YadomeViewData yvd = new YadomeViewData(elem, data.getYadome(), data.getDisplayBuilder());
                    TreeItem<YadomeViewData> ti = new TreeItem<>(yvd);
                    TreeItem<YadomeViewData> tiParent = this.getTreeItem().getParent();

                    int index = tiParent.getChildren().indexOf(this.getTreeItem());
                    tiParent.getChildren().add(index, ti);
                });
                insertBefore.getItems().add(insertBeforeElement);

                MenuItem insertBeforeText = new MenuItem("Text");
                insertBeforeText.setOnAction((ActionEvent e) -> {
                    Text text = data.getNode().getOwnerDocument().createTextNode("Text");
                    System.out.println(data.getNode());
                    Node parentNode = data.getNode().getParentNode();
                    if (parentNode == null) {
                        return;
                    }

                    parentNode.insertBefore(text, data.getNode());
                    YadomeViewData yvd = new YadomeViewData(text, data.getYadome(), data.getDisplayBuilder());
                    TreeItem<YadomeViewData> ti = new TreeItem<>(yvd);
                    TreeItem<YadomeViewData> tiParent = this.getTreeItem().getParent();

                    int index = tiParent.getChildren().indexOf(this.getTreeItem());
                    tiParent.getChildren().add(index, ti);
                });
                insertBefore.getItems().add(insertBeforeText);

                MenuItem insertBeforeComment = new MenuItem("Comment");
                insertBeforeComment.setOnAction((ActionEvent e) -> {
                    Comment comment = data.getNode().getOwnerDocument().createComment("Comment");
                    System.out.println(data.getNode());
                    Node parentNode = data.getNode().getParentNode();
                    if (parentNode == null) {
                        return;
                    }

                    parentNode.insertBefore(comment, data.getNode());
                    YadomeViewData yvd = new YadomeViewData(comment, data.getYadome(), data.getDisplayBuilder());
                    TreeItem<YadomeViewData> ti = new TreeItem<>(yvd);
                    TreeItem<YadomeViewData> tiParent = this.getTreeItem().getParent();

                    int index = tiParent.getChildren().indexOf(this.getTreeItem());
                    tiParent.getChildren().add(index, ti);
                });
                insertBefore.getItems().add(insertBeforeComment);

                MenuItem insertBeforeCdata = new MenuItem("CDATA");
                insertBeforeCdata.setOnAction((ActionEvent e) -> {
                    CDATASection cdata = data.getNode().getOwnerDocument().createCDATASection("CDATA");
                    System.out.println(data.getNode());
                    Node parentNode = data.getNode().getParentNode();
                    if (parentNode == null) {
                        return;
                    }

                    parentNode.insertBefore(cdata, data.getNode());
                    YadomeViewData yvd = new YadomeViewData(cdata, data.getYadome(), data.getDisplayBuilder());
                    TreeItem<YadomeViewData> ti = new TreeItem<>(yvd);
                    TreeItem<YadomeViewData> tiParent = this.getTreeItem().getParent();

                    int index = tiParent.getChildren().indexOf(this.getTreeItem());
                    tiParent.getChildren().add(index, ti);
                });
                insertBefore.getItems().add(insertBeforeCdata);
            }

            if (data.getNode().getNodeType() == Node.ATTRIBUTE_NODE) {
                MenuItem insertBeforeAttr = new MenuItem("Attr");
                insertBeforeAttr.setOnAction((ActionEvent e) -> {
                    System.out.println(data.getNode());
                    Element parentNode = (Element)((Attr)data.getNode()).getOwnerElement();
                    if (parentNode == null) {
                        return;
                    }
                    parentNode.setAttribute("Attribute", "value");

                    YadomeViewData yvd = new YadomeViewData(((Element)data.getNode()).getAttributes().getNamedItem("Attribute"), data.getYadome(), data.getDisplayBuilder());
                    TreeItem<YadomeViewData> ti = new TreeItem<>(yvd);
                    TreeItem<YadomeViewData> tiParent = this.getTreeItem().getParent();

                    int index = tiParent.getChildren().indexOf(this.getTreeItem());
                    tiParent.getChildren().add(index, ti);
                });
                insertBefore.getItems().add(insertBeforeAttr);
            }
            items.add(insertBefore);

            if (data.getNode().getNodeType() == Node.ELEMENT_NODE) {
                Menu addChild = new Menu("子として追加");

                MenuItem addChildElement = new MenuItem("Element");
                addChildElement.setOnAction((ActionEvent e) -> {
                    Element elem = data.getNode().getOwnerDocument().createElement("element");
                    data.getNode().appendChild(elem);

                    YadomeViewData yvd = new YadomeViewData(elem, data.getYadome(), data.getDisplayBuilder());
                    TreeItem<YadomeViewData> ti = new TreeItem<>(yvd);

                    this.getTreeItem().getChildren().add(ti);
                });
                addChild.getItems().add(addChildElement);

                MenuItem addChildAttribute = new MenuItem("Attribute");
                addChildAttribute.setOnAction((ActionEvent e) -> {
                    // TODO: ここで作られるテキストノードを TreeItem として登録する
                    ((Element)data.getNode()).setAttribute("Attribute", "value");

                    YadomeViewData yvd = new YadomeViewData(((Element)data.getNode()).getAttributes().getNamedItem("Attribute"), data.getYadome(), data.getDisplayBuilder());
                    TreeItem<YadomeViewData> ti = new TreeItem<>(yvd);

                    this.getTreeItem().getChildren().add(ti);
                });
                addChild.getItems().add(addChildAttribute);

                MenuItem addChildText = new MenuItem("Text");
                addChildText.setOnAction((ActionEvent e) -> {
                    Text text = data.getNode().getOwnerDocument().createTextNode("Text");
                    data.getNode().appendChild(text);

                    YadomeViewData yvd = new YadomeViewData(text, data.getYadome(), data.getDisplayBuilder());
                    TreeItem<YadomeViewData> ti = new TreeItem<>(yvd);

                    this.getTreeItem().getChildren().add(ti);
                });
                addChild.getItems().add(addChildText);

                MenuItem addChildComment = new MenuItem("Comment");
                addChildComment.setOnAction((ActionEvent e) -> {
                    Comment comment = data.getNode().getOwnerDocument().createComment("Comment");
                    data.getNode().appendChild(comment);

                    YadomeViewData yvd = new YadomeViewData(comment, data.getYadome(), data.getDisplayBuilder());
                    TreeItem<YadomeViewData> ti = new TreeItem<>(yvd);

                    this.getTreeItem().getChildren().add(ti);
                });
                addChild.getItems().add(addChildComment);

                MenuItem addChildCdata = new MenuItem("CDATA");
                addChildCdata.setOnAction((ActionEvent e) -> {
                    CDATASection cdata = data.getNode().getOwnerDocument().createCDATASection("CDATA");
                    data.getNode().appendChild(cdata);

                    YadomeViewData yvd = new YadomeViewData(cdata, data.getYadome(), data.getDisplayBuilder());
                    TreeItem<YadomeViewData> ti = new TreeItem<>(yvd);

                    this.getTreeItem().getChildren().add(ti);
                });
                addChild.getItems().add(addChildCdata);

                items.add(addChild);
            }

            setContextMenu(cm);

        }

        @Override
        public void startEdit() {
            Node node = getItem().getNode();

            switch (node.getNodeType()) {
                case Node.TEXT_NODE:
                case Node.COMMENT_NODE:
                case Node.CDATA_SECTION_NODE:
                    super.startEdit();
                    break;
                default:
                    // do nothing.
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            updateHeader();
        }

        private void updateHeader() {
            YadomeViewData data = getItem();

            javafx.scene.text.Text t;
            Node node = data.getNode();
            switch (node.getNodeType()) {
                case Node.ELEMENT_NODE:
                    t = new javafx.scene.text.Text("Element");
                    break;
                case Node.ATTRIBUTE_NODE:
                    t = new javafx.scene.text.Text("Attr");
                    break;
                default:
                    t = new javafx.scene.text.Text(node.getNodeName());
                    break;
            }
            setGraphic(t);
        }
    }
}

