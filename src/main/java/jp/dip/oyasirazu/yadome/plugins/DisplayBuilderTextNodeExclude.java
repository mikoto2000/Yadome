package jp.dip.oyasirazu.yadome.plugins;

import jp.dip.oyasirazu.yadome.DisplayBuilder;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * DisplayBuilderTextNodeExclude
 */
public class DisplayBuilderTextNodeExclude implements DisplayBuilder {
    @Override
    public String buildString(Node node) {
        StringBuilder sb = new StringBuilder();

        sb.append(DisplayBuilder.super.buildString(node));

        // 直下のテキストノード収集
        NodeList nodes = node.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeType() == Node.TEXT_NODE) {
                sb.append(" ");

                // TODO: replace をどうにかしたい
                sb.append(nodes.item(i).getTextContent().replace(" ", "").replace("　", "").replace("\t", "").replace("\r", "").replace("\n", ""));
            }
        }

        return sb.toString();
    }

    @Override
    public boolean isExclude(Node node) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            return true;
        }

        return false;
    }

}

