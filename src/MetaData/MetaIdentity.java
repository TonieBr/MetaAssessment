package MetaData;

public class MetaIdentity {
    private String copyright;
    private String artist;
    private String psurl;
    private String byline;
    private String credit;

    public static void setMetaIdentity(String ident) {

    }

    // Getters
    public String getCopyright() {
    return this.copyright;
    }
    public String getArist() {
        return this.artist;
    }
    public String getPsUrl() {
        return this.psurl;
    }
    public String getByline() {
        return this.byline;
    }
    public String getCredit() {
        return this.credit;
    }

    //Setters
    public void setCopyright(String copyright){
        this.copyright = copyright;
    }
    public void setArist(String artist){
        this.artist = artist;
    }
    public void setPsurl(String psurl){
        this.psurl = psurl;
    }
    public void setByline(String byline){
        this.byline = byline;
    }
    public void setCredit(String credit){
        this.credit = credit;
    }
}



