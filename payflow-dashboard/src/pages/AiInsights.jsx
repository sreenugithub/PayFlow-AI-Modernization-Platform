import { useEffect, useState } from "react";
import {
    Alert,
    Box,
    Button,
    Card,
    CardContent,
    Chip,
    CircularProgress,
    Container,
    Grid,
    List,
    ListItem,
    Stack,
    Typography
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import axiosClient from "../api/axiosClient";

const ownerTeams = [
    "Settlement Team",
    "Payment Operations",
    "Messaging Team"
];

function getSeverityColor(severityTrend) {
    if (severityTrend === "HIGH_RISK") {
        return "error";
    }

    if (severityTrend === "MEDIUM_RISK") {
        return "warning";
    }

    return "success";
}

function AiInsights() {
    const navigate = useNavigate();
    const [insights, setInsights] =
        useState(null);
    const [loading, setLoading] =
        useState(true);
    const [error, setError] =
        useState("");

    useEffect(() => {
        axiosClient
            .get("/api/investigation/incident-history-analysis")
            .then((response) => {
                setInsights(response.data);
            })
            .catch((apiError) => {
                console.error(apiError);
                setError("Unable to load AI operational insights");
            })
            .finally(() => {
                setLoading(false);
            });
    }, []);

    if (loading) {
        return (
            <Container sx={{ py: 3 }}>
                <CircularProgress />
            </Container>
        );
    }

    if (error) {
        return (
            <Container sx={{ py: 3 }}>
                <Alert severity="error">
                    {error}
                </Alert>
            </Container>
        );
    }

    if (!insights) {
        return null;
    }

    const topRootCause =
        insights.topRootCauses?.[0] || "No data";
    const topOwnerTeam =
        insights.topOwnerTeams?.[0] || "No data";

    return (
        <Container sx={{ py: 3 }}>
            <Button
                variant="outlined"
                sx={{ mb: 2 }}
                onClick={() => navigate("/")}>
                Back to Dashboard
            </Button>

            <Typography
                variant="h4"
                gutterBottom>
                AI Operational Insights
            </Typography>

            <Grid container spacing={2} sx={{ mb: 3 }}>
                <Grid item xs={12} sm={6} md={3}>
                    <Card>
                        <CardContent>
                            <Typography color="text.secondary">
                                Total Incidents
                            </Typography>

                            <Typography variant="h4">
                                {insights.totalIncidents}
                            </Typography>
                        </CardContent>
                    </Card>
                </Grid>

                <Grid item xs={12} sm={6} md={3}>
                    <Card>
                        <CardContent>
                            <Typography color="text.secondary">
                                Severity Trend
                            </Typography>

                            <Chip
                                label={insights.severityTrend}
                                color={getSeverityColor(insights.severityTrend)}
                                sx={{ mt: 1 }}
                            />
                        </CardContent>
                    </Card>
                </Grid>

                <Grid item xs={12} sm={6} md={3}>
                    <Card>
                        <CardContent>
                            <Typography color="text.secondary">
                                Top Root Cause
                            </Typography>

                            <Typography variant="h6">
                                {topRootCause}
                            </Typography>
                        </CardContent>
                    </Card>
                </Grid>

                <Grid item xs={12} sm={6} md={3}>
                    <Card>
                        <CardContent>
                            <Typography color="text.secondary">
                                Top Owner Team
                            </Typography>

                            <Typography variant="h6">
                                {topOwnerTeam}
                            </Typography>
                        </CardContent>
                    </Card>
                </Grid>
            </Grid>

            <Grid container spacing={2}>
                <Grid item xs={12}>
                    <Card>
                        <CardContent>
                            <Typography variant="h5">
                                Top Root Causes
                            </Typography>

                            <List>
                                {(insights.topRootCauses || []).map((cause) => (
                                    <ListItem key={cause}>
                                        {cause}
                                    </ListItem>
                                ))}
                            </List>
                        </CardContent>
                    </Card>
                </Grid>

                <Grid item xs={12}>
                    <Card>
                        <CardContent>
                            <Typography variant="h5">
                                Top Owner Teams
                            </Typography>

                            <Stack spacing={1.5} sx={{ mt: 2 }}>
                                {ownerTeams.map((team) => {
                                    const active =
                                        (insights.topOwnerTeams || [])
                                                .includes(team);

                                    return (
                                        <Box
                                            key={team}
                                            sx={{
                                                display: "flex",
                                                justifyContent: "space-between",
                                                alignItems: "center",
                                                border: "1px solid",
                                                borderColor: active
                                                    ? "primary.main"
                                                    : "divider",
                                                borderRadius: 1,
                                                p: 1.5
                                            }}>
                                            <Typography>
                                                {team}
                                            </Typography>

                                            <Chip
                                                label={active ? "Trending" : "Stable"}
                                                color={active ? "primary" : "default"}
                                                size="small"
                                            />
                                        </Box>
                                    );
                                })}
                            </Stack>
                        </CardContent>
                    </Card>
                </Grid>

                <Grid item xs={12}>
                    <Card>
                        <CardContent>
                            <Typography variant="h5">
                                AI Summary
                            </Typography>

                            <Typography sx={{ mt: 1.5 }}>
                                {insights.aiSummary}
                            </Typography>
                        </CardContent>
                    </Card>
                </Grid>

                <Grid item xs={12}>
                    <Card
                        sx={{
                            backgroundColor: "#e8f5e9"
                        }}>
                        <CardContent>
                            <Typography variant="h5">
                                Recommendations
                            </Typography>

                            <Typography sx={{ mt: 1.5 }}>
                                {insights.recommendation}
                            </Typography>
                        </CardContent>
                    </Card>
                </Grid>
            </Grid>
        </Container>
    );
}

export default AiInsights;
