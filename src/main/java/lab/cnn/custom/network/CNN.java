package lab.cnn.custom.network;

import lab.cnn.custom.filehandlers.CsvHandler;
import lab.cnn.custom.filehandlers.image.ImageData;
import lab.cnn.custom.filehandlers.image.ImageHandler;
import lab.cnn.custom.network.layers.Layer;
import lab.cnn.custom.network.layers.ModifyingLayer;
import lab.cnn.custom.network.layers.OutputLayer;
import lab.controlBase.commandlineInterface.LoggingBase;
import lab.controlBase.commandlineInterface.ProductiveOutput;
import lab.controlBase.commandlineInterface.TrainingOutput;
import lab.controlBase.commandlineInterface.questions.*;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

/**
 * Models a full CNN
 * future functions:
 * run
 * train
 * prod
 * save
 * load
 */
public class CNN implements Serializable {
    protected String name;
    protected Enums.OperatingMode mode;
    protected ArrayList<Layer> layers;
    protected ImageHandler imageHandler;
    protected int numOfSamples;
    protected int possibleCounter;
    protected int milestonePossibleCounter;
    protected int totalCounter;
    protected int milestoneTotalCounter;
    protected int milestoneCorrectCounter;
    protected int correctCounter;
    protected int countSinceLastCorrect;
    protected int milestoneCounter;
    protected double learningRate;


    public CNN(String name, ImageHandler imageHandler) {
        this.name = name;
        layers = new ArrayList<>();
        this.imageHandler = imageHandler;
        learningRate = 0.8;
    }

    public void setMode(Enums.OperatingMode mode) {
        this.mode = mode;
    }

    public String getName() {
        return name;
    }

    public void addLayer(Layer layer) {
        layers.add(layer);
    }

    /**
     * starts the workflow with a selection of general options and forwards to executions for different modes
     *
     * @throws Exception unkown mode is entered
     */
    public void start() throws Exception {


        if (mode == Enums.OperatingMode.PRODUCTIVE) {
            prod();
        } else if (mode == Enums.OperatingMode.TRAINING) {
            train();
        } else if (mode == Enums.OperatingMode.TEST) {
            test();
        } else {
            LoggingBase.log.error("Unknown Mode: " + mode);
        }


    }

    @Override
    public String toString() {
        return LoggingBase.solidHLine + "\n" + LoggingBase.hLine + "\nCNN{" +
                "name='" + name + '\'' +
                ", layers=" + layers +
                "\n, possibleCounter=" + possibleCounter +
                ", totalCounter=" + totalCounter +
                ", correctCounter=" + correctCounter +
                "}\n" + LoggingBase.hLine + "\n" + LoggingBase.solidHLine;
    }

    /**
     * executes a training run of the network
     *
     * @throws Exception
     */
    private void train() throws Exception {


        // get sample files by questions
        Object[] answers = trainQuestionaire();

        // LoggingBase.log.debug(toString());

        numOfSamples = (int) answers[2];
        ArrayList<String> samples = getSubfiles((File) answers[0], numOfSamples);


        Map<String, Integer> mapping = CsvHandler.handle((File) answers[1]);

        Map<Integer, Double> out;
        TrainingOutput output = new TrainingOutput();
        for (String s : samples) {
            // read and run image
            ImageData imageData = imageHandler.handle(s);
            out = runSingle(imageData);

            // adjust network based on mapping file
            String filename = s.substring(s.lastIndexOf("/") + 1);
            filename = filename.substring(filename.lastIndexOf("\\") + 1);

            // LoggingBase.log.trace(filename);

            int actual = mapping.get(filename);

            // log output
            output.clear();
            output.setImageName(s.substring(s.lastIndexOf("/") + 1));
            output.putAllProbabilities(out);
            output.setActual(actual);
            output.print();

            adjustNetwork(actual, out);
        }

        // LoggingBase.log.debug(toString());

        logStats("FINAL");

    }

