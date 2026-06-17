import {
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    Chip,
    Typography
} from "@mui/material";

function getHealthColor(healthScore) {
    if (healthScore === "CRITICAL") return "error";
    if (healthScore === "WARNING") return "warning";
    return "success";
}

function InvestigationTable({ investigations }) {
    if (!investigations || investigations.length === 0) {
        return <Typography>No investigations found</Typography>;
    }

    return (
        <TableContainer component={Paper}>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell>Payment Reference</TableCell>
                        <TableCell>Payment Status</TableCell>
                        <TableCell>Investigation Status</TableCell>
                        <TableCell>Health Score</TableCell>
                        <TableCell>Total Events</TableCell>
                        <TableCell>DLT Events</TableCell>
                    </TableRow>
                </TableHead>

                <TableBody>
                    {investigations.map((item) => (
                        <TableRow key={item.paymentReference}>
                            <TableCell>{item.paymentReference}</TableCell>
                            <TableCell>{item.paymentStatus}</TableCell>
                            <TableCell>{item.investigationStatus}</TableCell>
                            <TableCell>
                                <Chip
                                    label={item.healthScore}
                                    color={getHealthColor(item.healthScore)}
                                    size="small"
                                />
                            </TableCell>
                            <TableCell>{item.totalEvents}</TableCell>
                            <TableCell>{item.totalDltEvents}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
}

export default InvestigationTable;