package expenditurediary.tool.se.expenditurediary;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import test1.sql.se.sqltest.SQLhelper;

public class IrregularDateSpanSQLhelper {

    private SQLhelper sqLhelper;
    float[] sum_expenditure;

    public IrregularDateSpanSQLhelper(Context context) {

        sqLhelper = new SQLhelper(context);
    }


    public String getSql(int position) {

        Object[][] catTable = sqLhelper.getSQLdataCategories();
        sum_expenditure = new float[catTable.length];
        GregorianCalendar cal = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());

        //dagens datum uppdelat
        int year = Integer.parseInt(today.split("-")[0]);
        int month = Integer.parseInt(today.split("-")[1]);
        int monthJ_API = month - 1;
        int day = Integer.parseInt(today.split("-")[2]);

        cal.set(year, monthJ_API, day);
        int w = cal.get((Calendar.DAY_OF_WEEK));
        int weekday = (w == 1) ? 7 : w - 1;

        ArrayList<DateHolder> recentDates = new ArrayList<>();
        for(int i = day; i > (day - weekday); i--){
            cal.set(year, monthJ_API, i);
            Date date = cal.getTime();
            recentDates.add(new DateHolder(sdf.format(date)));
        }

        switch (position) {

            case 0:
                for (int i = 0; i < sum_expenditure.length; i++)
                    sum_expenditure[i] = sqLhelper.getSumOfExpenditTableCol(catTable[i][0].toString());

                Object[] sortedDates = sqLhelper.getSortedDays();
                return sortedDates[0].toString() + " - " + sortedDates[sortedDates.length - 1].toString();

            case 1:
                for (int i = 0; i < sum_expenditure.length; i++)
                    sum_expenditure[i] = sqLhelper.sumExpTableColumnYearCat(year, catTable[i][0].toString());

                return year + "-01-01" + " - " + today;
            case 2:

                for (int i = 0; i < sum_expenditure.length; i++)
                    sum_expenditure[i] = sqLhelper.sumTableColumnMonthCat(month, catTable[i][0].toString());

                return year + "-" + month + "-" + "01" + " - " + today;
             case 3:

                for (DateHolder dateHolder : recentDates) {
                    for (int i = 0; i < sum_expenditure.length; i++) {
                        String[] selArgs = {dateHolder.getDate(), catTable[i][0].toString()};
                        float tempValue = sqLhelper.getSumOfExpenditTableCol(selArgs);
                        sum_expenditure[i] += tempValue;
                    }
                }

                return recentDates.get(recentDates.size() - 1).getDate() + " - " + recentDates.get(0).getDate();
            case 4:

                for (int i = 0; i < sum_expenditure.length; i++) {
                    String[] selArgs = {today, catTable[i][0].toString()};
                    sum_expenditure[i] = sqLhelper.getSumOfExpenditTableCol(selArgs);
                }

                return today;
        }

        return null;
    }

    class DateHolder {

        private String date = "";

        public DateHolder(String date) {

            this.date = date;
        }

        public String getDate() {
            return date;
        }
    }

    public float[] getSum_expenditure() {
        return sum_expenditure;
    }
}
