
function showPage(pageId) {
    document.querySelectorAll(".page").forEach(page => {
        page.classList.add("hidden");
    });

    const selectedPage = document.getElementById(pageId);

    if (selectedPage) {
        selectedPage.classList.remove("hidden");
    }

    refreshSystem();
}

// Format Philippine peso
function formatMoney(amount) {
    return "₱" + Number(amount).toLocaleString("en-PH", {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });
}

// Display saved projects
function displayProjects() {

    const projectsList = document.getElementById("projectsList");

    if (!projectsList || !window.javaApp) return;

    const projects = JSON.parse(window.javaApp.getProjectsJson());

    projectsList.innerHTML = "";

    if (projects.length === 0) {
        projectsList.textContent = "No projects saved yet.";
        return;
    }

    projects.forEach(project => {

        const card = document.createElement("div");
        card.className = "project-card";

        const title = document.createElement("h3");
        title.textContent = project.name;

        const contractor = document.createElement("p");
        contractor.textContent = "Contractor: " + project.contractor;

        const engineer = document.createElement("p");
        engineer.textContent = "Engineer: " + project.engineer;

        const budget = document.createElement("p");
        budget.textContent = "Total Budget: " + formatMoney(project.budget);

        card.append(title, contractor, engineer, budget);
        projectsList.appendChild(card);
    });
}

// Populate project dropdown
function updateProjectDropdown() {

    const dropdown = document.getElementById("expenseProject");

    if (!dropdown || !window.javaApp) return;

    const selectedValue = dropdown.value;
    const projects = JSON.parse(window.javaApp.getProjectsJson());

    dropdown.innerHTML = '<option value="">Select a project</option>';

    projects.forEach(project => {

        const option = document.createElement("option");

        option.value = project.name;
        option.textContent = project.name;

        dropdown.appendChild(option);
    });

    dropdown.value = selectedValue;
}

// Display saved expenses
function displayExpenses() {

    const expensesList = document.getElementById("expensesList");

    if (!expensesList || !window.javaApp) return;

    const expenses = JSON.parse(window.javaApp.getExpensesJson());

    expensesList.innerHTML = "";

    if (expenses.length === 0) {
        expensesList.textContent = "No expenses recorded yet.";
        return;
    }

    const table = document.createElement("table");

    const header = table.createTHead().insertRow();

    ["Project", "Description", "Supplier", "Amount", "Date"]
        .forEach(text => {
            const th = document.createElement("th");
            th.textContent = text;
            header.appendChild(th);
        });

    const body = table.createTBody();

    expenses.forEach(expense => {

        const row = body.insertRow();

        [
            expense.projectName,
            expense.description,
            expense.supplier,
            formatMoney(expense.amount),
            expense.date
        ].forEach(value => {

            const cell = row.insertCell();
            cell.textContent = value;
        });
    });

    expensesList.appendChild(table);
}

// Display budget reports
function displayReports() {

    if (!window.javaApp) return;

    const totalBudget = window.javaApp.getTotalBudget();
    const totalExpenses = window.javaApp.getTotalExpenses();
    const remainingBudget = window.javaApp.getRemainingBudget();

    document.getElementById("totalBudget").textContent =
        formatMoney(totalBudget);

    document.getElementById("totalExpenses").textContent =
        formatMoney(totalExpenses);

    document.getElementById("remainingBudget").textContent =
        formatMoney(remainingBudget);

    const reportList = document.getElementById("reportList");

    if (!reportList) return;

    const reports = JSON.parse(window.javaApp.getReportsJson());

    reportList.innerHTML = "";

    if (reports.length === 0) {
        reportList.textContent = "No project reports available.";
        return;
    }

    const table = document.createElement("table");

    const header = table.createTHead().insertRow();

    [
        "Project",
        "Budget",
        "Expenses",
        "Remaining",
        "Usage"
    ].forEach(text => {

        const th = document.createElement("th");
        th.textContent = text;
        header.appendChild(th);
    });

    const body = table.createTBody();

    reports.forEach(report => {

        const row = body.insertRow();

        const values = [
            report.projectName,
            formatMoney(report.budget),
            formatMoney(report.expenses),
            formatMoney(report.remaining),
            Number(report.utilization).toFixed(2) + "%"
        ];

        values.forEach(value => {

            const cell = row.insertCell();
            cell.textContent = value;
        });
    });

    reportList.appendChild(table);
}

// Refresh all system displays
function refreshSystem() {

    if (!window.javaApp) return;

    displayProjects();
    updateProjectDropdown();
    displayExpenses();
    displayReports();
}

// Initialize the system after Java connects
function initializeSystem() {

    if (!window.javaApp) return;

    const status = document.getElementById("systemStatus");

    if (status) {
        status.textContent = window.javaApp.getStatus();
    }

    refreshSystem();
}

// Wait for the Java connection
document.addEventListener("DOMContentLoaded", function () {

    const budgetInput = document.getElementById("budget");

    if (budgetInput) {

        budgetInput.addEventListener("input", function () {

            let value = this.value.replace(/,/g, "");
            value = value.replace(/\D/g, "");

            this.value = value
                ? Number(value).toLocaleString("en-US")
                : "";
        });
    }

    // Save project
    const projectForm = document.getElementById("projectForm");

    if (projectForm) {

        projectForm.addEventListener("submit", function(event) {

            event.preventDefault();

            if (!window.javaApp) {
                alert("Java connection is not available.");
                return;
            }

            const name =
                document.getElementById("projectName").value.trim();

            const contractor =
                document.getElementById("contractorName").value.trim();

            const engineer =
                document.getElementById("engineerName").value.trim();

            const budget = Number(
                document.getElementById("budget").value.replace(/,/g, "")
            );

            if (!name || !contractor || !engineer ||
                !Number.isFinite(budget) || budget <= 0) {

                alert("Please enter valid project information.");
                return;
            }

            const result = window.javaApp.saveProject(
                name, contractor, engineer, budget
            );

            if (result === "SUCCESS") {

                refreshSystem();
                projectForm.reset();

                alert("Project successfully saved!");

            } else {
                alert(result);
            }
        });
    }

    // Save expense
    const expenseForm = document.getElementById("expenseForm");

    if (expenseForm) {

        expenseForm.addEventListener("submit", function(event) {

            event.preventDefault();

            if (!window.javaApp) {
                alert("Java connection is not available.");
                return;
            }

            const projectName =
                document.getElementById("expenseProject").value;

            const description =
                document.getElementById("expenseDescription").value.trim();

            const supplier =
                document.getElementById("expenseSupplier").value.trim();

            const amount = Number(
                document.getElementById("expenseAmount").value
            );

            const date =
                document.getElementById("expenseDate").value;

            if (!projectName || !description || !supplier ||
                !date || !Number.isFinite(amount) || amount <= 0) {

                alert("Please complete all expense information correctly.");
                return;
            }

            const result = window.javaApp.addExpense(
                projectName,
                description,
                supplier,
                amount,
                date
            );

            if (result === "SUCCESS") {

                refreshSystem();
                expenseForm.reset();

                alert("Expense successfully saved!");

            } else {
                alert(result);
            }
        });
    }

    // Set today's date
    const expenseDate = document.getElementById("expenseDate");

    if (expenseDate) {
        expenseDate.value = new Date().toISOString().split("T")[0];
    }

});

// Java connection event
window.addEventListener("java-ready", initializeSystem);
