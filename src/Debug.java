import DialogBoxes.ErrorBox_Controller;

public class Debug {

    public static void debugException(Exception e)
    {
        if(Settings.DEBUG_MODE)
            Debug.debugException(e);
    }

    public static void debugDialog(String _title, String _core)
    {
        if(Settings.DEBUG_MODE)
            ErrorBox_Controller.showErrorBox(Settings.appStage, _title, _core);
    }

    public static void debugMsg(String msg)
    {
        if(Settings.DEBUG_MODE)
            System.err.println(msg);
    }
}
