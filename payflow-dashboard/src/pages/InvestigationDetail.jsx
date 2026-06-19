import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {
    Alert,
    Box,
    Button,
    Card,
    CardContent,
    Chip,
    Divider,
    List,
    ListItem,
    ListItemText,
    Stack,
    Typography
} from "@mui/material";
import axiosClient from "../api/axiosClient";

function InvestigationDetail() {

    const { paymentReference } =
        useParams();
    const navigate = useNavigate();
    const [investigation, setInvestigation] =
        useState(null);
    const [aiResponse, setAiResponse] =
        useState(null);
    const [error, setError] =
        useState("");
    const [loading, setLoading] =
        useState(false);

    const getSeverityColor = (severity) => {
        if (severity === "CRITICAL" || severity === "HIGH") {
            return "error";
        }

        if (severity === "MEDIUM") {
            return "warning";
        }

        return "success";
    };

    const getConfidenceColor = (confidence) => {
        if (confidence === "HIGH") {
            return "success";
        }

        if (confidence === "MEDIUM") {
            return "warning";
        }

        return "default";
    };

    useEffect(() => {
        setError("");

        axiosClient
            .get(`/api/investigation/${paymentReference}`)
            .then((response) => {
                setInvestigation(response.data);
            })
            .catch((apiError) => {
                console.error(apiError);
                setError("Unable to load investigation detail");
            });
    }, [paymentReference]);

    const runAiAnalysis = () => {
        setLoading(true);
        setError("");

        axiosClient
            .get(`/api/investigation/${paymentReference}/ai-analysis`)
            .then((response) => {
                setAiResponse(response.data);
            })
            .catch((apiError) => {
                console.error(apiError);
                setError("Unable to run AI analysis");
            })
            .finally(() => setLoading(false));
    };

    return (
        <Box sx={{ padding: "18px" }}>
            <Button
                variant="outlined"
                sx={{ mb: 2 }}
                onClick={() => navigate("/")}>
                Back to Dashboard
            </Button>

            <Typography
                variant="h4"
                component="h2"
                gutterBottom
                sx={{ color: "cyan" }}>
                Investigation Detail
            </Typography>

            <Typography sx={{ marginBottom: 2 }}>
                paymentReference :  {paymentReference}
            </Typography>

            {investigation && (
                <Card sx={{ mb: 3 }}>
                    <CardContent>
                        <Stack
                            direction={{ xs: "column", sm: "row" }}
                            spacing={2}
                            sx={{ mb: 3 }}>
                            <Box sx={{ flex: 1 }}>
                                <Typography variant="h6"  color="text.secondary">
                                    Payment Status
                                </Typography>
                                <Typography variant="h6">
                                    {investigation.paymentStatus}
                                </Typography>
                            </Box>

                            <Box sx={{ flex: 1 }}>
                                <Typography variant="h6" color="text.secondary">
                                    Investigation Status
                                </Typography>
                                <Typography variant="h6">
                                    {investigation.investigationStatus}
                                </Typography>
                            </Box>
                        </Stack>

                        <Typography variant="h6" gutterBottom>
                            Timeline
                        </Typography>

                        <List disablePadding>
                            {(investigation.timeline || []).map((event, index) => (
                                <Box key={`${event.timestamp}-${event.eventType}-${index}`}>
                                    <ListItem alignItems="flex-start" disableGutters>
                                        <ListItemText
                                            primary={
                                                <Stack
                                                    direction={{ xs: "column", sm: "row" }}
                                                    spacing={1}
                                                    alignItems={{ xs: "flex-start", sm: "center" }}>
                                                    <Typography fontWeight={600}>
                                                        {event.eventType}
                                                    </Typography>
                                                    <Chip
                                                        label={event.status}
                                                        size="small"
                                                        variant="outlined"
                                                    />
                                                    {event.severity && (
                                                        <Chip
                                                            label={event.severity}
                                                            size="small"
                                                            color={
                                                                event.severity === "CRITICAL"
                                                                    ? "error"
                                                                    : "default"
                                                            }
                                                        />
                                                    )}
                                                </Stack>
                                            }
                                            secondary={
                                                <>
                                                    <Typography
                                                        component="span"
                                                        display="block"
                                                        color="text.secondary">
                                                        {event.timestamp} | {event.source}
                                                    </Typography>
                                                    <Typography
                                                        component="span"
                                                        display="block"
                                                        color="text.primary">
                                                        {event.description}
                                                    </Typography>
                                                    {event.recommendation && (
                                                        <Typography
                                                            component="span"
                                                            display="block"
                                                            color="text.secondary">
                                                            Recommendation: {event.recommendation}
                                                        </Typography>
                                                    )}
                                                </>
                                            }
                                        />
                                    </ListItem>
                                    {index < (investigation.timeline || []).length - 1 && (
                                        <Divider />
                                    )}
                                </Box>
                            ))}
                        </List>
                    </CardContent>
                </Card>
            )}

            <Button
                variant="contained"
                color="error"
                onClick={runAiAnalysis}
                disabled={loading}
                sx={{
                    animation: loading
                        ? "none"
                        : "aiPulse 1.4s ease-in-out infinite",
                    "@keyframes aiPulse": {
                        "0%": {
                            transform: "scale(1)",
                            boxShadow: "0 0 0 0 rgba(211, 47, 47, 0.45)"
                        },
                        "50%": {
                            transform: "scale(1.04)",
                            boxShadow: "0 0 0 8px rgba(211, 47, 47, 0)"
                        },
                        "100%": {
                            transform: "scale(1)",
                            boxShadow: "0 0 0 0 rgba(211, 47, 47, 0)"
                        }
                    }
                }}>
                Analyze With AI
            </Button>

            {error && (
                <Alert severity="error" sx={{ marginTop: 2 }}>
                    {error}
                </Alert>
            )}

            {aiResponse && (
                <Card
                    sx={{
                        mt: 3,
                        borderLeft: "5px solid",
                        borderColor: "success.main"
                    }}>
                    <CardContent>
                        <Typography variant="h5" sx={{ mb: 2 }}>
                            AI Analysis
                        </Typography>

                        <Box sx={{ mb: 2 }}>
                            <Typography
                                variant="subtitle2"
                                color="text.secondary"
                                sx={{ mb: 0.5 }}>
                                Summary
                            </Typography>
                            <Typography>
                                {aiResponse.summary}
                            </Typography>
                        </Box>

                        <Stack
                            direction={{ xs: "column", sm: "row" }}
                            spacing={2}
                            sx={{ mb: 2 }}>
                            <Box>
                                <Typography
                                    variant="subtitle2"
                                    color="text.secondary"
                                    sx={{ mb: 0.5 }}>
                                    Severity
                                </Typography>
                                <Chip
                                    label={aiResponse.severity}
                                    color={getSeverityColor(aiResponse.severity)}
                                    size="small"
                                />
                            </Box>

                            <Box>
                                <Typography
                                    variant="subtitle2"
                                    color="text.secondary"
                                    sx={{ mb: 0.5 }}>
                                    Confidence
                                </Typography>
                                <Chip
                                    label={aiResponse.confidence}
                                    color={getConfidenceColor(aiResponse.confidence)}
                                    size="small"
                                />
                            </Box>

                            <Box>
                                <Typography
                                    variant="subtitle2"
                                    color="text.secondary"
                                    sx={{ mb: 0.5 }}>
                                    Owner Team
                                </Typography>
                                <Chip
                                    label={aiResponse.ownerTeam}
                                    color="primary"
                                    size="small"
                                    variant="outlined"
                                />
                            </Box>
                        </Stack>

                        <Box
                            sx={{
                                backgroundColor: "success.light",
                                borderRadius: 1,
                                p: 2
                            }}>
                            <Typography
                                variant="subtitle2"
                                color="success.dark"
                                sx={{ mb: 0.5 }}>
                                Recommendation
                            </Typography>
                            <Typography color="success.dark" fontWeight={600}>
                                {aiResponse.recommendedAction}
                            </Typography>
                        </Box>
                    </CardContent>
                </Card>
            )}
        </Box>
    );
}

export default InvestigationDetail;
