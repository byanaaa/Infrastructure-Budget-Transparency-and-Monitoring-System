
import java.time.LocalDate;

public class Expense {

    private String description;
    private double amount;
    private LocalDate date;
    private String supplier;

    // Constructor
    public Expense(String description, double amount,
                   LocalDate date, String supplier) {

        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Expense description is required."
            );
        }

        if (amount <= 0) {
            throw new IllegalArgumentException(
                "Expense amount must be greater than zero."
            );
        }

        this.description = description;
        this.amount = amount;
        this.date = date;
        this.supplier = supplier;
    }

    // Getters
    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getSupplier() {
        return supplier;
    }

    // Setters
    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                "Expense amount must be greater than zero."
            );
        }

        this.amount = amount;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
}
