package Andy.myproject.Service;

import Andy.myproject.classes.Users2;
import Andy.myproject.utils.JsonUtil;

import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class DatabaseService {
    private static Logger logger = Logger.getLogger(DatabaseService.class.getName());

    /**
     * Méthode pour se connecter à la base de données  sans avoir a le
     * faire dans chaque méthode
      */
    public static Connection connectToDatabase() throws SQLException {
        return DriverManager.getConnection("jdbc:mariadb://localhost:3306/tp3", "root", "");
    }

    // Sauvegarde initiale dans le fichier backup qui change pas normalement
    public static void saveInitialBackup() {
        Logger logger = Logger.getLogger(DatabaseService.class.getName());
        try (Connection con = connectToDatabase();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM users2")) {

            List<Users2> list = getUsersListFromResultSet(rs);
            JsonUtil.writeToJsonFile(list, "./data/users2_backup.json");
            System.out.println("Backup initial sauvegardé dans : ./data/users2_backup.json");

        } catch (SQLException e) {
            logger.info("Erreur SQL lors de la sauvegarde du backup : " + e.getMessage());
        } catch (Exception e) {
            logger.info("Erreur lors de la sauvegarde du backup : " + e.getMessage());
        }
    }

    /**
     * Sauvegarde de l'état actuel dans le fichier principal (users2.json)
     * Donc a chaque fois que la table va etre modifier je vais voir dans mon
     * fichier Json vue qu'il enregistre la table
      */

    public static void saveTableToJson() {
        Logger logger = Logger.getLogger(DatabaseService.class.getName());
        try (Connection con = connectToDatabase()) {
            if (!doesTableExist(con, "users2")) {
                logger.info("Table users2 inexistante, annulation de la sauvegarde.");
                return;
            }
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT * FROM users2")) {

                List<Users2> list = new ArrayList<>();
                while (rs.next()) {
                    Users2 user = new Users2(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
                    list.add(user);
                }

                JsonUtil.writeToJsonFile(list, "./data/users2.json");
                System.out.println("Table sauvegardée dans : ./data/users2.json");
            }
        } catch (SQLException e) {
            logger.info("Erreur SQL lors de la sauvegarde : " + e.getMessage());
        } catch (Exception e) {
            logger.info("Erreur lors de la sauvegarde en JSON : " + e.getMessage());
        }
    }


    // Charger la list des utilisateurs (Result set dans une méthode)
    private static List<Users2> getUsersListFromResultSet(ResultSet rs) throws SQLException {
        List<Users2> list = new ArrayList<>();
        while (rs.next()) {
            Users2 user = new Users2(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
            list.add(user);
        }
        return list;
    }

    // Vérifier si la table existe si c'est non on change par le backup.Json
    public static boolean doesTableExist(Connection con, String tableName) {
        if (con == null) {
            logger.info("Connexion à la table nulle.");
            return false;
        }

        try (ResultSet rs = con.getMetaData().getTables(null, null, tableName, null)) {
            return rs.next();
        } catch (SQLException e) {
            logger.info("Erreur lors de la vérification de la table : " + e.getMessage());
            return false;
        }
    }

    // Recréer la table users2 a l'aide du fichier json
    public static void recreateTable(Connection con) {
        String createTableSQL = " CREATE TABLE users2 ( id INT , username VARCHAR(255), password VARCHAR(255) ";
        try (Statement st = con.createStatement()) {
            st.executeUpdate(createTableSQL);
            logger.info("Table users2 recréée avec succès.");
        } catch (SQLException e) {
            logger.info("Erreur lors de la recréation de la table : " + e.getMessage());
        }
    }

    // Supprimer toutes les données de la table
    public static void clearTable(Connection con) {
        try (Statement st = con.createStatement()) {
            st.executeUpdate("DELETE FROM users2");
        } catch (SQLException e) {
            Logger.getLogger(DatabaseService.class.getName()).info("Erreur lors de la suppression des données : " + e.getMessage());
        }
    }


    // Une autre facon pour créer la table
    // facon du professeur

    public static Properties getProps(String pathname) throws Exception {
        Properties props = new Properties();
        props.load(new FileInputStream(pathname));
        return props;
    }

    //vérifier si la table exist
    public static boolean tableExists(String tableName) throws Exception {
        boolean flagExist = false;
        Properties props = getProps("./data/application.properties");
        Connection conn = DriverManager.getConnection(props.getProperty("url"),
                props.getProperty("username"),
                props.getProperty("password"));

        ResultSet rs =
                conn.getMetaData().getTables
                        (null, null, tableName, null);
        while (rs.next()) {
            System.out.println(rs.getString("TABLE_NAME"));
            flagExist = true;
        }

        return flagExist;
    }

    //drop la table si elle existe et remplacer par la table backup
    public static boolean createOrDrop(String query, String tableName) throws Exception {
        boolean flagExist = false;

        ResultSet rs = null;
        Connection conn = null;
        Statement stmt = null;

        Properties props = getProps("./data/application.properties");

        conn = DriverManager.getConnection(props.getProperty("url"),
                props.getProperty("username"),
                props.getProperty("password"));

        stmt = conn.createStatement();
        stmt.executeUpdate(query);
        System.out.println("OKOKOKOK");

        rs = conn.getMetaData().getTables(null, null, tableName, null);
        while (rs.next()) {
            flagExist = true;
        }
        return flagExist;
    }

    //execute la méthode du prof
    public static void main(String[] args) throws Exception {


        if (tableExists("etudiant") && !tableExists("etudiantbackup")) {
            System.out.println(createOrDrop(getProps("./data/application.properties").getProperty("BACKUP2"),
                    "etudiant"));
        } else {
            logger.info("IMPOSSIBLE DE CRÉER LE BACKUP!!!");
        }

    }
}




