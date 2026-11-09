
import java.util.ArrayList;

public class ExpenseManager {

    // Stores all infrastructure projects
    private ArrayList<Project> projects;

    // Constructor
    public ExpenseManager() {
        projects = new ArrayList<>();
    }

    // Add a new project
    public void addProject(Project project) {

        if (project == null) {
            throw new IllegalArgumentException(
                "Project cannot be empty."
            );
        }

        projects.add(project);
    }

    // Get all projects
    public ArrayList<Project> getProjects() {
        return new ArrayList<>(projects);
    }

    // Search for a project using its name
    public Project findProject(String projectName) {

        for (Project project : projects) {

            if (project.getProjectName()
                    .equalsIgnoreCase(projectName)) {
                return project;
            }
        }

        return null;
    }

    // Add an expense to a selected project
    public void addExpense(String projectName, Expense expense) {

        Project project = findProject(projectName);

        if (project == null) {
            throw new IllegalArgumentException(
                "Project not found."
            );
        }

        project.addExpense(expense);
    }

    // Get the total allocated budget
    public double getTotalBudget() {

        double total = 0;

        for (Project project : projects) {
            total += project.getBudget();
        }

        return total;
    }

    // Get the total expenses of all projects
    public double getTotalExpenses() {

        double total = 0;

        for (Project project : projects) {
            total += project.getTotalExpenses();
        }

        return total;
    }

    // Get the remaining budget
    public double getRemainingBudget() {
        return getTotalBudget() - getTotalExpenses();
    }

    // Convert project names into an array
    public String[] getProjectNamesArray() {

        String[] names = new String[projects.size()];

        for (int i = 0; i < projects.size(); i++) {
            names[i] = projects.get(i).getProjectName();
        }

        return names;
    }

    // Get the number of registered projects
    public int getProjectCount() {
        return projects.size();
    }
}
