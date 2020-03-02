import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MounirDev{
    //-----------------------
    // Tu dois terminer ca au max avant SAMEDI 22 Fev 2020 !!!!!!! oops ~

    // tt a etait testé , si il y as un pb c est la faut des arabes :^
    //-----------------------

    public static void fillLngsInCBox(ComboBox c ) // utilise le fichier langues.xml
    {
        String path = "langues.xml"; // path relatif vers le fichier

        c.setVisibleRowCount(5); // nombre d elements visible


        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance() ;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder() ;
            Document doc = builder.parse(path) ;
            NodeList languages = doc.getElementsByTagName("langues") ;
            Node lang = languages.item(0) ;
            NodeList lang_list = lang.getChildNodes() ;   // jusqu ici recuperation de la liste des langues

            for(int i= 0 ;i<lang_list.getLength();i++){ // pour chaque langue l inserer dans la combobox ;
                Node l = lang_list.item(i) ;
                if(l.getNodeType()==Node.ELEMENT_NODE) {
                    Element el = (Element)l ;
                    c.getItems().add(el.getTagName()) ;
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }


    }




    public static void fillLngsInCBox_withImage(ComboBox c){  // affiche en plus du text le drapeua corespandant , bon c est pas glorieu mais ca "marche "

        String path = "langues.xml";  // fichier source
        c.setVisibleRowCount(5); // nb elmnts vissibles
        String path_img = "src/sample/img/flags/" ;  //path vers le dossier contenant les images  ;
        int IMG_H  = 30 ;
        int IMG_W = 35 ; // dim des images a l interieur de la combobox


        TreeMap<String,String> mem = new TreeMap<String,String>();



        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance() ;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder() ;
            Document doc = builder.parse(path) ;
            NodeList languages = doc.getElementsByTagName("langues") ;
            Node lang = languages.item(0) ;
            NodeList lang_list = lang.getChildNodes() ;   // jusqu ici recuperation de la liste des langues

            for(int i= 0 ;i<lang_list.getLength();i++){ // pour chaque langue l inserer dans la combobox ;
                Node l = lang_list.item(i) ;
                if(l.getNodeType()==Node.ELEMENT_NODE) {
                    Element el = (Element)l ;

                    String name = el.getTagName() ;
                    String pict = el.getAttribute("linkImg") ;
                    c.getItems().add(el.getTagName()) ;
                    mem.put(name,pict) ;
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        c.setCellFactory( new Callback<ListView<String>, ListCell<String>>()  // ~

        {
            @Override public ListCell<String> call(ListView<String> param) {
                final ListCell<String> cell = new ListCell<String>() {
                    {
                        super.setPrefWidth(100);
                    }
                    @Override public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item);

                            File input = new File(path_img+mem.get(item));
                            Image img =new Image(input.toURI().toString()) ;
                            ImageView image = new ImageView(img);
                            image.setFitHeight(IMG_H);
                            image.setFitWidth(IMG_W);
                            image.setPreserveRatio(true);
                            setGraphic(image);
                        }
                        else {
                            setText(null);
                            setGraphic(null);
                        }
                    }
                };
                return cell;
            }

        });
    }

    private String getErrorText(String errorType, String language)
    {
        String path = "ERRORS.xml" ; // chemain relatif fichier  (meme struct que le tien )
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance() ;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder() ;
            Document doc = builder.parse(path) ;
            NodeList erreur = doc.getElementsByTagName("ERRORS") ; // retrouver l erreur coresândante , plus qu a toruver la langue
            Node lang = erreur.item(0) ;   // un tag unqiue par type d erreur , sinon il faudra modifier cca
            NodeList lang_list = lang.getChildNodes() ;   // jusqu ici recuperation de la liste des erreurs

            for(int i= 0 ;i<lang_list.getLength();i++){
                Node l = lang_list.item(i) ;
                if(l.getNodeType()==Node.ELEMENT_NODE && l.getNodeName().equals(errorType)) {
                    NodeList lng = l.getChildNodes() ;
                    for(int j=0;j<lng.getLength();j++){
                        Node ll = lng.item(j) ;
                        if(ll.getNodeType()==Node.ELEMENT_NODE && ll.getNodeName().equals(language)) {
                            Element lnge = (Element)ll ;
                            return lnge.getAttribute("msg") ;
                        }
                    }

                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }



        return "" ;
    }

    private Boolean checkEmail(String email) // cara avant et apres le @ , pas de point au debut et a la fin , pas de point successifs

    {
        String mail_format = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        Pattern pattern = Pattern.compile(mail_format);
        Matcher match =  pattern.matcher(email) ;
        return match.matches() ;

    }
    private Boolean checkPhone(String phone) // valide si ne contien que de chiffres , en suposant qque le +(code pays) ser aajoiuter automatiquement

    {
        String phone_num = "^[0-9]*$" ;
        Pattern pattern = Pattern.compile(phone_num);
        Matcher match = pattern.matcher(phone) ;
        return match.matches() ;
    }

}

