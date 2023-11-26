package dms.dms.domain;

public class DateData {
    String year = "";
    String month = "";
    String date = "";
    String value = "";

    public DateData(String year, String month, String date, String value) {
        this.year = year;
        this.month = month;
        this.date = date;
        this.value = value;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
