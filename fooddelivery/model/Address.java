package model;

public class Address {
    private String city;
    private String pincode;
    private Double latitude;
    private Double longitude;

    public Address(String city, String pincode, Double latitude, Double longitude){
        this.city = city;
        this.pincode = pincode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getPincode(){
        return this.pincode;
    }

    public String getCity(){
        return this.city;
    }

    public Double getLatitude(){
        return this.latitude;
    }

    public Double getLongitude(){
        return this.longitude;
    }

    public Double distanceBetween(Address otherAddress) {
        Double latDiff = this.latitude - otherAddress.latitude;
        Double lonDiff = this.longitude - otherAddress.longitude;
        return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff);
    }
    
}
