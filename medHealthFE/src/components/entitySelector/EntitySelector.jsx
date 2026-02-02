import { useState, useEffect } from 'react';
import Button from '../button/Button';
import Modal from '../modal/Modal';
import styles from './EntitySelector.module.css';

const EntitySelector = ({ 
  isOpen, 
  onClose, 
  onSelect,
  fetchFunction,
  title,
  columns,
  searchFields
}) => {
  const [list, setList] = useState({ content: [], total: 0, totalPages: 0 });
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(0);
  const [search, setSearch] = useState('');
  const size = 10;

  useEffect(() => {
    if (isOpen) {
      fetchList();
    }
  }, [isOpen, page, search]);

  const fetchList = async () => {
    setLoading(true);
    try {
      const data = await fetchFunction({ page, size, sort: 'person.fullName,asc' });
      if (search) {
        const filtered = data.content.filter((item) => {
          return searchFields.some((field) => {
            const value = field.split('.').reduce((obj, key) => obj?.[key], item);
            return value?.toLowerCase().includes(search.toLowerCase());
          });
        });
        setList({ ...data, content: filtered });
      } else {
        setList(data);
      }
    } catch (err) {
      console.error('Erro ao carregar lista', err);
      setList({ content: [], total: 0, totalPages: 0 });
    } finally {
      setLoading(false);
    }
  };

  const handleSelect = (entity) => {
    onSelect(entity);
    onClose();
    setSearch('');
    setPage(0);
  };

  const handleSearchChange = (e) => {
    setSearch(e.target.value);
    setPage(0);
  };

  const renderCellValue = (item, columnKey) => {
    return columnKey.split('.').reduce((obj, key) => obj?.[key], item) || '-';
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title={title} size="lg">
      <div className={styles.container}>
        <input
          type="text"
          className="input"
          placeholder="Buscar..."
          value={search}
          onChange={handleSearchChange}
        />

        {loading ? (
          <div className={styles.loading}>Carregando...</div>
        ) : list.content.length === 0 ? (
          <div className={styles.empty}>Nenhum resultado encontrado.</div>
        ) : (
          <>
            <div className={styles.tableWrap}>
              <table className={styles.table}>
                <thead>
                  <tr>
                    {columns.map((col) => (
                      <th key={col.key}>{col.label}</th>
                    ))}
                    <th>Ações</th>
                  </tr>
                </thead>
                <tbody>
                  {list.content.map((item) => (
                    <tr key={item.id}>
                      {columns.map((col) => (
                        <td key={col.key}>{renderCellValue(item, col.key)}</td>
                      ))}
                      <td>
                        <Button
                          variant="primary"
                          size="sm"
                          onClick={() => handleSelect(item)}
                        >
                          Selecionar
                        </Button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
            
            {list.totalPages > 1 && (
              <div className={styles.pagination}>
                <span className={styles.paginationInfo}>
                  Página {page + 1} de {list.totalPages}
                </span>
                <div className={styles.paginationBtns}>
                  <Button
                    variant="outline"
                    size="sm"
                    disabled={page <= 0}
                    onClick={() => setPage(page - 1)}
                  >
                    Anterior
                  </Button>
                  <Button
                    variant="outline"
                    size="sm"
                    disabled={page >= list.totalPages - 1}
                    onClick={() => setPage(page + 1)}
                  >
                    Próxima
                  </Button>
                </div>
              </div>
            )}
          </>
        )}
      </div>
    </Modal>
  );
};

export default EntitySelector;
