import javafx.scene.control.ComboBox;

public class MounirDev{
    //-----------------------
    // Tu dois terminer ca au max avant SAMEDI 22 Fev 2020 !!!!!!!
    //-----------------------

    public static void fillLngsInCBox(ComboBox c)
    {
        // le Combobox doit etre remplit par toute les langues qui figure dans Google-Traduction
        // On utilisera apres un script python pour les prendres en charge en faisant la traduction avec l'API de Google
        // utilise un fichier (txt/xml) pour rentrer les langue
        // Limite le nombre de colonne a afficher du ComboBox a 5
    }

    private getErrorText(String errorType, String language)
    {
        // doit trouver dans l'XML errors.xml le texte d'erreur correspondant a l'erreur "errorType" et a la langue "language"
        /*
        exemple:
        mon xml:

        <?xml xmls="...">
        <errors>
            <email>
               <en> invalid email address </en>
               <fr> L'addresse email que vous avez entre n'est pas valide </fr>
               <es> ........ </es>
               <ar> ........ </ar>
            </email>
            <phone>
               <en> invalid phone number </en>
               <fr> Le numero que vous avec entre est invalide </fr>
               <es> ........ </es>
               <ar> ........ </ar>
            </phone>
            <username>
                .....
            </username>
        </errors>
         */
    }

    private String checkEmail(String email)
    {
        // utilise les expression reguliere pour tester si l'@ email est valide
        // renvoie le message d'erreur associe en utilisant getErrorText("email", "en"), le 2nd parametre pour la langue
        // le test de l'existence sera ajoutee apres qu'on a la BD
    }
    private String checkPhone(String phone)
    {
        // meme chose pour l'email, on teste le numero de tel ici
    }

}

