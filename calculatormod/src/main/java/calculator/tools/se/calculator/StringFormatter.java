package calculator.tools.se.calculator;

/**
 * Klassen sköter formattering av strängar samt vissa stränganalyser som att hitta förekomsten av ett tecken i en given sträng.
 * Björn Hallström
 * Version: 1 (2018-11-23)
 */

public class StringFormatter {


    /**
     * Metoden formatterar en rå sträng med avseende på ett givet tecken
     * @param inputString sträng som parsas
     * @param operands tecken-vektor (om ett tecken från denna vektor hittas i strängen så sätts omit till true)
     * @param target Aktuellt tecken som ska rensas från strängen.
     * @return
     */
    public static String removeChars(String inputString, String[] operands, String target) {

        boolean omit = true;
        String formatString = ""; //den nya strängen
        for (int i = 0; i < inputString.length(); i++) {

            if (inputString.charAt(i) == target.charAt(0) && omit)  {
                formatString += inputString.charAt(i);
                omit = false;
            } else if (inputString.charAt(i) != target.charAt(0))
                formatString += inputString.charAt(i);

            String s = Character.toString(inputString.charAt(i));
            if (stringContainsItemFromList(s, operands))
                omit = true;
        }

        return formatString;
    }



    /**
     * Returnerar sant om den hittar ett givet tecken i den aktuella strängen
     * @param inputStr sträng som parsas
     * @param items tecken-vektor
     * @return
     */
    public static boolean stringContainsItemFromList(String inputStr, String[] items) {
        for(int i = 0; i < items.length; i++) {
            if(inputStr.contains(items[i]))
                return true;
        }
        return false;
    }


    /**
     * Returnerar ett givet tecken som hittats i den aktuella strängen
     * @param inputStr sträng som parsas
     * @param items vektor med tecken
     * @return
     */
    public static String stringGetItemFromList(String inputStr, String[] items) {
        for(int i = 0; i < items.length; i++) {
            if(inputStr.contains(items[i]))
                return items[i];
        }
        return "";
    }


    /**
     * Räknar antalet givna tecken
     * @param inputStr som parsas
     * @param items vektor med tecken som matchas mot inputStr
     * @param nCounts antalet träffar som krävs för att returnera true
     * @return
     */
    public static boolean characterCounter(String inputStr, String[] items, int nCounts) {

        int charCounter = 0;
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < inputStr.length(); j++) {

                if (Character.toString(inputStr.charAt(j)).contains(items[i]))
                    charCounter++;
                if (charCounter >= nCounts)
                    return true;
            }
        }

        return false;
    }

}