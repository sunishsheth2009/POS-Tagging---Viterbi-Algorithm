/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package postagging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Sunish
 */
public class CreatingModel {

    public HashMap<String, HashMap<String, Double>> createTransistionCountMatrix(ArrayList<String> tags) {

        HashMap<String, HashMap<String, Double>> stateTrasitionCount = new HashMap<String, HashMap<String, Double>>();

        for (int j = 0; j < tags.size(); j++) {

            String tagsSplit[] = tags.get(j).split(Constants.SPACES);
            for (int i = 0; i <= tagsSplit.length; i++) {

                String previousTag = "";
                String currentTag = "";
                if (i == 0) {
                    previousTag = Constants.START;
                    currentTag = tagsSplit[i];
                } else if (i == tagsSplit.length) {
                    previousTag = tagsSplit[i - 1];
                    currentTag = Constants.END;
                } else {
                    previousTag = tagsSplit[i - 1];
                    currentTag = tagsSplit[i];
                }

                if (stateTrasitionCount.containsKey(previousTag)) {
                    HashMap<String, Double> row = stateTrasitionCount.get(previousTag);
                    if (row.containsKey(currentTag)) {
                        double newValue = row.get(currentTag) + 1;
                        stateTrasitionCount.get(previousTag).put(currentTag, newValue);
                    } else {
                        row.put(currentTag, Constants.DOUBLEONE);
                        stateTrasitionCount.put(previousTag, row);
                    }
                } else {
                    HashMap<String, Double> row = new HashMap<String, Double>();
                    row.put(currentTag, Constants.DOUBLEONE);
                    stateTrasitionCount.put(previousTag, row);
                }

            }


        }
        return stateTrasitionCount;
    }

    public HashMap<String, HashMap<String, Double>> createObservationCount(ArrayList<String> tags, ArrayList<String> sentences) {

        HashMap<String, HashMap<String, Double>> observationTrasitionCount = new HashMap<String, HashMap<String, Double>>();

        for (int j = 0; j < tags.size(); j++) {
            String tagsSplit[] = tags.get(j).split(Constants.SPACES);
            String sentencesSplit[] = sentences.get(j).split(Constants.SPACES);

            if (tagsSplit.length != sentencesSplit.length) {
                System.out.print("Error in spliting");
                System.out.println(tags);
                System.out.println(sentences);
            }

            for (int i = 0; i < tagsSplit.length; i++) {

                String word = sentencesSplit[i];
                String tag = tagsSplit[i];

                if (observationTrasitionCount.containsKey(word)) {
                    HashMap<String, Double> row = observationTrasitionCount.get(word);
                    if (row.containsKey(tag)) {
                        double newValue = row.get(tag) + 1;
                        observationTrasitionCount.get(word).put(tag, newValue);
                    } else {
                        row.put(tag, Constants.DOUBLEONE);
                        observationTrasitionCount.put(word, row);
                    }
                } else {
                    HashMap<String, Double> row = new HashMap<String, Double>();
                    row.put(tag, Constants.DOUBLEONE);
                    observationTrasitionCount.put(word, row);
                }

            }
        }

        return observationTrasitionCount;

    }

    public HashMap<String, Double> createTagCountMatrix(ArrayList<String> tags) {

        HashMap<String, Double> tagCount = new HashMap<String, Double>();

        for (int j = 0; j < tags.size(); j++) {

            String tagsSplit[] = tags.get(j).split(Constants.SPACES);
            for (int i = 0; i < tagsSplit.length; i++) {
                if (tagCount.containsKey(tagsSplit[i])) {
                    double newValue = tagCount.get(tagsSplit[i]) + 1;
                    tagCount.put(tagsSplit[i], newValue);
                } else {
                    tagCount.put(tagsSplit[i], Constants.DOUBLEONE);
                }
            }
        }

        //Adding start and end tag count
        tagCount.put(Constants.START, (double) tags.size());
        tagCount.put(Constants.END, (double) tags.size());

        return tagCount;
    }

    public HashMap<String, HashMap<String, Double>> createTransistionProbabilityMatrix(ArrayList<String> tags) {

        CreatingModel cm = new CreatingModel();
        HashMap<String, Double> tagCount = cm.createTagCountMatrix(tags);
        HashMap<String, HashMap<String, Double>> stateTrasitionCount = cm.createTransistionCountMatrix(tags);
        HashMap<String, HashMap<String, Double>> stateTrasitionProbability = new HashMap<String, HashMap<String, Double>>();

        for (String previosTag : stateTrasitionCount.keySet()) {
            if (tagCount.containsKey(previosTag)) {
                double denominator = tagCount.get(previosTag);
                HashMap<String, Double> row = stateTrasitionCount.get(previosTag);

                for (String currentTag : row.keySet()) {
                    double newValue = row.get(currentTag) / denominator;
                    row.put(currentTag, newValue);
                }
                stateTrasitionProbability.put(previosTag, row);

            } else {
                System.out.println("Tag count HashMap incorrect");
            }
        }

        return stateTrasitionProbability;
    }

    public HashMap<String, HashMap<String, Double>> createObservationProbabilityMatrix(ArrayList<String> tags, ArrayList<String> sentences) {

        HashMap<String, HashMap<String, Double>> observationProbability = new HashMap<String, HashMap<String, Double>>();
        CreatingModel cm = new CreatingModel();
        HashMap<String, Double> tagCount = cm.createTagCountMatrix(tags);
        HashMap<String, HashMap<String, Double>> observationTrasitionCount = cm.createObservationCount(tags, sentences);

        for (String word : observationTrasitionCount.keySet()) {
            HashMap<String, Double> row = observationTrasitionCount.get(word);

            for (String tag : row.keySet()) {
                if (tagCount.containsKey(tag)) {
                    double denominator = tagCount.get(tag);
                    double newValue = row.get(tag) / denominator;
                    row.put(tag, newValue);
                } else {
                    System.out.println("Tag count HashMap incorrect");
                }
            }
            observationProbability.put(word, row);
        }

        return observationProbability;
    }

    public Set<String> createTagSet(ArrayList<String> tags) {
        CreatingModel cm = new CreatingModel();
        HashMap<String, Double> tagCount = cm.createTagCountMatrix(tags);
        Set<String> tagSet = new HashSet<String>();
        tagSet = tagCount.keySet();
        tagSet.remove(Constants.START);
        tagSet.remove(Constants.END);
        return tagSet;
    }
}
