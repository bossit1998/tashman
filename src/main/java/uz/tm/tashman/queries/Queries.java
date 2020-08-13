package uz.tm.tashman.queries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import uz.tm.tashman.models.ResponseData;
import uz.tm.tashman.models.SignUpModel;

public class Queries {

    @Autowired
    @Qualifier("jdbcMaster")
    public JdbcTemplate jdbcTemplate;


    public static String GET_USERS = " SELECT * FROM clients";

    public static String GET_USER = " SELECT * FROM clients WHERE username = ?";

    public static String GET_PRODUCTS = " SELECT * FROM products";

    public static String INSERT_USER =
            "insert into public.clients " +
                    "(name, surname, email, username, password, phone_number, region, company_name, privilege) " +
                    "VALUES (?,?,?,?,?,?,?,?,?) ";
    
}
