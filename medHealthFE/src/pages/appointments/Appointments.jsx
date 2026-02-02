import { useState, useEffect, useCallback } from 'react';
import { toast } from 'react-toastify';
import { useSearchParams } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import appointmentService from '../../services/appointmentService';
import patientService from '../../services/patientService';
import doctorService from '../../services/doctorService';
import Button from '../../components/button/Button';
import Modal from '../../components/modal/Modal';
import EntitySelector from '../../components/entitySelector/EntitySelector';
import { extractErrorMessage } from '../../utils/errorHandler';
import styles from './Appointments.module.css';

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

const emptyForm = () => ({
  patientId: null,
  patientName: '',
  doctorId: null,
  doctorName: '',
  specialty: '',
  date: '',
});

const Appointments = () => {
  const { user } = useAuth();
  const [searchParams, setSearchParams] = useSearchParams();
  const isAdmin = user?.role === 'ROLE_ADMIN';
  const isDoctor = user?.role === 'ROLE_DOCTOR';
  const isPatient = user?.role === 'ROLE_PATIENT';

  const [list, setList] = useState({ content: [], total: 0, totalPages: 0 });
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(() => parseInt(searchParams.get('page') || '0', 10));
  const [size] = useState(10);

  const [modal, setModal] = useState(null);
  const [formData, setFormData] = useState(emptyForm());
  const [submitLoading, setSubmitLoading] = useState(false);
  const [detailAppointment, setDetailAppointment] = useState(null);
  
  const [cancelReason, setCancelReason] = useState('');
  const [cancelMessage, setCancelMessage] = useState('');
  const [cancellationReasons, setCancellationReasons] = useState([]);
  
  const [specialties, setSpecialties] = useState([]);
  const [selectorType, setSelectorType] = useState(null);

  const fetchList = useCallback(async () => {
    setLoading(true);
    try {
      const data = await appointmentService.getAll({ page, size, sort: 'date,desc' });
      setList(data);
    } catch (err) {
      toast.error(extractErrorMessage(err, 'Erro ao carregar consultas.'));
    } finally {
      setLoading(false);
    }
  }, [page, size]);

  const fetchCancellationReasons = useCallback(async () => {
    try {
      const data = await appointmentService.getCancellationReasons();
      setCancellationReasons(data);
    } catch (err) {
      toast.error('Erro ao carregar motivos de cancelamento.');
    }
  }, []);

  const fetchSpecialties = useCallback(async () => {
    try {
      const data = await doctorService.getSpecialties();
      setSpecialties(data);
    } catch (err) {
      toast.error('Erro ao carregar especialidades.');
    }
  }, []);

  useEffect(() => {
    fetchList();
    fetchCancellationReasons();
    fetchSpecialties();
  }, [fetchList, fetchCancellationReasons, fetchSpecialties]);

  useEffect(() => {
    const p = searchParams.get('page');
    if (p !== null) setPage(parseInt(p, 10) || 0);
  }, [searchParams]);

  useEffect(() => {
    const action = searchParams.get('action');
    if (action === 'new' && !modal) {
      openCreate();
      setSearchParams((prev) => {
        const next = new URLSearchParams(prev);
        next.delete('action');
        return next;
      });
    }
  }, [searchParams, modal]);

  const openCreate = async () => {
    const newForm = emptyForm();
    
    if (isPatient && user) {
      try {
        const patientData = await patientService.getMyData();
        newForm.patientId = patientData.id;
        newForm.patientName = patientData.fullName;
      } catch (err) {
        console.error('Erro ao carregar dados do paciente', err);
      }
    }
    
    if (isDoctor && user) {
      try {
        const doctorData = await doctorService.getMyData();
        newForm.doctorId = doctorData.id;
        newForm.doctorName = doctorData.fullName;
      } catch (err) {
        console.error('Erro ao carregar dados do médico', err);
      }
    }
    
    setFormData(newForm);
    setDetailAppointment(null);
    setModal('form');
  };

  const openDetail = (row) => {
    setDetailAppointment(row);
    setModal('detail');
  };

  const openCancelModal = (row) => {
    setDetailAppointment(row);
    setCancelReason('');
    setCancelMessage('');
    setModal('cancel');
  };

  const closeModal = () => {
    setModal(null);
    setDetailAppointment(null);
    setSelectorType(null);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    
    if (name === 'specialty' && value) {
      setFormData((prev) => ({ 
        ...prev, 
        [name]: value,
        doctorId: null,
        doctorName: ''
      }));
    } else {
      setFormData((prev) => ({ ...prev, [name]: value }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!formData.date) {
      toast.error('Data e hora são obrigatórias.');
      return;
    }

    if (!formData.patientId) {
      toast.error('Selecione um paciente.');
      return;
    }

    if (!formData.doctorId && !formData.specialty) {
      toast.error('Selecione um médico ou uma especialidade.');
      return;
    }

    setSubmitLoading(true);
    try {
      const [datePart, timePart] = formData.date.split('T');
      const localDate = new Date(`${datePart}T${timePart}`);
      
      const adjustedDate = new Date(localDate.getTime());
      const isoDate = adjustedDate.toISOString();
      
      const payload = {
        patientId: formData.patientId,
        date: isoDate,
      };

      if (formData.doctorId) {
        payload.doctorId = formData.doctorId;
      } else {
        payload.specialty = formData.specialty;
      }

      await appointmentService.create(payload);
      toast.success('Consulta agendada com sucesso.');
      closeModal();
      fetchList();
    } catch (err) {
      toast.error(extractErrorMessage(err, 'Erro ao agendar consulta.'));
    } finally {
      setSubmitLoading(false);
    }
  };

  const handleCancel = async () => {
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
      await appointmentService.cancel(detailAppointment.id, cancelReason, cancelMessage);
      toast.success('Consulta cancelada com sucesso.');
      closeModal();
      fetchList();
    } catch (err) {
      toast.error(extractErrorMessage(err, 'Erro ao cancelar consulta.'));
    } finally {
      setSubmitLoading(false);
    }
  };

  const handleComplete = async (id) => {
    if (!window.confirm('Deseja marcar esta consulta como concluída?')) return;
    
    try {
      await appointmentService.complete(id);
      toast.success('Consulta marcada como concluída.');
      fetchList();
    } catch (err) {
      toast.error(extractErrorMessage(err, 'Erro ao concluir consulta.'));
    }
  };

  const goToPage = (newPage) => {
    setPage(newPage);
    setSearchParams((prev) => {
      const next = new URLSearchParams(prev);
      next.set('page', String(newPage));
      return next;
    });
  };

  const formatDateTime = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
      hour12: false,
    });
  };

  const isPastAppointment = (dateString) => {
    return new Date(dateString) < new Date();
  };

  const openPatientSelector = () => {
    setSelectorType('patient');
  };

  const openDoctorSelector = () => {
    setSelectorType('doctor');
  };

  const handlePatientSelect = (patient) => {
    setFormData((prev) => ({
      ...prev,
      patientId: patient.id,
      patientName: patient.fullName,
    }));
    setSelectorType(null);
  };

  const handleDoctorSelect = (doctor) => {
    setFormData((prev) => ({
      ...prev,
      doctorId: doctor.id,
      doctorName: doctor.fullName,
      specialty: '', // Limpa especialidade ao selecionar médico
    }));
    setSelectorType(null);
  };

  const clearDoctorSelection = () => {
    setFormData((prev) => ({
      ...prev,
      doctorId: null,
      doctorName: '',
    }));
  };

  const canCreateAppointment = isAdmin || isDoctor || isPatient;

  return (
    <>
      <div className={styles.header}>
        <h2 className={styles.title}>Consultas</h2>
        {canCreateAppointment && (
          <Button variant="primary" onClick={openCreate}>
            Nova Consulta
          </Button>
        )}
      </div>

      {loading ? (
        <div className={styles.loading}>Carregando...</div>
      ) : list.content.length === 0 ? (
        <div className={styles.empty}>Nenhuma consulta encontrada.</div>
      ) : (
        <>
          <div className={styles.tableWrap}>
            <table className={styles.table}>
              <thead>
                <tr>
                  <th>Paciente</th>
                  <th>Médico</th>
                  <th>Especialidade</th>
                  <th>Data/Hora</th>
                  <th>Status</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {list.content.map((row) => (
                  <tr key={row.id}>
                    <td>{row.patient.fullName}</td>
                    <td>{row.doctor.fullName}</td>
                    <td>{row.doctor.specialtyDescription}</td>
                    <td>{formatDateTime(row.date)}</td>
                    <td>
                      <span className={`${styles.status} ${styles[statusClasses[row.status]]}`}>
                        {statusLabels[row.status]}
                      </span>
                    </td>
                    <td>
                      <div className={styles.actions}>
                        <Button variant="outline" size="sm" onClick={() => openDetail(row)}>
                          Ver
                        </Button>
                        {row.status === 'SCHEDULED' && (
                          <>
                            {isPastAppointment(row.date) && (
                              <Button
                                variant="outline"
                                size="sm"
                                onClick={() => handleComplete(row.id)}
                              >
                                Concluir
                              </Button>
                            )}
                            <Button
                              variant="outline"
                              size="sm"
                              onClick={() => openCancelModal(row)}
                            >
                              Cancelar
                            </Button>
                          </>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          {list.totalPages > 1 && (
            <div className={styles.pagination}>
              <span className={styles.paginationInfo}>
                Página {page + 1} de {list.totalPages} ({list.total} itens)
              </span>
              <div className={styles.paginationBtns}>
                <Button variant="outline" size="sm" disabled={page <= 0} onClick={() => goToPage(page - 1)}>
                  Anterior
                </Button>
                <Button
                  variant="outline"
                  size="sm"
                  disabled={page >= list.totalPages - 1}
                  onClick={() => goToPage(page + 1)}
                >
                  Próxima
                </Button>
              </div>
            </div>
          )}
        </>
      )}

      <Modal
        isOpen={modal === 'detail'}
        onClose={closeModal}
        title="Consulta"
        size="md"
        actions={[
          {
            label: 'Fechar',
            variant: 'primary',
            onClick: closeModal,
          },
        ]}
      >
        {detailAppointment && (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
            <p>
              <strong>Paciente:</strong> {detailAppointment.patient.fullName}
            </p>
            <p>
              <strong>Médico:</strong> {detailAppointment.doctor.fullName}
            </p>
            <p>
              <strong>Especialidade:</strong> {detailAppointment.doctor.specialtyDescription}
            </p>
            <p>
              <strong>Data/Hora:</strong> {formatDateTime(detailAppointment.date)}
            </p>
            <p>
              <strong>Status:</strong>{' '}
              <span className={`${styles.status} ${styles[statusClasses[detailAppointment.status]]}`}>
                {statusLabels[detailAppointment.status]}
              </span>
            </p>
            {detailAppointment.cancellation && (
              <>
                <p>
                  <strong>Motivo do cancelamento:</strong> {detailAppointment.cancellation.reasonDescription}
                </p>
                {detailAppointment.cancellation.message && (
                  <p>
                    <strong>Mensagem:</strong> {detailAppointment.cancellation.message}
                  </p>
                )}
              </>
            )}
          </div>
        )}
      </Modal>

      <Modal
        isOpen={modal === 'cancel'}
        onClose={closeModal}
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
            <Button variant="outline" onClick={closeModal}>
              Voltar
            </Button>
            <Button variant="primary" onClick={handleCancel} disabled={submitLoading}>
              {submitLoading ? 'Cancelando...' : 'Confirmar Cancelamento'}
            </Button>
          </div>
        </div>
      </Modal>

      <Modal
        isOpen={modal === 'form'}
        onClose={closeModal}
        title="Nova Consulta"
        size="lg"
      >
        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr', gap: '1rem' }}>
            {!isPatient && (
              <div>
                <label className="block text-sm mb-xs">Paciente *</label>
                <div style={{ display: 'flex', gap: '0.5rem' }}>
                  <input
                    type="text"
                    className="input"
                    value={formData.patientName}
                    placeholder="Clique em Selecionar para escolher"
                    readOnly
                    style={{ flex: 1 }}
                  />
                  <Button type="button" variant="outline" onClick={openPatientSelector}>
                    Selecionar
                  </Button>
                </div>
              </div>
            )}

            {!isDoctor && (
              <>
                <div>
                  <label className="block text-sm mb-xs">Médico</label>
                  <div style={{ display: 'flex', gap: '0.5rem' }}>
                    <input
                      type="text"
                      className="input"
                      value={formData.doctorName}
                      placeholder="Clique em Selecionar para escolher"
                      readOnly
                      style={{ flex: 1 }}
                    />
                    {formData.doctorId && (
                      <Button
                        type="button"
                        variant="outline"
                        size="fit"
                        onClick={clearDoctorSelection}
                        title="Limpar seleção"
                      >
                        ✕
                      </Button>
                    )}
                    <Button
                      type="button"
                      variant="outline"
                      onClick={openDoctorSelector}
                      disabled={!!formData.specialty}
                    >
                      Selecionar
                    </Button>
                  </div>
                </div>

                <div style={{ textAlign: 'center', color: '#6b7280', fontSize: '0.875rem' }}>
                  - ou -
                </div>

                <div>
                  <label className="block text-sm mb-xs">Especialidade</label>
                  <select
                    name="specialty"
                    className="input"
                    value={formData.specialty}
                    onChange={handleChange}
                    disabled={!!formData.doctorId}
                  >
                    <option value="">Selecione...</option>
                    {specialties.map((spec) => (
                      <option key={spec.specialty} value={spec.specialty}>
                        {spec.specialtyDescription}
                      </option>
                    ))}
                  </select>
                </div>
              </>
            )}

            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
              <div>
                <label className="block text-sm mb-xs">Data *</label>
                <input
                  type="date"
                  name="date"
                  className="input"
                  value={formData.date ? formData.date.split('T')[0] : ''}
                  onChange={(e) => {
                    const date = e.target.value;
                    const time = formData.date ? formData.date.split('T')[1] : '09:00';
                    setFormData(prev => ({ ...prev, date: `${date}T${time}` }));
                  }}
                  required
                />
              </div>
              <div>
                <label className="block text-sm mb-xs">Hora *</label>
                <input
                  type="time"
                  name="time"
                  className="input"
                  step="60"
                  value={formData.date ? formData.date.split('T')[1] : ''}
                  onChange={(e) => {
                    const time = e.target.value;
                    const date = formData.date ? formData.date.split('T')[0] : '';
                    if (date) {
                      setFormData(prev => ({ ...prev, date: `${date}T${time}` }));
                    }
                  }}
                  required
                />
              </div>
            </div>
          </div>

          <div style={{ display: 'flex', gap: '0.75rem', justifyContent: 'flex-end', marginTop: '1rem' }}>
            <Button type="button" variant="outline" onClick={closeModal}>
              Cancelar
            </Button>
            <Button type="submit" disabled={submitLoading}>
              {submitLoading ? 'Agendando...' : 'Agendar'}
            </Button>
          </div>
        </form>
      </Modal>

      {/* Seletores de Entidade */}
      <EntitySelector
        isOpen={selectorType === 'patient'}
        onClose={() => setSelectorType(null)}
        onSelect={handlePatientSelect}
        fetchFunction={patientService.getAll}
        title="Selecionar Paciente"
        columns={[
          { key: 'fullName', label: 'Nome' },
          { key: 'email', label: 'E-mail' },
          { key: 'cpf', label: 'CPF' },
        ]}
        searchFields={['fullName', 'email', 'cpf']}
      />

      <EntitySelector
        isOpen={selectorType === 'doctor'}
        onClose={() => setSelectorType(null)}
        onSelect={handleDoctorSelect}
        fetchFunction={doctorService.getAll}
        title="Selecionar Médico"
        columns={[
          { key: 'fullName', label: 'Nome' },
          { key: 'email', label: 'E-mail' },
          { key: 'crm', label: 'CRM' },
          { key: 'specialtyDescription', label: 'Especialidade' },
        ]}
        searchFields={['fullName', 'email', 'crm']}
      />
    </>
  );
};

export default Appointments;
