import { useEffect, useState } from "react";
import { Box, Button, TextField } from "@mui/material";
import { useNavigate } from "react-router-dom";
import axiosClient from "../api/axiosClient";
import SummaryCards from "../components/SummaryCards";
import InvestigationTable from "../components/InvestigationTable";

function Dashboard() {
    const navigate = useNavigate();
    const [summary, setSummary] = useState(null);
    const [investigations, setInvestigations] = useState([]);
    const [searchKeyword, setSearchKeyword] = useState("");

    const loadInvestigations = () => {
        axiosClient
            .get("/api/investigation")
            .then((response) => setInvestigations(response.data))
            .catch((error) => console.error(error));
    };

    const handleSearch = () => {
        if (!searchKeyword.trim()) {
            loadInvestigations();
            return;
        }

        axiosClient
            .get(`/api/investigation/search?keyword=${encodeURIComponent(searchKeyword)}`)
            .then((response) => setInvestigations(response.data))
            .catch((error) => console.error(error));
    };

    useEffect(() => {
        axiosClient
            .get("/api/investigation/dashboard-summary")
            .then((response) => setSummary(response.data))
            .catch((error) => console.error(error));

        loadInvestigations();
    }, []);

    return (
        <div style={{ padding: "18px" }}>
            <h2>Payments Investigation Dashboard</h2>

            <SummaryCards summary={summary} />

            <Box
                sx={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                    mb: 2
                }}>
                <h2>Investigations</h2>

                <Button
                    variant="contained"
                    color="secondary"
                    onClick={() => navigate("/ai-insights")}>
                    AI Insights
                </Button>
            </Box>

            <Box sx={{ display: "flex", gap: 2, mb: 3 }}>
                <TextField
                    label="Search by Payment Reference"
                    variant="outlined"
                    value={searchKeyword}
                    onChange={(e) => setSearchKeyword(e.target.value)}
                    fullWidth
                    sx={{
                        "& .MuiOutlinedInput-root": {
                            backgroundColor: "background.paper",
                            color: "text.primary",
                            "& fieldset": {
                                borderColor: "error.main"
                            },
                            "&:hover fieldset": {
                                borderColor: "error.dark"
                            },
                            "&.Mui-focused fieldset": {
                                borderColor: "error.main"
                            }
                        },
                        "& .MuiInputLabel-root": {
                            color: "primary.main"
                        },
                        "& .MuiInputLabel-root.Mui-focused": {
                            color: "primary.main"
                        },
                        "& .MuiOutlinedInput-input": {
                            color: "text.primary"
                        }
                    }}
                />

                <Button variant="contained" onClick={handleSearch}>
                    Search
                </Button>

                <Button
                    variant="outlined"
                    onClick={() => {
                        setSearchKeyword("");
                        loadInvestigations();
                    }}>
                    Reset
                </Button>
            </Box>

            <InvestigationTable investigations={investigations} />
        </div>
    );
}

export default Dashboard;
