package xyz.nokturnal.barka.privateeye;

public class ReportsJO {
    private String name, time, date, url;

    public ReportsJO() {
    }

    public ReportsJO(String name, String time, String date, String url) {
        this.name = name;
        this.time = time;
        this.date = date;
        this.url = url;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
