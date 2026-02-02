import { useEffect, useState, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import statusService from '../../services/statusService';
import dashboardService from '../../services/dashboardService';
import appointmentService from '../../services/appointmentService';
import { useAuth } from '../../context/AuthContext';
import styles from './HomePage.module.css';
import StatsCard from '../../components/statsCard/StatsCard';
import QuickActions from '../../components/quickActions/QuickActions';
import RecentAppointments from '../../components/recentAppointments/RecentAppointments';
import Modal from '../../components/modal/Modal';
import Button from '../../components/button/Button';
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
  const [totalAppointments, setTotalAppointments] = useState(0);
  
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [selectedAppointmentId, setSelectedAppointmentId] = useState(null);
  const [cancelReason, setCancelReason] = useState('');
  const [cancelMessage, setCancelMessage] = useState('');
  const [cancellationReasons, setCancellationReasons] = useState([]);
  const [submitLoading, setSubmitLoading] = useState(false);

  const isAdmin = user?.role === 'ROLE_ADMIN';
  const isDoctor = user?.role === 'ROLE_DOCTOR';
  const isPatient = user?.role === 'ROLE_PATIENT';

  const loadDashboardData = useCallback(async () => {
    
    try {
      const statusData = await statusService.getStatus().catch(() => null);
      setStatus(statusData);

      const recentData = await appointmentService.getRecent().catch(() => []);
      setRecentAppointments(recentData);
      
      const reasonsData = await appointmentService.getCancellationReasons().catch(() => []);
      setCancellationReasons(reasonsData);

      if (isDoctor || isPatient) {
         const allAppointmentsData = await appointmentService.getAll({ size: 1 }).catch(() => ({ total: 0 }));
         setTotalAppointments(allAppointmentsData.total);
      }

      if (isAdmin) {
        const statsData = await dashboardService.getStats().catch(() => null);
        if (statsData) setStats(statsData);
      }
    } catch (error) {
      toast.error('Erro ao carregar o dashboard. Tente novamente.');
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
    setSelectedAppointmentId(id);
    setCancelReason('');
    setCancelMessage('');
    setShowCancelModal(true);
  };

  const handleCompleteAppointment = async (id) => {
    if (!window.confirm('Deseja marcar esta consulta como concluída?')) return;
    
    try {
      await appointmentService.complete(id);
      toast.success('Consulta marcada como concluída.');
      loadDashboardData();
    } catch (error) {
      toast.error('Erro ao concluir consulta.');
    }
  };
  
  const confirmCancelAppointment = async () => {
    if (!cancelReason) {
      toast.error('Selecione um motivo de cancelamento.');
      return;
    }

    if (cancelReason === 'OTHER' && !cancelMessage.trim()) {
      toast.error('Mensagem é obrigatória para o motivo "Outro".');
      return;
    }

    setSubmitLoading(true);
    try {
      await appointmentService.cancel(selectedAppointmentId, cancelReason, cancelMessage);
      toast.success('Consulta cancelada com sucesso.');
      setShowCancelModal(false);
      loadDashboardData();
    } catch (error) {
      toast.error('Erro ao cancelar consulta.');
    } finally {
      setSubmitLoading(false);
    }
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
              secondaryActionLabel="Cadastrar"
            />
            <StatsCard
              icon={<PatientIcon />}
              title="Pacientes"
              mainValue={stats.patients.total}
              mainLabel="Cadastrados"
              primaryAction={() => navigate('/patients')}
              primaryActionLabel="Ver Todos"
              secondaryAction={() => navigate('/patients?action=new')}
              secondaryActionLabel="Cadastrar"
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
              secondaryActionLabel="Agendar"
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
              mainValue={totalAppointments}
              mainLabel={isDoctor ? 'Consultas totais' : 'Histórico de Consultas'}
              subLabel="Consulte os últimos registros abaixo"
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
        onCancel={handleCancelAppointment} 
        onComplete={handleCompleteAppointment}
        isAdmin={isAdmin}
      />
      
      <Modal
        isOpen={showCancelModal}
        onClose={() => setShowCancelModal(false)}
        title="Cancelar Consulta"
        size="md"
      >
        <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
          <div>
            <label className="block text-sm mb-xs">Motivo *</label>
            <select
              className="input"
              value={cancelReason}
              onChange={(e) => setCancelReason(e.target.value)}
              required
            >
              <option value="">Selecione...</option>
              {cancellationReasons.map((reason) => (
                <option key={reason.cancellationReason} value={reason.cancellationReason}>
                  {reason.cancellationReasonDescription}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-sm mb-xs">
              Mensagem {cancelReason === 'OTHER' && '*'}
            </label>
            <textarea
              className="input"
              rows={4}
              placeholder="Descreva o motivo do cancelamento..."
              value={cancelMessage}
              onChange={(e) => setCancelMessage(e.target.value)}
              required={cancelReason === 'OTHER'}
            />
          </div>

          <div style={{ display: 'flex', gap: '0.75rem', justifyContent: 'flex-end' }}>
            <Button variant="outline" onClick={() => setShowCancelModal(false)}>
              Voltar
            </Button>
            <Button variant="primary" onClick={confirmCancelAppointment} disabled={submitLoading}>
              {submitLoading ? 'Cancelando...' : 'Confirmar Cancelamento'}
            </Button>
          </div>
        </div>
      </Modal>
    </>
  );
};

export default HomePage;
