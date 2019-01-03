package calculator.tools.se.calculator;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.StringTokenizer;

/**
 * Klassen sköter validering och beräkning av matematiska uttryck som användaren matar in
 * Björn Hallström
 * Version: 1 (2018-11-23)
 *
 */

public class Calculate {

    private static final String DECIMAL_PATTERN = "#0.00";

    public static final String OP_DIV = "/";
    public static final String OP_MULT = "*";
    public static final String OP_SUB = "-";
    public static final String OP_ADD = "+";
    public static final String EQUALS = "=";
    public static final String DECIMAL = ".";

    public static final String[] OPERANDS_DECIM_EQ = {OP_DIV, OP_MULT, OP_SUB, OP_ADD, DECIMAL, EQUALS};
    public static final String[] OPERANDS_EQ = {OP_DIV, OP_MULT, OP_SUB, OP_ADD, EQUALS};
    public static final String[] OPERANDS = {OP_DIV, OP_MULT, OP_SUB, OP_ADD};
    public static final String[] OPERANDS_DIV_MULT_EQ = {OP_DIV, OP_MULT, EQUALS};
    public static final String[] OPERANDS_SUB_ADD = {OP_SUB, OP_ADD};
    public static final String[] NUMBERS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};


    /**
     * Huvudmetod som kontrollerar inmatning från användaren - om inmatning är korrekt men inget färdigt matematisk uttryck finns returnas den inmatade strängen till displayen
     * Om det dock visas sig att inmatningen utgör ett färdigt matematiskt uttryck bearbetas uttrycket i en särskild metod och returnerar det färdiga resultatet till displayen
     * @param displayContent
     * @param tempContent
     * @return
     */
    public String checkInput(String displayContent, String tempContent) {

        //Inmatning måste godkännas - är den inte godkänd returnera direkt med det gamla innehållet
        boolean isValid = validateInput(displayContent, tempContent);
        tempContent = StringFormatter.removeChars(tempContent, OPERANDS, DECIMAL);
        if (isValid)
            displayContent = tempContent;
        else
            return displayContent;

        boolean isExpression = checkIfExpression(tempContent);


        if (isExpression)
            displayContent = calculateExpression(tempContent);

        return displayContent;
    }


    /**
     * Bearbetar ett validerat matematiskt uttryck
     * @param tempContent det matematiska uttrycket
     * @return result
     */
    private String calculateExpression(String tempContent) {

            //snyggar till uttrycket - ta bort sista tecknet
            String endOperator = tempContent.substring(tempContent.length() - 1, tempContent.length());
            String expression = tempContent.substring(0, tempContent.length() - 1);
            String exprOperator = "";
            int signMult = 1;
            boolean hasSign = StringFormatter.characterCounter(expression.substring(0, 1), OPERANDS_SUB_ADD, 1);
            if (hasSign) {

                if (expression.substring(0, 1).equals(OP_SUB))
                    signMult = -1;

                exprOperator = StringFormatter.stringGetItemFromList(expression.substring(1, expression.length()), OPERANDS);
                expression = tempContent.substring(1, tempContent.length() - 1);

            } else
                exprOperator = StringFormatter.stringGetItemFromList(expression, OPERANDS);

            StringTokenizer st = new StringTokenizer(expression, exprOperator);

            float[] numbs = new float[2];
            int cnt = 0;
            while(st.hasMoreElements()) {

                String s = st.nextElement().toString();
                if (cnt == 0) {

                    char ch = tempContent.charAt(0);
                    int ascii = (int) ch;
                    System.err.println("ascii: " + ascii);

                }
                numbs[cnt] = Float.parseFloat(s);
                cnt++;

            }
            float result = MathOperations.divMultSubAdd(numbs[0] * signMult, numbs[1], exprOperator);
            String resultString = formatResult(result);

            if (endOperator.equals(EQUALS))
                return resultString;
            else
                return resultString + endOperator;
    }


    /**
     * Reglerar antalet
     * @param result
     * @return
     */
    private String formatResult(float result) {

        float resultF = (float) Math.floor(result);
        float diff = Math.abs(result - resultF);
        String formatString = "";

        if (diff == 0) {

            int formatResult = (int) result;
            formatString = "" + formatResult;
        } else {

            NumberFormat nf = new DecimalFormat(DECIMAL_PATTERN);
            formatString = nf.format(result);
            formatString = formatString.replace(",", DECIMAL);

            //tydligen så medför formateringen att minustecknet med ascii 45 ändras till ett unicode minus med värdet 8722. Håller på att reda ut detta men dett bör fungera tillsvidare
            //konstigt nog påverkas endast första minustecknet om strängen börjar med minustecken.
            int ascii = (int) formatString.charAt(0);
            if (ascii == 8722) {
                formatString = formatString.substring(1, formatString.length());
                formatString = OP_SUB + formatString;
            }

            //formatString = "" + result;
        }

        return formatString;
    }


    /**
     * Validerar det som användaren matar in - ett uttryck måste följa bestämda regler - här sätts reglerna!
     * @param displayContent det som redan visas i miniräknaren
     * @param tempContent obearbetat innehåll som kommer att parsas
     * @return tempcontent kommer antingen att godkännas eller falsifieras
     */
    private boolean validateInput(String displayContent, String tempContent) {

        //returnera om det sista 2 värdena är operander (även =)
        if (tempContent.length() > 1) {
            String lastChar = Character.toString(tempContent.charAt(tempContent.length() - 1));
            String secondLastChar = Character.toString(tempContent.charAt(tempContent.length() - 2));
            if (StringFormatter.stringContainsItemFromList(lastChar, OPERANDS_EQ) && StringFormatter.stringContainsItemFromList(secondLastChar, OPERANDS_EQ))
                return false;

            //det första tecknet tillåts vara +-, den räknas då inte med
            String omitFirst = tempContent.substring(1);
            lastChar = Character.toString(tempContent.charAt(tempContent.length() - 1));
            if (!StringFormatter.stringContainsItemFromList(omitFirst, OPERANDS) && lastChar.equals(EQUALS))
                return false;
        }

        // testar om första inmatningen är ett tecken - inte tillåtet.
        if (StringFormatter.stringContainsItemFromList(Character.toString(tempContent.charAt(0)), OPERANDS_DIV_MULT_EQ) && displayContent.length() == 0)
            return false;

        //Alltid Okay att mata in ett nummer (0 - 9)
        if(StringFormatter.stringContainsItemFromList(Character.toString(tempContent.charAt(tempContent.length() - 1)), NUMBERS))
            return true;

        // +- alltid ok mata in som första tecken
        if(StringFormatter.stringContainsItemFromList(Character.toString(tempContent.charAt(0)), OPERANDS_SUB_ADD) && displayContent.length() == 0)
            return true;

        if (displayContent.length() >= 1) {

            char lastCharTempString = tempContent.charAt(tempContent.length() - 1);
            char lastCharDisplayString = displayContent.charAt(displayContent.length() - 1);

            //Okay om matat in tecken samtidigt som den befintliga strängens (dvs displayen) sista värde INTE är ett tecken (/*-+)
            if (StringFormatter.stringContainsItemFromList(Character.toString(lastCharTempString), OPERANDS_DECIM_EQ) && !StringFormatter.stringContainsItemFromList(Character.toString(lastCharDisplayString), OPERANDS_DECIM_EQ))
                return true;

        }

        return false;
    }


    /**
     * Validerar matetmatiskt uttryck
     * @param inputStr
     * @return (true om uttrycket är ett godkänt matematisk uttryck - redo att processas)
     */
    private boolean checkIfExpression(String inputStr) {

        // kollar om operator i böran (+- tillåtet) och tar då temp. bort den för att räkningen av relevanta operander ska bli korrekt. Relevant i så mening att de
        //är avgörande för en kommande uträkning. Det måste vara två operander varav 1 är det sista (=).
        String tempInputString = inputStr;
        boolean hasOperatorBegin = StringFormatter.characterCounter(inputStr.substring(0, 1), OPERANDS_SUB_ADD, 1);
        if (hasOperatorBegin && inputStr.length() > 1)
            tempInputString = inputStr.substring(1, inputStr.length());


        boolean hasOperatorEnd = false;
        if (tempInputString.length() > 0)
            hasOperatorEnd = StringFormatter.characterCounter(tempInputString, OPERANDS_EQ, 2);
        if (hasOperatorEnd)
            return true;
        else
            return false;

    }
}
