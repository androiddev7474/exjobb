package calculator.tools.se.calculator;

public class MathOperations {



    public static float div(float numb1, float numb2) {

        return numb1 / numb2;
    }

    public static float mult(float numb1, float numb2) {

        return numb1 * numb2;
    }

    public static float sub(float numb1, float numb2) {

        return numb1 - numb2;
    }

    public static float add(float numb1, float numb2) {

        return numb1 + numb2;
    }

    public static float divMultSubAdd(float numb1, float numb2, String operator) {

        float result = 0;
        switch (operator) {

            case Calculate.OP_DIV:
                result = MathOperations.div(numb1, numb2);
                break;
            case Calculate.OP_MULT:
                result = MathOperations.mult(numb1, numb2);
                break;
            case Calculate.OP_SUB:
                result = MathOperations.sub(numb1, numb2);
                break;
            case Calculate.OP_ADD:
                result = MathOperations.add(numb1, numb2);
                break;

        }

        return result;
    }

}
