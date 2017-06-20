package jp.dip.oyasirazu.yadome;

import java.lang.StringBuilder;

import javafx.scene.control.TreeCell;
import javafx.scene.control.cell.TextFieldTreeCell;

import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * DisplayBuilder
 */
public interface DisplayBuilder {

    // TODO: getCellFactory 作ったらこっちいらなくならない？？？
    //       テキスト表示しかしない場合に CellFactory 用意するのはしんどいから
    //       簡単化のために残しておく？？？
    default public String buildString(Node node) {
        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE:
            case Node.ATTRIBUTE_NODE:
                return node.getNodeName();
            default:
                return node.getTextContent();
        }
    }

    default public boolean isExclude(Node node) {
        return false;
    }

    default public TreeCell<YadomeViewData> getCellFactory() {
        return new TextFieldTreeCell<YadomeViewData>();
    }
}
