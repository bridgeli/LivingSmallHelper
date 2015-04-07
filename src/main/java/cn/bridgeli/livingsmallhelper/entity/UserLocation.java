package cn.bridgeli.livingsmallhelper.entity;

public class UserLocation {
    private int id;
    private String open_id;
    private String lng;
    private String lat;
    private String bd09_lng;
    private String bd09_lat;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpen_id() {
        return open_id;
    }

    public void setOpen_id(String open_id) {
        this.open_id = open_id;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getBd09_lng() {
        return bd09_lng;
    }

    public void setBd09_lng(String bd09_lng) {
        this.bd09_lng = bd09_lng;
    }

    public String getBd09_lat() {
        return bd09_lat;
    }

    public void setBd09_lat(String bd09_lat) {
        this.bd09_lat = bd09_lat;
    }

}
