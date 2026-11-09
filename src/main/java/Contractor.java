
public class Contractor extends Person {

    private String companyName;

    // Constructor
    public Contractor(String name, String companyName) {
        super(name);
        this.companyName = companyName;
    }

    // Getter
    public String getCompanyName() {
        return companyName;
    }

    // Setter
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    // Polymorphism: overriding the abstract method
    @Override
    public String displayDetails() {
        return "Contractor: " + getName()
                + " | Company: " + companyName;
    }
}
