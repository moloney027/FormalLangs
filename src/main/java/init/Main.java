package init;

import entity.FiniteStateAutomate;
import recognition.StateMachineOperation;
import utils.Pair;

import java.io.IOException;
import java.util.List;

public class Main {

    public static StateMachineOperation smo = new StateMachineOperation();
    public static CreateListFSA createListFSA = new CreateListFSA();

    // Рассматриваются все возможные автоматы, в результате для каждого получена
    // пара: <допустима ли в нем данная лексема (true/false), кол-во занимаемых ею символов>.
    public static void task1(String input) throws IOException {
        System.out.println("Задание 1: (<допуск, кол-во символов>)");
        Pair<Boolean, Integer> max = null;
        List<FiniteStateAutomate[]> finiteStateAutomates = createListFSA.create();
        for (FiniteStateAutomate[] fsa : finiteStateAutomates) {
            for (FiniteStateAutomate automate : fsa) {
                if (automate.getInputs() != null) {
                    automate.setInputs(smo.forCreateInputs(automate));
                }
                max = smo.max(automate, input, 0);
                System.out.println("Автомат: " + automate.getName() + " — " + max);
            }
        }
    }

    // Основана на предыдущей задаче, но теперь, в результате анализа, для каждой лексемы в введенной строке будет получена
    // пара: <название класса лексемы, сама лексема>.
    public static void task2() throws IOException {
        String strCode = "int a = 123;\n" +
                "double b = 2.2e5;\n" +
                "boolean break = false;\n" +
                "\n" +
                "while (b > a && !break) {\n" +
                "  b = b - a\n" +
                "  if (b <= 0 && a > 0) {\n" +
                "    break;\n" +
                "  }\n" +
                "  a = a / 15.0;\n" +
                "}";
        smo.createPairs(strCode).forEach(System.out::println);
    }

    public static void main(String[] args) throws IOException {
//        System.out.println("Задание 1: (<допуск, кол-во символов>)");
//        task1();
        System.out.println("Задание 2: (<класс лексем, данная лексема>)");
        task2();
    }
}
