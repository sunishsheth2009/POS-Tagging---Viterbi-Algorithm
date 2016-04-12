/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package postagging;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Sunish
 */
public class AccuracyTesting {

    private int totalTag;
    private int correctTag;
    private int incorrectTag;
    private HashMap<String, HashMap<String, Integer>> confusionMatrix;

    AccuracyTesting() {
        totalTag = 0;
        correctTag = 0;
        incorrectTag = 0;
        confusionMatrix = new HashMap<String, HashMap<String, Integer>>();
    }

    public int getTotalTag() {
        return totalTag;
    }

    public int getCorrectTag() {
        return correctTag;
    }

    public int getInCorrectTag() {
        return incorrectTag;
    }

    public HashMap<String, HashMap<String, Integer>> getConfusionMatrix() {
        return confusionMatrix;
    }

    public void calculateAccuracy(ArrayList<String> tTags, ArrayList<String> cTags) {

        //tTag = Tags otained from testing
        //cTag = Tags found from viterbi model

        if (tTags.size() != cTags.size()) {
            System.out.println("Error in number of tags");
            return;
        }

        for (int i = 0; i < cTags.size(); i++) {
            String calculatedTag = cTags.get(i);
            String testTag = tTags.get(i);

            String cTag[] = calculatedTag.split(Constants.SPACES);
            String tTag[] = testTag.split(Constants.SPACES);

            if (cTag.length != tTag.length) {
                System.out.println("Error in tag length");
            }

            for (int j = 0; j < cTag.length; j++) {
                
                totalTag++;
                if (cTag[j].equals(tTag[j])) {
                    correctTag++;
                } else {
                    incorrectTag++;
                }
                
                if(confusionMatrix.containsKey(cTag[j])){
                    HashMap<String, Integer> row = confusionMatrix.get(cTag[j]);
                    if(row.containsKey(tTag[j])){
                        int newValue = row.get(tTag[j]) + 1;
                        confusionMatrix.get(cTag[j]).put(tTag[j], newValue);
                    }else{
                        row.put(tTag[j], 1);
                        confusionMatrix.put(cTag[j], row);
                    }
                }else{
                    HashMap<String, Integer> row = new HashMap<String, Integer>();
                    row.put(tTag[j], 1);
                    confusionMatrix.put(cTag[j], row);
                }
            }

        }

    }
}
