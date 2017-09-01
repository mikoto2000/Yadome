package jp.dip.oyasirazu.yadome.plugins;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;

import jp.dip.oyasirazu.yadome.NodeVisitResult;
import jp.dip.oyasirazu.yadome.NodeVisitor;
import jp.dip.oyasirazu.yadome.Yadome;
import jp.dip.oyasirazu.yadome.YadomeTreeCell;
import jp.dip.oyasirazu.yadome.YadomeViewData;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class DefaultYadomeTreeCell extends YadomeTreeCell {

    private Yadome yadome;

    public DefaultYadomeTreeCell(Yadome yadome) {
        super();

        this.yadome = yadome;

        setConverter(new DefaultStringConverter(this));
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
                Node parentNode = data.getNode().getParentNode();
                if (parentNode == null) {
                    return;
                }
                parentNode.insertBefore(elem, data.getNode());

                // 前に挿入した場合、その親のノードから更新する必要があるので、親を探して updateTree に渡す。
                updateTree(this.getTreeItem().getParent());
            });
            insertBefore.getItems().add(insertBeforeElement);

            MenuItem insertBeforeText = new MenuItem("Text");
            insertBeforeText.setOnAction((ActionEvent e) -> {
                Text text = data.getNode().getOwnerDocument().createTextNode("Text");
                Node parentNode = data.getNode().getParentNode();
                if (parentNode == null) {
                    return;
                }

                parentNode.insertBefore(text, data.getNode());

                // 前に挿入した場合、その親のノードから更新する必要があるので、親を探して updateTree に渡す。
                updateTree(this.getTreeItem().getParent());
            });
            insertBefore.getItems().add(insertBeforeText);

            MenuItem insertBeforeComment = new MenuItem("Comment");
            insertBeforeComment.setOnAction((ActionEvent e) -> {
                Comment comment = data.getNode().getOwnerDocument().createComment("Comment");
                Node parentNode = data.getNode().getParentNode();
                if (parentNode == null) {
                    return;
                }

                parentNode.insertBefore(comment, data.getNode());

                // 前に挿入した場合、その親のノードから更新する必要があるので、親を探して updateTree に渡す。
                updateTree(this.getTreeItem().getParent());
            });
            insertBefore.getItems().add(insertBeforeComment);

            MenuItem insertBeforeCdata = new MenuItem("CDATA");
            insertBeforeCdata.setOnAction((ActionEvent e) -> {
                CDATASection cdata = data.getNode().getOwnerDocument().createCDATASection("CDATA");
                Node parentNode = data.getNode().getParentNode();
                if (parentNode == null) {
                    return;
                }

                parentNode.insertBefore(cdata, data.getNode());

                // 前に挿入した場合、その親のノードから更新する必要があるので、親を探して updateTree に渡す。
                updateTree(this.getTreeItem().getParent());
            });
            insertBefore.getItems().add(insertBeforeCdata);
                   }

        if (data.getNode().getNodeType() == Node.ATTRIBUTE_NODE) {
            MenuItem insertBeforeAttr = new MenuItem("Attr");
            insertBeforeAttr.setOnAction((ActionEvent e) -> {
                Element parentNode = (Element)((Attr)data.getNode()).getOwnerElement();
                if (parentNode == null) {
                    return;
                }
                parentNode.setAttribute("Attribute", "value");

                // 前に挿入した場合、その親のノードから更新する必要があるので、親を探して updateTree に渡す。
                updateTree(this.getTreeItem().getParent());
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

                // 対象 TreeItem を updateTree に渡す。
                updateTree(this.getTreeItem());
            });
            addChild.getItems().add(addChildElement);

            MenuItem addChildAttribute = new MenuItem("Attribute");
            addChildAttribute.setOnAction((ActionEvent e) -> {
                // TODO: ここで作られるテキストノードを TreeItem として登録する
                ((Element)data.getNode()).setAttribute("Attribute", "value");

                // 対象 TreeItem を updateTree に渡す。
                updateTree(this.getTreeItem());
            });
            addChild.getItems().add(addChildAttribute);

            MenuItem addChildText = new MenuItem("Text");
            addChildText.setOnAction((ActionEvent e) -> {
                Text text = data.getNode().getOwnerDocument().createTextNode("Text");
                data.getNode().appendChild(text);

                // 対象 TreeItem を updateTree に渡す。
                updateTree(this.getTreeItem());
            });
            addChild.getItems().add(addChildText);

            MenuItem addChildComment = new MenuItem("Comment");
            addChildComment.setOnAction((ActionEvent e) -> {
                Comment comment = data.getNode().getOwnerDocument().createComment("Comment");
                data.getNode().appendChild(comment);

                // 対象 TreeItem を updateTree に渡す。
                updateTree(this.getTreeItem());
            });
            addChild.getItems().add(addChildComment);

            MenuItem addChildCdata = new MenuItem("CDATA");
            addChildCdata.setOnAction((ActionEvent e) -> {
                CDATASection cdata = data.getNode().getOwnerDocument().createCDATASection("CDATA");
                data.getNode().appendChild(cdata);

                // 対象 TreeItem を updateTree に渡す。
                updateTree(this.getTreeItem());
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
            case Node.ELEMENT_NODE:
            case Node.ATTRIBUTE_NODE:
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
    public void commitEdit(YadomeViewData data) {
        super.commitEdit(data);
        if (this.getTreeItem().getParent() == null) {
            updateTree(this.getTreeItem());
        } else {
            updateTree(this.getTreeItem().getParent());
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

    @Override
    public boolean isExclude(Node node) {
        return false;
    }

    /**
     * 対象の TreeItem 以下を更新する。
     *
     * 引数で渡された TreeItem 以下のノードを再走査し、 TreeItem の作り直す。
     * この関数実行後に、 TreeItem インスタンスが新しいものになっているのに注意。
     * (同一内容の別インスタンスになっている)
     *
     * @param treeItem 更新対象の TreeItem
     */
    protected void updateTree(TreeItem<YadomeViewData> treeItem) {
        YadomeViewData data = treeItem.getValue();

        // ノードから TreeItem への紐づけを記憶する Map を作成
        Map<Node, TreeItem<YadomeViewData>> map = new HashMap<>();
        yadome.walkTree(data.getNode(), new NodeVisitor() {

            // 空白文字しかないテキストノードは無視する。
            // exclude 指定されているノードは無視する。
            @Override
            public NodeVisitResult visitNode(Node node) {
                if ((node.getNodeType() == Node.TEXT_NODE
                            && node.getTextContent().trim().isEmpty())
                        || isExclude(node)) {
                    return NodeVisitResult.CONTINUE;
                        }

                TreeItem<YadomeViewData> target =
                    new TreeItem<YadomeViewData>(new YadomeViewData(node));
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

        // 今回作った TreeItem
        TreeItem<YadomeViewData> newTreeItem = map.get(data.getNode());

        // 今ぶら下がっている TreeItem
        TreeItem<YadomeViewData> oldTreeItem = treeItem;

        // 作った TreeItm の入れ替え
        oldTreeItem.getChildren().clear();

        // 今ぶら下がっている TreeItem の子要素を、今回作った TreeItem の子要素に入れ替える
        for (TreeItem<YadomeViewData> item : newTreeItem.getChildren()) {
            oldTreeItem.getChildren().add(item);
        }
    }
}

