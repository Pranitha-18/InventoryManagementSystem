package models;

public class Supplier {
    private String name;
    private String contact;
    private String email;

    public Supplier(String name, String contact, String email) {
        this.name = name;
        this.contact = contact;
        this.email = email;
    }

    public String getName() { return name; }
    public String getContact() { return contact; }
    public String getEmail() { return email; }
}
