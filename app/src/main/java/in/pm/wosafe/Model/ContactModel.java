package in.pm.wosafe.Model;

import java.util.Date;

/**
 * Created by Prakash on 6/13/2021.
 */
public class ContactModel {
    String Name;
    String familyType;
    String Number;
    Date dateTime;
    String id;


    public ContactModel(String name, String familyType, String number, Date dateTime, String id) {
        Name = name;
        this.familyType = familyType;
        Number = number;
        this.dateTime = dateTime;
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFamilyType() {
        return familyType;
    }

    public void setFamilyType(String familyType) {
        this.familyType = familyType;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
