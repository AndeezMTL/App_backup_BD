package Andy.myproject.threads;
import Andy.myproject.Service.DatabaseService;
import Andy.myproject.attaquesSQL.AttackJDBCV2;

import java.util.Timer;
public class ExecuteProcessAttack {
    private Timer timer;

    /**
     * C'est la que je fais mon Thread
     * @param delay : le delais avant l'attaque se fasse
     * @param frequency : a une fréquence de 8 seconde (Cela ne marche pas,
     *                  jai préférer mettre un thread.sleep a chaque attaque)
     */
    public ExecuteProcessAttack(int delay, int frequency) {
        timer = new Timer();
        timer.schedule(new AttackJDBCV2(), delay, frequency);
    }

    /**
     *
     * AVANT DE LANCER LE PROGRAMME
     * Créer la table dans Heidi avant de commencer
     * Voici le code a utiliser dans Heidi
     *
     *
     * DROP TABLE if EXISTS users2;
     *
     * create table users2 (
     * 	id INT,
     * 	username VARCHAR(50),
     * 	password VARCHAR(50)
     * );
     * insert into users2 (id, username, password) values (1, 'dosheils0', 'eU6''>');
     * insert into users2 (id, username, password) values (2, 'efiggins1', 'nN2&''BJ>');
     * insert into users2 (id, username, password) values (3, 'nrubertis2', 'bP4+{}f');
     * insert into users2 (id, username, password) values (4, 'mhowison3', 'xE4)!i\rI');
     * insert into users2 (id, username, password) values (5, 'dpickton4', 'nK8|3a}vlKc');
     * insert into users2 (id, username, password) values (6, 'awilne5', 'uU1/(!4n@');
     * insert into users2 (id, username, password) values (7, 'gjerome6', 'qT8~y(.wvg');
     * insert into users2 (id, username, password) values (8, 'mhallgate7', 'rS2}7HleB');
     * insert into users2 (id, username, password) values (9, 'gtzar8', 'sQ9"N<p9(');
     * insert into users2 (id, username, password) values (10, 'akobierski9', 'wB7\d}d<D0y');
     *
     */

    public static void main(String[] args) throws Exception {
        // Sauvegarder le backup initial (une seule fois)
        DatabaseService.saveInitialBackup();

        // Sauvegarder l'état actuel dans le fichier JSON user2 a chaque fois que la table est attaqué
        DatabaseService.saveTableToJson();

        // Lancer les attaques de AttackJDBCV2
        new ExecuteProcessAttack(4000, 8000);
    }
}

