package init;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.FiniteStateAutomate;
import recognition.StateMachineOperation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static final String INPUT = "C:\\Projects\\FormalLangs\\src\\main\\resources\\lexer\\integer.json";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static StateMachineOperation smo = new StateMachineOperation();

    public static void main(String[] args) throws IOException {
        String str = Files.readString(Paths.get(INPUT));
        FiniteStateAutomate[] fsa = mapper.readValue(str, FiniteStateAutomate[].class);
        for (FiniteStateAutomate automate : fsa) {
            if (automate.getInputs() != null) {
                automate.setInputs(smo.forCreateInputs(automate));
            }
            System.out.println(automate.getInputs());
            System.out.println(smo.max(automate, "+-123-+", 1));
        }
    }
}
