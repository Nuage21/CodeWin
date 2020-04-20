import java.sql.Date;
import java.sql.*;
import java.time.*;
import java.util.TreeMap;

public class User {

    private String userName;
    private String lastName;
    private String firstName;
    private String telephone;
    private String address;
    private String email;
    private String photo;
    private String password;
    private Date dob;
    private boolean darkmode;
    private int lang;
    // pour les stats
    private TreeMap<Integer,Date> point ;
    private TreeMap<Integer,Date> activité ;



    // database settings
    public static final String USERNAME = "houssamtertaki";
    public static final String PASSWORD = "houssam2020";
    public static final String CONN = "jdbc:mysql://localhost:3306/projet";

    public User() {

    }


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONN, USERNAME, PASSWORD);
    }


    public static boolean userEmailExiste(String email) {
        String read = "SELECT * From users WHERE email = ?";

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
        String read = "SELECT * From users WHERE username = ?";

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
            System.err.println("Error");
            return true;
        }
    }



	public static boolean addUser(String firstName, String lastName, String userName, Date dob, String email, String password, String numtel, String photo, String adresse, int langue, boolean darkmod) {
        String insert = "INSERT INTO users (firstName,lastName,userName,dob,email,password,numtel,photo,adresse,langue,darkmod) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
         boolean b = false;
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(insert);

            if (userEmailExiste(email) || userNameExiste(userName)) {
             //   System.out.println("l'utilisateur existe deja on peut pas l'ajouté");
            	b = true;
            } else {
               // String tableQuestions = "CREATE TABLE " + username + " " + "( question text,reponse_1 text DEFAULT NULL,reponse_2 text DEFAULT NULL ,reponse_3 text DEFAULT NULL, reponse_4 text DEFAULT NULL);";


                stmt.setString(1, firstName);
                stmt.setString(2, lastName);
                stmt.setString(3, userName);
                stmt.setDate(4, dob);
                stmt.setString(5, email);
                stmt.setString(6, password);
                stmt.setString(7, numtel);
                stmt.setString(8, photo);
                stmt.setString(9, adresse);
                stmt.setInt(10, langue);
                stmt.setBoolean(11, darkmod);

                stmt.execute();
                stmt.close();

            /*    stmt = conn.prepareStatement(tableQuestions);

                stmt.execute();
                stmt.close();    */

              b=false ;
            }
        } catch (SQLException e) {
            System.err.println(e);
        }finally{
        	return b;
        }

    }


	public static boolean updateUserName(User user,String userName){
  boolean b = false;
if(User.userNameExiste(userName))
{
	return  false;
}
else{String update = "UPDATE users SET userName = ? WHERE userName = '" + user.getUserName()+ "'";
try {
    Connection conn = getConnection();
    PreparedStatement stmt = conn.prepareStatement(update);


        stmt.setString(1, userName);
        stmt.execute();
        stmt.close();

        b = true ;
} catch (SQLException e) {
    System.err.println(e);
}finally{
	return b;
}
}

	}


	public static boolean updateEmail(User user,String email){
		  boolean b = false;
		if(User.userEmailExiste(email))
		{
			return  false;
		}
		else{String update = "UPDATE users SET email = ? WHERE email = '" + user.getEmail()+ "'";
		try {
		    Connection conn = getConnection();
		    PreparedStatement stmt = conn.prepareStatement(update);


		        stmt.setString(1, email);
		        stmt.execute();
		        stmt.close();

		        b = true ;
		} catch (SQLException e) {
		    System.err.println(e);
		}finally{
			return b;
		}
		}

			}



	public static boolean updateFirstName(User user,String firstName){
		  boolean b = false;

		String update = "UPDATE users SET firstName = ? WHERE email = '" + user.getEmail()+ "' and userName = '" + user.getUserName()+"'";
		try {
		    Connection conn = getConnection();
		    PreparedStatement stmt = conn.prepareStatement(update);


		        stmt.setString(1, firstName);
		        stmt.execute();
		        stmt.close();

		        b = true ;
		} catch (SQLException e) {
		    System.err.println(e);
		}finally{
			return b;
		}
		}


	public static boolean updateLastName(User user,String lastName){
		  boolean b = false;

		String update = "UPDATE users SET lastName = ? WHERE email = '" + user.getEmail()+ "' and userName = '" + user.getUserName()+"'";
		try {
		    Connection conn = getConnection();
		    PreparedStatement stmt = conn.prepareStatement(update);


		        stmt.setString(1, lastName);
		        stmt.execute();
		        stmt.close();

		        b = true ;
		} catch (SQLException e) {
		    System.err.println(e);
		}finally{
			return b;
		}
		}

	public static boolean updateNumtel(User user,String numtel){
		  boolean b = false;

		String update = "UPDATE users SET numtel = ? WHERE email = '" + user.getEmail()+ "' and userName = '" + user.getUserName()+"'";
		try {
		    Connection conn = getConnection();
		    PreparedStatement stmt = conn.prepareStatement(update);


		        stmt.setString(1, numtel);
		        stmt.execute();
		        stmt.close();

		        b = true ;
		} catch (SQLException e) {
		    System.err.println(e);
		}finally{
			return b;
		}
		}

	public static boolean updatePassword(User user,String password){
		  boolean b = false;

		String update = "UPDATE users SET password = ? WHERE email = '" + user.getEmail()+ "' and userName = '" + user.getUserName()+"'";
		try {
		    Connection conn = getConnection();
		    PreparedStatement stmt = conn.prepareStatement(update);


		        stmt.setString(1, password);
		        stmt.execute();
		        stmt.close();

		        b = true ;
		} catch (SQLException e) {
		    System.err.println(e);
		}finally{
			return b;
		}
		}




    //les setters et les getters


    public String getTelephone() {
        return telephone;
    }


    public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public void setTelephone(String telephone) {
        this.telephone = telephone;
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




    public String getPhoto() {
		return photo;
	}


	public void setPhoto(String photo) {
		this.photo = photo;
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


    public int getLang() {
        return lang;
    }


    public void setLang(int lang) {
        this.lang = lang;
    }




}
