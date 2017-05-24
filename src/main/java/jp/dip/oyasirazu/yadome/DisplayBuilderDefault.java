package jp.dip.oyasirazu.yadome;

import java.lang.StringBuilder;

import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * DisplayBuilderDefault
 */
public class DisplayBuilderDefault implements DisplayBuilder {

    private static DisplayBuilderDefault instance;

    private DisplayBuilderDefault() {}

    public static DisplayBuilderDefault newInstance() {
        if (instance == null) {
            instance = new DisplayBuilderDefault();
        }
        return instance;
    }

    public String buildString(Node node) {
        String str;
        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE:
                return buildAttributeString(node);
            case Node.COMMENT_NODE:
                return "<!--" + node.getTextContent() + "-->";
            case Node.CDATA_SECTION_NODE:
                return "<![CDATA[" + node.getTextContent() + "]]>";
            default:
                return "[ " + node.getNodeName() + " : "
                        + node.getTextContent() + " ]";
        }
    }

    /**
     * Node の情報から、 `<tagName attributeName="value">` の文字列を組み立てる。
     */
    private static String buildAttributeString(Node node) {
        StringBuilder sb = new StringBuilder();
        sb.append("<" + node.getNodeName());

        NamedNodeMap attrs = node.getAttributes();
        int loopLength = attrs.getLength();
        for (int i = 0; i < loopLength; i++) {
            Attr attr = (Attr) attrs.item(i);
            sb.append(" " + attr.getName() + "=\"" + attr.getValue() + "\"");
        }
        sb.append(">");

        return sb.toString();
    }
}
