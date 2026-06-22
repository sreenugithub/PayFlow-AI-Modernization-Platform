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
import {
    Bar,
    BarChart,
    CartesianGrid,
    Cell,
    Pie,
    PieChart,
    ResponsiveContainer,
    Tooltip,
    XAxis,
    YAxis
} from "recharts";
import axiosClient from "../api/axiosClient";
import {
    getRiskChipProps,
    getRiskHexColor,
    normalizeRiskLevel
} from "../utils/riskStyle";

const ownerTeams = [
    "Settlement Team",
    "Payment Operations",
    "Messaging Team"
];

function buildRankDistribution(items) {
    return (items || []).map((item, index) => ({
        name: item,
        value: items.length - index
    }));
}

function formatGeneratedAt(date) {
    const day =
        String(date.getDate()).padStart(2, "0");
    const month =
        date.toLocaleString("en-US", {
            month: "short"
        });
    const year =
        date.getFullYear();
    const time =
        date.toLocaleTimeString([], {
            hour: "2-digit",
            minute: "2-digit"
        });

    return `${day}-${month}-${year} ${time}`;
}

function ChartEmptyState({ message }) {
    return (
        <Box
            sx={{
                height: 260,
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                border: "1px dashed",
                borderColor: "divider",
                borderRadius: 1,
                color: "text.secondary",
                textAlign: "center",
                px: 2
            }}>
            <Typography>
                {message}
            </Typography>
        </Box>
    );
}

