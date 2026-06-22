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
import { useNavigate } from "react-router-dom";
import { getRiskChipProps } from "../utils/riskStyle";

function InvestigationTable({ investigations }) {
    const navigate = useNavigate();

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
                    {investigations.map((item) => {
                        const healthChipProps =
                            getRiskChipProps(item.healthScore);

                        return (
                            <TableRow
                                key={item.paymentReference}
                                hover
                                sx={{ cursor: "pointer" }}
                                onClick={() =>
                                    navigate(
                                        `/investigation/${item.paymentReference}`
                                    )
                                }>
                                <TableCell>{item.paymentReference}</TableCell>
                                <TableCell>{item.paymentStatus}</TableCell>
                                <TableCell>{item.investigationStatus}</TableCell>
                                <TableCell>
                                    <Chip
                                        {...healthChipProps}
                                        size="small"
                                        sx={healthChipProps.sx}
                                    />
                                </TableCell>
                                <TableCell>{item.totalEvents}</TableCell>
                                <TableCell>{item.totalDltEvents}</TableCell>
                            </TableRow>
                        );
                    })}
                </TableBody>
            </Table>
        </TableContainer>
    );
}

export default InvestigationTable;
