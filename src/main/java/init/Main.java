package init;

import entity.FiniteStateAutomate;
import recognition.StateMachineOperation;

import java.io.IOException;
import java.util.List;

public class Main {

    public static StateMachineOperation smo = new StateMachineOperation();
    public static CreateListFSA createListFSA = new CreateListFSA();

    public static void main(String[] args) throws IOException {
        List<FiniteStateAutomate[]> finiteStateAutomates = createListFSA.create();
        for (FiniteStateAutomate[] fsa : finiteStateAutomates) {
            for (FiniteStateAutomate automate : fsa) {
                if (automate.getInputs() != null) {
                    automate.setInputs(smo.forCreateInputs(automate));
//                    System.out.println(automate.getInputs());
                }
                System.out.println("Автомат: " + automate.getName() + smo.max(automate, "123", 0));
            }
        }
    }
}
