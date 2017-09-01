package jp.dip.oyasirazu.yadome.plugins;

import java.util.ArrayList;
import java.util.List;

import javafx.util.StringConverter;

import jp.dip.oyasirazu.yadome.YadomeTreeCell;
import jp.dip.oyasirazu.yadome.YadomeViewData;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DefaultStringConverter extends StringConverter<YadomeViewData> {

    private YadomeTreeCell cell;

    public DefaultStringConverter(YadomeTreeCell cell) {
        this.cell = cell;
    }

    @Override
    public String toString(YadomeViewData data) {
        return data.toString();
    }

    @Override
    public YadomeViewData fromString(String string) {
        Node node = cell.getItem().getNode();

        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE:
                Element elem = (Element)node;
                if (!elem.getTagName().equals(string)) {
                    Document document = elem.getOwnerDocument();
                    Element newElement = document.createElement(string);

                    // 属性の入れ替え
                    NamedNodeMap nnm = elem.getAttributes();
                    List<Attr> attrs = new ArrayList<>(nnm.getLength());
                    for (int i = 0; i < nnm.getLength(); i++) {
                        attrs.add((Attr)nnm.item(i));
                    }

                    for (Attr attr : attrs) {
                        elem.removeAttribute(attr.getName());
                        newElement.setAttribute(attr.getName(), attr.getValue());
                    }

                    // 子要素の入れ替え
                    NodeList nodes = elem.getChildNodes();
                    List<Node> childs = new ArrayList<>(nodes.getLength());
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Node childNode = nodes.item(i);
                        childs.add(childNode);
                    }

                    for (Node childNode : childs) {
                        elem.removeChild(childNode);
                        newElement.appendChild(childNode);
                    }

                    Node parent = elem.getParentNode();
                    if (elem.getParentNode() != elem.getOwnerDocument()) {
                        parent.insertBefore(newElement, elem);
                        parent.removeChild(elem);
                    } else {
                        // ルート要素だった場合、
                        // YadomeViewData のノード入れ替えまで
                        // 行わないとうまく更新されないのでその対応
                        parent.removeChild(elem);
                        parent.appendChild(newElement);
                        cell.getItem().setNode(newElement);
                    }
                }
                break;
            case Node.ATTRIBUTE_NODE:
                Attr attr = (Attr)node;
                if (!attr.getName().equals(string)) {
                    String value = attr.getValue();
                    Element parent = attr.getOwnerElement();
                    parent.setAttribute(string, value);
                    parent.removeAttribute(attr.getName());
                }
                break;
            default:
                node.setTextContent(string);
        }
        return cell.getItem();
    }
}

