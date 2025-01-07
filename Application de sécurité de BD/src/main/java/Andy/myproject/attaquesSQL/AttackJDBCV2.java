package Andy.myproject.attaquesSQL;

import Andy.myproject.Service.*;

import Andy.myproject.Service.DatabaseServiceV2;
import Andy.myproject.classes.Users2;
import Andy.myproject.utils.JsonUtil;

import java.sql.Connection;
import java.util.List;
import java.util.TimerTask;
import java.util.logging.Logger;

public class AttackJDBCV2 extends TimerTask {

    Logger logger = Logger.getLogger(AttackJDBCV2.class.getName());

    @Override
    public void run() {
        try {
            int max = 4;
            int min = 1;
            int range = max - min + 1;
            int number;
            String userFile = "./data/users2.json";
            String backupFile = "./data/users2_backup.json";

            // Connexion à la base de données une seule fois en utilisant DatabaseService
            Connection con = DatabaseService.connectToDatabase();

            for (int i = 0; i < 4; i++) {
                int rand = (int) (Math.random() * range) + min;
                number = rand;
                System.out.println("Attaque numéro : " + rand);

                try {
                    // Je compare les deux fichiers Json pour voir si ils sont identiques de base

                    if (!JsonUtil.comparerJson(userFile, backupFile)) {
                        System.out.println("Les fichiers ne sont pas identiques, restauration du backup...");
                        // Restaurer la table depuis le backup qui est un fichier JSOON
                        DatabaseServiceV2.repairTableFromBackup(con);
                        System.out.println("LA TABLE À ÉTÉ RÉPARÉ");
                        System.out.println("VOUS POUVEZ QUITTER POUR VERIFIER");
                        Thread.sleep(5000);
                        System.out.println("L'ATTAQUE RECOMMENCE");
                    }

                    /**
                     * Lancer l'attaque en fonction du numéro généré et attendre 5 secondes entre chaque attaque
                     * Poyr avoir le temps de comparer les fichiers et si non de reparé
                      */

                    switch (number) {
                        case 1:
                            new DELETESQL().run();
                            System.out.println("Attaque DELETE lancée");
                            Thread.sleep(5000);
                            break;
                        case 2:
                             new DROPSQL().run();
                            Thread.sleep(1000);
                            DatabaseServiceV2.repairTableFromBackup(con);
                            System.out.println("Attaque DROP lancée");
                            Thread.sleep(5000);
                            break;
                        case 3:
                            new INSERTSQL().run();
                            System.out.println("Attaque INSERT lancée");
                            Thread.sleep(5000);
                            break;
                        case 4:
                            new UPDATESQL().run();
                            System.out.println("Attaque UPDATE lancée");
                            Thread.sleep(5000);
                            break;
                    }

                    // Sauvegarder l'état actuel de la table dans `users2.json` avec DatabaseService
                    DatabaseService.saveTableToJson(); // Cette méthode n'écrit QUE dans `users2.json` et pas dans userBackup
                    System.out.println("Table mise à jour dans le fichier JSON après l'attaque.");

                } catch (Exception e) {
                    logger.info("Erreur lors de l'attaque ou de la restauration : " + e.getMessage());
                }
            }

        } catch (Exception e) {
            logger.info("Erreur lors de l'attaque programmée : " + e.getMessage());

        }
    }
}

