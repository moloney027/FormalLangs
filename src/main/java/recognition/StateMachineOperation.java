package recognition;

import entity.FiniteStateAutomate;
import utils.Pair;

import java.util.*;

public class StateMachineOperation {

    public Map<String, Set<String>> forCreateInputs(FiniteStateAutomate fsa) {
        Map<String, Set<String>> newInputs = new HashMap<>();
        Map<String, Set<String>> inputs = fsa.getInputs();
        Set<String> newDigits = new HashSet<>();
        Set<String> newSigns = new HashSet<>();
        String digits = inputs.get("digit").stream().findFirst().get().replaceAll("[^0-9]", "");
        String signs = inputs.get("sign").stream().findFirst().get().replaceAll("[^+-]", "");
        char[] charsDigits = digits.toCharArray();
        char[] charsSigns = signs.toCharArray();
        for (int i = Character.getNumericValue(charsDigits[0]);
             i <= Character.getNumericValue(charsDigits[charsDigits.length - 1]); i++) {
            newDigits.add(String.valueOf(i));
        }
        for (char ch : charsSigns) {
            newSigns.add(String.valueOf(ch));
        }
        newInputs.put("digit", newDigits);
        newInputs.put("sign", newSigns);
        return newInputs;
    }

    public Pair<Boolean, Integer> max(FiniteStateAutomate fsa, String input, int skip) {
        Pair<Boolean, Integer> pair;
        char[] charsOfInput = input.substring(skip).toCharArray();
        String currentState = fsa.getStart().stream().findFirst().get(); // q0
        String previousState;
        int count = 0;
        for (char ch : charsOfInput) {
            boolean check = false;
            for (Set<String> valInp : fsa.getInputs().values()) {
                if (valInp.contains(String.valueOf(ch))) {
                    check = true;
                }
            }
            if (!check) break;
            previousState = currentState;
            currentState = findState(fsa, currentState, ch);
            if (currentState == null) {
                currentState = previousState;
                break;
            }
            count++;
        }

        if (count != 0 && Objects.equals(currentState, fsa.getFinish().stream().findFirst().get())) {
            pair = Pair.createPair(true, count);
        } else {
            pair = Pair.createPair(false, 0);
        }
        return pair;
    }

    public String findState(FiniteStateAutomate fsa, String currentState, char item) {
        Map<String, Set<String>> stateInMatrix = fsa.getMatrix().get(currentState);
        if (currentState == null) return null;
        String typeInputs = getType(fsa, item);
        Set<String> nextState = stateInMatrix.get(typeInputs);
        if (nextState == null) return null;
        return nextState.stream().findFirst().orElse(null);
    }

    public String getType(FiniteStateAutomate fsa, char item) {
        for (Map.Entry<String, Set<String>> inp : fsa.getInputs().entrySet()) {
            if (inp.getValue().contains(String.valueOf(item))) {
                return inp.getKey();
            }
        }
        return String.valueOf(item);
    }
}
