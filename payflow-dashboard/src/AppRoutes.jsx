import { Route, Routes } from "react-router-dom";
import Dashboard from "./pages/Dashboard";
import InvestigationDetail from "./pages/InvestigationDetail";

function AppRoutes() {
  return (
    <Routes>
      <Route
        path="/"
        element={<Dashboard />}
      />

      <Route
        path="/investigation/:paymentReference"
        element={<InvestigationDetail />}
      />
    </Routes>
  );
}

export default AppRoutes;
