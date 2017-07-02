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
    private Yadome yadome;
    private DisplayBuilder displayBuilder;

    @Override
    public String toString() {
        return displayBuilder.buildString(node);
    }
}

