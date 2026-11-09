
public class Engineer extends Person {

    private String specialization;

    // Constructor
    public Engineer(String name, String specialization) {
        super(name);
        this.specialization = specialization;
    }

    // Getter
    public String getSpecialization() {
        return specialization;
    }

    // Setter
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    // Polymorphism: overriding the abstract method
    @Override
    public String displayDetails() {
        return "Engineer: " + getName()
                + " | Specialization: " + specialization;
    }
}
