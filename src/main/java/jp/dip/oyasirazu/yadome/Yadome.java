package jp.dip.oyasirazu.yadome;

import java.io.IOException;
import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import lombok.Getter;

import org.w3c.dom.Document;
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
}