function AiInsights() {
    const navigate = useNavigate();
    const [insights, setInsights] =
        useState(null);
    const [loading, setLoading] =
        useState(true);
    const [error, setError] =
        useState("");
    const [generatedAt, setGeneratedAt] =
        useState("");

    const loadInsights = () => {
        setLoading(true);
        setError("");

        axiosClient
            .get("/api/investigation/incident-history-analysis")
            .then((response) => {
                setInsights(response.data);
                setGeneratedAt(
                    formatGeneratedAt(new Date())
                );
            })
            .catch((apiError) => {
                console.error(apiError);
                setError("Unable to load AI operational insights");
            })
            .finally(() => {
                setLoading(false);
            });
    };

    useEffect(() => {
        loadInsights();
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
    const riskLevel =
        normalizeRiskLevel(insights.severityTrend);
    const riskChipProps =
        getRiskChipProps(riskLevel);
    const rootCauseDistribution =
        buildRankDistribution(insights.topRootCauses);
    const ownerTeamDistribution =
        buildRankDistribution(insights.topOwnerTeams);
    const hasIncidentData =
        Number(insights.totalIncidents) > 0;
    const hasRootCauseData =
        hasIncidentData && rootCauseDistribution.length > 0;
    const hasOwnerTeamData =
        hasIncidentData && ownerTeamDistribution.length > 0;
    const severityDistribution =
        hasIncidentData
            ? ["LOW", "MEDIUM", "HIGH", "CRITICAL"].map((level) => ({
                name: level,
                value: level === riskLevel ? 3 : 1
            }))
            : [];

    return (
        <Container sx={{ py: 3 }}>
            <Stack
                direction={{ xs: "column", sm: "row" }}
                spacing={2}
                sx={{ mb: 2 }}>
                <Button
                    variant="outlined"
                    onClick={() => navigate("/")}>
                    Back to Dashboard
                </Button>

                <Button
                    variant="contained"
                    onClick={loadInsights}
                    disabled={loading}>
                    Refresh Insights
                </Button>
            </Stack>

            <Typography
                variant="h4"
                gutterBottom>
                AI Operational Insights
            </Typography>

            <Typography
                color="text.secondary"
                sx={{ mb: 3 }}>
                Generated at: {generatedAt}
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
                                {...riskChipProps}
                                sx={{ mt: 1, ...riskChipProps.sx }}
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
                <Grid item xs={12} md={4}>
                    <Card>
                        <CardContent>
                            <Typography variant="h5" gutterBottom>
                                Root Cause Distribution
                            </Typography>

                            {hasRootCauseData ? (
                                <Box sx={{ height: 260 }}>
                                    <ResponsiveContainer width="100%" height="100%">
                                        <BarChart data={rootCauseDistribution}>
                                            <CartesianGrid strokeDasharray="3 3" />
                                            <XAxis dataKey="name" />
                                            <YAxis allowDecimals={false} />
                                            <Tooltip />
                                            <Bar dataKey="value" fill="#1976d2" />
                                        </BarChart>
                                    </ResponsiveContainer>
                                </Box>
                            ) : (
                                <ChartEmptyState message="No root cause data found in MongoDB." />
                            )}
                        </CardContent>
                    </Card>
                </Grid>

                <Grid item xs={12} md={4}>
                    <Card>
                        <CardContent>
                            <Typography variant="h5" gutterBottom>
                                Severity Distribution
                            </Typography>

                            {hasIncidentData ? (
                                <Box sx={{ height: 260 }}>
                                    <ResponsiveContainer width="100%" height="100%">
                                        <PieChart>
                                            <Pie
                                                data={severityDistribution}
                                                dataKey="value"
                                                nameKey="name"
                                                outerRadius={90}
                                                label>
                                                {severityDistribution.map((entry) => (
                                                    <Cell
                                                        key={entry.name}
                                                        fill={getRiskHexColor(entry.name)}
                                                    />
                                                ))}
                                            </Pie>
                                            <Tooltip />
                                        </PieChart>
                                    </ResponsiveContainer>
                                </Box>
                            ) : (
                                <ChartEmptyState message="No severity data found in MongoDB." />
                            )}
                        </CardContent>
                    </Card>
                </Grid>

                <Grid item xs={12} md={4}>
                    <Card>
                        <CardContent>
                            <Typography variant="h5" gutterBottom>
                                Owner Team Distribution
                            </Typography>

                            {hasOwnerTeamData ? (
                                <Box sx={{ height: 260 }}>
                                    <ResponsiveContainer width="100%" height="100%">
                                        <BarChart data={ownerTeamDistribution}>
                                            <CartesianGrid strokeDasharray="3 3" />
                                            <XAxis dataKey="name" />
                                            <YAxis allowDecimals={false} />
                                            <Tooltip />
                                            <Bar dataKey="value" fill="#9c27b0" />
                                        </BarChart>
                                    </ResponsiveContainer>
                                </Box>
                            ) : (
                                <ChartEmptyState message="No owner team data found in MongoDB." />
                            )}
                        </CardContent>
                    </Card>
                </Grid>

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

                            <Grid container spacing={2} sx={{ mt: 0.5 }}>
                                <Grid item xs={12} md={4}>
                                    <Box
                                        sx={{
                                            border: "1px solid",
                                            borderColor: "divider",
                                            borderRadius: 1,
                                            height: "100%",
                                            p: 2
                                        }}>
                                        <Typography
                                            variant="subtitle1"
                                            fontWeight={700}>
                                            Business Risk
                                        </Typography>

                                        <Chip
                                            {...riskChipProps}
                                            sx={{ mt: 1, ...riskChipProps.sx }}
                                        />

                                        <Typography sx={{ mt: 1.5 }}>
                                            {insights.severityTrend}
                                        </Typography>
                                    </Box>
                                </Grid>

                                <Grid item xs={12} md={4}>
                                    <Box
                                        sx={{
                                            border: "1px solid",
                                            borderColor: "divider",
                                            borderRadius: 1,
                                            height: "100%",
                                            p: 2
                                        }}>
                                        <Typography
                                            variant="subtitle1"
                                            fontWeight={700}>
                                            Key Findings
                                        </Typography>

                                        <Typography sx={{ mt: 1.5 }}>
                                            {insights.aiSummary}
                                        </Typography>
                                    </Box>
                                </Grid>

                                <Grid item xs={12} md={4}>
                                    <Box
                                        sx={{
                                            border: "1px solid",
                                            borderColor: "divider",
                                            borderRadius: 1,
                                            height: "100%",
                                            p: 2
                                        }}>
                                        <Typography
                                            variant="subtitle1"
                                            fontWeight={700}>
                                            AI Recommendation
                                        </Typography>

                                        <Typography sx={{ mt: 1.5 }}>
                                            {insights.recommendation}
                                        </Typography>
                                    </Box>
                                </Grid>
                            </Grid>
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
