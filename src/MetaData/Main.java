// PACKAGE //
package MetaData;

/*
* Meta Assessment - a Privacy Awareness Tool
*
* This (rudimentary) Command Line tool accepts .jpg pictures and analyzes the available metadata.
* Depending on the device and its setting, this data can range from benign colour palettes to data such as location and time stamps.
* Using this technique - given enough data - can lead to cross-validating an identity (a fact used by forensics and doxxers alike).
*
* This tool briefly reviews four main indicators (Location, Time, Device & Identity) and will output all metadata to a report.txt
*
*/

// IMPORTS //

// Java API //
import java.io.*;
import java.util.Date;
import java.util.Locale;

// External metadata-extractor
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.iptc.IptcDirectory;
import com.drew.metadata.photoshop.PhotoshopDirectory;

// External OfflineReverseGeocode
import geocode.*;

public class Main {
    public static void main(String[] args) throws Exception {

        // Check if correct command has been used
        if (args.length != 1 ){
            System.out.println("USAGE: JAVA META [FILENAME]");
            System.exit(1);
        }

        // Check if a correct file has been given
        if (!Check(args[0])){
            System.out.println("ONLY VALID .JPG or .JPEG ACCEPTED");
            System.exit(1);
        }

        // Check if file actually exists and is a valid image//
        File file = new File(args[0]);
        Metadata metadata = null;
        try {
            metadata = ImageMetadataReader.readMetadata(file);
        } catch (FileNotFoundException e){
            System.out.println("FILE NOT FOUND");
            System.exit(1);
        } catch (ImageProcessingException f){
            System.out.println("CANNOT READ THIS IMAGE");
            System.exit(1);
        }

        // Attempt to open required Metadata Directories //
        ExifSubIFDDirectory exifSubDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        ExifDirectoryBase exifDirectory = metadata.getFirstDirectoryOfType(ExifDirectoryBase.class);
        GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
        PhotoshopDirectory photoshopDirectory = metadata.getFirstDirectoryOfType(PhotoshopDirectory.class);
        IptcDirectory iptcDirectory = metadata.getFirstDirectoryOfType(IptcDirectory.class);

        // Checks - Date //
        // Checks for EXIF Date and GPS Date //
        MetaDate metaDate = new MetaDate();

        // EXIF Date
        System.out.println("Date Check ..");
        if (exifSubDirectory != null) {
            Date date = exifSubDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);

            if (date != null) {
                metaDate.setDate(metaDate.convertDate(date));
            }
        }

        // GPS Date
        if (gpsDirectory != null) {
            Date gpsdate = gpsDirectory.getGpsDate();

            if (gpsdate != null) {
                metaDate.setGpsate(metaDate.convertDate(gpsdate));
            }
        }
            if (metaDate.getDate() != null) {
                System.out.println("Caution: Date can (possibly) be retrieved! Found the following:\n" + "Photo taken on " + metaDate.getDate() + "\n");
            }
            else if (metaDate.getGpsdate() != null) {
                System.out.println("Caution: Date can (possibly) be retrieved! Found the following:\n" + "Photo taken on " + metaDate.getGpsdate() + "\n");
            }
            else {
                System.out.println("No apparent date found. But be sure to check the output report!\n");
            }

        System.out.println("Location Check ..");

        // Checks - Location //
        // Checks GPS //
        MetaLocation metalocation = new MetaLocation();
        if (gpsDirectory != null) {
           metalocation.setLocation(gpsDirectory.getGeoLocation());
            if (metalocation.getLocation() != null) {
                // Latitude & Longitude
                metalocation.setLat(metalocation.getLocation().getLatitude());
                metalocation.setLng(metalocation.getLocation().getLongitude());

                // Offline list of places to cross-reference for location
                InputStream stream = Main.class.getClassLoader().getResourceAsStream("MetaData/cities.txt");
                ReverseGeoCode reverseGeoCode = new ReverseGeoCode(stream, true);

                System.out.println("Caution: Location can (possibly) be retrieved! Found the following:\n" + "Latitude: " + String.format(Locale.US, "%.3f", metalocation.getLat()) + ", Longitude: " + String.format(Locale.US, "%.3f", metalocation.getLng()));
                System.out.println("Photo taken in " + reverseGeoCode.nearestPlace(metalocation.getLat(), metalocation.getLng()) + ", " + reverseGeoCode.nearestPlace(metalocation.getLat(), metalocation.getLng()).country + "\n");
            } else {
                System.out.println("No apparent location found. But be sure to check the output report!\n");
            }
        }
        else{
            System.out.println("No apparent location found. But be sure to check the output report!\n");
        }

