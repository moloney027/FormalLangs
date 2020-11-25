package recognition;

import entity.FiniteStateAutomate;
import init.CreateListFSA;
import utils.Pair;

import java.io.IOException;
import java.util.*;

public class StateMachineOperation {

    public static Map<String, Set<String>> forCreateInputs(FiniteStateAutomate fsa) {
        if (fsa.getInputs() == null) {
            return null;
        }
        Map<String, Set<String>> inputs = fsa.getInputs();
        Map<String, Set<String>> newInputs = new HashMap<>();
        Set<String> newSetForInput = new HashSet<>();

        for (String in: inputs.keySet()) {
            switch (in) {
                case "digit":
                    for (int j = 0; j <= 9; j++) {
                        newSetForInput.add(String.valueOf(j));
                    }
                    newInputs.put(in, new HashSet<>(newSetForInput));
                    newSetForInput.clear();
                    break;
                case "char":
                    char[] chars = "azAZ".toCharArray();
                    for (int k = 0; k < chars.length; k += 2) {
                        for (char ch = chars[k]; ch <= chars[k + 1]; ch++) {
                            newSetForInput.add(String.valueOf(ch));
                        }
                    }
                    newInputs.put(in, new HashSet<>(newSetForInput));
                    newSetForInput.clear();
                    break;
                case "whitespace":
                    newSetForInput.add("\n");
                    newSetForInput.add("\r");
                    newSetForInput.add("\t");
                    newSetForInput.add(" ");
                    newInputs.put(in, new HashSet<>(newSetForInput));
                    newSetForInput.clear();
                    break;
                case "exp":
                    char[] ex = "eE".toCharArray();
                    for (char e : ex) {
                        newSetForInput.add(String.valueOf(e));
                    }
                    newInputs.put(in, new HashSet<>(newSetForInput));
                    newSetForInput.clear();
                    break;
                case "ops":
                    char[] op = "*/%".toCharArray();
                    for (char o : op) {
                        newSetForInput.add(String.valueOf(o));
                    }
                    newInputs.put(in, new HashSet<>(newSetForInput));
                    newSetForInput.clear();
                    break;
                case "ops2":
                    char[] op2 = "&|+-".toCharArray();
                    for (char o2 : op2) {
                        newSetForInput.add(String.valueOf(o2));
                    }
                    newInputs.put(in, new HashSet<>(newSetForInput));
                    newSetForInput.clear();
                    break;
                case "sign":
                    if (fsa.getName().equals("id")) {
                        char[] signId = "&+-<>!#*/$@~/".toCharArray();
                        for (char id : signId) {
                            newSetForInput.add(String.valueOf(id));
                        }
                        newInputs.put(in, new HashSet<>(newSetForInput));
                        newSetForInput.clear();
                    } else if ((fsa.getName().equals("integer") || fsa.getName().equals("real"))) {
                        char[] signIntR = "+-".toCharArray();
                        for (char intR : signIntR) {
                            newSetForInput.add(String.valueOf(intR));
                        }
                        newInputs.put(in, new HashSet<>(newSetForInput));
                        newSetForInput.clear();
                    } else if (fsa.getName().equals("special")) {
                        char[] signSp = ".,:;!?(){}[]".toCharArray();
                        for (char sp : signSp) {
                            newSetForInput.add(String.valueOf(sp));
                        }
                        newInputs.put(in, new HashSet<>(newSetForInput));
                        newSetForInput.clear();
                    }
                    break;
            }
        }
        return newInputs;
    }

    public static String getType(FiniteStateAutomate fsa, char item) {
        if (fsa.getInputs() != null) {
            for (Map.Entry<String, Set<String>> inp : fsa.getInputs().entrySet()) {
                if (inp.getValue().contains(String.valueOf(item))) {
                    return inp.getKey();
                }
            }
        }
        return String.valueOf(item);
    }

    public static String findState(FiniteStateAutomate fsa, String currentState, char item) {
        if (currentState == null) return null;
        Map<String, Set<String>> stateInMatrix = fsa.getMatrix().get(currentState);
        if (stateInMatrix == null) return null;
        String typeInputs = getType(fsa, item);
        Set<String> nextState = stateInMatrix.get(typeInputs);
        if (nextState == null) return null;
        return nextState.stream().findFirst().orElse(null);
    }

    // <допуск автоматом, кол-во допускаемых символов>
    public static Pair<Boolean, Integer> max(FiniteStateAutomate fsa, String input, int skip) {
        Pair<Boolean, Integer> pair = Pair.createPair(false, 0);
        char[] charsOfInput = input.substring(skip).toCharArray();
        String currentState = fsa.getStart().stream().findFirst().get(); // q0
        int count = 0;
        for (char ch : charsOfInput) {
            currentState = findState(fsa, currentState, ch);
            if (currentState == null) {
                break;
            }
            count++;
            if (fsa.getFinish().contains(currentState)) {
                pair = Pair.createPair(true, count);
            }
        }
        return pair;
    }

    public static Pair<String, String> parse(String input, int skip) throws IOException {
        Integer priority = 0;
        Integer longValue = 0;
        String nameAutomate = null;
        String newInput = null;
        List<FiniteStateAutomate[]> finiteStateAutomates = CreateListFSA.create();
        for (FiniteStateAutomate[] automates : finiteStateAutomates) {
            for (FiniteStateAutomate fsa : automates) {
                if (fsa.getInputs() != null) {
                    fsa.setInputs(forCreateInputs(fsa)); // создание инпутов
                }
                Pair<Boolean, Integer> pairMax = max(fsa, input, skip);
                if (pairMax.getElement0()) {
                    if (pairMax.getElement1() > longValue || (pairMax.getElement1().equals(longValue) && fsa.getPriority() > priority)) {
                        nameAutomate = fsa.getName();
                        newInput = input.substring(skip, pairMax.getElement1() + skip);
                        longValue = pairMax.getElement1();
                        priority = fsa.getPriority();
                    }
                }
            }
        }
        return Pair.createPair(nameAutomate, newInput);
    }

    public static List<Pair<String, String>> createPairs(String str) throws IOException {
        // <название класса лексемы, сама лексема>
        List<Pair<String, String>> pairList = new ArrayList<>();
        for (int i = 0; i < str.length(); ) {
            Pair<String, String> pairParse = parse(str, i);
            pairList.add(pairParse);
            i += pairParse.getElement1().length();
        }
        return pairList;
    }
}
