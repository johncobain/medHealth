import { useState, useEffect, useCallback } from 'react';
import { toast } from 'react-toastify';
import { useSearchParams } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import doctorService from '../../services/doctorService';
import Button from '../../components/button/Button';
import Modal from '../../components/modal/Modal';
import AddressForm from '../../components/addressForm/AddressForm';
import { formatPhone, formatCPF, formatCRM } from '../../utils/formatters';
import { validatePhone, validateCPF, validateCRM, validateAddress } from '../../utils/validators';
import { extractErrorMessage } from '../../utils/errorHandler';
import styles from './Doctors.module.css';

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
  crm: '',
  specialty: '',
  address: emptyAddress(),
});

const Doctors = () => {
  const { user } = useAuth();
  const [searchParams, setSearchParams] = useSearchParams();
  const isAdmin = user?.role === 'ROLE_ADMIN';

  const [list, setList] = useState({ content: [], total: 0, totalPages: 0 });
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(() => parseInt(searchParams.get('page') || '0', 10));
  const [size] = useState(10);
  const [specialties, setSpecialties] = useState([]);

  const [modal, setModal] = useState(null);
  const [formData, setFormData] = useState(emptyForm());
  const [submitLoading, setSubmitLoading] = useState(false);
  const [detailDoctor, setDetailDoctor] = useState(null);

  const fetchList = useCallback(async () => {
    setLoading(true);
    try {
      const data = await doctorService.getAll({ page, size, sort: 'person.fullName,asc' });
      setList(data);
    } catch (err) {
      toast.error(extractErrorMessage(err, 'Erro ao carregar médicos.'));
    } finally {
      setLoading(false);
    }
  }, [page, size]);

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
    fetchSpecialties();
  }, [fetchList, fetchSpecialties]);

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
    setDetailDoctor(null);
    setModal('form');
  };

  const openEdit = async (row) => {
    try {
      const fullDoctor = await doctorService.getById(row.id);
      setFormData({
        fullName: fullDoctor.fullName || '',
        email: fullDoctor.email || '',
        phone: fullDoctor.phone || '',
        crm: fullDoctor.crm || '',
        specialty: fullDoctor.specialtyDescription || '',
        address: fullDoctor.address || emptyAddress(),
      });
      setDetailDoctor(fullDoctor);
      setModal('form');
    } catch (err) {
      toast.error('Erro ao carregar dados do médico.');
    }
  };

  const openDetail = (row) => {
    setDetailDoctor(row);
    setModal('detail');
  };

  const closeModal = () => {
    setModal(null);
    setDetailDoctor(null);
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

    if (name === 'crm') {
      setFormData((prev) => ({ ...prev, crm: formatCRM(value) }));
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
    const isEdit = detailDoctor != null;

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
      if (!formData.crm?.trim()) {
        toast.error('CRM é obrigatório.');
        return;
      }
      if (!validateCRM(formData.crm)) {
        toast.error('CRM no formato CRM-UF-NUMERO (ex: CRM-SP-12345).');
        return;
      }
      if (!formData.specialty?.trim()) {
        toast.error('Especialidade é obrigatória.');
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
        payload.crm = formData.crm.trim();
        payload.specialty = formData.specialty.trim();
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
        await doctorService.update(detailDoctor.id, payload);
        toast.success('Médico atualizado.');
      } else {
        await doctorService.create(payload);
        toast.success('Médico cadastrado.');
      }

      closeModal();
      fetchList();
    } catch (err) {
      toast.error(extractErrorMessage(err, `Erro ao ${isEdit ? 'atualizar' : 'cadastrar'} médico.`));
    } finally {
      setSubmitLoading(false);
    }
  };

  const handleDelete = async (row) => {
    if (!window.confirm(`Desativar o médico ${row.fullName}? Esta ação não pode ser desfeita.`))
      return;
    try {
      await doctorService.delete(row.id);
      toast.success('Médico desativado.');
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

  const isEdit = modal === 'form' && detailDoctor != null;

  return (
    <>
      <div className={styles.header}>
        <h2 className={styles.title}>Médicos</h2>
        {isAdmin && (
          <Button variant="primary" onClick={openCreate}>
            Novo Médico
          </Button>
        )}
      </div>

      {loading ? (
        <div className={styles.loading}>Carregando...</div>
      ) : list.content.length === 0 ? (
        <div className={styles.empty}>Nenhum médico encontrado.</div>
      ) : (
        <>
          <div className={styles.tableWrap}>
            <table className={styles.table}>
              <thead>
                <tr>
                  <th>Nome</th>
                  <th>E-mail</th>
                  <th>CRM</th>
                  <th>Especialidade</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {list.content.map((row) => (
                  <tr key={row.id}>
                    <td>{row.fullName}</td>
                    <td>{row.email}</td>
                    <td>{row.crm}</td>
                    <td>{row.specialtyDescription}</td>
                    <td>
                      <div className={styles.actions}>
                        <Button variant="outline" size="fit" onClick={() => openDetail(row)}>
                          Ver
                        </Button>
                        {isAdmin && (
                          <>
                            <Button variant="primary" size="fit" onClick={() => openEdit(row)}>
                              Editar
                            </Button>
                            <Button variant="danger" size="fit" onClick={() => handleDelete(row)}>
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
        title="Médico"
        size="md"
        actions={[
          ...(isAdmin
            ? [
                {
                  label: 'Editar',
                  variant: 'outline',
                  onClick: () => openEdit(detailDoctor),
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
        {detailDoctor && (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
            <p>
              <strong>Nome:</strong> {detailDoctor.fullName}
            </p>
            <p>
              <strong>E-mail:</strong> {detailDoctor.email}
            </p>
            <p>
              <strong>CRM:</strong> {detailDoctor.crm}
            </p>
            <p>
              <strong>Especialidade:</strong> {detailDoctor.specialtyDescription}
            </p>
          </div>
        )}
      </Modal>

      <Modal
        isOpen={modal === 'form'}
        onClose={closeModal}
        title={isEdit ? 'Editar Médico' : 'Novo Médico'}
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
            <div>
              <label className="block text-sm mb-xs">CRM *</label>
              <input
                type="text"
                name="crm"
                className="input"
                placeholder="CRM-UF-12345"
                value={formData.crm}
                onChange={handleChange}
                maxLength={14}
                readOnly={isEdit}
                disabled={isEdit}
                required={!isEdit}
              />
            </div>
            <div>
              <label className="block text-sm mb-xs">Especialidade *</label>
              <select
                name="specialty"
                className="input"
                value={formData.specialty}
                onChange={handleChange}
                disabled={isEdit}
                required={!isEdit}
              >
                <option value="">Selecione...</option>
                {specialties.map((spec) => (
                  <option key={spec.specialty} value={spec.specialty}>
                    {spec.specialtyDescription || spec.specialty}
                  </option>
                ))}
              </select>
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

export default Doctors;
