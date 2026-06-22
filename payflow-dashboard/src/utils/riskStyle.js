export function normalizeRiskLevel(value) {
    if (!value) {
        return "LOW";
    }

    if (value === "HIGH_RISK") {
        return "HIGH";
    }

    if (value === "MEDIUM_RISK" || value === "WARNING") {
        return "MEDIUM";
    }

    return value;
}

export function getRiskChipProps(value) {
    const riskLevel =
        normalizeRiskLevel(value);

    if (riskLevel === "CRITICAL") {
        return {
            label: riskLevel,
            sx: {
                backgroundColor: "#7f1d1d",
                color: "#fff"
            }
        };
    }

    if (riskLevel === "HIGH") {
        return {
            label: riskLevel,
            color: "error"
        };
    }

    if (riskLevel === "MEDIUM") {
        return {
            label: riskLevel,
            color: "warning"
        };
    }

    return {
        label: riskLevel,
        color: "success"
    };
}

export function getRiskHexColor(value) {
    const riskLevel =
        normalizeRiskLevel(value);

    if (riskLevel === "CRITICAL") {
        return "#7f1d1d";
    }

    if (riskLevel === "HIGH") {
        return "#d32f2f";
    }

    if (riskLevel === "MEDIUM") {
        return "#ed6c02";
    }

    return "#2e7d32";
}
