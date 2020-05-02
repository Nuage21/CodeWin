import DialogBoxes.ErrorBox_Controller;

import java.sql.*;
import java.time.LocalDate;
import java.util.TreeMap;

public class User {

    private String username;
    private String lastname;
    private String firstname;
    private String mobile;
    private String address;
    private String email;
    private String password;
    private java.sql.Date dob;
    private java.sql.Date signupDate;
    private String lastAnsweredQuestion; // ex: "2/3/30" -> chapterID/courseID/QuestionID
    private String stats;
    private boolean darkmode; // locally saved
    private String lang; // locally saved

    // database settings
    public static final String USERNAME = "sql7335090";
    public static final String PASSWORD = "3Wzzy8jQpe";
    public static final String CONN = "jdbc:mysql://sql7.freemysqlhosting.net:3306/sql7335090";

    public User() {

    }

    public User(String _username, String _pwd, String email, String _firstname, String _lastname, Date _dob, String _address, String _mobile) {
        this.username = _username;
        this.password = _pwd;
        this.email = email;
        this.firstname = _firstname;
        this.lastname = _lastname;
        this.dob = _dob;
        this.address = _address;
        this.mobile = _mobile;
    }


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONN, USERNAME, PASSWORD);
    }


    public static boolean userEmailExiste(String email) {
        String read = "SELECT * From users_info WHERE email = ?";

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(read);
            stmt.setString(1, email);
            ResultSet rs = null;

            rs = stmt.executeQuery();
            if (rs.first() == false) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error");
            return true;
        }
    }


    public static boolean userNameExiste(String userName) {
        String read = "SELECT * From users_info WHERE username = ?";

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(read);
            stmt.setString(1, userName);
            ResultSet rs = null;

            rs = stmt.executeQuery();
            //   System.out.println(rs.first());
            if (rs.first() == false) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            if(Settings.DEBUG_MODE)
                System.err.println("Error @ User.userNameExiste");
            return true;
        }
    }


    public static boolean addUser(User _user) {
        String insert = "INSERT INTO users_info (username, password, email, firstname, lastname, dob, address, mobile, sdate, lqst, stats) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        boolean b = false;
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(insert);

            if (userEmailExiste(_user.email) || userNameExiste(_user.username)) {
                if(Settings.DEBUG_MODE)
                    System.out.println("_________________________________USER EXIST_________________________");
                return false;
            } else {
                // String tableQuestions = "CREATE TABLE " + username + " " + "( question text,reponse_1 text DEFAULT NULL,reponse_2 text DEFAULT NULL ,reponse_3 text DEFAULT NULL, reponse_4 text DEFAULT NULL);";

                stmt.setString(1, _user.username);
                stmt.setString(2, _user.password);
                stmt.setString(3, _user.email);
                stmt.setString(4, _user.firstname);
                stmt.setString(5, _user.lastname);
                stmt.setDate(6, _user.dob);
                stmt.setString(7, _user.address);
                stmt.setString(8, _user.mobile);
                stmt.setDate(9, java.sql.Date.valueOf(LocalDate.now()));
                stmt.setString(10, ""); // last answered question
                stmt.setString(11, ""); // stats


                stmt.execute();
                stmt.close();
                conn.close();

                if(Settings.DEBUG_MODE)
                    System.out.println("_________________________________USER INSERTED_________________________");
            }
        } catch (SQLException e) {
            if(Settings.DEBUG_MODE)
            {
                System.out.println("_________________________________SQL ERROR_________________________");
                e.printStackTrace();
            }
        } finally {
            return b;
        }

    }


    public static User getUserDB(String username, String provided_pwd) {

        User user = null;
        String select = "SELECT * From users_info WHERE username = ?";

        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(select);

            if (!userNameExiste(username))
                return null;
            else {
                stmt.setString(1, username);
                ResultSet rs = null;
                rs = stmt.executeQuery();
                rs.first();
                if(!rs.getString("password").equals(provided_pwd))
                {
                    ErrorBox_Controller.showErrorBox(Settings.appStage, "Mot de passe Incorrect", "Veulliez verifier votre mot de passe et reessayer.");
                }
                user = new User(username, rs.getString("password"), rs.getString("email"), rs.getString("firstname"), rs.getString("lastname"), rs.getDate("dob"), rs.getString("address"), rs.getString("mobile"));
                rs.close();
            }


        } catch (SQLException e) {
            ErrorBox_Controller.showErrorBox(Settings.appStage, "Erreur de Connexion", "Veuillez verifier votre connexion avant d'essayer de vous connecter.");
        } finally {
            return user;
        }


    }


    public static boolean updateUserName(User user, String userName) {
        boolean b = false;
        if (User.userNameExiste(userName)) {
            return false;
        } else {
            String update = "UPDATE users SET userName = ? WHERE userName = '" + user.getUsername() + "'";
            try {
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(update);


                stmt.setString(1, userName);
                stmt.execute();
                stmt.close();

                b = true;
            } catch (SQLException e) {
                System.err.println(e);
            } finally {
                return b;
            }
        }

    }


    public static boolean updateEmail(User user, String email) {
        boolean b = false;
        if (User.userEmailExiste(email)) {
            return false;
        } else {
            String update = "UPDATE users SET email = ? WHERE email = '" + user.getEmail() + "'";
            try {
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(update);


                stmt.setString(1, email);
                stmt.execute();
                stmt.close();

                b = true;
            } catch (SQLException e) {
                System.err.println(e);
            } finally {
                return b;
            }
        }

    }


    public static boolean updateFirstName(User user, String firstName) {
        boolean b = false;

        String update = "UPDATE users SET firstName = ? WHERE email = '" + user.getEmail() + "' and userName = '" + user.getUsername() + "'";
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(update);


            stmt.setString(1, firstName);
            stmt.execute();
            stmt.close();

            b = true;
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            return b;
        }
    }


    public static boolean updateLastName(User user, String lastName) {
        boolean b = false;

        String update = "UPDATE users SET lastName = ? WHERE email = '" + user.getEmail() + "' and userName = '" + user.getUsername() + "'";
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(update);


            stmt.setString(1, lastName);
            stmt.execute();
            stmt.close();

            b = true;
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            return b;
        }
    }

    public static boolean updateNumtel(User user, String numtel) {
        boolean b = false;

        String update = "UPDATE users SET numtel = ? WHERE email = '" + user.getEmail() + "' and userName = '" + user.getUsername() + "'";
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(update);


            stmt.setString(1, numtel);
            stmt.execute();
            stmt.close();

            b = true;
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            return b;
        }
    }

    public static boolean updatePassword(User user, String password) {
        boolean b = false;

        String update = "UPDATE users SET password = ? WHERE email = '" + user.getEmail() + "' and userName = '" + user.getUsername() + "'";
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(update);


            stmt.setString(1, password);
            stmt.execute();
            stmt.close();

            b = true;
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            return b;
        }
    }


    public static boolean deleteUser(User user) {
        boolean b = false;
        if (!((User.userNameExiste(user.getUsername())) || (User.userEmailExiste(user.getEmail())))) {
            return false;
        } else {
            String del = "DELETE FROM users WHERE email='" + user.getEmail() + "' and userName='" + user.getUsername() + "'";
            try {
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(del);


                stmt.execute();
                stmt.close();

                b = true;
            } catch (SQLException e) {
                System.err.println(e);
            } finally {
                return b;
            }

        }

    }


    //les setters et les getters


    public String getMobile() {
        return mobile;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getLastname() {
        return lastname;
    }


    public void setLastname(String lastname) {
        this.lastname = lastname;
    }


    public String getFirstname() {
        return firstname;
    }


    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }


    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public Date getDob() {
        return dob;
    }


    public void setDob(Date dob) {
        this.dob = dob;
    }


    public boolean isDarkmode() {
        return darkmode;
    }


    public void setDarkmode(boolean darkmode) {
        this.darkmode = darkmode;
    }


    public String getLang() {
        return lang;
    }

    public class Coord2D {
        public int x;
        public int y;

        Coord2D(int _x, int _y) {
            this.x = _x;
            this.y = _y;
        }
    }
}
