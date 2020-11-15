package recognition;

import entity.FiniteStateAutomate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

class StateMachineOperationTest {

    FiniteStateAutomate fsa;
    StateMachineOperation smo;

    @BeforeAll
    public void start() {
        fsa = new FiniteStateAutomate();
        smo = new StateMachineOperation();
    }

    @Test
    public void testForCreateInputs() {
        Map<String, Set<String>> createInp = smo.forCreateInputs(fsa);

        Assertions.assertEquals(Set.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"), createInp.get("digit"));
        for (char ch = 'a'; ch < 'Z'; ch++){
            Assertions.assertTrue(createInp.get("char").contains(String.valueOf(ch)));
        }
    }
}