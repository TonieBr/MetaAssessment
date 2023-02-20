package MetaData;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class MetaDate {
    private String date;
    private String gpsdate;

    // Getters
    public String getDate(){
        return this.date;
    }
    public String getGpsdate(){
        return this.gpsdate;
    }

    // Setters
    public void setDate(String date){
        this.date = date;
    }
    public void setGpsate(String gpsdate){
        this.gpsdate = gpsdate;
    }

    // Convert Date
    public String convertDate(Date date){
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        String uDate = localDateTime.toString().replace("T", " ");
        String month = uDate.substring(5, 7), day = uDate.substring(8, 10), year = uDate.substring(0, 4);
        StringBuilder fDate = new StringBuilder(uDate);
        fDate.replace(0, 2, day).replace(2, 3, "-").replace(3, 5, month).replace(5, 6, "-").replace(6, 10, year);
        String formattedDate = fDate.toString();
        return formattedDate;
    }
}




