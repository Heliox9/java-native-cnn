package lab.cnn.custom.network.layers;

import lab.cnn.custom.filehandlers.image.ImageData;

/**
 * abstract base for all layers that modify the data in any way
 */
public abstract class ModifyingLayer extends Layer {
    public ModifyingLayer(Object[] config) {
        super(config);
    }

    public abstract ImageData modify(ImageData image);


}
