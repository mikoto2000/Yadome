package jp.dip.oyasirazu.yadome.plugins;

import jp.dip.oyasirazu.yadome.DisplayBuilder;

import org.w3c.dom.Node;

/**
 * DisplayBuilderDefault
 *
 * 実験用なので後で削除する。
 * その際、"META-INF/services/jp.dip.oyasirazu.yadome.DisplayBuilder" の該当行も忘れずに消すこと。
 */
public class DisplayBuilderSimple implements DisplayBuilder {

    @Override
    public String buildString(Node node) {
        return node.getNodeName();
    }
}

