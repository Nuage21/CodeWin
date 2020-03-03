package projet_users;

import java.util.*;
import java.sql.Date;
import java.sql.*;
import java.time.*;

public class User {

    private String nom = null,prenom = null , numtel = null , adresse = null , email = null , photo = null, password = null;
    private LocalDate dob;
	private boolean darkmod;
	private int langue;
	public static final String USERNAME = "houssamtertaki";
	public static final String PASSWORD = "houssam2020";
	public static final String CONN = "jdbc:mysql://localhost:3306/projet";

   //constructeur vide
	public User(){

     }



	public static Connection getConnection() throws SQLException{
		return DriverManager.getConnection(CONN, USERNAME, PASSWORD);
	}




	public static boolean userEmailExiste(String email)
	{  String read = "SELECT * From users WHERE email = ?";

	try{
		Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(read);
			stmt.setString(1, email);
			ResultSet rs = null;

			rs = stmt.executeQuery();
System.out.println(rs.first());
    if(rs.first() == false)
			{return false;}
			else{return true;}
		}
		catch(SQLException e)
		{
			System.err.println("Error");
return true;
		}
	}






	public static boolean userNameExiste(String userName)
	{  String read = "SELECT * From users WHERE username = ?";

	try{
		Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(read);
			stmt.setString(1, userName);
			ResultSet rs = null;

			rs = stmt.executeQuery();
System.out.println(rs.first());
    if(rs.first() == false)
			{return false;}
			else{return true;}
		}
		catch(SQLException e)
		{
			System.err.println("Error");
return true;
		}
	}







	public static void addUser(String nom,String prenom,String username,Date dob,String email,String password,String numtel,String photo,String adresse ,int langue,boolean darkmod)
	{   String insert = "INSERT INTO users (nom,prenom,username,dob,email,password,numtel,photo,adresse,langue,darkmod) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

	try{Connection conn = getConnection();
	PreparedStatement stmt = conn.prepareStatement(insert);

	if(userEmailExiste(email) || userNameExiste(username))
	{
		System.out.println("l'utilisateur existe deja on peut pas l'ajouté");
	}
	else
	{
String tableQuestions = "CREATE TABLE " + username + " " + "( question text,reponse_1 text DEFAULT NULL,reponse_2 text DEFAULT NULL ,reponse_3 text DEFAULT NULL, reponse_4 text DEFAULT NULL);";


    stmt.setString(1, nom);
	stmt.setString(2, prenom);
	stmt.setString(3, username);
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

stmt = conn.prepareStatement(tableQuestions);

stmt.execute();
stmt.close();

	}
	}catch(SQLException e)
	{
		System.err.println(e);
	}
	}





	//les setters et les getters
	public String getNom() {
		return nom;
	}


	public void setNom(String nom) {
		this.nom = nom;
	}


	public String getPrenom() {
		return prenom;
	}


	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}


	public String getNumtel() {
		return numtel;
	}


	public void setNumtel(String numtel) {
		this.numtel = numtel;
	}


	public String getAdresse() {
		return adresse;
	}


	public void setAdresse(String adresse) {
		this.adresse = adresse;
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


	public LocalDate getDob() {
		return dob;
	}


	public void setDob(LocalDate dob) {
		this.dob = dob;
	}


	public boolean isDarkmod() {
		return darkmod;
	}


	public void setDarkmod(boolean darkmod) {
		this.darkmod = darkmod;
	}


	public int getLangue() {
		return langue;
	}


	public void setLangue(int langue) {
		this.langue = langue;
	}










}
