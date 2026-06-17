import { useEffect, useState } from "react";
import axiosClient from "../api/axiosClient";
import SummaryCards from "../components/SummaryCards";
import InvestigationTable from "../components/InvestigationTable";

function Dashboard() {
    const [summary, setSummary] = useState(null);
    const [investigations, setInvestigations] = useState([]);

    useEffect(() => {
        axiosClient
            .get("/api/investigation/dashboard-summary")
            .then((response) => setSummary(response.data))
            .catch((error) => console.error(error));

        axiosClient
            .get("/api/investigation")
            .then((response) => setInvestigations(response.data))
            .catch((error) => console.error(error));
    }, []);

    return (
        <div style={{ padding: "18px" }}>
            <h2>Payments Investigation Dashboard</h2>

            <SummaryCards summary={summary} />

            <h2>Investigations</h2>

            <InvestigationTable investigations={investigations} />
        </div>
    );
}

export default Dashboard;