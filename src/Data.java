import java.util.ArrayList;

public class Data {

    private final ArrayList<String> data = new ArrayList<>();
    private String dataColumnName;
    private String dataAttributeType;
    private double minimumAttributeValue;
    private double maximumAttributeValue;
    private boolean normalized = false;
    private int uniqueDomainValues;
    private String domainValues;
    private double normalizedValue;
    private ArrayList<String> normalizedValues;

    public Data(String dataColumnName) {
        this.dataColumnName = dataColumnName;
    }

    public boolean isNormalized() {
        return normalized;
    }

    public void setNormalized(boolean normalized) {
        this.normalized = normalized;
    }

    public String getDomainValues() {
        return domainValues;
    }

    public void setDomainValues(String domainValues) {
        this.domainValues = domainValues;
    }

    public String getDataColumnName() {
        return dataColumnName;
    }

    public void setDataColumnName(String dataColumnName) {
        this.dataColumnName = dataColumnName;
    }

    public String getDataAttributeType() {
        return dataAttributeType;
    }

    public void setDataAttributeType(String dataAttributeType) {
        this.dataAttributeType = dataAttributeType;
    }

    public double getMinimumAttributeValue() {
        return minimumAttributeValue;
    }

    public void setMinimumAttributeValue(double minimumAttributeValue) {
        this.minimumAttributeValue = minimumAttributeValue;
    }

    public double getMaximumAttributeValue() {
        return maximumAttributeValue;
    }

    public void setMaximumAttributeValue(double maximumAttributeValue) {
        this.maximumAttributeValue = maximumAttributeValue;
    }

    public void addData(String newData) {
        this.data.add(newData);
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void removeData(int i) {
        data.remove(i);
    }

    public int getUniqueDomainValues() {
        return uniqueDomainValues;
    }

    public void setUniqueDomainValues(int uniqueDomainValues) {
        this.uniqueDomainValues = uniqueDomainValues;
    }

    public ArrayList<String> getNormalizedValues() {
        return normalizedValues;
    }

    public void setNormalizedValues(ArrayList<String> normalizedValues) {
        this.normalizedValues = normalizedValues;
    }

    public String toString() {
        System.out.println("bar");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getDataAttributeType()).append(",")
                .append(this.getDataColumnName()).append(",");
        if (this.dataAttributeType.equals("n")) {
            System.out.println("foo");
            stringBuilder.append(this.getMinimumAttributeValue()).append(",")
                    .append(this.getMaximumAttributeValue()).append("\n");
        } else if (this.dataAttributeType.equals("c")) {
            stringBuilder.append(this.getUniqueDomainValues()).append(",")
                    .append(this.getDomainValues()).append("\n");
        }
        return stringBuilder.toString();
    }

    public String dataToString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : data) {
            stringBuilder.append(s);
            stringBuilder.append(",");
        }
        return stringBuilder.toString();
    }


}