        // Checks - Device //
        // Checks EXIF //
        System.out.println("Device Check ..");
        MetaDevice metaDevice = new MetaDevice();
        if (exifDirectory != null) {
            int nullCounter = 0;

            if (exifDirectory.getDescription(ExifDirectoryBase.TAG_MAKE) != null) {
                metaDevice.setBrand(exifDirectory.getDescription(ExifDirectoryBase.TAG_MAKE));
                nullCounter++;
            }
            if (exifDirectory.getDescription(ExifDirectoryBase.TAG_MODEL) != null) {
                metaDevice.setModel(exifDirectory.getDescription(ExifDirectoryBase.TAG_MODEL));
                nullCounter++;
            }
            if (exifDirectory.getDescription(ExifDirectoryBase.TAG_SOFTWARE) != null) {
                metaDevice.setFirmware(exifDirectory.getDescription(ExifDirectoryBase.TAG_SOFTWARE));
                nullCounter++;
            }
            if (nullCounter > 0) {
                System.out.println("Caution: Device can (possibly) be retrieved! Found the following:\nPhoto taken by a " + metaDevice.getBrand() + " " + metaDevice.getModel() + " (" + metaDevice.getFirmware() +  ")\n");
            } else {
                System.out.println("No apparent device found. But be sure to check the output report!\n");
            }
        }
        else{
            System.out.println("No apparent device found. But be sure to check the output report!\n");
        }

        // Checks - Identity //
        // Checks EXIF, PhotoShop and IPTC (Press)  //
        MetaIdentity metaIdentity = new MetaIdentity();
        System.out.println("Identity Check ..");

        if (exifDirectory != null) {
            metaIdentity.setArist(exifDirectory.getDescription(ExifDirectoryBase.TAG_ARTIST));
            metaIdentity.setCopyright(exifDirectory.getDescription(ExifDirectoryBase.TAG_COPYRIGHT));
        }

        if (photoshopDirectory != null) {
            metaIdentity.setPsurl(photoshopDirectory.getDescription(PhotoshopDirectory.TAG_URL));
        }

        if (iptcDirectory != null) {
            metaIdentity.setByline(iptcDirectory.getDescription(IptcDirectory.TAG_CREDIT));
            metaIdentity.setCredit(iptcDirectory.getDescription(IptcDirectory.TAG_BY_LINE));
        }

        if (metaIdentity.getArist() != null) {
            System.out.println("Caution: Name can (possibly) be retrieved! Found the following:\nPhoto taken by " + metaIdentity.getArist() + "\n");
        }
        else if (metaIdentity.getCopyright() != null) {
            System.out.println("Caution: Name can (possibly) be retrieved! Found the following:\nPhoto taken by" + metaIdentity.getCopyright() + "\n");
        }
        else if (metaIdentity.getPsUrl() != null) {
            System.out.println("Caution: Name can (possibly) be retrieved! Found the following:\nPhoto taken by" + metaIdentity.getPsUrl() + "\n");
        }
        else if (metaIdentity.getByline() != null) {
            System.out.println("Caution: Name can (possibly) be retrieved! Found the following:\nPhoto taken by" + metaIdentity.getByline() + "\n");
        }
        else if (metaIdentity.getCredit() != null) {
            System.out.println("Caution: Name can (possibly) be retrieved! Found the following:\nPhoto taken by" + metaIdentity.getCredit() + "\n");
        }
        else {
            System.out.println("No apparent identity found. But be sure to check the output report!\n");
        }

        // Wrap Up //
        File output = new File("output.txt");
        FileWriter writer = new FileWriter(output);

        int lineCount = 0;
        for (Directory allDirectories : metadata.getDirectories()) {
            for (Tag tag : allDirectories.getTags()) {
                writer.write(tag.toString() + "\n");
                lineCount++;
            }
        }
        System.out.println("Written " + lineCount + " lines of metadata to output.txt");
    }

    // Validity Check Method
    public static boolean Check(String path){
        String[] pathArray = path.split("\\.");

        if (pathArray.length != 2){
            return false;
        }

        String fileExt = pathArray[1];
        if (fileExt.compareToIgnoreCase("jpg") == 0 || fileExt.compareToIgnoreCase("jpeg") == 0) {
            return true;
        }

        return false;
    }
}
