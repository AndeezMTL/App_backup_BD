package Andy.myproject.utils;

import Andy.myproject.classes.Users2;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class JsonUtil {

    // Méthode pour écrire une liste d'objets dans un fichier JSON
    public static boolean writeToJsonFile(List<Users2> list, String fileName) {
        try {
            new ObjectMapper().writeValue(new FileOutputStream(fileName), list);
            return new File(fileName).exists() && new File(fileName).length() > 0;
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture du fichier JSON : " + e.getMessage());
            return false;
        }
    }

    // Méthode pour lire un fichier JSON et le convertir en une liste d'objets
    public static List<Users2> readFromJsonFile(String fileName) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(new File(fileName), new TypeReference<List<Users2>>() {});
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier JSON : " + e.getMessage());
            return null;
        }
    }

    // Méthode pour comparer deux fichiers JSON
    public static boolean comparerJson(String file1, String file2) {
        List<Users2> list1 = readFromJsonFile(file1);
        List<Users2> list2 = readFromJsonFile(file2);
        if (list1 == null || list2 == null) {
            return false;
        }
        return list1.equals(list2);
    }
}

