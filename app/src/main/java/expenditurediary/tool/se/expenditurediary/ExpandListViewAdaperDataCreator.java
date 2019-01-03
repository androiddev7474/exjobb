package expenditurediary.tool.se.expenditurediary;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;

import test1.sql.se.sqltest.SQLhelper;

public class ExpandListViewAdaperDataCreator {

    private Context context;
    private Object[] base_items;
    private Object[][] sub_items_names;
    private Object[][] sub_items_numbs;
    private Object[][] catTable;
    private Object[][] expTable;

    public ExpandListViewAdaperDataCreator(Context context) {

        this.context = context;
    }

    public void createUnitDateData(SQLhelper sqLhelper) {

        catTable = sqLhelper.getSQLdataCategories();
        expTable = sqLhelper.getSQLdataExpenditure();

        ArrayList<String> fullYearList = getRawYears(expTable);
        Set<String> uniqueYearList = new HashSet<String>(fullYearList);

        ArrayList <DateArrayListHolder> dateArrList = getDateCatExp(expTable, fullYearList, uniqueYearList);

        int nUniqueYears = uniqueYearList.size();

        base_items = new Object[nUniqueYears];
        for (int i = 0; i < nUniqueYears; i++)
            base_items[i] = uniqueYearList.toArray()[i].toString();


        String[][] rawMonths = new String[nUniqueYears][];
        String[][] rawCat = new String[nUniqueYears][];
        float[][] rawExp = new float[nUniqueYears][];
        getRawMonthsCatExp(nUniqueYears, dateArrList, rawMonths, rawCat, rawExp);

        sub_items_names = new Object[rawMonths.length][];
        sub_items_numbs = new Object[rawMonths.length][];

        getSubItems(rawMonths, base_items, sub_items_names, sub_items_numbs);

    }


    private ArrayList<String> getRawYears(Object[][] expTable) {

        //hämta alla befintliga årtal
        ArrayList <String> fullYearList = new ArrayList<>();
        for (int i = 0; i < expTable.length; i++) {

            StringTokenizer st = new StringTokenizer(expTable[i][1].toString(), "-");
            //hämta endast årtal - lämna sedan loopen
            while(st.hasMoreElements()) {
                fullYearList.add(new String(st.nextElement().toString()));
                break;
            }
        }
        return fullYearList;
    }


    private void getRawMonthsCatExp(int nUniqueYears, ArrayList <DateArrayListHolder> dateArrList, String[][] rawMonths, String[][] rawCat, float[][] rawExp) {

        for (int i = 0; i < nUniqueYears; i++) {

            ArrayList <DateExpenditure> arrList = dateArrList.get(i).getList();
            String[] sMonth = new String[arrList.size()];
            String[] sCat = new String[arrList.size()];
            float[] sExp = new float[arrList.size()];
            for (int index = 0; index < arrList.size(); index++) {

                sMonth[index] = arrList.get(index).getDate();
                sCat[index] = arrList.get(index).getCategory();
                sExp[index] = arrList.get(index).getExpenditure();
            }

            rawMonths[i] = sMonth;
            rawCat[i] = sCat;
            rawExp[i] = sExp;
        }

    }

    private ArrayList <DateArrayListHolder> getDateCatExp(Object[][] expTable, ArrayList <String> fullYearList, Set<String> uniqueYearList) {

        ArrayList <DateArrayListHolder> dateArrList = new ArrayList<>();
        for (String uYear: uniqueYearList) {
            ArrayList <DateExpenditure> temp = new ArrayList<>();
            int index = 0;
            for (String year: fullYearList) {

                if (uYear.equals(year)) {

                    String rawDate = expTable[index][1].toString();
                    String category = expTable[index][2].toString();
                    float expenditure = Float.parseFloat(expTable[index][3].toString());
                    StringTokenizer st = new StringTokenizer(rawDate, "-");
                    int count = 0;
                    String month = "";
                    while(st.hasMoreElements()) {

                        String tempString = st.nextElement().toString();
                        if (count == 1) {
                            month = tempString;
                            break;
                        }
                        count++;
                    }
                    temp.add(new DateExpenditure(month, category, expenditure));

                }
                index++;

            }

            dateArrList.add(new DateArrayListHolder(temp));
        }

        return dateArrList;
    }


    private void getSubItems(String[][] rawMonths, Object[] base_items, Object[][] sub_items_names, Object[][] sub_items_numbs) {

        ArrayList<UniqueListHolder> uniqueMonthNamesListHolder = new ArrayList<>();
        ArrayList<UniqueListHolder> uniqueMonthNumbsListHolder = new ArrayList<>();
        for (int iYear = 0; iYear < rawMonths.length; iYear++) {

            ArrayList<String> alist1 = new ArrayList<>();
            int len = rawMonths[iYear].length;
            for (int j = 0; j < len; j++) {

                alist1.add(rawMonths[iYear][j]);
            }
            Set<String> uniqueMonthList = new HashSet<String>(alist1);

            Object[] o = uniqueMonthList.toArray();
            Arrays.sort(o);

            Object[] ooNames = new Object[o.length + 1];
            ooNames[0] = base_items[iYear];
            Object[] ooNumbers = new Object[o.length + 1];
            ooNumbers[0] = base_items[iYear];
            String[] monthNames = context.getResources().getStringArray(R.array.months);
            for (int i = 1; i < ooNames.length; i++) {

                int monthID = Integer.parseInt(o[i - 1].toString());
                ooNames[i] = monthNames[monthID - 1];
                ooNumbers[i] = o[i - 1];
            }
            uniqueMonthNamesListHolder.add(new UniqueListHolder(ooNames));
            uniqueMonthNumbsListHolder.add(new UniqueListHolder(ooNumbers));
        }


        for (int i = 0; i <uniqueMonthNamesListHolder.size(); i++) {

            sub_items_names[i] = uniqueMonthNamesListHolder.get(i).getUniqueList();
            sub_items_numbs[i] = uniqueMonthNumbsListHolder.get(i).getUniqueList();
        }

    }

    class UniqueListHolder {

        private Object[] uniqueList;

        public UniqueListHolder(Object[] uniqueList) {

            this.uniqueList = uniqueList;
        }

        public Object[] getUniqueList() {
            return uniqueList;
        }


    }

    class DateArrayListHolder {

        private ArrayList <DateExpenditure> list;
        public DateArrayListHolder(ArrayList <DateExpenditure> list) {

            this.list = list;
        }

        public ArrayList<DateExpenditure> getList() {
            return list;
        }
    }

    class DateExpenditure {

        private String date;
        private String category;
        private float expenditure;

        public DateExpenditure(String date, String category, float expenditure) {

            this.date = date;
            this.expenditure = expenditure;
            this.category = category;

        }

        public String getDate() {
            return date;
        }

        public float getExpenditure() {
            return expenditure;
        }

        public String getCategory() {
            return category;
        }
    }


    public Object[] getBase_items() {
        return base_items;
    }


    public Object[][] getSub_items_names() {
        return sub_items_names;
    }


    public Object[][] getSub_items_numbs() {
        return sub_items_numbs;
    }

    public Object[][] getCatTable() {
        return catTable;
    }

    public Object[][] getExpTable() {
        return expTable;
    }
}
