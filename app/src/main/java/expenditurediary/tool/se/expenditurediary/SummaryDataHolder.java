package expenditurediary.tool.se.expenditurediary;

public class SummaryDataHolder {

    private String name;
    private float data;

    public SummaryDataHolder(String name, float data) {

        this.name = name;
        this.data = data;

    }

    public String getName() {
        return name;
    }

    public float getData() {
        return data;
    }
}
