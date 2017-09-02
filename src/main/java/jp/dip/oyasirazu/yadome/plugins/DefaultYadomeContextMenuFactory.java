package jp.dip.oyasirazu.yadome.plugins;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import jp.dip.oyasirazu.yadome.YadomeTreeCell;
import jp.dip.oyasirazu.yadome.YadomeViewData;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * DefaultYadomeContextMenuFactory
 */
public class DefaultYadomeContextMenuFactory {
    public static ContextMenu createContextMenu(DefaultYadomeTreeCell cell) {
        YadomeViewData data = cell.getItem();
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
                cell.updateTree(cell.getTreeItem().getParent());
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
                cell.updateTree(cell.getTreeItem().getParent());
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
                cell.updateTree(cell.getTreeItem().getParent());
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
                cell.updateTree(cell.getTreeItem().getParent());
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
                cell.updateTree(cell.getTreeItem().getParent());
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
                cell.updateTree(cell.getTreeItem());
            });
            addChild.getItems().add(addChildElement);

            MenuItem addChildAttribute = new MenuItem("Attribute");
            addChildAttribute.setOnAction((ActionEvent e) -> {
                // TODO: ここで作られるテキストノードを TreeItem として登録する
                ((Element)data.getNode()).setAttribute("Attribute", "value");

                // 対象 TreeItem を updateTree に渡す。
                cell.updateTree(cell.getTreeItem());
            });
            addChild.getItems().add(addChildAttribute);

            MenuItem addChildText = new MenuItem("Text");
            addChildText.setOnAction((ActionEvent e) -> {
                Text text = data.getNode().getOwnerDocument().createTextNode("Text");
                data.getNode().appendChild(text);

                // 対象 TreeItem を updateTree に渡す。
                cell.updateTree(cell.getTreeItem());
            });
            addChild.getItems().add(addChildText);

            MenuItem addChildComment = new MenuItem("Comment");
            addChildComment.setOnAction((ActionEvent e) -> {
                Comment comment = data.getNode().getOwnerDocument().createComment("Comment");
                data.getNode().appendChild(comment);

                // 対象 TreeItem を updateTree に渡す。
                cell.updateTree(cell.getTreeItem());
            });
            addChild.getItems().add(addChildComment);

            MenuItem addChildCdata = new MenuItem("CDATA");
            addChildCdata.setOnAction((ActionEvent e) -> {
                CDATASection cdata = data.getNode().getOwnerDocument().createCDATASection("CDATA");
                data.getNode().appendChild(cdata);

                // 対象 TreeItem を updateTree に渡す。
                cell.updateTree(cell.getTreeItem());
            });
            addChild.getItems().add(addChildCdata);

            items.add(addChild);
        }

        return cm;
    }
}
