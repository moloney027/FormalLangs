package entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Map;
import java.util.Set;

@JsonAutoDetect
public class FiniteStateAutomate {

    private Set<String> start;
    private Set<String> finish;
    private Map<String, Set<String>> inputs;
    private Map<String, Map<String, Set<String>>> matrix;

    public FiniteStateAutomate() {
    }

    public Set<String> getStart() {
        return start;
    }

    public void setStart(Set<String> start) {
        this.start = start;
    }

    public Set<String> getFinish() {
        return finish;
    }

    public void setFinish(Set<String> finish) {
        this.finish = finish;
    }

    public Map<String, Set<String>> getInputs() {
        return inputs;
    }

    public void setInputs(Map<String, Set<String>> inputs) {
        this.inputs = inputs;
    }

    public Map<String, Map<String, Set<String>>> getMatrix() {
        return matrix;
    }

    public void setMatrix(Map<String, Map<String, Set<String>>> matrix) {
        this.matrix = matrix;
    }
}
