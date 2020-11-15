package recognition;

import entity.FiniteStateAutomate;
import init.CreateListFSA;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

class StateMachineOperationTest {

    static StateMachineOperation smo;
    static CreateListFSA createListFSA;
    static List<FiniteStateAutomate[]> finiteStateAutomates;

    @BeforeAll
    static void start() throws IOException {
        smo = new StateMachineOperation();
        createListFSA = new CreateListFSA();
        finiteStateAutomates = createListFSA.create();
    }

    @Test
    public void testForCreateFSA() throws IOException {
        List<FiniteStateAutomate[]> finiteStateAutomates = createListFSA.create();
        Assertions.assertEquals(9, finiteStateAutomates.size());
    }

    @Test
    public void testForCreateInputs() {
        for (FiniteStateAutomate[] fsa : finiteStateAutomates) {
            for (FiniteStateAutomate automate : fsa) {
                if (smo.forCreateInputs(automate) != null && automate.getInputs() != null) {
                    Map<String, Set<String>> createInp = smo.forCreateInputs(automate);
                    if (createInp.get("digit") != null) {
                        Assertions.assertEquals(Set.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"), createInp.get("digit"));
                    }
                    if (createInp.get("char") != null) {
                        char[] alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
                        for (char ch: alphabet) {
                            Assertions.assertTrue(createInp.get("char").contains(String.valueOf(ch)));
                        }
                    }
                }
            }
        }
    }
}