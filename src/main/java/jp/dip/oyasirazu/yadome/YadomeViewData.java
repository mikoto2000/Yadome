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
        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE:
            case Node.ATTRIBUTE_NODE:
                return node.getNodeName();
            default:
                return node.getTextContent();
        }
    }
}

