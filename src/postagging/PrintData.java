/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package postagging;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Sunish
 */
public class PrintData {
    
    public void printTable(HashMap<String, HashMap<String, Integer>> input) {
        System.out.format("%-10s", "");
        List<String> keys = new LinkedList<>();
        keys.addAll(input.keySet());
        for (String key : input.keySet()) {
            System.out.format("%-10s", key);
        }
        System.out.println("");
        for (String key : input.keySet()) {
            System.out.format("%-10s", key);
            for (int i = 0; i < input.keySet().size(); i++) {
                String w = keys.get(i);
                HashMap<String, Integer> row = input.get(key);
                String val = "0";
                if (row.containsKey(w)) {
                    val = Integer.toString(row.get(w));
                }
                System.out.format("%-10s", val);
            }
            System.out.println("");
        }

    }
    
}
