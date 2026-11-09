
import java.util.ArrayList;
import java.util.List;

public class Project {

    private String projectName;
    private Contractor contractor;
    private Engineer engineer;
    private double budget;

    private ArrayList<Expense> expenses;

    // Constructor
    public Project(String projectName, Contractor contractor,
                   Engineer engineer, double budget) {

        this.projectName = projectName;
        this.contractor = contractor;
        this.engineer = engineer;
        this.budget = budget;
        this.expenses = new ArrayList<>();
    }

    // Getters
    public String getProjectName() {
        return projectName;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public Engineer getEngineer() {
        return engineer;
    }

    public double getBudget() {
        return budget;
    }

    public List<Expense> getExpenses() {
        return new ArrayList<>(expenses);
    }

    // Setters
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
    }

    public void setEngineer(Engineer engineer) {
        this.engineer = engineer;
    }

    public void setBudget(double budget) {
        if (budget < 0) {
            throw new IllegalArgumentException(
                "Budget cannot be negative."
            );
        }

        this.budget = budget;
    }

    // Add an expense to the project
    public void addExpense(Expense expense) {
        if (expense == null) {
            throw new IllegalArgumentException(
                "Expense cannot be empty."
            );
        }

        expenses.add(expense);
    }

    // Calculate total expenses
    public double getTotalExpenses() {
        double total = 0;

        for (Expense expense : expenses) {
            total += expense.getAmount();
        }

        return total;
    }

    // Calculate remaining budget
    public double getRemainingBudget() {
        return budget - getTotalExpenses();
    }

    // Calculate budget utilization
    public double getBudgetUtilization() {
        if (budget == 0) {
            return 0;
        }

        return (getTotalExpenses() / budget) * 100;
    }
}
