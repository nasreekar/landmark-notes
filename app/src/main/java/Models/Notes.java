package Models;

/**
 * Created by home on 29/8/17.
 */

public class Notes {

    private String email;
    private String title;
    private String description;
    private String date;
    private String location;
    private String latitude;
    private String longitute;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitute() {
        return longitute;
    }

    public void setLongitute(String longitute) {
        this.longitute = longitute;
    }

    public Notes(String title, String description,String email, String date, String location, String latitude, String longitute) {
        this.email = email;
        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
        this.latitude = latitude;
        this.longitute = longitute;
    }

    public Notes() {
    }
}
