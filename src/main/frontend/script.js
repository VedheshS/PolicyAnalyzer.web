// ---------------- Redirect to login if JWT token is missing ----------------
if (!localStorage.getItem("token")) {
    window.location.href = "login.html";
}

// ---------------- Element References ----------------
const policyForm = document.getElementById("policyForm");
const uploadForm = document.getElementById("uploadForm");
const resultContainer = document.getElementById("resultContainer");
const historySection = document.getElementById("historySection");
const historyBody = document.getElementById("historyBody");
const logoutBtn = document.getElementById("logoutBtn");

const API_URL = "http://localhost:8080/api";
let chartInstance = null; // for avoiding duplicate charts

// ---------------- Logout ----------------
if (logoutBtn) {
    logoutBtn.addEventListener("click", () => {
        localStorage.removeItem("token");
        window.location.href = "login.html";
    });
}

// ---------------- Analyze Policy ----------------
policyForm?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const appName = document.getElementById("appName").value.trim();
    const policyText = document.getElementById("policyText").value.trim();

    if (!appName || !policyText) {
        alert("Please fill in all fields before submitting.");
        return;
    }

    try {
        const res = await fetch(`${API_URL}/policies/analyze`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${localStorage.getItem("token")}`
            },
            body: JSON.stringify({ appName, policyText })
        });

        if (res.status === 401) {
            alert("Session expired. Please login again.");
            localStorage.removeItem("token");
            window.location.href = "login.html";
            return;
        }

        const data = await res.json();
        if (data.status === "error") {
            alert(data.message);
            return;
        }
        displayResult(data);
        await loadHistory();
    } catch (err) {
        console.error("Error analyzing policy:", err);
        alert("Error analyzing policy. Please try again.");
    }
});

// ---------------- Upload Policy File ----------------
uploadForm?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const appName = document.getElementById("uploadAppName").value.trim();
    const file = document.getElementById("policyFile").files[0];

    if (!appName || !file) {
        alert("Please provide an app name and select a file.");
        return;
    }

    const formData = new FormData();
    formData.append("appName", appName);
    formData.append("file", file);

    try {
        const res = await fetch(`${API_URL}/policies/upload`, {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${localStorage.getItem("token")}`
            },
            body: formData
        });

        if (res.status === 401) {
            alert("Session expired. Please login again.");
            localStorage.removeItem("token");
            window.location.href = "login.html";
            return;
        }

        const data = await res.json();
        if (data.status === "error") {
            alert(data.message);
            return;
        }
        displayResult(data);
        await loadHistory();
    } catch (err) {
        console.error("Error uploading policy file:", err);
        alert("Error uploading policy file. Please try again.");
    }
});

// ---------------- Display Analysis Result ----------------
function displayResult(data) {
    if (!data) return;
    resultContainer?.classList.remove("hidden");

    document.getElementById("resAppName").textContent = data.appName || "N/A";
    document.getElementById("resRiskLevel").textContent = data.riskLevel || "Unknown";
    document.getElementById("resRiskScore").textContent =
        data.riskScore ? data.riskScore.toFixed(2) : "0.00";

    const ctx = document.getElementById("riskChart").getContext("2d");

    // Destroy previous chart if it exists
    if (chartInstance) {
        chartInstance.destroy();
    }

    chartInstance = new Chart(ctx, {
        type: "bar",
        data: {
            labels: ["Risk Score"],
            datasets: [{
                label: "Score",
                data: [data.riskScore],
                backgroundColor: ["#FF4C4C"]
            }]
        },
        options: {
            scales: {
                y: { beginAtZero: true, max: 1 }
            },
            plugins: {
                legend: { display: false }
            }
        }
    });
}

// ---------------- Load User-Specific History ----------------
async function loadHistory() {
    try {
        const res = await fetch(`${API_URL}/policies/history`, {
            headers: {
                "Authorization": `Bearer ${localStorage.getItem("token")}`
            }
        });

        if (res.status === 401) {
            alert("Session expired. Please login again.");
            localStorage.removeItem("token");
            window.location.href = "login.html";
            return;
        }

        if (!res.ok) throw new Error("Failed to fetch history");

        const data = await res.json();
        if (!Array.isArray(data) || data.length === 0) {
            historySection?.classList.add("hidden");
            return;
        }

        historySection?.classList.remove("hidden");
        historyBody.innerHTML = "";

        data.forEach(policy => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${policy.id}</td>
                <td>${policy.appName}</td>
                <td>${policy.riskLevel}</td>
                <td>${policy.riskScore.toFixed(2)}</td>
                <td>${new Date(policy.createdAt).toLocaleString()}</td>
            `;
            historyBody.appendChild(tr);
        });
    } catch (err) {
        console.error("Error loading history:", err);
        alert("Failed to load history. Please try again.");
    }
}

// ---------------- Initial Load ----------------
loadHistory();
