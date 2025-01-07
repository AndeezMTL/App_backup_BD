package Andy.myproject.Service;

import Andy.myproject.classes.Users2;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class DatabaseServiceV2 {
    private static Logger logger = Logger.getLogger(DatabaseServiceV2.class.getName());
    private static Properties props;

    static {
        try {
            // Charger les propriétés une seule fois au démarrage pour pas que ca charge a chaque fois
            props = getProps("./data/application.properties");
        } catch (Exception e) {
            logger.info("Erreur lors du chargement des propriétés : " + e.getMessage());
        }
    }


    // Charger les propriétés depuis le fichier application.properties
    public static Properties getProps(String pathname) throws Exception {
        Properties props = new Properties();
        props.load(new FileInputStream(pathname));
        return props;
    }


    // charger le fichier backup depuis le Json file
    public static List<Users2> loadBackupData(String filePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(new File(filePath), new TypeReference<List<Users2>>() {});
        } catch (IOException e) {
            logger.info("Erreur lors du chargement des données de backup depuis le fichier JSON : " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Restauration de la table users2 à partir du fichier backup Json
    /**
     * on met dans la list user2 les données du fichier Json
     * ensuite on drop la table user2
     * on recrée la table user avec les données de la list
     * et le tour est joué
     */
    public static void repairTableFromBackup(Connection con) {
        try {

            List<Users2> backupData = loadBackupData("./data/users2_backup.json");
            String dropSQL = "DROP TABLE IF EXISTS users2";

            try (Statement stmt = con.createStatement()) {
                stmt.executeUpdate(dropSQL);
                logger.info("Table users2 supprimée avant la restauration.");

                String createSQL = "CREATE TABLE users2 (id INT , username VARCHAR(100), password VARCHAR(100))";
                stmt.executeUpdate(createSQL);  // Crée la table users2 avec la structure basique
                logger.info("Table users2 recréée.");

                for (Users2 user : backupData) {
                    String insertSQL = "INSERT INTO users2 (id, username, password) VALUES (?, ?, ?)";
                    try (PreparedStatement pstmt = con.prepareStatement(insertSQL)) {
                        pstmt.setInt(1, user.getId());
                        pstmt.setString(2, user.getUsername());
                        pstmt.setString(3, user.getPassword());
                        pstmt.executeUpdate();
                    }
                }

                logger.info("Données insérées dans la table users2 à partir du backup.");

            }

        } catch (SQLException e) {
            logger.info("Erreur lors de la restauration de la table : " + e.getMessage());
        } catch (Exception e) {
            logger.info("Erreur lors de la lecture du fichier de propriétés : " + e.getMessage());
        }
    }

    public static void repairTableFromDropAttack(Connection con) {
        try {

            List<Users2> backupData = loadBackupData("./data/users2_backup.json");


            try (Statement stmt = con.createStatement()) {
                logger.info("Table users2 supprimée avant la restauration.");

                String createSQL = "CREATE TABLE users2 (id INT , username VARCHAR(100), password VARCHAR(100))";
                stmt.executeUpdate(createSQL);  // Crée la table users2 avec la structure basique
                logger.info("Table users2 recréée.");

                for (Users2 user : backupData) {
                    String insertSQL = "INSERT INTO users2 (id, username, password) VALUES (?, ?, ?)";
                    try (PreparedStatement pstmt = con.prepareStatement(insertSQL)) {
                        pstmt.setInt(1, user.getId());
                        pstmt.setString(2, user.getUsername());
                        pstmt.setString(3, user.getPassword());
                        pstmt.executeUpdate();
                    }
                }

                logger.info("Données insérées dans la table users2 à partir du backup.");

            }

        } catch (SQLException e) {
            logger.info("Erreur lors de la restauration de la table : " + e.getMessage());
        } catch (Exception e) {
            logger.info("Erreur lors de la lecture du fichier de propriétés : " + e.getMessage());
        }
    }

}
