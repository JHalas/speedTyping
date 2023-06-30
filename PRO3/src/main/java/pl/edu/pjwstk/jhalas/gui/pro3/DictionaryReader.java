package pl.edu.pjwstk.jhalas.gui.pro3;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DictionaryReader {

    String path = "dictionary";
    File directory = new File(path);
    Random random = new Random();
    public  List<String> listFilesInDirectory() {
        List<String> fileList = new ArrayList<>();

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileList.add(file.getName());
                }
            }
        }

        return fileList;
    }

    public List<String> getAvailableLanguageForMenu() {
        return listFilesInDirectory();
    }

    public String getRandomTextFromDictionaryForGivenLanguage(String name, int numLines) {
        List<String> lines = new ArrayList<>();
        System.out.println(name);
        String filename = directory.getAbsolutePath()+"\\"+name;
        System.out.println(filename);


        try (FileReader fr = new FileReader(filename, StandardCharsets.UTF_8); BufferedReader reader = new BufferedReader(fr)) {
            String line;

            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder result = new StringBuilder();


        for (int i = 0; i < numLines; i++) {
            if (lines.isEmpty()) {
                break;
            }

            int randomIndex = random.nextInt(lines.size());
            String randomLine = lines.get(randomIndex);
            result.append(randomLine).append(" ");
            lines.remove(randomIndex);
        };
        return result.toString();
    }
}

