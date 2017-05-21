package jp.dip.oyasirazu.yadome;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.w3c.dom.Node;

/**
 * YadomeViewData
 */
@AllArgsConstructor
@Data
public class YadomeViewData {
    private Node node;

    @Override
    public String toString() {
        String str;
        if (node.getNodeType() == Node.TEXT_NODE) {
            return "[ " + node.getNodeName() + " : "
                    + node.getTextContent() + " ]";
        } else {
            return "[" + node.getNodeName() + "]";
        }
    }
}

