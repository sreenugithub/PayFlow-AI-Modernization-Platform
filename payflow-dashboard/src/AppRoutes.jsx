import { Navigate, Route, Routes } from "react-router-dom";
import AiInsights from "./pages/AiInsights";
import Dashboard from "./pages/Dashboard";
import InvestigationDetail from "./pages/InvestigationDetail";

function AppRoutes() {
  return (
    <Routes>
        <Route
            path="/ai-insights"
            element={<AiInsights />}
        />
      <Route
        path="/"
        element={<Dashboard />}
      />

      <Route
        path="/investigation/:paymentReference"
        element={<InvestigationDetail />}
      />

      <Route
        path="/investigation"
        element={<Dashboard />}
      />

      <Route
        path="*"
        element={<Navigate to="/" replace />}
      />
    </Routes>
  );
}

export default AppRoutes;
