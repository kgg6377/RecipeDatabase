package Objects;

public class User {

    private final String username;
    private final String password;
    private final String created_date;
    private final String last_accessed;

    public User(String username, String password, String created_date, String last_accessed){
        this.username = username;
        this.password = password;
        this.created_date = created_date;
        this.last_accessed = last_accessed;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public String getCreatedDate(){
        return this.created_date;
    }

    public String getLastAccessed(){
        return this.last_accessed;
    }

}
