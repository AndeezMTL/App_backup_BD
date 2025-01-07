package Andy.myproject.attaquesSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DROPSQL {

    public void run() {
        try (Connection con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/tp3", "root", "");
             Statement st = con.createStatement()) {

            String attaqueDrop = "DROP TABLE IF EXISTS users2";
            st.executeUpdate(attaqueDrop);
            System.out.println("Table users2 supprimée.");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}

