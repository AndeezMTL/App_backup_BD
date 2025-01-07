package Andy.myproject.attaquesSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DELETESQL {

    public void run() {
        try (Connection con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/tp3", "root", "");
             Statement st = con.createStatement()) {

            String attaqueDelete = "DELETE FROM users2";
            int lignesAffecté = st.executeUpdate(attaqueDelete);
            System.out.println(lignesAffecté + " ligne(s) supprimée(s).");

        } catch (Exception e) {
            System.out.println(e.getMessage());;
        }
    }
 
}
