package ph.codebuddy.weeha.model;

/**
 * Created by rommeldavid on 24/07/16.
 */
public class TrackRequest {
    private String id;
    private String first_name;
    private String last_name;
    private String avatar;

    public String getId(){
        return id;
    }
    public String getFirstName(){
        return first_name;
    }
    public String getlastName(){
        return last_name;
    }
    public String getAvatar(){
        return avatar;
    }
    public String getFullName(){
        return getFirstName() + " " + getlastName();
    }

    public void setTrackRequest(String id, String first_name, String last_name, String avatar){
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.avatar = avatar;
    }
}