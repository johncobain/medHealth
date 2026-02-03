import { useState, useEffect, useCallback } from 'react';
import { toast } from 'react-toastify';
import { useAuth } from '../../context/AuthContext';
import doctorRequestService from '../../services/doctorRequestService';
import Button from '../../components/button/Button';
import { formatPhone, formatCPF, formatCRM } from '../../utils/formatters';
import { extractErrorMessage } from '../../utils/errorHandler';
import styles from './DoctorRequests.module.css';

const DoctorRequests = () => {
  const { user } = useAuth();
  const isAdmin = user?.role === 'ROLE_ADMIN';

  const [list, setList] = useState({ content: [], total: 0, totalPages: 0 });
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [size] = useState(10);
  
  const [detailRequest, setDetailRequest] = useState(null);
  const [showDetailModal, setShowDetailModal] = useState(false);
  const [actionLoading, setActionLoading] = useState(false);

  const fetchList = useCallback(async () => {
    setLoading(true);
    try {
      const data = await doctorRequestService.getAll({ page, size, sort: 'createdAt,desc' });
      setList(data);
    } catch (err) {
      toast.error(extractErrorMessage(err, 'Erro ao carregar solicitações.'));
    } finally {
      setLoading(false);
    }
  }, [page, size]);

  useEffect(() => {
    if (isAdmin) {
      fetchList();
    }
  }, [fetchList, isAdmin]);

  const openDetail = (request) => {
    setDetailRequest(request);
    setShowDetailModal(true);
  };

  const closeDetailModal = () => {
    setDetailRequest(null);
    setShowDetailModal(false);
  };

  const handleAccept = async (id) => {
    if (!window.confirm('Deseja aprovar esta solicitação de registro?')) return;

    setActionLoading(true);
    try {
      await doctorRequestService.accept(id);
      toast.success('Solicitação aprovada com sucesso! O médico receberá um email com a senha.');
      closeDetailModal();
      fetchList();
    } catch (err) {
      toast.error(extractErrorMessage(err, 'Erro ao aprovar solicitação.'));
    } finally {
      setActionLoading(false);
    }
  };

  const handleDecline = async (id) => {
    if (!window.confirm('Deseja recusar esta solicitação de registro?')) return;

    setActionLoading(true);
    try {
      await doctorRequestService.decline(id);
      toast.success('Solicitação recusada.');
      closeDetailModal();
      fetchList();
    } catch (err) {
      toast.error(extractErrorMessage(err, 'Erro ao recusar solicitação.'));
    } finally {
      setActionLoading(false);
    }
  };

  const goToPage = (newPage) => {
    if (newPage < 0 || newPage >= list.totalPages) return;
    setPage(newPage);
  };

  const getStatusClass = (status) => {
    switch (status) {
      case 'PENDING':
        return 'pending';
      case 'APROVED':
        return 'approved';
      case 'DECLINED':
        return 'declined';
      default:
        return '';
    }
  };

  const getStatusLabel = (status) => {
    switch (status) {
      case 'PENDING':
        return 'Pendente';
      case 'APROVED':
        return 'Aprovado';
      case 'DECLINED':
        return 'Recusado';
      default:
        return status;
    }
  };

  if (!isAdmin) {
    return (
      <div className={styles.empty}>
        <p>Você não tem permissão para acessar esta página.</p>
      </div>
    );
  }

  return (
    <>
      <header className={styles.header}>
        <div>
          <h1 className={styles.title}>Solicitações de Registro</h1>
          <p className={styles.subtitle}>Gerencie as solicitações de registro de médicos</p>
        </div>
      </header>

      {loading && list.content.length === 0 ? (
        <div className={styles.loading}>Carregando...</div>
      ) : list.content.length === 0 ? (
        <div className={styles.empty}>
          <p>Nenhuma solicitação encontrada.</p>
        </div>
      ) : (
        <>
          <div className={styles.tableWrap}>
            <table className={styles.table}>
              <thead>
                <tr>
                  <th>Nome</th>
                  <th>CRM</th>
                  <th>Especialidade</th>
                  <th>Email</th>
                  <th>Status</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {list.content.map((request) => (
                  <tr key={request.id}>
                    <td>{request.fullName}</td>
                    <td>{formatCRM(request.crm)}</td>
                    <td>{request.specialtyDescription || request.specialty}</td>
                    <td>{request.email}</td>
                    <td>
                      <span className={`${styles.status} ${styles[getStatusClass(request.status)]}`}>
                        {getStatusLabel(request.status)}
                      </span>
                    </td>
                    <td>
                      <div className={styles.actions}>
                        <Button
                          size="fit"
                          variant="outline"
                          onClick={() => openDetail(request)}
                        >
                          Ver
                        </Button>
                        {request.status === 'PENDING' && (
                          <>
                            <Button
                              size="fit"
                              variant="primary"
                              onClick={() => handleAccept(request.id)}
                              disabled={actionLoading}
                            >
                              Aprovar
                            </Button>
                            <Button
                              size="fit"
                              variant="danger"
                              onClick={() => handleDecline(request.id)}
                              disabled={actionLoading}
                            >
                              Recusar
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
                Página {page + 1} de {list.totalPages} • Total: {list.total}
              </span>
              <div className={styles.paginationBtns}>
                <Button
                  size="fit"
                  variant="outline"
                  onClick={() => goToPage(page - 1)}
                  disabled={page === 0}
                >
                  Anterior
                </Button>
                <Button
                  size="fit"
                  variant="outline"
                  onClick={() => goToPage(page + 1)}
                  disabled={page >= list.totalPages - 1}
                >
                  Próxima
                </Button>
              </div>
            </div>
          )}
        </>
      )}

      {showDetailModal && detailRequest && (
        <div className={styles.modalOverlay} onClick={closeDetailModal}>
          <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
            <div className={styles.modalHeader}>
              <h2 className={styles.modalTitle}>Detalhes da Solicitação</h2>
              <button className={styles.modalClose} onClick={closeDetailModal}>
                ×
              </button>
            </div>

            <div className={styles.modalBody}>
              <div className={styles.detailGrid}>
                <div className={styles.detailItem}>
                  <span className={styles.detailLabel}>Nome Completo</span>
                  <span className={styles.detailValue}>{detailRequest.fullName}</span>
                </div>

                <div className={styles.detailItem}>
                  <span className={styles.detailLabel}>Email</span>
                  <span className={styles.detailValue}>{detailRequest.email}</span>
                </div>

                <div className={styles.detailItem}>
                  <span className={styles.detailLabel}>Telefone</span>
                  <span className={styles.detailValue}>{formatPhone(detailRequest.phone)}</span>
                </div>

                <div className={styles.detailItem}>
                  <span className={styles.detailLabel}>CPF</span>
                  <span className={styles.detailValue}>{formatCPF(detailRequest.cpf)}</span>
                </div>

                <div className={styles.detailItem}>
                  <span className={styles.detailLabel}>CRM</span>
                  <span className={styles.detailValue}>{formatCRM(detailRequest.crm)}</span>
                </div>

                <div className={styles.detailItem}>
                  <span className={styles.detailLabel}>Especialidade</span>
                  <span className={styles.detailValue}>
                    {detailRequest.specialtyDescription || detailRequest.specialty}
                  </span>
                </div>

                <div className={styles.detailItem}>
                  <span className={styles.detailLabel}>Status</span>
                  <span className={`${styles.status} ${styles[getStatusClass(detailRequest.status)]}`}>
                    {getStatusLabel(detailRequest.status)}
                  </span>
                </div>

                <div className={styles.detailSection}>
                  <h3 className={styles.detailSectionTitle}>Endereço</h3>
                  <div className={styles.detailGrid}>
                    <div className={styles.detailItem}>
                      <span className={styles.detailLabel}>Estado</span>
                      <span className={styles.detailValue}>{detailRequest.state}</span>
                    </div>

                    <div className={styles.detailItem}>
                      <span className={styles.detailLabel}>Cidade</span>
                      <span className={styles.detailValue}>{detailRequest.city}</span>
                    </div>

                    <div className={styles.detailItem}>
                      <span className={styles.detailLabel}>Bairro</span>
                      <span className={styles.detailValue}>{detailRequest.neighborhood}</span>
                    </div>

                    <div className={styles.detailItem}>
                      <span className={styles.detailLabel}>Rua</span>
                      <span className={styles.detailValue}>{detailRequest.street}</span>
                    </div>

                    <div className={styles.detailItem}>
                      <span className={styles.detailLabel}>Número</span>
                      <span className={styles.detailValue}>{detailRequest.number || 'N/A'}</span>
                    </div>

                    <div className={styles.detailItem}>
                      <span className={styles.detailLabel}>CEP</span>
                      <span className={styles.detailValue}>{detailRequest.zipCode}</span>
                    </div>

                    {detailRequest.complement && (
                      <div className={styles.detailItem}>
                        <span className={styles.detailLabel}>Complemento</span>
                        <span className={styles.detailValue}>{detailRequest.complement}</span>
                      </div>
                    )}
                  </div>
                </div>
              </div>
            </div>

            <div className={styles.modalFooter}>
              <Button variant="outline" onClick={closeDetailModal} disabled={actionLoading}>
                Fechar
              </Button>
              {detailRequest.status === 'PENDING' && (
                <>
                  <Button
                    variant="danger"
                    onClick={() => handleDecline(detailRequest.id)}
                    disabled={actionLoading}
                  >
                    {actionLoading ? 'Processando...' : 'Recusar'}
                  </Button>
                  <Button
                    onClick={() => handleAccept(detailRequest.id)}
                    disabled={actionLoading}
                  >
                    {actionLoading ? 'Processando...' : 'Aprovar'}
                  </Button>
                </>
              )}
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default DoctorRequests;
