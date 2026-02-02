import { useState, useEffect, useCallback } from 'react';
import { toast } from 'react-toastify';
import { useSearchParams } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import patientService from '../../services/patientService';
import Button from '../../components/button/Button';
import Modal from '../../components/modal/Modal';
import AddressForm from '../../components/addressForm/AddressForm';
import { formatPhone, formatCPF } from '../../utils/formatters';
import { validatePhone, validateCPF, validateAddress } from '../../utils/validators';
import { extractErrorMessage } from '../../utils/errorHandler';
import styles from './Patients.module.css';

const emptyAddress = () => ({
  state: '',
  city: '',
  neighborhood: '',
  street: '',
  number: '',
  complement: '',
  zipCode: '',
});

const emptyForm = () => ({
  fullName: '',
  email: '',
  phone: '',
  cpf: '',
  address: emptyAddress(),
});

const Patients = () => {
  const { user } = useAuth();
  const [searchParams, setSearchParams] = useSearchParams();
  const isAdmin = user?.role === 'ROLE_ADMIN';

  const [list, setList] = useState({ content: [], total: 0, totalPages: 0 });
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(() => parseInt(searchParams.get('page') || '0', 10));
  const [size] = useState(10);

  const [modal, setModal] = useState(null);
  const [formData, setFormData] = useState(emptyForm());
  const [submitLoading, setSubmitLoading] = useState(false);
  const [detailPatient, setDetailPatient] = useState(null);

  const fetchList = useCallback(async () => {
    setLoading(true);
    try {
      const data = await patientService.getAll({ page, size, sort: 'person.fullName,asc' });
      setList(data);
    } catch (err) {
      toast.error(extractErrorMessage(err, 'Erro ao carregar pacientes.'));
    } finally {
      setLoading(false);
    }
  }, [page, size]);

  useEffect(() => {
    fetchList();
  }, [fetchList]);

  useEffect(() => {
    const p = searchParams.get('page');
    if (p !== null) setPage(parseInt(p, 10) || 0);
  }, [searchParams]);

  useEffect(() => {
    const action = searchParams.get('action');
    if (action === 'new' && isAdmin && !modal) {
      openCreate();
      setSearchParams((prev) => {
        const next = new URLSearchParams(prev);
        next.delete('action');
        return next;
      });
    }
  }, [searchParams, isAdmin, modal]);

  const openCreate = () => {
    setFormData(emptyForm());
    setDetailPatient(null);
    setModal('form');
  };

  const openEdit = async (row) => {
    try {
      const fullPatient = await patientService.getById(row.id);
      setFormData({
        fullName: fullPatient.fullName || '',
        email: fullPatient.email || '',
        phone: fullPatient.phone || '',
        cpf: fullPatient.cpf || '',
        address: fullPatient.address || emptyAddress(),
      });
      setDetailPatient(fullPatient);
      setModal('form');
    } catch (err) {
      toast.error('Erro ao carregar dados do paciente.');
    }
  };

  const openDetail = (row) => {
    setDetailPatient(row);
    setModal('detail');
  };

  const closeModal = () => {
    setModal(null);
    setDetailPatient(null);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;

    if (name === 'phone') {
      setFormData((prev) => ({ ...prev, phone: formatPhone(value) }));
      return;
    }

    if (name === 'cpf') {
      setFormData((prev) => ({ ...prev, cpf: formatCPF(value) }));
      return;
    }

    if (name.startsWith('address.')) {
      const field = name.replace('address.', '');
      setFormData((prev) => ({
        ...prev,
        address: { ...prev.address, [field]: value },
      }));
    } else {
      setFormData((prev) => ({ ...prev, [name]: value }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const isEdit = detailPatient != null;

    if (!formData.fullName?.trim()) {
      toast.error('Nome completo é obrigatório.');
      return;
    }

    if (!isEdit) {
      if (!formData.email?.trim() || !formData.cpf?.trim()) {
        toast.error('E-mail e CPF são obrigatórios.');
        return;
      }
      if (!validateCPF(formData.cpf)) {
        toast.error('CPF no formato 999.999.999-99.');
        return;
      }
    }

    if (formData.phone && !validatePhone(formData.phone)) {
      toast.error('Telefone no formato (99) 99999-9999.');
      return;
    }

    const addressValidation = validateAddress(formData.address);
    if (!addressValidation.valid) {
      toast.error(addressValidation.message);
      return;
    }

    setSubmitLoading(true);
    try {
      const payload = {
        fullName: formData.fullName.trim(),
        ...(formData.phone && { phone: formData.phone }),
      };

      if (!isEdit) {
        payload.email = formData.email.trim();
        payload.cpf = formData.cpf;
      }

      const hasAddress = Object.values(formData.address).some((v) => v?.trim());
      if (hasAddress) {
        payload.address = {
          state: formData.address.state.trim(),
          city: formData.address.city.trim(),
          neighborhood: formData.address.neighborhood.trim(),
          street: formData.address.street.trim(),
          number: formData.address.number?.trim() || '',
          complement: formData.address.complement?.trim() || '',
          zipCode: formData.address.zipCode,
        };
      }

      if (isEdit) {
        await patientService.update(detailPatient.id, payload);
        toast.success('Paciente atualizado.');
      } else {
        await patientService.create(payload);
        toast.success('Paciente cadastrado.');
      }

      closeModal();
      fetchList();
    } catch (err) {
      toast.error(extractErrorMessage(err, `Erro ao ${isEdit ? 'atualizar' : 'cadastrar'} paciente.`));
    } finally {
      setSubmitLoading(false);
    }
  };

  const handleDelete = async (row) => {
    if (!window.confirm(`Desativar o paciente ${row.fullName}? Esta ação não pode ser desfeita.`))
      return;
    try {
      await patientService.delete(row.id);
      toast.success('Paciente desativado.');
      fetchList();
    } catch (err) {
      toast.error(extractErrorMessage(err, 'Erro ao desativar.'));
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

  const isEdit = modal === 'form' && detailPatient != null;

  return (
    <>
      <div className={styles.header}>
        <h2 className={styles.title}>{isAdmin ? 'Gerenciar Pacientes' : 'Pacientes'}</h2>
        {isAdmin && (
          <Button variant="primary" onClick={openCreate}>
            Novo Paciente
          </Button>
        )}
      </div>

      {loading ? (
        <div className={styles.loading}>Carregando...</div>
      ) : list.content.length === 0 ? (
        <div className={styles.empty}>Nenhum paciente encontrado.</div>
      ) : (
        <>
          <div className={styles.tableWrap}>
            <table className={styles.table}>
              <thead>
                <tr>
                  <th>Nome</th>
                  <th>E-mail</th>
                  <th>CPF</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {list.content.map((row) => (
                  <tr key={row.id}>
                    <td>{row.fullName}</td>
                    <td>{row.email}</td>
                    <td>{row.cpf}</td>
                    <td>
                      <div className={styles.actions}>
                        <Button variant="outline" size="sm" onClick={() => openDetail(row)}>
                          Ver
                        </Button>
                        {isAdmin && (
                          <>
                            <Button variant="outline" size="sm" onClick={() => openEdit(row)}>
                              Editar
                            </Button>
                            <Button variant="outline" size="sm" onClick={() => handleDelete(row)}>
                              Desativar
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
        title="Paciente"
        size="md"
        actions={[
          ...(isAdmin
            ? [
                {
                  label: 'Editar',
                  variant: 'outline',
                  onClick: () => openEdit(detailPatient),
                },
              ]
            : []),
          {
            label: 'Fechar',
            variant: 'primary',
            onClick: closeModal,
          },
        ]}
      >
        {detailPatient && (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
            <p>
              <strong>Nome:</strong> {detailPatient.fullName}
            </p>
            <p>
              <strong>E-mail:</strong> {detailPatient.email}
            </p>
            <p>
              <strong>CPF:</strong> {detailPatient.cpf}
            </p>
          </div>
        )}
      </Modal>

      <Modal
        isOpen={modal === 'form'}
        onClose={closeModal}
        title={isEdit ? 'Editar Paciente' : 'Novo Paciente'}
        size="lg"
      >
        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
            <div>
              <label className="block text-sm mb-xs">Nome completo *</label>
              <input
                type="text"
                name="fullName"
                className="input"
                value={formData.fullName}
                onChange={handleChange}
                required
              />
            </div>
            <div>
              <label className="block text-sm mb-xs">E-mail *</label>
              <input
                type="email"
                name="email"
                className="input"
                value={formData.email}
                onChange={handleChange}
                readOnly={isEdit}
                disabled={isEdit}
                required={!isEdit}
              />
            </div>
            <div>
              <label className="block text-sm mb-xs">Telefone {!isEdit && '*'}</label>
              <input
                type="text"
                name="phone"
                className="input"
                placeholder="(99) 99999-9999"
                value={formData.phone}
                onChange={handleChange}
                maxLength={15}
                required={!isEdit}
              />
            </div>
            <div>
              <label className="block text-sm mb-xs">CPF *</label>
              <input
                type="text"
                name="cpf"
                className="input"
                value={formData.cpf}
                onChange={handleChange}
                placeholder="999.999.999-99"
                maxLength={14}
                readOnly={isEdit}
                disabled={isEdit}
                required={!isEdit}
              />
            </div>
          </div>

          <AddressForm address={formData.address} onChange={handleChange} required={!isEdit} />

          <div style={{ display: 'flex', gap: '0.75rem', justifyContent: 'flex-end', marginTop: '1rem' }}>
            <Button type="button" variant="outline" onClick={closeModal}>
              Cancelar
            </Button>
            <Button type="submit" disabled={submitLoading}>
              {submitLoading ? 'Salvando...' : 'Salvar'}
            </Button>
          </div>
        </form>
      </Modal>
    </>
  );
};

export default Patients;