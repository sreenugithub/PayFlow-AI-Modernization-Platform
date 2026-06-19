import { Grid, Card, CardContent, Typography } from "@mui/material";

function SummaryCards({ summary }) {

    if (!summary) return null;

    return (
        <Grid container spacing={2} sx={{ mb: 4 }}>

            <Grid item xs={3}>
                <Card>
                    <CardContent>
                        <Typography variant="h6">
                            Total Payments
                        </Typography>
                        <Typography variant="h4">
                            {summary.totalPayments}
                        </Typography>
                    </CardContent>
                </Card>
            </Grid>

            <Grid item xs={3}>
                <Card>
                    <CardContent>
                        <Typography variant="h6">
                            Healthy
                        </Typography>
                        <Typography variant="h4">
                            {summary.healthyPayments}
                        </Typography>
                    </CardContent>
                </Card>
            </Grid>

            <Grid item xs={3}>
                <Card>
                    <CardContent>
                        <Typography variant="h6">
                            Medium
                        </Typography>
                        <Typography variant="h4">
                            {summary.warningPayments}
                        </Typography>
                    </CardContent>
                </Card>
            </Grid>

            <Grid item xs={3}>
                <Card>
                    <CardContent>
                        <Typography variant="h6">
                            Critical
                        </Typography>
                        <Typography variant="h4">
                            {summary.criticalPayments}
                        </Typography>
                    </CardContent>
                </Card>
            </Grid>

        </Grid>
    );
}

export default SummaryCards;
