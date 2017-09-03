package jp.dip.oyasirazu.yadome;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.TextFieldTreeCell;

import jp.dip.oyasirazu.yadome.YadomeViewData;

import org.w3c.dom.Node;

public abstract class YadomeTreeCell extends TextFieldTreeCell<YadomeViewData> {

    @Override
    public void updateItem(YadomeViewData data, boolean empty) {
        super.updateItem(data, empty);
        setContextMenu(null);

        if (data == null || empty == true) {
            return;
        }

        setGraphic(createGraphic());

        ContextMenu cm = new ContextMenu();
        cm.getItems().addAll(
            createExternalContextMenu().getItems()
        );

        setContextMenu(cm);
    }

    @Override
    public void startEdit() {
        Node node = getItem().getNode();

        if (isEditable(node)) {
            startEditCallback();
            super.startEdit();
        }
    }

    protected abstract boolean isEditable(Node node);
    protected abstract void startEditCallback();

    @Override
    public void commitEdit(YadomeViewData data) {
        super.commitEdit(data);

        commitEditCallback();
    }

    protected abstract void commitEditCallback();

    @Override
    public void cancelEdit() {
        cancelEditCallback();
    }

    protected abstract void cancelEditCallback();

    protected abstract javafx.scene.Node createGraphic();
    protected abstract ContextMenu createExternalContextMenu();
    public abstract boolean isExclude(Node node);
}

