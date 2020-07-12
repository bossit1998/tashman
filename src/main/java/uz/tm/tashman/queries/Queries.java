package uz.tm.tashman.queries;

public class Queries {

    public static String GET_USERS = " SELECT * FROM clients";

    public static String GET_USER = " SELECT * FROM clients WHERE username = ?";

    public static String INSERT_USER = "INSERT INTO tashman.clients (full_name, email, username, password, phone_number, region, company_name, privilege) VALUES (?,?,?,?,?,?,?,?)  ";

}
