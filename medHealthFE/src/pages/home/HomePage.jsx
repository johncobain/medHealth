import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import statusService from '../../services/statusService';
import { useAuth } from '../../context/AuthContext';
import styles from './HomePage.module.css';
import StatsCard from '../../components/statsCard/StatsCard';
import QuickActions from '../../components/quickActions/QuickActions';
import RecentAppointments from '../../components/recentAppointments/RecentAppointments';

const HomePage = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [status, setStatus] = useState(null);
  const [loading, setLoading] = useState(true);
  const [stats, setStats] = useState({
    doctors: { total: 0},
    patients: { total: 0},
    appointments: { total: 0, pending: 0, attended: 0, cancelled: 0, today: 0 },
  });
  const [recentAppointments, setRecentAppointments] = useState([]);

  const isAdmin = user?.role === 'ROLE_ADMIN';
  const isDoctor = user?.role === 'ROLE_DOCTOR';
  const isPatient = user?.role === 'ROLE_PATIENT';

  useEffect(() => {
    const fetchStatus = async () => {
      try {
        const data = await statusService.getStatus();
        setStatus(data);
      } catch (error) {
        console.error('Erro ao buscar status do servidor:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchStatus();
    const interval = setInterval(fetchStatus, 5000);
    return () => clearInterval(interval);
  }, []);

  // Mock data - Replace with actual API calls
  useEffect(() => {
    // TODO: Replace with actual API calls
    setStats({
      doctors: { total: 12 },
      patients: { total: 245 },
      appointments: { total: 100, pending: 7, attended: 10, cancelled: 3, today: 18 },
    });

    setRecentAppointments([
      {
        id: 1,
        patientName: 'Maria Silva',
        doctorName: 'Dr. João Santos',
        specialty: 'Cardiologia',
        status: 'SCHEDULED',
        date: '2026-01-29T14:30:00',
      },
      {
        id: 2,
        patientName: 'Carlos Oliveira',
        doctorName: 'Dra. Ana Paula',
        specialty: 'Dermatologia',
        status: 'ATTENDED',
        date: '2026-01-29T15:00:00',
      },
      {
        id: 3,
        patientName: 'Lucia Fernandes',
        doctorName: 'Dr. Pedro Lima',
        specialty: 'Ortopedia',
        status: 'CANCELLED',
        date: '2026-01-29T10:00:00',
      },
    ]);
  }, []);

  const adminQuickActions = [
    {
      label: 'Cadastrar Paciente',
      variant: 'primary',
      icon: (
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="18"
          height="18"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
        >
          <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2" />
          <circle cx="9" cy="7" r="4" />
          <line x1="19" x2="19" y1="8" y2="14" />
          <line x1="22" x2="16" y1="11" y2="11" />
        </svg>
      ),
      onClick: () => navigate('/patients?action=new'),
    },
    {
      label: 'Cadastrar Médico',
      variant: 'secondary',
      icon: (
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="18"
          height="18"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
        >
          <path d="M3 9a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2" />
          <path d="M3 18a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2" />
          <path d="M3 9v9" />
          <path d="M21 9v9" />
          <path d="M12 3v6" />
          <path d="M9 6h6" />
        </svg>
      ),
      onClick: () => navigate('/doctors?action=new'),
    },
    {
      label: 'Nova Consulta',
      variant: 'outline',
      icon: (
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="18"
          height="18"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
        >
          <path d="M8 2v4" />
          <path d="M16 2v4" />
          <rect width="18" height="18" x="3" y="4" rx="2" />
          <path d="M3 10h18" />
          <path d="M12 14v4" />
          <path d="M10 16h4" />
        </svg>
      ),
      onClick: () => navigate('/appointments?action=new'),
    },
  ];

  // Doctor/Patient quick actions
  const userQuickActions = [
    {
      label: 'Minhas Consultas',
      variant: 'primary',
      icon: (
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="18"
          height="18"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
        >
          <path d="M8 2v4" />
          <path d="M16 2v4" />
          <rect width="18" height="18" x="3" y="4" rx="2" />
          <path d="M3 10h18" />
        </svg>
      ),
      onClick: () => navigate('/appointments'),
    },
    {
      label: 'Meu Perfil',
      variant: 'outline',
      icon: (
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="18"
          height="18"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
        >
          <circle cx="12" cy="8" r="5" />
          <path d="M20 21a8 8 0 0 0-16 0" />
        </svg>
      ),
      onClick: () => navigate('/settings'),
    },
  ];

  const handleConfirmAppointment = (id) => {
    console.log('Confirmar consulta:', id);
    // TODO: Implement API call
  };

  const handleCompleteAppointment = (id) => {
    console.log('Marcar como concluída:', id);
    // TODO: Implement API call
  };

  // Icons for stats cards
  const DoctorIcon = () => (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width="18"
      height="18"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      <path d="M3 9a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2" />
      <path d="M3 18a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2" />
      <path d="M3 9v9" />
      <path d="M21 9v9" />
      <path d="M12 3v6" />
      <path d="M9 6h6" />
    </svg>
  );

  const PatientIcon = () => (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width="18"
      height="18"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2" />
      <circle cx="9" cy="7" r="4" />
      <path d="M22 21v-2a4 4 0 0 0-3-3.87" />
      <path d="M16 3.13a4 4 0 0 1 0 7.75" />
    </svg>
  );

  const AppointmentIcon = () => (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width="18"
      height="18"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      <path d="M8 2v4" />
      <path d="M16 2v4" />
      <rect width="18" height="18" x="3" y="4" rx="2" />
      <path d="M3 10h18" />
    </svg>
  );

  return (
    <>
      <section className={styles.statusCard}>
        <h2>Status do Sistema</h2>
        {loading ? (
          <p>Carregando status...</p>
        ) : status ? (
          <div className={styles.statusGrid}>
            <div className={styles.statusItem}>
              <span className="text-light">Status:</span>
              <strong className={status.status === 'UP' ? styles.statusUp : ''}>
                {status.status}
              </strong>
            </div>
            <div className={styles.statusItem}>
              <span className="text-light">Timestamp:</span>
              <strong>{new Date(status.updated_at).toLocaleString('pt-BR')}</strong>
            </div>
          </div>
        ) : (
          <p className={styles.error}>Erro ao carregar status do sistema.</p>
        )}
      </section>

      {isAdmin && (
        <section className={styles.statsSection}>
          <div className={styles.statsGrid}>
            <StatsCard
              icon={<DoctorIcon />}
              title="Médicos"
              mainValue={stats.doctors.total}
              mainLabel="Total de Médicos Cadastrados"
              primaryAction={() => navigate('/doctors')}
              primaryActionLabel="Ver Todos"
              secondaryAction={() => navigate('/doctors?action=new')}
              secondaryActionLabel="Novo"
            />
            <StatsCard
              icon={<PatientIcon />}
              title="Pacientes"
              mainValue={stats.patients.total}
              mainLabel="Total de Pacientes Cadastrados"
              primaryAction={() => navigate('/patients')}
              primaryActionLabel="Ver Todos"
              secondaryAction={() => navigate('/patients?action=new')}
              secondaryActionLabel="Novo"
            />
            <StatsCard
              icon={<AppointmentIcon />}
              title="Consultas"
              mainValue={stats.appointments.today}
              mainLabel="Total de Consultas Pendentes para Hoje"
              subLabel={`
                Total: ${stats.appointments.total} | 
                Pendentes: ${stats.appointments.pending} | 
                Atendidas: ${stats.appointments.attended} | 
                Canceladas: ${stats.appointments.cancelled}
                `}
              primaryAction={() => navigate('/appointments')}
              primaryActionLabel="Ver Consultas"
              secondaryAction={() => navigate('/appointments?action=new')}
              secondaryActionLabel="Nova Consulta"
            />
          </div>
        </section>
      )}

      {(isDoctor || isPatient) && (
        <section className={styles.personalStats}>
          <h2>Minhas Estatísticas</h2>
          <div className={styles.statsGrid}>
            <StatsCard
              icon={<AppointmentIcon />}
              title="Minhas Consultas"
              mainValue={recentAppointments.length}
              mainLabel={isDoctor ? 'Consultas Agendadas' : 'Histórico de Consultas'}
              subLabel={isDoctor ? 'Próximas consultas do dia' : 'Consultas realizadas'}
              primaryAction={() => navigate('/appointments')}
              primaryActionLabel="Ver Todas"
              secondaryAction={() => navigate('/appointments?action=new')}
              secondaryActionLabel="Agendar"
            />
          </div>
        </section>
      )}

      <QuickActions actions={isAdmin ? adminQuickActions : userQuickActions} />

      <RecentAppointments
        appointments={recentAppointments}
        onConfirm={handleConfirmAppointment}
        onComplete={handleCompleteAppointment}
        isAdmin={isAdmin}
      />
    </>
  );
};

export default HomePage;
