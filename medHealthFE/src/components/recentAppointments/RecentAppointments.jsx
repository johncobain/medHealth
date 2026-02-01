import styles from './RecentAppointments.module.css';

const statusLabels = {
  SCHEDULED: 'Agendada',
  ATTENDED: 'Concluída',
  CANCELLED: 'Cancelada',
};

const statusClasses = {
  SCHEDULED: 'scheduled',
  ATTENDED: 'attended',
  CANCELLED: 'cancelled',
};

const RecentAppointments = ({ appointments, onCancel, onComplete }) => {
  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('pt-BR');
  };

  const formatTime = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
  };

  if (!appointments || appointments.length === 0) {
    return (
      <section className={styles.container}>
        <h2 className={styles.title}>Consultas Recentes</h2>
        <div className={styles.emptyState}>
          <p>Nenhuma consulta encontrada.</p>
        </div>
      </section>
    );
  }
  return (
    <section className={styles.container}>
      <h2 className={styles.title}>Consultas Recentes</h2>
      <div className={styles.list}>
        {appointments.map((appointment) => (
          <div key={appointment.id} className={styles.card}>
            <div className={styles.cardHeader}>
              <span className={styles.appointmentId}>Consulta #{appointment.id}</span>
              <span className={styles.patientName}>Paciente {appointment.patient.fullName}</span>
            </div>
            <div className={styles.cardBody}>
              <div className={styles.info}>
                <span className={styles.label}>Médico:</span>
                <span>{appointment.doctor.fullName}</span>
              </div>
              <div className={styles.info}>
                <span className={styles.label}>Especialidade:</span>
                <span>{appointment.doctor.specialtyDescription}</span>
              </div>
              <div className={styles.info}>
                <span className={styles.label}>Status:</span>
                <span className={`${styles.status} ${styles[statusClasses[appointment.status]]}`}>
                  {statusLabels[appointment.status]}
                </span>
              </div>
              <div className={styles.info}>
                <span className={styles.label}>Data:</span>
                <span>{formatDate(appointment.date)}</span>
              </div>
              <div className={styles.info}>
                <span className={styles.label}>Hora:</span>
                <span>{formatTime(appointment.date)}</span>
              </div>
            </div>
            {appointment.status === 'SCHEDULED' && (
              <div className={styles.cardActions}>
                <button
                  className={styles.completeBtn}
                  onClick={() => onComplete && onComplete(appointment.id)}
                >
                  Marcar como Concluída
                </button>
                <button
                  className={styles.cancelBtn}
                  onClick={() => onCancel && onCancel(appointment.id)}
                >
                  Cancelar Consulta
                </button>
              </div>
            )}
          </div>
        ))}
      </div>
    </section>
  );
};

export default RecentAppointments;