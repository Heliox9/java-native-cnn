package lab;

import lab.controlBase.commandlineInterface.LoggingBase;
import lab.controlBase.commandlineInterface.questions.ChooseQuestion;
import lab.controlBase.commandlineInterface.questions.Questionaire;
import lab.cnn.custom.network.CNN;
import lab.cnn.custom.network.Enums;
import lab.cnn.custom.processControl.CnnDirector;
import lab.cnn.dl4j.CnnDl4j;
import lab.cnn.dl4j.CnnDl4jDirector;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;

import java.io.*;

/**
 * Root class for the application
 * handles the workflow of the application
 */
public class Controller {
    /**
     * Entry point for wizard execution
     * <p>
     * asks general questions and creates the corresponding director to create or load a network
     *
     * @param args not used
     */
    public static void main(String[] args) {
        // init execution instance
        Controller controller = new Controller();

        LoggingBase.log.info("Welcome to the CNN-Wizard");

        // choose cnn implementation
        String networkChoice = controller.pickBackend();
        if (networkChoice.contains("custom")) {
            controller.customFlow();
        } else {
            controller.dl4jFlow();
        }
    }

    /**
     * workflow entry point for dl4j implementation
     */
    private void dl4jFlow() {
        CnnDl4jDirector director = new CnnDl4jDirector();
        CnnDl4j networkFrame;
        MultiLayerNetwork network;

        // read user inputs
        Object[] answers = createCustomQuestionnaire().ask();

        // create(configure) or load network
        if (answers[0].equals("yes")) {
            // create
            networkFrame = director.construct();
            network = director.finishConstructingNetwork();

            // confirmation dialog and store
            if (storeCnnQuestion()) {
                serializeDl4j(networkFrame, network);
            }
        } else {
            // load
            networkFrame = (CnnDl4j) load("dl4j");
            network = loadDl4jNetwork(networkFrame.getName());
        }


        // set chosen mode
        Enums.OperatingMode mode = (Enums.OperatingMode) Enums.get(getOperatingMode());
        do {
            // start with mode
            networkFrame.start(mode, network);

            // confirmation dialog and store
            if (mode == Enums.OperatingMode.TRAINING && storeCnnQuestion()) {
                serializeDl4j(networkFrame, network);
            }

            //add continue option
            mode = nextMode();
        } while (mode != null);
    }


    /**
     * Loads a Network from a file and rebuilds it
     *
     * @param typ specifies folder to load from and class to build
     * @return loaded Network
     */
    public Serializable load(String typ) {
        Serializable cnn;

        do {
            String folderDir = "networks/" + typ;
            File folder = new File(folderDir);

            // get all serialized files in defined folder
            String[] files = folder.list((file, name) -> {
                if (!file.exists()) return false;

                return name.endsWith(".ser");
            });

            // user input to pick serialized file
            String name = new ChooseQuestion("Choose a file to load", files).ask();

            // construct full path
            String location = folderDir + "/" + name;

            try {
                // build input stream
                FileInputStream fileIn = new FileInputStream(new File(location));
                ObjectInputStream in = new ObjectInputStream(fileIn);

                // read object from stream
                cnn = (Serializable) in.readObject();

                // close used streams
                in.close();
                fileIn.close();
            } catch (IOException i) {
                i.printStackTrace();
                // rerun loop
                cnn = null;
            } catch (ClassNotFoundException c) {
                c.printStackTrace();
                // cancel execution
                return null;
            }
        } while (cnn == null);

        return cnn;
    }

