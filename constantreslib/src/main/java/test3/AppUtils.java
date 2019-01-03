package test3;

import android.content.Context;

public class AppUtils {

    public static int getIntFromRes(int stringResource, Context context) {

        try {

            return Integer.parseInt(context.getString(stringResource));
        } catch (NumberFormatException ne){
            System.err.println("NumberFormatException EditFragment (stringParser");
        } catch (Exception e) {
            System.err.println("Exception EditFragment (stringParser");
        }


        return -555;
    }

    public static String getStringFromRes(int stringResource, Context context) {

        return context.getString(stringResource);
    }

}
