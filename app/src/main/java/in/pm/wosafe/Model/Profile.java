package in.pm.wosafe.Model;


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Profile {
    private String Id;
    private String Number;
    private String Token;
    private String Password;

    public Profile(String id, String number, String password) {
        Id = id;
        Number = number;
        Password = password;
    }

    public Profile(String token) {
        Token = token;
    }

    public Profile() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
