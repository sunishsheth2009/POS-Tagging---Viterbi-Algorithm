package postagging;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Sunish
 */
public class TextIO {    
    public ArrayList<String> read(File attachFileName){
        ArrayList<String> data = new ArrayList<String>();
        
        try {
            BufferedReader r = new BufferedReader(new FileReader(attachFileName));
            String line = r.readLine();
            if (line != null) {
                do {
                    data.add(line.toLowerCase());
                    line = r.readLine();
                } while (line != null);
            }
            r.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        return data;
    }
}
