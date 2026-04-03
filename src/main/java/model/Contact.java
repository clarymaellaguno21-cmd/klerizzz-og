package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Contact {
    private final StringProperty firstName = new SimpleStringProperty("");
    private final StringProperty middleName = new SimpleStringProperty("");
    private final StringProperty lastName = new SimpleStringProperty("");
    private final StringProperty email = new SimpleStringProperty("");
    private final StringProperty phone = new SimpleStringProperty("");

    public Contact(String firstName, String middleName, String lastName, String email, String phone) {
        setFirstName(firstName);
        setMiddleName(middleName);
        setLastName(lastName);
        setEmail(email);
        setPhone(phone);
    }

    public String getFullName() {
        return (getFirstName() + " " + getMiddleName() + " " + getLastName()).trim().replaceAll(" +", " ");
    }

    public StringProperty firstNameProperty() { return firstName; }
    public String getFirstName() { return firstName.get(); }
    public void setFirstName(String f) { firstName.set(f); }

    public StringProperty middleNameProperty() { return middleName; }
    public String getMiddleName() { return middleName.get(); }
    public void setMiddleName(String m) { middleName.set(m); }

    public StringProperty lastNameProperty() { return lastName; }
    public String getLastName() { return lastName.get(); }
    public void setLastName(String l) { lastName.set(l); }

    public StringProperty emailProperty() { return email; }
    public String getEmail() { return email.get(); }
    public void setEmail(String e) { email.set(e); }

    public StringProperty phoneProperty() { return phone; }
    public String getPhone() { return phone.get(); }
    public void setPhone(String p) { phone.set(p); }
}