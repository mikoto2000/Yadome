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
    private DisplayBuilder displayBuilder;

    public YadomeViewData(Node node) {
        this(node, new DisplayBuilder(){});
    }

    @Override
    public String toString() {
        return displayBuilder.buildString(node);
    }
}

