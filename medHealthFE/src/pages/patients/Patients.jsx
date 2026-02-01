import { useState, useEffect, useCallback } from 'react';
import { toast } from 'react-toastify';
import { useSearchParams } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import patientService from '../../services/patientService';
import Button from '../../components/button/Button';
import styles from './Patients.module.css';

const PHONE_MASK = /^\(\d{2}\) \d{4,5}-\d{4}$/;
const CPF_MASK = /^\d{3}\.\d{3}\.\d{3}-\d{2}$/;
const ZIP_MASK = /^\d{5}-\d{3}$/;

const formatPhone = (value) => {
  const numbers = value.replace(/\D/g, '');
  if (numbers.length <= 2) return numbers;
  if (numbers.length <= 6) return `(${numbers.slice(0, 2)}) ${numbers.slice(2)}`;
  if (numbers.length <= 10) {
    return `(${numbers.slice(0, 2)}) ${numbers.slice(2, 6)}-${numbers.slice(6)}`;
  }
  return `(${numbers.slice(0, 2)}) ${numbers.slice(2, 7)}-${numbers.slice(7, 11)}`;
};

const formatZipCode = (value) => {
  const numbers = value.replace(/\D/g, '');
  if (numbers.length <= 5) return numbers;
  return `${numbers.slice(0, 5)}-${numbers.slice(5, 8)}`;
};

