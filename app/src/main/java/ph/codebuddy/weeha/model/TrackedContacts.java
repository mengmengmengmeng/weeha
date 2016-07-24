package ph.codebuddy.weeha.model;

/**
 * Created by rommeldavid on 24/07/16.
 */
public class TrackedContacts {
    private String id;
    private String first_name;
    private String last_name;
    private String avatar;
    private String locations;

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
    public String getLocations(){
        return locations;
    }

    public void setTrackedContacts(String id, String first_name, String last_name, String avatar, String locations){
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.avatar = avatar;
        this.locations = locations;
    }
}