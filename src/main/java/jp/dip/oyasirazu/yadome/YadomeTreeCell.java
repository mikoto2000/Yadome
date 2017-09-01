package jp.dip.oyasirazu.yadome;

import javafx.scene.control.cell.TextFieldTreeCell;

import jp.dip.oyasirazu.yadome.YadomeViewData;

import org.w3c.dom.Node;

public abstract class YadomeTreeCell extends TextFieldTreeCell<YadomeViewData> {
    public abstract boolean isExclude(Node node);
}