    /**
     * prints formatted milestone data
     *
     * @param name for the milestone
     */
    private void logStats(String name) {
        //     LoggingBase.printSolidHLine();
        LoggingBase.log.info("Milestone: " + name);

        if (numOfSamples > 1000 && totalCounter > 1000 && numOfSamples > totalCounter) {
            double completion = (((double) totalCounter) / ((double) numOfSamples)) * 100.0;
            LoggingBase.log.info("Completion: " + completion + "%");
        }

        LoggingBase.log.info("");//empty line for better seperation

        if (milestoneTotalCounter == 0) {
            // view total values for final milestone
            milestoneTotalCounter = totalCounter;
            milestonePossibleCounter = possibleCounter;
            milestoneCorrectCounter = correctCounter;
        }

        double possibleAccuracy = Math.floor((double) milestonePossibleCounter / milestoneTotalCounter * 100);
        double accuracy = Math.floor((double) milestoneCorrectCounter / milestoneTotalCounter * 100);
        LoggingBase.log.info("Correct: " + correctCounter + " | Possible: " + possibleCounter);
        LoggingBase.log.info("Total: " + totalCounter);


        LoggingBase.printHLine();

        LoggingBase.log.info("Accuracy: " + accuracy + "% (max possible: " + possibleAccuracy + "%)");

        LoggingBase.printSolidHLine();

        milestoneCorrectCounter = 0;
        milestonePossibleCounter = 0;
        milestoneTotalCounter = 0;

        // reset milestone counter if name is not a number (i.e. FINAL)
        try {
            Integer.parseInt(name);
        } catch (Exception e) {
            LoggingBase.log.info("resetting counters");
            milestoneCounter = 0;
            totalCounter = 0;
            milestoneCounter = 0;
            milestoneCorrectCounter = 0;
            countSinceLastCorrect = 0;
            milestonePossibleCounter = 0;
            milestoneTotalCounter = 0;
        }
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    /**
     * adjusts the network in order to learn
     *
     * @param actual    the correct mapping for the image
     * @param predicted the outputs that the network predicted
     */
    private void adjustNetwork(int actual, Map<Integer, Double> predicted) {
        //calc errors output nodes
        int counter = 0;
        Map.Entry<Integer, Double> prediction = null;
        Node[] outNodes = layers.get(layers.size() - 1).getNodes();

        // iterate predicted values for error adjustment
        for (Map.Entry<Integer, Double> e : predicted.entrySet()) {
            if (e.getKey() == actual) {
                possibleCounter++;
                milestonePossibleCounter++;
                outNodes[counter].calculateError(1);
            } else {
                outNodes[counter].calculateError(0);
            }

            // update output nodes
            outNodes[counter].updateWeights(learningRate, layers.get(layers.size() - 2));

            // counter to set accuracy
            if (prediction == null || prediction.getValue() < e.getValue()) {
                prediction = e;
            }
            counter++;
        }

        totalCounter++;
        milestoneTotalCounter++;
        // count correct predictions
        if (prediction.getKey() == actual) {
            // LoggingBase.log.trace("CORRECT");
            // LoggingBase.log.trace("Since last correct: " + countSinceLastCorrect + "\nProbability " + prediction.getValue() + " for " + prediction.getKey());
            correctCounter++;
            milestoneCorrectCounter++;
            countSinceLastCorrect = 0;
        } else {
            countSinceLastCorrect++;
        }

        if (totalCounter % 1000 == 0) {
            milestoneCounter++;
            logStats(milestoneCounter + "");
        }

        // calc errors convolution nodes
        Layer currLayer;
        Layer followLayer;
        Layer previousLayer;
        Node currNode;
        Node followNode;

        // parse all conv layers
        for (int i = layers.size() - 2; i > 0; i--) {
            currLayer = layers.get(i);
            followLayer = layers.get(i + 1);
            previousLayer = layers.get(i - 1);

            // parse each node in current layer
            for (int j = 0; j < currLayer.getNumOfNodes() - 1; j++) {
                currNode = currLayer.getNodes()[j];

                currNode.resetError();

                // adjust error of node depending on all nodes in following layer
                for (int k = 0; k < followLayer.getNumOfNodes() - 1; k++) {
                    followNode = followLayer.getNodes()[k];
                    currNode.calculateError(followNode.getError() * followNode.getWeight(j));
                }

                //update convolution nodes
                currNode.updateWeights(learningRate, previousLayer);
            }
        }
    }

    /**
     * generates a random set of files within a folder
     *
     * @param folder folder to find subfiles in
     * @param number number of files
     * @return List of absolute paths to the files
     * @throws Exception when a negative number is given
     */
    private ArrayList<String> getSubfiles(File folder, int number) throws Exception {
        return getSubfiles(folder, number, true);
    }

    /**
     * generates a random set of files within a folder
     *
     * @param folder     folder to find subfiles in
     * @param number     number of files
     * @param duplicates defines whether duplicates are allowed
     * @return List of absolute paths to the files
     * @throws Exception when a negative number is given
     */
    private ArrayList<String> getSubfiles(File folder, int number, boolean duplicates) throws Exception {
        ArrayList<String> files = new ArrayList<>();
        String folderPath = folder.getAbsolutePath();

        // get all png files in folder
        ArrayList<String> allFiles = new ArrayList<>();
        allFiles.addAll(
                Arrays.asList(
                        folder.list(
                                (File dir, String name) ->
                                        name.toLowerCase().endsWith(".png")
                        )
                )
        );

        //read all pictures when 0 entered
        if (number == 0 || !duplicates) {
            String f;
            int randomIndex;
            int counter = 0;
            while (allFiles.size() > 0 && (number == 0 || number > counter)) {
                randomIndex = (int) Math.random() * allFiles.size();
                f = allFiles.get(randomIndex);
                allFiles.remove(randomIndex);
                counter++;

                File file = new File(folderPath + "/" + f);
                if (file.isFile()) {
                    files.add(file.getAbsolutePath());
                }
            }
        } else if (number < 0) {
            throw new Exception("Negative number of files not legal: " + number);
        } else {
            //generate random samples
            for (int i = 0; i < number; i++) {
                //add a random file
                File file = new File(folderPath + "/"
                        + allFiles.get(
                        new Random().nextInt(allFiles.size())
                ));
                if (file.isFile()) {
                    files.add(file.getAbsolutePath());
                }
            }
        }

        return files;
    }

    /**
     * initializes the questionaire needed for a training run of the lab.custom.cnn
     *
     * @return mapping: 0: FolderQuestion
     * 1: IntegerQuestion
     * 2: FileQuestion
     */
    private Object[] trainQuestionaire() {
        Questionaire questionaire = new Questionaire("Training parameters");
        questionaire.addQuestion(new FolderQuestion("Path to train folder: "));
        questionaire.addQuestion(new FileQuestion("Path to mapping file: "));
        questionaire.addQuestion(new IntegerQuestion("Number of samples (0=all available): "));

        return questionaire.ask();
    }

    /**
     * initializes the questionaire needed for a training run of the lab.custom.cnn
     *
     * @return mapping: 0: FolderQuestion
     * 1: IntegerQuestion
     * 2: FileQuestion
     */
    private Object[] testQuestionaire() {
        Questionaire questionaire = new Questionaire("Testing parameters");
        questionaire.addQuestion(new FolderQuestion("Path to test folder: "));
        questionaire.addQuestion(new FileQuestion("Path to mapping file: "));
        questionaire.addQuestion(new IntegerQuestion("Number of samples (0=all available): "));

        return questionaire.ask();
    }

    /**
     * executes productive run of the lab.custom.cnn
     */
    protected void prod() {
        // execute questionnaire
        Object[] answers = prodQuestionaire();

        // read and run image
        ImageData image = (ImageData) answers[0];
        Map<Integer, Double> out = runSingle(image);

        int value = -42;
        double chance = -42;

        // find predicted output
        for (Map.Entry<Integer, Double> e : out.entrySet()) {
            if (e.getValue() > chance) {
                value = e.getKey();
                chance = e.getValue();
            }
        }

        // create and print output
        ProductiveOutput output = new ProductiveOutput(value, chance);
        output.print();
    }

    /**
     * executes productive run of the lab.custom.cnn
     *
     * @throws Exception forwarded from filepicker
     */
    protected void test() throws Exception {

        // get sample files by questions
        Object[] answers = testQuestionaire();

        // LoggingBase.log.debug(toString());

        ArrayList<String> samples = getSubfiles((File) answers[0], (int) answers[2], false);

        Map<String, Integer> mapping = CsvHandler.handle((File) answers[1]);

        //resetCounters
        correctCounter = 0;
        milestoneCorrectCounter = 0;
        possibleCounter = 0;
        milestonePossibleCounter = 0;
        totalCounter = 0;
        milestoneTotalCounter = 0;

        ArrayList<Integer> impossible = new ArrayList<>();

        // run samples
        Map<Integer, Double> out;
        for (String s : samples) {
            // read and run image
            ImageData image = imageHandler.handle(s);
            out = runSingle(image);
            totalCounter++;
            milestoneTotalCounter++;

            // adjust network based on mapping file
            String filename = s.substring(s.lastIndexOf("/") + 1);
            filename = filename.substring(filename.lastIndexOf("\\") + 1);

            // LoggingBase.log.trace(filename);

            int actual = mapping.get(filename);

            int value = -42;
            double chance = -42;

            // logging
            boolean found = false;
            for (Map.Entry<Integer, Double> e : out.entrySet()) {
                if (e.getValue() > chance) {
                    value = e.getKey();
                    chance = e.getValue();
                }

                if (e.getKey() == actual) {
                    possibleCounter++;
                    milestonePossibleCounter++;
                    found = true;
                }
            }

            // adjust counter
            if (found == false) {
                impossible.add(actual);
            }

            // LoggingBase.log.trace(value + " : " + chance);

            if (value == actual) {
                correctCounter++;
                milestoneCorrectCounter++;
            }
        }

        LoggingBase.log.info("Impossible values: \n" + impossible);
        //log final state
        logStats("FINAL");

    }

    /**
     * executes forwardpropogation for a single file
     *
     * @param image mapped data of a file
     * @return mapping of probabilities for all output nodes
     */
    private Map<Integer, Double> runSingle(ImageData image) {
        for (int i = 0; i < layers.size() - 1; i++) {
            image = ((ModifyingLayer) layers.get(i)).modify(image);
        }

        return ((OutputLayer) layers.get(layers.size() - 1)).interpret(image);
    }


    /**
     * initializes the questionaire needed for a productive run of the lab.custom.cnn
     *
     * @return mapping: 0: Imagequestion
     */
    private Object[] prodQuestionaire() {
        Questionaire questionaire = new Questionaire("Productive parameters");
        questionaire.addQuestion(new ImageQuestion("Path to test image: ", imageHandler));

        return questionaire.ask();
    }
}
