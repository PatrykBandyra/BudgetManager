package sample;

public class DataRow {

    private final int id;
    private double value;
    private double amount;
    private String unit;
    private int year;
    private int month;
    private int day;
    private String name;
    private String category;

    private String date;

    public DataRow(int id, double value, double amount, String unit, int year, int month, int day, String name, String category) {
        this.id = id;
        this.value = value;
        this.amount = amount;
        this.unit = unit;
        this.year = year;
        this.month = month;
        this.day = day;
        this.name = name;
        this.category = category;
        this.date = day+"/"+month+"/"+year;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getMonth() {
        return month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDate() {
        date = day+"/"+month+"/"+year;
    }

    public String getDate() {
        return date;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
