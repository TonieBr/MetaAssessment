package MetaData;

public class MetaDevice {
    private String brand = "[BRAND]";
    private String model = "[MODEL]";
    private String firmware = "[FIRMWARE]";

    // Getters
    public String getBrand(){
        return this.brand;
    }
    public String getModel(){
        return this.model;
    }
    public String getFirmware(){
        return this.firmware;
    }

    // Setters
    public void setBrand(String brand){
        this.brand = brand;
    }
    public void setModel(String model){
        this.model = model;
    }
    public void setFirmware(String firmware){
        this.firmware = firmware;
    }
}

