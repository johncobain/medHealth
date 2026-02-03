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
  const [activeSearch, setActiveSearch] = useState('');
  const size = 10;

  useEffect(() => {
    if (isOpen) {
      fetchList();
    }
  }, [isOpen, page, activeSearch]);

  const fetchList = async () => {
    setLoading(true);
    try {
      const params = { 
        page, 
        size, 
        sort: 'person.fullName,asc'
      };
      
      if (activeSearch) {
        params.name = activeSearch;
      }
      
      const data = await fetchFunction(params);
      setList(data);
    } catch (err) {
      console.error('Erro ao carregar lista', err);
      setList({ content: [], total: 0, totalPages: 0 });
    } finally {
      setLoading(false);
    }
  };

  const handleSelect = (entity) => {
    onSelect(entity);
    handleClose();
  };

  const handleClose = () => {
    onClose();
    setSearch('');
    setActiveSearch('');
    setPage(0);
  };

  const handleSearch = () => {
    setActiveSearch(search);
    setPage(0);
  };

  const handleClearSearch = () => {
    setSearch('');
    setActiveSearch('');
    setPage(0);
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleSearch();
    }
  };

  const renderCellValue = (item, columnKey) => {
    return columnKey.split('.').reduce((obj, key) => obj?.[key], item) || '-';
  };

  return (
    <Modal isOpen={isOpen} onClose={handleClose} title={title} size="lg">
      <div className={styles.container}>
        <div style={{ display: 'flex', gap: '0.5rem', marginBottom: '1rem' }}>
          <input
            type="text"
            className="input"
            placeholder="Buscar por nome..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            onKeyPress={handleKeyPress}
            style={{ flex: 1 }}
          />
          <Button 
            variant="primary" 
            size="fit"
            onClick={handleSearch}
            disabled={loading}
          >
            Buscar
          </Button>
          {activeSearch && (
            <Button 
              variant="outline" 
              size="fit"
              onClick={handleClearSearch}
              disabled={loading}
            >
              Limpar
            </Button>
          )}
        </div>

        {activeSearch && (
          <p style={{ marginBottom: '0.5rem', color: '#666', fontSize: '0.9rem' }}>
            Buscando por: <strong>{activeSearch}</strong>
          </p>
        )}

        {loading ? (
          <div className={styles.loading}>Carregando...</div>
        ) : list.content.length === 0 ? (
          <div className={styles.empty}>
            {activeSearch ? 'Nenhum resultado encontrado para a busca.' : 'Nenhum resultado encontrado.'}
          </div>
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
