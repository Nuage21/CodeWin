import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LanguageManager {

    // loaders (for lang sync)
    public static Document langXmler;

    public static FXMLLoader authenticatorLoader;
    public static FXMLLoader loggedinLoader;

    public static ArrayList<String> installed_languages = new ArrayList<>();
    public static ArrayList<String> installed_languages_files = new ArrayList<>();

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
                        try{
                            Label t = (Label) loader.getNamespace().get(nodeID);
                            t.setText(nodeContent);
                        }
                        catch (Exception e)
                        {
                            Debug.debugDialog("LManager", nodeID);
                            Debug.debugException(e);
                        }

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

    public static String getContentOf(String id)
    {
        Element docEle = langXmler.getDocumentElement();
        Element nl = (Element) docEle.getElementsByTagName(id).item(0);
        return nl.getTextContent();
    }

    public static void loadInstalledLanguages()
    {
        String filePath = Settings.projectPath + "\\lang\\installed_langs.json";
        Scanner scanner = null;
        try {
            scanner = new Scanner( new File(filePath), "UTF-8" );
        } catch (FileNotFoundException e) {
            Debug.debugException(e);
        }
        String jsonInner = scanner.useDelimiter("\\A").next();
        scanner.close(); // Put this call in a finally block

        JSONObject obj = new JSONObject(jsonInner);

        JSONArray langs = obj.getJSONArray("langs");
        JSONArray files = obj.getJSONArray("files");

        for(int i = 0; i < langs.length(); ++i)
        {
            installed_languages.add(langs.getString(i));
            installed_languages_files.add(files.getString(i));
        }
    }
}
