package Andy.myproject.Service;

import Andy.myproject.classes.Users2;
import Andy.myproject.utils.JsonUtil;

import java.sql.Connection;
import java.util.List;

public class DBServiceV2 {


        public static void main(String[] args) {
            try {
                // Sauvegarde initiale dans le fichier backup qu'on va faire une fois
                DatabaseService.saveInitialBackup();

                // Sauvegarde actuelle dans users2.json qu'on va faire dans le fichier users2
                DatabaseService.saveTableToJson();

                // Connexion à la base de données grâce a la méthode ce qui nous évite de le faire a chaque fois
                Connection con = DatabaseService.connectToDatabase();

                // Restaurer la table users2 depuis le backup qui est users2_backup.json normalement
                DatabaseServiceV2.repairTableFromBackup(con);

            } catch (Exception e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }
    }