const formatCPF = (value) => {
  const numbers = value.replace(/\D/g, '');
  if (numbers.length <= 3) return numbers;
  if (numbers.length <= 6) return `${numbers.slice(0, 3)}.${numbers.slice(3)}`;
  if (numbers.length <= 9) {
    return `${numbers.slice(0, 3)}.${numbers.slice(3, 6)}.${numbers.slice(6)}`;
  }
  return `${numbers.slice(0, 3)}.${numbers.slice(3, 6)}.${numbers.slice(6, 9)}-${numbers.slice(9, 11)}`;
};

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
      toast.error(err.response?.data?.reason || 'Erro ao carregar pacientes.');
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
    setModal('form');
  };

  const openEdit = (row) => {
    setFormData({
      fullName: row.fullName || '',
      email: row.email || '',
      phone: row.phone || '',
      cpf: row.cpf || '',
      address: emptyAddress(),
    });
    setDetailPatient(row);
    setModal('form');
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
      const formatted = formatPhone(value);
      setFormData((prev) => ({ ...prev, phone: formatted }));
      return;
    }

    if (name === 'cpf') {
      const formatted = formatCPF(value);
      setFormData((prev) => ({ ...prev, cpf: formatted }));
      return;
    }

    if (name === 'address.zipCode') {
      const formatted = formatZipCode(value);
      setFormData((prev) => ({
        ...prev,
        address: { ...prev.address, zipCode: formatted },
      }));
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

  const handleSubmitCreate = async (e) => {
    e.preventDefault();
    if (
      !formData.fullName?.trim() ||
      !formData.email?.trim() ||
      !formData.phone?.trim() ||
      !formData.cpf?.trim()
    ) {
      toast.error('Preencha nome, e-mail, telefone e CPF.');
      return;
    }
    if (!PHONE_MASK.test(formData.phone)) {
      toast.error('Telefone no formato (99) 99999-9999.');
      return;
    }
    if (!CPF_MASK.test(formData.cpf)) {
      toast.error('CPF no formato 999.999.999-99.');
      return;
    }
    const { state, city, neighborhood, street, number, complement, zipCode } = formData.address;
    if (
      !state?.trim() ||
      !city?.trim() ||
      !neighborhood?.trim() ||
      !street?.trim() ||
      !zipCode?.trim()
    ) {
      toast.error('Preencha estado, cidade, bairro, rua e CEP.');
      return;
    }
    if (!ZIP_MASK.test(zipCode)) {
      toast.error('CEP no formato 99999-999.');
      return;
    }
    setSubmitLoading(true);
    try {
      await patientService.create({
        fullName: formData.fullName.trim(),
        email: formData.email.trim(),
        phone: formData.phone,
        cpf: formData.cpf,
        address: {
          state: state.trim(),
          city: city.trim(),
          neighborhood: neighborhood.trim(),
          street: street.trim(),
          number: number?.trim() || '',
          complement: complement?.trim() || '',
          zipCode,
        },
      });
      toast.success('Paciente cadastrado.');
      closeModal();
      fetchList();
    } catch (err) {
      let errorMessage = 'Erro ao cadastrar paciente.';
      
      if (err.response?.data) {
        const { reason, errors, message } = err.response.data;
        
        if (reason) {
          errorMessage = reason;
        }
        else if (errors && typeof errors === 'object') {
          const errorMessages = Object.entries(errors)
            .map(([field, messages]) => {
              const msgs = Array.isArray(messages) ? messages : [messages];
              return msgs.join(', ');
            })
            .filter(Boolean)
            .join('\n');
          
          if (errorMessages) {
            errorMessage = errorMessages;
          }
        }
        else if (message) {
          errorMessage = message;
        }
      }
      
      toast.error(errorMessage);
    } finally {
      setSubmitLoading(false);
    }
  };

  const handleSubmitEdit = async (e) => {
    e.preventDefault();
    if (!detailPatient?.id) return;
    if (!formData.fullName?.trim()) {
      toast.error('Nome é obrigatório.');
      return;
    }
    if (formData.phone && !PHONE_MASK.test(formData.phone)) {
      toast.error('Telefone no formato (99) 99999-9999.');
      return;
    }
    const { state, city, neighborhood, street, number, complement, zipCode } = formData.address;
    const hasAddress = [state, city, neighborhood, street, zipCode].some((v) => v?.trim());
    if (hasAddress && !ZIP_MASK.test(zipCode)) {
      toast.error('CEP no formato 99999-999.');
      return;
    }
    setSubmitLoading(true);
    try {
      await patientService.update(detailPatient.id, {
        fullName: formData.fullName.trim(),
        ...(formData.phone && { phone: formData.phone }),
        ...(hasAddress && {
          address: {
            state: (state || '').trim(),
            city: (city || '').trim(),
            neighborhood: (neighborhood || '').trim(),
            street: (street || '').trim(),
            number: (number || '').trim(),
            complement: (complement || '').trim(),
            zipCode: zipCode || '',
          },
        }),
      });
      toast.success('Paciente atualizado.');
      closeModal();
      fetchList();
    } catch (err) {
      let errorMessage = 'Erro ao atualizar paciente.';
      
      if (err.response?.data) {
        const { reason, errors, message } = err.response.data;
        
        if (reason) {
          errorMessage = reason;
        } else if (errors && typeof errors === 'object') {
          const errorMessages = Object.entries(errors)
            .map(([field, messages]) => {
              const msgs = Array.isArray(messages) ? messages : [messages];
              return msgs.join(', ');
            })
            .filter(Boolean)
            .join('\n');
          
          if (errorMessages) {
            errorMessage = errorMessages;
          }
        } else if (message) {
          errorMessage = message;
        }
      }
      
      toast.error(errorMessage);
    } finally {
      setSubmitLoading(false);
    }
  };

  const handleDelete = async (row) => {
    if (!window.confirm(`Desativar o paciente ${row.fullName}? Esta ação não pode ser desfeita.`)) return;
    try {
      await patientService.delete(row.id);
      toast.success('Paciente desativado.');
      fetchList();
    } catch (err) {
      toast.error(err.response?.data?.reason || 'Erro ao desativar.');
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

  const isFormModal = modal === 'form';
  const isEdit = isFormModal && detailPatient != null;

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
                <Button
                  variant="outline"
                  size="sm"
                  disabled={page <= 0}
                  onClick={() => goToPage(page - 1)}
                >
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

      {modal === 'detail' && detailPatient && (
        <div className={styles.modalOverlay} onClick={closeModal}>
          <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
            <div className={styles.modalHeader}>
              <h3 className={styles.modalTitle}>Paciente</h3>
              <button
                type="button"
                className={styles.modalClose}
                onClick={closeModal}
                aria-label="Fechar"
              >
                &times;
              </button>
            </div>
            <div className={styles.modalBody}>
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
            <div className={styles.modalFooter}>
              {isAdmin && (
                <Button
                  variant="outline"
                  onClick={() => {
                    setFormData({
                      fullName: detailPatient.fullName || '',
                      email: detailPatient.email || '',
                      phone: '',
                      cpf: detailPatient.cpf || '',
                      address: emptyAddress(),
                    });
                    setModal('form');
                  }}
                >
                  Editar
                </Button>
              )}
              <Button variant="primary" onClick={closeModal}>
                Fechar
              </Button>
            </div>
          </div>
        </div>
      )}

      {isFormModal && (
        <div className={styles.modalOverlay} onClick={closeModal}>
          <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
            <div className={styles.modalHeader}>
              <h3 className={styles.modalTitle}>{isEdit ? 'Editar Paciente' : 'Novo Paciente'}</h3>
              <button
                type="button"
                className={styles.modalClose}
                onClick={closeModal}
                aria-label="Fechar"
              >
                &times;
              </button>
            </div>
            <form
              onSubmit={isEdit ? handleSubmitEdit : handleSubmitCreate}
              className={styles.modalBody}
            >
              <div className={styles.formGrid}>
                <div className={styles.formGroup}>
                  <label>Nome completo *</label>
                  <input
                    type="text"
                    name="fullName"
                    className="input"
                    value={formData.fullName}
                    onChange={handleChange}
                    required
                  />
                </div>
                <div className={styles.formGroup}>
                  <label>E-mail *</label>
                  <input
                    type="email"
                    name="email"
                    className={`input ${isEdit ? styles.readOnly : ''}`}
                    value={formData.email}
                    onChange={handleChange}
                    readOnly={!!isEdit}
                    disabled={!!isEdit}
                    required={!isEdit}
                  />
                </div>
                <div className={styles.formGroup}>
                  <label>Telefone *</label>
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
                <div className={styles.formGroup}>
                  <label>CPF *</label>
                  <input
                    type="text"
                    name="cpf"
                    className={`input ${isEdit ? styles.readOnly : ''}`}
                    value={formData.cpf}
                    onChange={handleChange}
                    placeholder="999.999.999-99"
                    maxLength={14}
                    readOnly={!!isEdit}
                    disabled={!!isEdit}
                    required={!isEdit}
                  />
                </div>
                <div className={styles.formGroup} style={{ gridColumn: '1 / -1' }}>
                  <label>Endereço</label>
                  <div className={styles.formGrid} style={{ gap: '0.75rem' }}>
                    <input
                      type="text"
                      name="address.state"
                      className="input"
                      placeholder="Estado*"
                      value={formData.address.state}
                      onChange={handleChange}
                    />
                    <input
                      type="text"
                      name="address.city"
                      className="input"
                      placeholder="Cidade*"
                      value={formData.address.city}
                      onChange={handleChange}
                    />
                    <input
                      type="text"
                      name="address.neighborhood"
                      className="input"
                      placeholder="Bairro*"
                      value={formData.address.neighborhood}
                      onChange={handleChange}
                    />
                    <input
                      type="text"
                      name="address.street"
                      className="input"
                      placeholder="Rua*"
                      value={formData.address.street}
                      onChange={handleChange}
                    />
                    <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0.5rem' }}>
                      <input
                        type="text"
                        name="address.number"
                        className="input"
                        placeholder="Número"
                        value={formData.address.number}
                        onChange={handleChange}
                      />
                      <input
                        type="text"
                        name="address.zipCode"
                        className="input"
                        placeholder="CEP* 99999-999"
                        value={formData.address.zipCode}
                        onChange={handleChange}
                        maxLength={9}
                      />
                    </div>
                    <input
                      type="text"
                      name="address.complement"
                      className="input"
                      placeholder="Complemento"
                      value={formData.address.complement}
                      onChange={handleChange}
                    />
                  </div>
                </div>
              </div>
              <div className={styles.modalFooter}>
                <Button type="button" variant="outline" onClick={closeModal}>
                  Cancelar
                </Button>
                <Button type="submit" disabled={submitLoading}>
                  {submitLoading ? 'Salvando...' : 'Salvar'}
                </Button>
              </div>
            </form>
          </div>
        </div>
      )}
    </>
  );
};

export default Patients;