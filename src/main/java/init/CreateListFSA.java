package init;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.FiniteStateAutomate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateListFSA {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String path = "C:\\\\Projects\\\\FormalLangs\\\\src\\\\main\\\\resources\\\\lexer";

    public static void findFiles(File item, List<File> resultListFiles) {
        if (item.isDirectory()) {
            if (item.listFiles() != null) {
                for (File file : Objects.requireNonNull(item.listFiles())) {
                    findFiles(file, resultListFiles);
                }
            }
        } else {
            resultListFiles.add(item);
        }
    }

    public static List<FiniteStateAutomate[]> create() throws IOException {
        List<File> allFiles = new ArrayList<>();
        List<FiniteStateAutomate[]> listFSA = new ArrayList<>();
        findFiles(new File(path), allFiles);
        for (File file : allFiles) {
            listFSA.add(mapper.readValue(Files.readString(file.toPath()), FiniteStateAutomate[].class));
        }
        return listFSA;
    }
}
