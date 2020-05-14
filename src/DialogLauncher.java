import DialogBoxes.ErrorBox_Controller;
import DialogBoxes.SuccessBox_Controller;
import org.w3c.dom.Element;

public class DialogLauncher {

    public static final int SUCCESS_BOX = 0x00;
    public static final int ERROR_BOX = 0x01;

    public static void launchDialog(String id, int _type)
    {
        Element docEle = LanguageManager.langXmler.getDocumentElement();
        Element nl = (Element) docEle.getElementsByTagName(id).item(0);
        String title = nl.getAttribute("title");
        String core = nl.getTextContent();
        if(_type == SUCCESS_BOX)
            SuccessBox_Controller.showSuccessBox(Settings.appStage, title, core);
        else
            ErrorBox_Controller.showErrorBox(Settings.appStage, title, core);
    }
}
