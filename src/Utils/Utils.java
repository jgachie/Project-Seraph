/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Utils;

import java.io.*;

/**
 *
 * @author Soup
 */
public class Utils {
    
    /**
     * Loads in files as Strings from given path
     * @param path File path of target file
     * @return String representing target file
     */
    public static String loadFileAsString(String path){
        StringBuilder builder = new StringBuilder(); //StringBuilder object; allows you to add characters to String easily
        
        try{
            BufferedReader br = new BufferedReader(new FileReader(path)); //Create buffered reader and load it with FileReader loaded with file path
            String line; //Current line of the file
            
            //While we haven't reached the end of the file
            while ((line = br.readLine()) != null)
                builder.append(line + "\n"); //Append a new line character to the end of the line
            
            br.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        
        return builder.toString(); //Return builder as newly built file made from Strings
    }
    
    /**
     * Parses Strings containing numbers into true integers
     * @param number The String number to be parsed
     * @return Parsed integer
     */
    public static int parseInt(String number){
        try{
            return Integer.parseInt(number);
        } catch (NumberFormatException e){
            e.printStackTrace();
            return 0; //Default to 0 is String that isn't a number is passed in
        }
    }
}
