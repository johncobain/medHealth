import { useEffect, useState, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import statusService from '../../services/statusService';
import dashboardService from '../../services/dashboardService';
import appointmentService from '../../services/appointmentService';
import { useAuth } from '../../context/AuthContext';
import styles from './HomePage.module.css';
import StatsCard from '../../components/statsCard/StatsCard';
import QuickActions from '../../components/quickActions/QuickActions';
import RecentAppointments from '../../components/recentAppointments/RecentAppointments';
import { useHomeActions } from '../../hooks/useHomeActions';
import { DoctorIcon, PatientIcon, AppointmentIcon } from '../../components/icons/AppIcons';

const HomePage = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const { adminQuickActions, userQuickActions } = useHomeActions();

  const [status, setStatus] = useState(null);
  const [loading, setLoading] = useState(true);
  const [stats, setStats] = useState(null);
  const [recentAppointments, setRecentAppointments] = useState([]);

  const isAdmin = user?.role === 'ROLE_ADMIN';
  const isDoctor = user?.role === 'ROLE_DOCTOR';
  const isPatient = user?.role === 'ROLE_PATIENT';

  const loadDashboardData = useCallback(async () => {
    
    try {
      const statusData = await statusService.getStatus().catch(() => null);
      setStatus(statusData);

      const appointmentsData = await appointmentService.getRecent().catch(() => []);
      setRecentAppointments(appointmentsData);

      if (isAdmin) {
        const statsData = await dashboardService.getStats().catch(() => null);
        if (statsData) setStats(statsData);
      }
    } catch (error) {
      console.error("Erro ao carregar dashboard:", error);
    } finally {
      setLoading(false);
    }
  }, [isAdmin]);

  useEffect(() => {
    loadDashboardData();
    
    const interval = setInterval(() => {
        statusService.getStatus().then(setStatus).catch(()=>{});
    }, 20000);
    
    return () => clearInterval(interval);
  }, [loadDashboardData]);

  const handleCancelAppointment = async (id) => {
    //TODO: implementar coleta de motivo
  };

  const handleCompleteAppointment = async (id) => {
    //TODO: implementar confirmação
  };

  if (loading && !stats && recentAppointments.length === 0) {
    return <div className={styles.loadingContainer}>Carregando sistema...</div>;
  }

  return (
    <>
      <section className={styles.statusCard}>
        <h2>Status do Sistema</h2>
        {status ? (
          <div className={styles.statusGrid}>
            <div className={styles.statusItem}>
              <span className="text-light">Status:</span>
              <strong className={status.status === 'UP' ? styles.statusUp : ''}>
                {status.status}
              </strong>
            </div>
            <div className={styles.statusItem}>
              <span className="text-light">Verificado em:</span>
              <strong>{new Date().toLocaleTimeString()}</strong>
            </div>
          </div>
        ) : (
          <p className={styles.error}>Conectando ao servidor...</p>
        )}
      </section>

      {isAdmin && stats && (
        <section className={styles.statsSection}>
          <div className={styles.statsGrid}>
            <StatsCard
              icon={<DoctorIcon />}
              title="Médicos"
              mainValue={stats.doctors.total}
              mainLabel="Cadastrados"
              primaryAction={() => navigate('/doctors')}
              primaryActionLabel="Ver Todos"
              secondaryAction={() => navigate('/doctors?action=new')}
              secondaryActionLabel="Novo"
            />
            <StatsCard
              icon={<PatientIcon />}
              title="Pacientes"
              mainValue={stats.patients.total}
              mainLabel="Cadastrados"
              primaryAction={() => navigate('/patients')}
              primaryActionLabel="Ver Todos"
              secondaryAction={() => navigate('/patients?action=new')}
              secondaryActionLabel="Novo"
            />
            <StatsCard
              icon={<AppointmentIcon />}
              title="Consultas Hoje"
              mainValue={stats.appointments.today}
              mainLabel="Agendadas para hoje"
              subLabel={`Pendentes: ${stats.appointments.pending} | Total: ${stats.appointments.total}`}
              primaryAction={() => navigate('/appointments')}
              primaryActionLabel="Ver Consultas"
              secondaryAction={() => navigate('/appointments?action=new')}
              secondaryActionLabel="Nova"
            />
          </div>
        </section>
      )}

      {(isDoctor || isPatient) && (
        <section className={styles.personalStats}>
          <h2>Bem-vindo(a), {user?.fullName || 'Usuário'}</h2>
          <div className={styles.statsGrid}>
            <StatsCard
              icon={<AppointmentIcon />}
              title="Visão Geral"
              mainValue={recentAppointments.length}
              mainLabel={isDoctor ? 'Consultas recentes' : 'Histórico recente'}
              subLabel="Consulte a lista abaixo"
              primaryAction={() => navigate('/appointments')}
              primaryActionLabel="Ver Todas"
            />
          </div>
        </section>
      )}

      <QuickActions actions={isAdmin ? adminQuickActions : userQuickActions} />

      <RecentAppointments
        appointments={recentAppointments}
        onCancel={handleCancelAppointment} 
        onComplete={handleCompleteAppointment}
        isAdmin={isAdmin}
      />
    </>
  );
};

export default HomePage;
