# META ASSESSMENT - A PRIVACY AWARENESS TOOL
# FINAL PROJECT OF CS50
# DESCRIPTION:

More often than not, image files are not purely made up of graphical pixels. Recall that in PSET4 "Filter" some metadata was stored in headers. Such metadata can consist of a wide array of information, ranging from relatively benign data such as the colour palette, to confidential information as name, location, date or device. Given a large enough dataset that is not protected, it becomes relatively easy to cross-validate one's identity - a fact used by forensics and doxxers alike. Especially known is the EXIF data embedded on (mostly) JPG files, which commonly include coordinates of where the picture was taken. Besides that, there are also other subsections - depending on the device and software used - of metadata such as IPTC (Press Data), Photoshop details, facial recognition metadata and so on and so forth.

There are tools to analyze the metadata of an image on the web and as applications. Operation Systems as Windows usually read out a select part of them natively. It can be a challenge to quickly uncover the metadata and especially the most useful/concerning parts of it. This application therefore quickly analyzes a .jpg/jpeg image according to the four indicators of location, date, device and identity. It nevertheless also writes a log of all unfiltered metadata if one is inclined to explore the (sometimes surprising) information available on a .jpg file. It is limited to a .jpg/jpeg file because this is the file type almost exclusively used on primary pictures (i.e. directly from a camera) and has the actual interesting information embedded onto it.

The application in question is made in IntelliJ IDEA, an IDE for the Java language. It makes use, besides the native features, of two external libraries. The first one is metadata-extractor (https://github.com/drewnoakes/metadata-extractor) which allows one to retrieve metadata from a range of data types. While it is certainly possible to do this manually by reading the byte-structure of a file (and this has been initially considered and implemented), this is cumbersome in Java and not universally extensible (i.e. to other data files, if I wanted to expand upon this project besides just .jpg files). The other one is "OfflineReverseGeocode" (https://github.com/AReallyGoodName/OfflineReverseGeocode) which links retrieved coordinates to a database of cities. This allows some details to the very literal coordinates and allows the application to be used offline and indefinitely (i.e. this also would be possible by using Google Maps API).

The ready-use application comes as a MetaData.jar file, which is the compiled version. This can be found in the artifacts/MetaData_jar folder. It can be ran with by CMD by prompting 'java -jar MetaData.jar [JPG file path]'. Provided one has given a valid command (of one argument) with a valid file (.jpg or .jpeg that can be succesfully read), it will read through the metadata directories. It will seek to retrieve some essential values such as date, location, name and device information. If these are found, the user will be informed in the CMD of what has been found and its value. Considering the inexhaustive (and often obscure) options for metadata values across all different devices, platforms and software applications all found key:values will still be printed to an external file (output.txt in the .jar directory) for manual inspection.# MetaAssessment
