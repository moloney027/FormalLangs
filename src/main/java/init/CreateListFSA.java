package init;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.FiniteStateAutomate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CreateListFSA {

    private final ObjectMapper mapper = new ObjectMapper();

    public void findFiles(File item, List<File> resultListFiles) {
        if (item.isDirectory()) {
            if (item.listFiles() != null) {
                for (File file : item.listFiles()) {
                    findFiles(file, resultListFiles);
                }
            }
        } else {
            resultListFiles.add(item);
        }
    }

    public List<FiniteStateAutomate[]> create() throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
//        System.out.println("Введите путь к папке: (C:\\\\Projects\\\\FormalLangs\\\\src\\\\main\\\\resources\\\\lexer)");
//        String path = bf.readLine();
        String path = "C:\\\\Projects\\\\FormalLangs\\\\src\\\\main\\\\resources\\\\lexer";
        File dir = new File(path);
        List<File> listFiles = new ArrayList<>();
        findFiles(dir, listFiles);
//        System.out.println("Список всех файлов: ");
//        listFiles.forEach(System.out::println);
        List<FiniteStateAutomate[]> listFSA = new ArrayList<>();
        for (int i = 0; i < listFiles.size(); i++) {
            listFSA.add(mapper.readValue(Files.readString(listFiles.get(i).toPath()), FiniteStateAutomate[].class));
        }
        return listFSA;
    }
}
