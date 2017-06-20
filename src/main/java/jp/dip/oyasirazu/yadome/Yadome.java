package jp.dip.oyasirazu.yadome;

import java.io.IOException;
import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import lombok.Getter;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Yadome
 */
public class Yadome {

    @Getter
    private Document document;

    /**
     * Constructor
     *
     * @param xmlFilePath target xml file path.
     */
    public Yadome(Path xmlFilePath)
            throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.parse(xmlFilePath.toFile());
    }

    public void walkTree(NodeVisitor visitor) {
        walkTreeP(document.getDocumentElement(), visitor);
    }

    private NodeVisitResult walkTreeP(
            Node target,
            NodeVisitor visitor) {

        NodeVisitResult visitResult = visitor.visitNode(target);
        if (visitResult == NodeVisitResult.TERMINATE) {
            return NodeVisitResult.TERMINATE;
        } else if (visitResult == NodeVisitResult.SKIP_SUBTREE) {
            return NodeVisitResult.CONTINUE;
        }

        NamedNodeMap attrs = target.getAttributes();
        if (attrs != null) {
            int attrsCount = attrs.getLength();
            for (int i = 0; i < attrsCount; i++) {
                walkTreeP(attrs.item(i), visitor);
            }
        }

        NodeList children = target.getChildNodes();
        int childrenCount = children.getLength();
        for (int i = 0; i < childrenCount; i++) {
            NodeVisitResult childResult = walkTreeP(children.item(i), visitor);
            if (childResult == NodeVisitResult.TERMINATE) {
                return NodeVisitResult.TERMINATE;
            } else if (childResult == NodeVisitResult.SKIP_SIBLINGS) {
                break;
            }
        }

        return NodeVisitResult.CONTINUE;
    }
}

