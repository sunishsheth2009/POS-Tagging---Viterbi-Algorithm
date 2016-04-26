/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package postagging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Sunish
 */
public class Viterbi {

    public ArrayList<String> ViterbiImp(ArrayList<String> testSentence, HashMap<String, HashMap<String, Double>> stateTrasitionProbability, HashMap<String, HashMap<String, Double>> observationProbability, Set<String> tagSet) {
        ArrayList<String> calculatedTags = new ArrayList<String>();

        HashMap<Integer, HashMap<String, NodeValue>> comp;

        for (String sentence : testSentence) {

            comp = new HashMap<Integer, HashMap<String, NodeValue>>();

            String observation[] = sentence.split(Constants.SPACES);

            for (int i = 0; i < observation.length; i++) {
                // Start
                if (i == 0) {
                    HashMap<String, NodeValue> row = new HashMap<String, NodeValue>();
                    for (String tag : tagSet) {

                        String previousTag = Constants.START;
                        String currentTag = tag;
                        String word = observation[i];
                        double nodeVal = 0;
                        if (!stateTrasitionProbability.get(previousTag).containsKey(currentTag)) {
                            if (!observationProbability.containsKey(word)) {
                                nodeVal = -100;
                            } else if (!observationProbability.get(word).containsKey(currentTag)) {
                                nodeVal = -100;
                            } else if (observationProbability.get(word).containsKey(currentTag)) {
                                nodeVal = -100;
                                //nodeVal = Math.log10(observationProbability.get(word).get(currentTag));
                            }
                        } else if (stateTrasitionProbability.get(previousTag).containsKey(currentTag)) {
                            if (!observationProbability.containsKey(word)) {
                                //nodeVal = 0;
                                nodeVal = Math.log10(stateTrasitionProbability.get(previousTag).get(currentTag)) + -10;
                            } else if (!observationProbability.get(word).containsKey(currentTag)) {
                                //nodeVal = 0;
                                nodeVal = Math.log10(stateTrasitionProbability.get(previousTag).get(currentTag)) + -10;
                            } else if (observationProbability.get(word).containsKey(currentTag)) {
                                nodeVal = Math.log10(stateTrasitionProbability.get(previousTag).get(currentTag)) + Math.log10(observationProbability.get(word).get(currentTag));
                                //System.out.println(tag + " --> "+nodeVal);
                            }
                        } else {
                            System.out.println("Missing Case");
                            System.out.println(currentTag + " " + word);
                            System.exit(1);
                        }
                        //System.out.println("Start " + currentTag);
                        NodeValue node = new NodeValue();
                        node.setNodeValue(nodeVal);
                        node.setBackTrackTag(Constants.START);
                        row.put(currentTag, node);
                    }
                    comp.put(i, row);
                } else {
                    String word = observation[i];
                    HashMap<String, NodeValue> row = new HashMap<String, NodeValue>();
                    for (String currentTag : tagSet) {
                        double maxValue = Double.NEGATIVE_INFINITY;
                        NodeValue currentNode = new NodeValue();
                        for (String previousTag : tagSet) {
                            HashMap<String, NodeValue> previousRow = comp.get(i - 1);
                            NodeValue previousNode = previousRow.get(previousTag);
                            double nodeVal = 0;
                            if (!stateTrasitionProbability.containsKey(previousTag)) {
                                System.out.println(previousTag);
                            }
                            if (!stateTrasitionProbability.get(previousTag).containsKey(currentTag)) {
                                if (!observationProbability.containsKey(word)) {
                                    nodeVal = -100;
                                    //nodeVal = previousNode.value;
                                } else if (!observationProbability.get(word).containsKey(currentTag)) {
                                    nodeVal = -100;
                                    //nodeVal = previousNode.value;
                                } else if (observationProbability.get(word).containsKey(currentTag)) {
                                    nodeVal = -100;
                                    //nodeVal = previousNode.value + Math.log10(observationProbability.get(word).get(currentTag));
                                }
                            } else if (stateTrasitionProbability.get(previousTag).containsKey(currentTag)) {
                                if (!observationProbability.containsKey(word)) {
                                    //nodeVal = 0;
                                    nodeVal = previousNode.value + Math.log10(stateTrasitionProbability.get(previousTag).get(currentTag)) + -10;
                                } else if (!observationProbability.get(word).containsKey(currentTag)) {
                                    //nodeVal = 0;
                                    nodeVal = previousNode.value + Math.log10(stateTrasitionProbability.get(previousTag).get(currentTag)) + -10;
                                } else if (observationProbability.get(word).containsKey(currentTag)) {
                                    nodeVal = previousNode.value + Math.log10(stateTrasitionProbability.get(previousTag).get(currentTag)) + Math.log10(observationProbability.get(word).get(currentTag));
                                }
                            } else {
                                System.out.println("Missing Case");
                                System.out.println(currentTag + " " + word);
                                System.exit(1);
                            }
                            if (nodeVal > maxValue) {
                                maxValue = nodeVal;
                                currentNode.backTrackTag = previousTag;
                                currentNode.value = nodeVal;
                            }
                        }
                        row.put(currentTag, currentNode);
                    }
                    comp.put(i, row);
                }
            }
            //Termination
            double maxValue = Double.NEGATIVE_INFINITY;
            NodeValue finalNode = new NodeValue();
            for (String previousTag : tagSet) {
                HashMap<String, NodeValue> row = comp.get(observation.length - 1);
                NodeValue previousNode = row.get(previousTag);
                double nodeVal = 0;
                if (!stateTrasitionProbability.get(previousTag).containsKey(Constants.END)) {
                    nodeVal = previousNode.value - 100;
                } else {
                    nodeVal = previousNode.value + Math.log10( stateTrasitionProbability.get(previousTag).get(Constants.END));
                }
                if (nodeVal > maxValue) {
                    maxValue = nodeVal;
                    finalNode.backTrackTag = previousTag;
                    finalNode.value = nodeVal;
                }
            }
            HashMap<String, NodeValue> endRow = new HashMap<String, NodeValue>();
            endRow.put(Constants.END, finalNode);
            comp.put(observation.length, endRow);

            // Back tracking
            String calculatedTagSequence = "";
            String tagInCheck = Constants.END;
            for (int i = comp.size() - 1; i > 0; i--) {
                HashMap<String, NodeValue> row = comp.get(i);
                NodeValue currentNode = row.get(tagInCheck);
                tagInCheck = currentNode.backTrackTag;
                calculatedTagSequence = tagInCheck + " " + calculatedTagSequence;
            }

            calculatedTags.add(calculatedTagSequence.trim());
        }

        return calculatedTags;
    }
}
