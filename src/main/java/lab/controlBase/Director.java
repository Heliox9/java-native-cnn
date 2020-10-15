package lab.controlBase;

import java.util.ArrayList;

/**
 * abstract base for objects that direct constrcution
 */
public abstract class Director {
    protected ArrayList<Builder> builders;

    /**
     * constructs the buildable Object
     *
     * @return finished Object
     */
    public abstract Object construct();

    /**
     * add a Builder to the construction workflow
     * creates capsule object if no flow is initialized
     *
     * @param b builder to add
     */
    public void addBuilder(Builder b) {
        if (builders == null) {
            builders = new ArrayList<>();
        }

        if (b != null) {
            builders.add(b);
        }
    }
}
