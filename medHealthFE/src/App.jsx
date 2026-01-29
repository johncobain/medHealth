import { Navigate, Route, Routes } from 'react-router-dom';
import HomePage from './pages/home/HomePage';
import Patients from './pages/patients/Patients';
import Doctors from './pages/doctors/Doctors';
import Appointments from './pages/appointments/Appointments';
import Settings from './pages/settings/Settings';
import NotFoundPage from './pages/notFoundPage/NotFoundPage';
import MainLayout from './layouts/mainLayout/MainLayout';
import { AuthProvider } from './context/AuthContext';
import LoginPage from './pages/login/LoginPage';
import ProtectedRoute from './components/protectedRoute/ProtectedRoute';

function App() {
  return (
    <AuthProvider>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route element={<MainLayout />}>
          <Route path="/home" element={<Navigate to="/" />} />

          <Route
            path="/"
            element={
              <ProtectedRoute>
                <HomePage />
              </ProtectedRoute>
            }
          />

          <Route
            path="/patients"
            element={
              <ProtectedRoute allowedRoles={['ROLE_ADMIN', 'ROLE_DOCTOR']}>
                <Patients />
              </ProtectedRoute>
            }
          />

          <Route
            path="/doctors"
            element={
              <ProtectedRoute allowedRoles={['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_PATIENT']}>
                <Doctors />
              </ProtectedRoute>
            }
          />

          <Route
            path="/appointments"
            element={
              <ProtectedRoute allowedRoles={['ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_PATIENT']}>
                <Appointments />
              </ProtectedRoute>
            }
          />

          <Route
            path="/settings"
            element={
              <ProtectedRoute>
                <Settings />
              </ProtectedRoute>
            }
          />
        </Route>
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </AuthProvider>
  );
}

export default App;
