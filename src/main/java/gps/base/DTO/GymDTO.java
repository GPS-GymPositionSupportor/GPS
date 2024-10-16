package gps.base.DTO;

public class GymDTO {
    private String gName;
    private String address;
    private double gLongitude;
    private double gLatitude;
    private String information;
    private String gymImage;
    private Byte rating;

    // Getters and setters
    public String getGName() {
        return gName;
    }

    public void setGName(String gName) {
        this.gName = gName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getGLongitude() {
        return gLongitude;
    }

    public void setGLongitude(double gLongitude) {
        this.gLongitude = gLongitude;
    }

    public double getGLatitude() {
        return gLatitude;
    }

    public void setGLatitude(double gLatitude) {
        this.gLatitude = gLatitude;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getGymImage() {
        return gymImage;
    }

    public void setGymImage(String gymImage) {
        this.gymImage = gymImage;
    }

    public Byte getRating() {
        return rating;
    }

    public void setRating(Byte rating) {
        this.rating = rating;
    }
}
