package recognition;

import entity.FiniteStateAutomate;
import utils.Pair;

import java.util.*;

public class StateMachineOperation {

    public boolean checkNotNull(Set<String> str) {
        if (str != null) return true;
        return false;
    }

    public Map<String, Set<String>> forCreateInputs(FiniteStateAutomate fsa) {
        if (fsa.getInputs() == null) {
            return null;
        }
        Map<String, Set<String>> newInputs = new HashMap<>();
        Map<String, Set<String>> inputs = fsa.getInputs();
        Set<String> newSetForInput = new HashSet<>();
        List<String> listOfInputs = new ArrayList<>();

        if (checkNotNull(inputs.get("digit"))) {
            String digit_ = inputs.get("digit").stream().findFirst().get().replaceAll("[^0-9]", "");
            listOfInputs.add("digit");
            listOfInputs.add(digit_);
        }
        if (checkNotNull(inputs.get("exp"))) {
            String exp_ = inputs.get("exp").stream().findFirst().get().replaceAll("[^eE]", "");
            listOfInputs.add("exp");
            listOfInputs.add(exp_);
        }
        if (checkNotNull(inputs.get("ops"))) {
            String ops_ = inputs.get("ops").stream().findFirst().get().replaceAll("[^*/%]", "");
            listOfInputs.add("ops");
            listOfInputs.add(ops_);
        }
        if (checkNotNull(inputs.get("ops2"))) {
            String ops_ = inputs.get("ops2").stream().findFirst().get().replaceAll("[^&|+\\-]", "");
            listOfInputs.add("ops2");
            listOfInputs.add(ops_);
        }
        if (checkNotNull(inputs.get("whitespace"))) {
            String whitespace_ = inputs.get("whitespace").stream().findFirst().get().replaceAll("[x='| ]", "");
            listOfInputs.add("whitespace");
            listOfInputs.add(whitespace_);
        }
        if (checkNotNull(inputs.get("char"))) {
            String char_ = inputs.get("char").stream().findFirst().get().replaceAll("[^a-wy-zA-WY-Z]", "");
            listOfInputs.add("char");
            listOfInputs.add(char_);
        }
        String signForId_ = null, signForInteger_ = null, signForSpecial_ = null;
        if (fsa.getName().equals("id") && checkNotNull(inputs.get("sign"))) {
            signForId_ = inputs.get("sign").stream().findFirst().get().replaceAll("[^&+\\-<>!#*/$@~]", "");
        } else if ((fsa.getName().equals("integer") || fsa.getName().equals("real")) && (checkNotNull(inputs.get("sign")))) {
            signForInteger_ = inputs.get("sign").stream().findFirst().get().replaceAll("[^+\\-]", "");
        } else if (fsa.getName().equals("special") && checkNotNull(inputs.get("sign"))) {
            signForSpecial_ = inputs.get("sign").stream().findFirst().get().replaceAll("[^.,:;!?(){}\\[\\]]", "");
        }
        if (signForId_ != null) {
            listOfInputs.add("sign");
            listOfInputs.add(signForId_);
        }
        if (signForInteger_ != null) {
            listOfInputs.add("sign");
            listOfInputs.add(signForInteger_);
        }
        if (signForSpecial_ != null) {
            listOfInputs.add("sign");
            listOfInputs.add(signForSpecial_);
        }

        for (int i = 1; i < listOfInputs.size(); i += 2) {
            if (listOfInputs.get(i - 1).equals("digit")) {
                for (int j = Character.getNumericValue(listOfInputs.get(i).toCharArray()[0]);
                     j <= Character.getNumericValue(listOfInputs.get(i).toCharArray()[listOfInputs.get(i).toCharArray().length - 1]); j++) {
                    newSetForInput.add(String.valueOf(j));
                }
                newInputs.put(listOfInputs.get(i - 1), new HashSet<>(newSetForInput));
                newSetForInput.clear();
            } else if (listOfInputs.get(i - 1).equals("char")) {
                char[] chars = listOfInputs.get(i).toCharArray();
                for (int k = 0; k < chars.length; k += 2) {
                    for (char ch = chars[k]; ch <= chars[k + 1]; ch++) {
                        newSetForInput.add(String.valueOf(ch));
                    }
                }
                newInputs.put(listOfInputs.get(i - 1), new HashSet<>(newSetForInput));
                newSetForInput.clear();
            } else if (listOfInputs.get(i - 1).equals("whitespace")) {
                newSetForInput.add("\n");
                newSetForInput.add("\r");
                newSetForInput.add("\t");
                newSetForInput.add(" ");
                newInputs.put(listOfInputs.get(i - 1), new HashSet<>(newSetForInput));
                newSetForInput.clear();
            } else {
                for (char ch : listOfInputs.get(i).toCharArray()) {
                    newSetForInput.add(String.valueOf(ch));
                }
                newInputs.put(listOfInputs.get(i - 1), new HashSet<>(newSetForInput));
                newSetForInput.clear();
            }
        }
        return newInputs;
    }

    public Pair<Boolean, Integer> max(FiniteStateAutomate fsa, String input, int skip) {
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

    public String findState(FiniteStateAutomate fsa, String currentState, char item) {
        if (currentState == null) return null;
        Map<String, Set<String>> stateInMatrix = fsa.getMatrix().get(currentState);
        if (stateInMatrix == null) return null;
        String typeInputs = getType(fsa, item);
        Set<String> nextState = stateInMatrix.get(typeInputs);
        if (nextState == null) return null;
        return nextState.stream().findFirst().orElse(null);
    }

    public String getType(FiniteStateAutomate fsa, char item) {
        if (fsa.getInputs() != null) {
            for (Map.Entry<String, Set<String>> inp : fsa.getInputs().entrySet()) {
                if (inp.getValue().contains(String.valueOf(item))) {
                    return inp.getKey();
                }
            }
        }
        return String.valueOf(item);
    }
}
