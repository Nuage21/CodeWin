public class Checker {

    public static boolean isImage(String file) {
        String lowered = file.toLowerCase();
        String extensions[] = {".jpeg", ".jpg", ".png"};
        for (String ext : extensions) {
            if (lowered.contains(ext))
                return true;
        }
        return false;
    }
}
