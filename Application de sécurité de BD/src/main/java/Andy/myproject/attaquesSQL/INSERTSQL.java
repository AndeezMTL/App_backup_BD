package Andy.myproject.attaquesSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class INSERTSQL {

    public void run() {
        try (Connection con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/tp3", "root", "");
             Statement st = con.createStatement()) {

            String attaqueInsert = "INSERT INTO users2 (id, username) VALUES (1, 'Andy')";
            int lignesAffecté = st.executeUpdate(attaqueInsert);
            System.out.println(lignesAffecté + " ligne(s) insérée(s).");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}

