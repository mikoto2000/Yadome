package jp.dip.oyasirazu.yadome.plugins;

import javafx.scene.control.TreeCell;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import jp.dip.oyasirazu.yadome.DisplayBuilder;
import jp.dip.oyasirazu.yadome.YadomeViewData;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * DisplayBuilderDefault
 */
public class DisplayBuilderDefault implements DisplayBuilder {

    @Override
    public TreeCell<YadomeViewData> getCellFactory() {
        return new YadomeTreeCell();
    }

    public class YadomeTreeCell extends TextFieldTreeCell<YadomeViewData> {
        public YadomeTreeCell() {
            super();

            setConverter(new StringConverter<YadomeViewData>() {
                @Override
                public String toString(YadomeViewData data) {
                    return data.toString();
                }

                @Override
                public YadomeViewData fromString(String string) {
                    Node node = getItem().getNode();

                    switch (node.getNodeType()) {
                        case Node.ELEMENT_NODE:
                        case Node.ATTRIBUTE_NODE:
                            break;
                        default:
                            node.setTextContent(string);
                    }
                    return getItem();
                }
            });
        }

        @Override
        public void updateItem(YadomeViewData data, boolean empty) {
            super.updateItem(data, empty);

            if (data == null || empty == true) {
                return;
            }

            updateHeader();
        }

        @Override
        public void startEdit() {
            Node node = getItem().getNode();

            switch (node.getNodeType()) {
                case Node.TEXT_NODE:
                case Node.COMMENT_NODE:
                case Node.CDATA_SECTION_NODE:
                    super.startEdit();
                    break;
                default:
                    // do nothing.
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            updateHeader();
        }

        private void updateHeader() {
            YadomeViewData data = getItem();

            Text t;
            Node node = data.getNode();
            switch (node.getNodeType()) {
                case Node.ELEMENT_NODE:
                    t = new Text("Element");
                    break;
                case Node.ATTRIBUTE_NODE:
                    t = new Text("Attr");
                    break;
                default:
                    t = new Text(node.getNodeName());
                    break;
            }
            setGraphic(t);
        }
    }
}