    /**
     * workflow entry point for custom implementation
     */
    private void customFlow() {
        CnnDirector director = new CnnDirector();
        CNN cnn;

        // get user input for creation
        Object[] answers = createCustomQuestionnaire().ask();

        // create or load
        if (answers[0].equals("yes")) {
            cnn = director.construct();
            // confirmation dialog and store
            if (storeCnnQuestion()) {
                serializeCustom(cnn);
            }
        } else {
            cnn = (CNN) load("custom");
        }

        // set chosen mode
        Enums.OperatingMode mode = (Enums.OperatingMode) Enums.get(getOperatingMode());

        try {
            do {
                // start with mode
                cnn.setMode(mode);
                cnn.start();

                // confirmation dialog and store
                if (mode == Enums.OperatingMode.TRAINING && storeCnnQuestion()) {
                    serializeCustom(cnn);
                }

                //add continue option
                mode = nextMode();
            } while (mode != null);
        } catch (Exception e) {
            LoggingBase.log.error("Error occurred after starting cnn!");
            LoggingBase.log.error(e.getMessage());
        }
    }

    /**
     * mode selection cli
     *
     * @return selected mode
     */
    private Enums.OperatingMode nextMode() {

        // continue
        if (new ChooseQuestion("Do you want to continue with this CNN?", new String[]{"yes", "no"}).ask().equals("no")) {
            return null;
        }

        //mode select
        String mode = new ChooseQuestion("Choose a operating mode: ", Enums.OperatingMode.class).ask();

        //parse mode
        return (Enums.OperatingMode) Enums.get(mode);

    }

    /**
     * user input for storing networks
     *
     * @return true to store, false to ignore
     */
    private boolean storeCnnQuestion() {
        return new ChooseQuestion("Do you want to store this CNN?", new String[]{"yes", "no"}).ask().equals("yes");
    }

    /**
     * user input for implementation selection
     *
     * @return custom="custom", dl4j="dl4j (recommended for complex data)"
     */
    private String pickBackend() {
        return new ChooseQuestion("Which backend do you want to use?", new String[]{"custom", "dl4j (recommended for complex data)"}).ask();
    }

    /**
     * ask operation mode from enum
     *
     * @return enum as String
     */
    private String getOperatingMode() {
        return new ChooseQuestion("Choose a operating mode: ", Enums.OperatingMode.class).ask();
    }

    /**
     * user input for custom implementation creation choice
     *
     * @return filled questionnaire
     */
    private Questionaire createCustomQuestionnaire() {
        // config questionaire
        Questionaire quest = new Questionaire("Custom Wizard");

        quest.addQuestion(new ChooseQuestion("Do you want to create a new CNN?", new String[]{"yes", "load existing CNN"}));

        return quest;
    }

    /**
     * saving flow for dl4j implementation
     *
     * @param cnn     configured wrapping framework
     * @param network initialized cnn
     */
    private void serializeDl4j(CnnDl4j cnn, MultiLayerNetwork network) {
        String locationBase = "networks/dl4j/" + cnn.getName();
        File file = new File(locationBase + ".dl4j");

        // serialize network
        try {
            ModelSerializer.writeModel(network, file, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //serialize framework
        file = new File(locationBase + ".ser");
        serialize(file, cnn);
    }

    /**
     * loading workflow for dl4j implementation
     *
     * @param name the name of the network
     * @return instance of MultiLayerNetwork built from serialized file
     */
    private MultiLayerNetwork loadDl4jNetwork(String name) {
        // set path
        String locationBase = "networks/dl4j/" + name;
        // build file for location
        File file = new File(locationBase + ".dl4j");

        try {
            // load network using dl4j custom implementation
            return ModelSerializer.restoreMultiLayerNetwork(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * store any fully serializable object
     *
     * @param file the file to save to
     * @param cnn  the object to store
     */
    private void serialize(File file, Serializable cnn) {
        try {
            // build file if needed
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }

            // build stream to file location
            FileOutputStream fileOut =
                    new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            // write object to file
            out.writeObject(cnn);

            // close streams
            out.close();
            fileOut.close();

            LoggingBase.log.info("Serialized data is saved in " + file.getAbsolutePath());
        } catch (
                IOException i) {
            i.printStackTrace();
        }
    }

    /**
     * workflow to serialize custom implementation
     *
     * @param cnn instance to save
     */
    private void serializeCustom(CNN cnn) {
        // build file location
        String location = "networks/custom/" + cnn.getName() + ".ser";
        File file = new File(location);

        // save object
        serialize(file, cnn);
    }

}
