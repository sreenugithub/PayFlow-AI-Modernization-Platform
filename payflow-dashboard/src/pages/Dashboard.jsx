import { useEffect, useState } from "react";
import AutoAwesomeIcon from "@mui/icons-material/AutoAwesome";
import PsychologyAltIcon from "@mui/icons-material/PsychologyAlt";
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
                    sx={{
                        position: "relative",
                        overflow: "visible",
                        minHeight: 56,
                        px: 2.25,
                        py: 1,
                        borderRadius: "999px",
                        border: "2px solid transparent",
                        color: "#fff",
                        background:
                            "linear-gradient(#101827, #101827) padding-box, linear-gradient(90deg, #ffe45e, #ff7f6e, #ec4899, #7c3aed) border-box",
                        textTransform: "none",
                        boxShadow: "0 12px 28px rgba(15, 23, 42, 0.22)",
                        animation: "aiInsightsPulse 1.6s ease-in-out infinite",
                        "&:hover": {
                            background:
                                "linear-gradient(#0b1220, #0b1220) padding-box, linear-gradient(90deg, #ffe45e, #ff7f6e, #ec4899, #7c3aed) border-box"
                        },
                        "@keyframes aiInsightsPulse": {
                            "0%": {
                                transform: "scale(1)",
                                boxShadow: "0 0 0 0 rgba(236, 72, 153, 0.42)"
                            },
                            "50%": {
                                transform: "scale(1.05)",
                                boxShadow: "0 0 0 10px rgba(236, 72, 153, 0)"
                            },
                            "100%": {
                                transform: "scale(1)",
                                boxShadow: "0 0 0 0 rgba(236, 72, 153, 0)"
                            }
                        },
                        "&::before": {
                            content: "\"✦\"",
                            position: "absolute",
                            top: -12,
                            right: -10,
                            color: "#ffeb3b",
                            fontSize: 18,
                            animation: "sparkleOne 1.2s ease-in-out infinite"
                        },
                        "&::after": {
                            content: "\"✦\"",
                            position: "absolute",
                            bottom: -10,
                            left: -10,
                            color: "#fff176",
                            fontSize: 14,
                            animation: "sparkleTwo 1.4s ease-in-out infinite"
                        },
                        "@keyframes sparkleOne": {
                            "0%, 100%": {
                                opacity: 0.35,
                                transform: "scale(0.8) rotate(0deg)"
                            },
                            "50%": {
                                opacity: 1,
                                transform: "scale(1.3) rotate(25deg)"
                            }
                        },
                        "@keyframes sparkleTwo": {
                            "0%, 100%": {
                                opacity: 0.3,
                                transform: "scale(0.7) rotate(0deg)"
                            },
                            "50%": {
                                opacity: 1,
                                transform: "scale(1.2) rotate(-25deg)"
                            }
                        }
                    }}
                    onClick={() => navigate("/ai-insights")}>
                    <Box
                        component="span"
                        sx={{
                            display: "inline-flex",
                            alignItems: "center",
                            gap: 1.25
                        }}>
                        <Box
                            component="span"
                            sx={{
                                display: "inline-flex",
                                alignItems: "center",
                                justifyContent: "center",
                                width: 34,
                                height: 34,
                                borderRadius: "50%",
                                color: "#ffe45e",
                                background:
                                    "linear-gradient(135deg, rgba(255, 228, 94, 0.18), rgba(236, 72, 153, 0.18))",
                                border: "1px solid rgba(255, 228, 94, 0.45)"
                            }}>
                            <PsychologyAltIcon fontSize="small" />
                        </Box>

                        <Box
                            component="span"
                            sx={{
                                display: "inline-flex",
                                alignItems: "baseline",
                                gap: 0.75,
                                whiteSpace: "nowrap"
                            }}>
                            <Box component="span" sx={{ fontWeight: 700 }}>
                                I AM
                            </Box>

                            <Box
                                component="span"
                                sx={{
                                    fontWeight: 900,
                                    fontSize: 24,
                                    lineHeight: 1,
                                    background:
                                        "linear-gradient(90deg, #ffe45e, #ff7f6e, #ec4899)",
                                    WebkitBackgroundClip: "text",
                                    WebkitTextFillColor: "transparent"
                                }}>
                                AI
                            </Box>

                            <Box component="span" sx={{ fontWeight: 600 }}>
                                to Research on payments
                            </Box>
                        </Box>

                        <AutoAwesomeIcon
                            sx={{
                                color: "#ffeb3b",
                                fontSize: 18
                            }}
                        />
                    </Box>
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
