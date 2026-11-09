
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import javafx.concurrent.Worker;
import netscape.javascript.JSObject;

import java.net.URL;
import java.time.LocalDate;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();

        URL page = getClass().getResource("/index.html");

        if (page == null) {
            System.out.println("Error: index.html not found!");
            return;
        }

        JavaBridge javaBridge = new JavaBridge();

        engine.getLoadWorker().stateProperty().addListener(
            (observable, oldState, newState) -> {

                if (newState == Worker.State.SUCCEEDED) {

                    JSObject window =
                        (JSObject) engine.executeScript("window");

                    window.setMember("javaApp", javaBridge);

                    engine.executeScript(
                        "window.dispatchEvent(new Event('java-ready'));"
                    );
                }
            }
        );

        engine.setOnAlert(event ->
            System.out.println("JAVASCRIPT ALERT: " + event.getData())
        );

        engine.load(page.toExternalForm());

        Scene scene = new Scene(webView, 1200, 750);

        stage.setTitle(
            "Infrastructure Budget Transparency and Monitoring System"
        );

        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // JavaScript-to-Java connection
    public static class JavaBridge {

        private final ExpenseManager expenseManager;

        public JavaBridge() {
            expenseManager = new ExpenseManager();
        }

        public String getStatus() {
            return "Java OOP System is connected successfully.";
        }

        // Save project information
        public String saveProject(
                String name,
                String contractorName,
                String engineerName,
                double budget) {

            try {

                if (name == null || name.trim().isEmpty()
                        || contractorName == null
                        || contractorName.trim().isEmpty()
                        || engineerName == null
                        || engineerName.trim().isEmpty()
                        || budget <= 0
                        || !Double.isFinite(budget)) {

                    return "ERROR: Please enter valid project information.";
                }

                Contractor contractor =
                    new Contractor(contractorName, "Not specified");

                Engineer engineer =
                    new Engineer(engineerName, "General");

                Project project = new Project(
                    name,
                    contractor,
                    engineer,
                    budget
                );

                expenseManager.addProject(project);

                return "SUCCESS";

            } catch (Exception error) {
                return "ERROR: " + error.getMessage();
            }
        }

        // Get all projects
        public String getProjectsJson() {

            StringBuilder json = new StringBuilder("[");
            var projects = expenseManager.getProjects();

            for (int i = 0; i < projects.size(); i++) {

                Project project = projects.get(i);

                if (i > 0) {
                    json.append(",");
                }

                json.append("{")
                    .append("\"name\":\"")
                    .append(escapeJson(project.getProjectName()))
                    .append("\",")
                    .append("\"contractor\":\"")
                    .append(escapeJson(project.getContractor().getName()))
                    .append("\",")
                    .append("\"engineer\":\"")
                    .append(escapeJson(project.getEngineer().getName()))
                    .append("\",")
                    .append("\"budget\":")
                    .append(project.getBudget())
                    .append("}");
            }

            json.append("]");
            return json.toString();
        }

        // Add expense to a project
        public String addExpense(
                String projectName,
                String description,
                String supplier,
                double amount,
                String date) {

            try {

                if (projectName == null || projectName.trim().isEmpty()
                        || description == null || description.trim().isEmpty()
                        || supplier == null || supplier.trim().isEmpty()
                        || date == null || date.trim().isEmpty()
                        || !Double.isFinite(amount) || amount <= 0) {

                    return "ERROR: Please enter valid expense information.";
                }

                Project project =
                    expenseManager.findProject(projectName);

                if (project == null) {
                    return "ERROR: Selected project was not found.";
                }

                Expense expense = new Expense(
                    description,
                    amount,
                    LocalDate.parse(date),
                    supplier
                );

                expenseManager.addExpense(projectName, expense);

                return "SUCCESS";

            } catch (Exception error) {
                return "ERROR: " + error.getMessage();
            }
        }

        // Get all expenses
        public String getExpensesJson() {

            StringBuilder json = new StringBuilder("[");
            var projects = expenseManager.getProjects();

            boolean first = true;

            for (Project project : projects) {

                for (Expense expense : project.getExpenses()) {

                    if (!first) {
                        json.append(",");
                    }

                    first = false;

                    json.append("{")
                        .append("\"projectName\":\"")
                        .append(escapeJson(project.getProjectName()))
                        .append("\",")
                        .append("\"description\":\"")
                        .append(escapeJson(expense.getDescription()))
                        .append("\",")
                        .append("\"supplier\":\"")
                        .append(escapeJson(expense.getSupplier()))
                        .append("\",")
                        .append("\"amount\":")
                        .append(expense.getAmount())
                        .append(",")
                        .append("\"date\":\"")
                        .append(expense.getDate())
                        .append("\"}");
                }
            }

            json.append("]");
            return json.toString();
        }

        // Generate project budget reports
        public String getReportsJson() {

            StringBuilder json = new StringBuilder("[");
            var projects = expenseManager.getProjects();

            for (int i = 0; i < projects.size(); i++) {

                Project project = projects.get(i);

                if (i > 0) {
                    json.append(",");
                }

                json.append("{")
                    .append("\"projectName\":\"")
                    .append(escapeJson(project.getProjectName()))
                    .append("\",")
                    .append("\"budget\":")
                    .append(project.getBudget())
                    .append(",")
                    .append("\"expenses\":")
                    .append(project.getTotalExpenses())
                    .append(",")
                    .append("\"remaining\":")
                    .append(project.getRemainingBudget())
                    .append(",")
                    .append("\"utilization\":")
                    .append(project.getBudgetUtilization())
                    .append("}");
            }

            json.append("]");
            return json.toString();
        }

        // Budget summary
        public double getTotalBudget() {
            return expenseManager.getTotalBudget();
        }

        public double getTotalExpenses() {
            return expenseManager.getTotalExpenses();
        }

        public double getRemainingBudget() {
            return expenseManager.getRemainingBudget();
        }

        public int getProjectCount() {
            return expenseManager.getProjectCount();
        }

        // Escape special characters for JSON
        private String escapeJson(String value) {

            if (value == null) {
                return "";
            }

            return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
        }
    }
}
