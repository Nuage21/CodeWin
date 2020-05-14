import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class LanguageManager {

    // loaders (for lang sync)
    public static Document langXmler;
    public static FXMLLoader authenticatorLoader;

    public static void loadLangData(String xmlfile)
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            langXmler = documentBuilder.parse(xmlfile);
        } catch (Exception e) {
            Debug.debugException(e);
        }
    }

    public static void resyncLanguage(FXMLLoader loader, String section){
        try {
            Element docEle = langXmler.getDocumentElement();
            NodeList nl = docEle.getElementsByTagName(section).item(0).getChildNodes();
            int length = nl.getLength();
            for (int i = 0; i < length; i++) {
                if (nl.item(i).getNodeType() == Element.ELEMENT_NODE) {
                    Element el = (Element) nl.item(i);
                    String nodeName = el.getNodeName();
                    String nodeID = el.getAttribute("id");
                    String nodeContent = el.getTextContent();
                    if (nodeName.equals("TextField") || nodeName.equals("PasswordField"))
                    {
                        TextField t = (TextField) loader.getNamespace().get(nodeID);
                        t.setPromptText(nodeContent);
                    }
                    else if (nodeName.equals("Label"))
                    {
                        Label t = (Label) loader.getNamespace().get(nodeID);
                        t.setText(nodeContent);
                    }
                    else if (nodeName.equals("Button"))
                    {
                        Button t = (Button) loader.getNamespace().get(nodeID);
                        t.setText(nodeContent);
                    }
                    else if (nodeName.equals("Tab"))
                    {
                        Tab t = (Tab) loader.getNamespace().get(nodeID);
                        t.setText(nodeContent);
                    }
                    else if (nodeName.equals("HyperLink"))
                    {
                        Hyperlink t = (Hyperlink) loader.getNamespace().get(nodeID);
                        t.setText(nodeContent);
                    }
                }
            }
        } catch (Exception e) {
            Debug.debugException(e);
        }
    }
}
