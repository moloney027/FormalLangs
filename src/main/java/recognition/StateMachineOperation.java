package recognition;

import entity.FiniteStateAutomate;
import init.CreateListFSA;
import utils.Pair;

import java.io.IOException;
import java.util.*;

public class StateMachineOperation {

    public static Map<String, Set<String>> forCreateInputs(FiniteStateAutomate fsa) {
        if (fsa.getInputs() == null) { return null; }
        Map<String, Set<String>> inputs = fsa.getInputs();
        Map<String, Set<String>> newInputs = new HashMap<>();
        Set<String> newSetForInput = new HashSet<>();
        List<String> listOfInputs = new ArrayList<>();

        if (inputs.get("digit") != null) {
            Optional<String> digit = inputs.get("digit").stream().findFirst();
            if (digit.isPresent()) {
                String digit_ = digit.get().replaceAll("[^0-9]", "");
                listOfInputs.add("digit");
                listOfInputs.add(digit_);
            }
        }
        if (inputs.get("exp") != null) {
            Optional<String> exp = inputs.get("exp").stream().findFirst();
            if (exp.isPresent()) {
                String exp_ = exp.get().replaceAll("[^eE]", "");
                listOfInputs.add("exp");
                listOfInputs.add(exp_);
            }
        }
        if (inputs.get("ops") != null) {
            Optional<String> ops = inputs.get("ops").stream().findFirst();
            if (ops.isPresent()) {
                String ops_ = ops.get().replaceAll("[^*/%]", "");
                listOfInputs.add("ops");
                listOfInputs.add(ops_);
            }
        }
        if (inputs.get("ops2") != null) {
            Optional<String> ops2 = inputs.get("ops2").stream().findFirst();
            if (ops2.isPresent()) {
                String ops_ = ops2.get().replaceAll("[^&|+\\-]", "");
                listOfInputs.add("ops2");
                listOfInputs.add(ops_);
            }
        }
        if (inputs.get("whitespace") != null) {
            Optional<String> whitespace = inputs.get("whitespace").stream().findFirst();
            if (whitespace.isPresent()) {
                String whitespace_ = whitespace.get().replaceAll("[x='| ]", "");
                listOfInputs.add("whitespace");
                listOfInputs.add(whitespace_);
            }
        }
        if (inputs.get("char") != null) {
            Optional<String> aChar = inputs.get("char").stream().findFirst();
            if (aChar.isPresent()) {
                String char_ = aChar.get().replaceAll("[^a-wy-zA-WY-Z]", "");
                listOfInputs.add("char");
                listOfInputs.add(char_);
            }
        }
        if (fsa.getName().equals("id") && (inputs.get("sign") != null)) {
            Optional<String> sign = inputs.get("sign").stream().findFirst();
            if (sign.isPresent()) {
                String signForId_ = sign.get().replaceAll("[^&+\\-<>!#*/$@~]", "");
                listOfInputs.add("sign");
                listOfInputs.add(signForId_);
            }
        } else if ((fsa.getName().equals("integer") || fsa.getName().equals("real")) && (inputs.get("sign") != null)) {
            Optional<String> sign = inputs.get("sign").stream().findFirst();
            if (sign.isPresent()) {
                String signForInteger_ = sign.get().replaceAll("[^+\\-]", "");
                listOfInputs.add("sign");
                listOfInputs.add(signForInteger_);
            }
        } else if (fsa.getName().equals("special") && (inputs.get("sign") != null)) {
            Optional<String> sign = inputs.get("sign").stream().findFirst();
            if (sign.isPresent()) {
                String signForSpecial_ = sign.get().replaceAll("[^.,:;!?(){}\\[\\]]", "");
                listOfInputs.add("sign");
                listOfInputs.add(signForSpecial_);
            }
        }

        for (int i = 1; i < listOfInputs.size(); i += 2) {
            switch (listOfInputs.get(i - 1)) {
                case "digit":
                    for (int j = Character.getNumericValue(listOfInputs.get(i).toCharArray()[0]);
                         j <= Character.getNumericValue(listOfInputs.get(i).toCharArray()[listOfInputs.get(i).toCharArray().length - 1]); j++) {
                        newSetForInput.add(String.valueOf(j));
                    }
                    newInputs.put(listOfInputs.get(i - 1), new HashSet<>(newSetForInput));
                    newSetForInput.clear();
                    break;
                case "char":
                    char[] chars = listOfInputs.get(i).toCharArray();
                    for (int k = 0; k < chars.length; k += 2) {
                        for (char ch = chars[k]; ch <= chars[k + 1]; ch++) {
                            newSetForInput.add(String.valueOf(ch));
                        }
                    }
                    newInputs.put(listOfInputs.get(i - 1), new HashSet<>(newSetForInput));
                    newSetForInput.clear();
                    break;
                case "whitespace":
                    newSetForInput.add("\n");
                    newSetForInput.add("\r");
                    newSetForInput.add("\t");
                    newSetForInput.add(" ");
                    newInputs.put(listOfInputs.get(i - 1), new HashSet<>(newSetForInput));
                    newSetForInput.clear();
                    break;
                default:
                    for (char ch : listOfInputs.get(i).toCharArray()) {
                        newSetForInput.add(String.valueOf(ch));
                    }
                    newInputs.put(listOfInputs.get(i - 1), new HashSet<>(newSetForInput));
                    newSetForInput.clear();
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
        Pair<Boolean, Integer> pair;
        char[] charsOfInput = input.substring(skip).toCharArray();
        String currentState = fsa.getStart().stream().findFirst().get(); // q0
        String previousState;
        int count = 0;
        for (char ch : charsOfInput) {
            previousState = currentState;
            currentState = findState(fsa, currentState, ch);
            if (currentState == null) {
                currentState = previousState;
                break;
            }
            count++;
        }
        if (count != 0 && fsa.getFinish().contains(currentState)) {
            pair = Pair.createPair(true, count);
        } else {
            pair = Pair.createPair(false, 0);
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
                    if (pairMax.getElement1() > longValue) {
                        nameAutomate = fsa.getName();
                        newInput = input.substring(skip, pairMax.getElement1() + skip);
                        longValue = pairMax.getElement1();
                        priority = fsa.getPriority();
                    } else if (pairMax.getElement1().equals(longValue) && fsa.getPriority() > priority) {
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
