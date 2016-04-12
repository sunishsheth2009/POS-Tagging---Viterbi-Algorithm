/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package postagging;

/**
 *
 * @author Sunish
 */
public class NodeValue {
    
    double value;
    String backTrackTag;
    
    public NodeValue(){
        value = 0.0;
        backTrackTag = "";
    }

    public double getNodeValue() {
        return value;
    }

    public String getBackTrackTag() {
        return backTrackTag;
    }

    public void setNodeValue(double value) {
        this.value = value;
    }

    public void setBackTrackTag(String backTrackTag) {
        this.backTrackTag = backTrackTag;
    }
    
}
