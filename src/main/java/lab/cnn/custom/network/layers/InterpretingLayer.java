package lab.cnn.custom.network.layers;

import lab.cnn.custom.filehandlers.image.ImageData;

import java.util.Map;

/**
 * abstract base for all layers that interpret data for output
 */
public abstract class InterpretingLayer extends Layer {

    public InterpretingLayer(Object[] config) {
        super(config);
    }

    abstract public Map<Integer, Double> interpret(ImageData imageData);
}
