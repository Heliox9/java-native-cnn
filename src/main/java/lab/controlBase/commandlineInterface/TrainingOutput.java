package lab.controlBase.commandlineInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * Basic output for a training cycle
 */
public class TrainingOutput extends Output {

    private Map<Integer, Double> probabilities;
    private String imageName;
    private int actual;

    public TrainingOutput() {
        clear();
    }

    public void setActual(int actual) {
        this.actual = actual;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Map<Integer, Double> getProbabilities() {
        return probabilities;
    }

    public void putAllProbabilities(Map<Integer, Double> probs) {
        probabilities.putAll(probs);
    }

    public void clear() {
        if (probabilities == null) {
            probabilities = new HashMap<>();
        } else {
            probabilities.clear();
        }
        imageName = "UNNAMED";
    }

    public void putProbability(int value, double probability) {
        probabilities.put(value, probability);
    }

    public void removeProbability(int value) {
        probabilities.remove(value);
    }

    @Override
    public void print() {
        Map.Entry<Integer, Double> largest = new Map.Entry<Integer, Double>() {
            @Override
            public Integer getKey() {
                return null;
            }

            @Override
            public Double getValue() {
                return (double) (-9999999);
            }

            @Override
            public Double setValue(Double value) {
                return null;
            }
        };
        printHLine();

        for (Map.Entry<Integer, Double> e : probabilities.entrySet()
        ) {
            if (e.getValue() > largest.getValue()) {
                largest = e;
            }

            log.trace(e.getKey() + ": " + (e.getValue() * 100) + "%");
        }
        // readable log
        log.debug("Prediction for " + imageName + " ------> " + largest.getKey() + " : " + (largest.getValue() * 100) + "% | Actual: " + actual);
    }
}
