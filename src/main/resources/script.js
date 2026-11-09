
function showPage(pageId) {
    document.querySelectorAll(".page").forEach(page => {
        page.classList.add("hidden");
    });

    const selectedPage = document.getElementById(pageId);

    if (selectedPage) {
        selectedPage.classList.remove("hidden");
    }
}

function getProjects() {
    try {
        return JSON.parse(localStorage.getItem("projects")) || [];
    } catch (error) {
        console.error("Unable to load projects:", error);
        return [];
    }
}

function displayProjects() {
    const projectsList = document.getElementById("projectsList");

    if (!projectsList) return;

    const projects = getProjects();
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
        budget.textContent = "Total Budget: ₱" +
            Number(project.budget).toLocaleString("en-US");

        card.append(title, contractor, engineer, budget);
        projectsList.appendChild(card);
    });
}

document.addEventListener("DOMContentLoaded", function () {

    // System status
    const status = document.getElementById("systemStatus");

    if (status) {
        if (window.javaApp) {
            status.textContent = window.javaApp.getStatus();
        } else {
            status.textContent = "Java connection is not available.";
        }
    }

    // Budget formatting
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
            alert("SAVE BUTTON CLICKED!");

            const name = document.getElementById("projectName").value.trim();
            const contractor = document.getElementById("contractorName").value.trim();
            const engineer = document.getElementById("engineerName").value.trim();

            const budget = Number(
                document.getElementById("budget").value.replace(/,/g, "")
            );

            if (!name || !contractor || !engineer || !Number.isFinite(budget) || budget <= 0) {
                alert("Please enter valid project information.");
                return;
            }

            const projects = getProjects();

            projects.push({
                name: name,
                contractor: contractor,
                engineer: engineer,
                budget: budget
            });

            try {
                localStorage.setItem("projects", JSON.stringify(projects));
            } catch (error) {
                alert("Unable to save project. Storage may be unavailable.");
                console.error(error);
                return;
            }

            displayProjects();
            projectForm.reset();

            alert("Project successfully saved!");
        });
    }

    // Show saved projects
    displayProjects();
});
