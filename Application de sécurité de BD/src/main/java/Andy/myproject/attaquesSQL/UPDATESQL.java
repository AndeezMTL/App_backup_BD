package Andy.myproject.attaquesSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class UPDATESQL {

    public void run() {
        try (Connection con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/tp3", "root", "");
             Statement st = con.createStatement()) {

            String attaqueUpdate = "UPDATE users2 SET username = 'Andy' WHERE id = 1";
            int lignesAffecté = st.executeUpdate(attaqueUpdate);
            System.out.println(lignesAffecté + " ligne(s) mise(s) à jour.");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}

