/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package postagging;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Sunish
 */
public class POSTagging {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        //Reading Data

        // Train Data
        TextIO text = new TextIO();
        File inputTrainSetenceFileName = new File("brown-train-sentences.txt");
        File inputTrainTagsFileName = new File("brown-train-tags.txt");
        File inputTestSetenceFileName = new File("brown-test-sentences.txt");
        File inputTestTagsFileName = new File("brown-test-tags.txt");

        ArrayList<String> tags = text.read(inputTrainTagsFileName);
        ArrayList<String> sentences = text.read(inputTrainSetenceFileName);
        // Test Data
        ArrayList<String> testTags = text.read(inputTestTagsFileName);
        ArrayList<String> testSentence = text.read(inputTestSetenceFileName);

//        for (String tag : tags) {
//            System.out.println(tag);
//        }
//
//        for (String sentence : sentences) {
//            System.out.println(sentence);
//        }

        // Training Part -- BiGram
        CreatingModel cm = new CreatingModel();
        HashMap<String, HashMap<String, Double>> stateTrasitionProbability = cm.createTransistionProbabilityMatrix(tags);
        HashMap<String, HashMap<String, Double>> observationProbability = cm.createObservationProbabilityMatrix(tags, sentences);
        Set<String> tagSet = cm.createTagSet(tags);

//        for(String data1 : observationProbability.keySet()){
//            HashMap<String, Double> row = observationProbability.get(data1);
//            for(String data2 : row.keySet()){
//                System.out.println(data1 + " --> " + data2 + " --> " + row.get(data2));
//            }
//        }

        // Testing Part -- Viterbi
        Viterbi v = new Viterbi();
        ArrayList<String> calculatedTags = v.ViterbiImp(testSentence, stateTrasitionProbability, observationProbability, tagSet);

        // Checking accuracy : Compare testTags and calculatedTags
//        for (String cTags : calculatedTags) {
//            System.out.println("C-Tags : " + cTags);
//        }
//        for (String tTags : testTags) {
//            System.out.println("T-Tags : " + tTags);
//        }

        AccuracyTesting at = new AccuracyTesting();
        at.calculateAccuracy(testTags, calculatedTags);
        double totalTag = (double) at.getTotalTag();
        double correctTags = (double) at.getCorrectTag();
        double inCorrectTags = (double) at.getInCorrectTag();
        HashMap<String, HashMap<String, Integer>> confusionMatrix = at.getConfusionMatrix();

        PrintData pd = new PrintData();
        pd.printTable(confusionMatrix);
        
        System.out.println("Total Tags : " + totalTag);
        System.out.println("Correct Tags : " + correctTags);
        System.out.println("InCorrect Tags : " + inCorrectTags);
        double accuracyRate = correctTags / totalTag * 100;
        System.out.println("Accuracy Rate : " + accuracyRate + "%");
        
        

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a sentence or type \"EXIT\" to Exit: ");
        String input  = sc.nextLine();
        
        while (!input.equals("EXIT")) {
            //Process
            ArrayList<String> inputData = new ArrayList<String>();
            inputData.add(input.toLowerCase());
            ArrayList<String> sentenceTags = v.ViterbiImp(inputData, stateTrasitionProbability, observationProbability, tagSet);
            for(String opTags: sentenceTags){
                System.out.print(opTags + " ");
            }
            System.out.println("");
            //attorneys for the mayor said that an amicable property settlement has been agreed upon .
            System.out.println("Enter a sentence or type \"EXIT\" to Exit: ");
            input  = sc.nextLine();
        }



    }
}
