package MetaData;

import com.drew.lang.GeoLocation;

public class MetaLocation {
    private GeoLocation geolocation;
    private double lat;
    private double lng;

    // Getters
    public GeoLocation getLocation(){
        return this.geolocation;
    }
    public double getLat(){
        return this.lat;
    }
    public double getLng(){
        return this.lng;
    }

    // Setters
    public void setLocation(GeoLocation location){
        this.geolocation = location;
    }
    public void setLat(double lat){
        this.lat = lat;
    }
    public void setLng(double lng){
        this.lng = lng;
    }
}

