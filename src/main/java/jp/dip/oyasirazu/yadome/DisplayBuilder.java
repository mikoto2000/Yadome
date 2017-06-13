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

                // Node の情報から、 `<tagName attributeName="value">` の文字列を組み立てる。
                StringBuilder sb = new StringBuilder();
                {
                    sb.append("<" + node.getNodeName());

                    NamedNodeMap attrs = node.getAttributes();
                    int loopLength = attrs.getLength();
                    for (int i = 0; i < loopLength; i++) {
                        Attr attr = (Attr) attrs.item(i);
                        sb.append(" " + attr.getName() + "=\"" + attr.getValue() + "\"");
                    }
                    sb.append(">");
                }

                return sb.toString();
            case Node.COMMENT_NODE:
                return "<!--" + node.getTextContent() + "-->";
            case Node.CDATA_SECTION_NODE:
                return "<![CDATA[" + node.getTextContent() + "]]>";
            default:
                return "[ " + node.getNodeName() + " : "
                        + node.getTextContent() + " ]";
        }
    }

    default public boolean isExclude(Node node) {
        return false;
    }

    default public TreeCell<YadomeViewData> getCellFactory() {
        return new TextFieldTreeCell<YadomeViewData>();
    }
}
