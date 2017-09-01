package jp.dip.oyasirazu.yadome.plugins;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.TreeItem;

import jp.dip.oyasirazu.yadome.NodeVisitResult;
import jp.dip.oyasirazu.yadome.NodeVisitor;
import jp.dip.oyasirazu.yadome.YadomeViewData;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;

/**
 * DefaultNodeVisitor
 *
 * TODO: コールバック関数を用意する
 *       visited(TreeItem<YadomeViewData> parent, TreeItem<YadomeViewData> current) みたいな感じのやつ。
 */
public class DefaultNodeVisitor implements NodeVisitor {

    public DefaultNodeVisitor() {
    }

    private Map<Node, TreeItem<YadomeViewData>> map = new HashMap<>();

    // 空白文字しかないテキストノードは無視する。
    // exclude 指定されているノードは無視する。
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

    public boolean isExclude(Node node) {
        return false;
    }

    public TreeItem<YadomeViewData> get(Node node) {
        return map.get(node);
    }
}

