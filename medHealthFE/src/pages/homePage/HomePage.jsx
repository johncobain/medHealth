import { useEffect, useState } from 'react';
import statusService from '../../services/statusService';
import styles from './HomePage.module.css';
import Button from '../../components/button/Button';

const HomePage = () => {
  const [status, setStatus] = useState(null);
  const [loading, setLoading] = useState(true);

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

    return () => {
      clearInterval(interval);
    };
  }, []);

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
      <div className={styles.actions}>
        <Button variant="primary" size="md">Novo Paciente</Button>
        <Button variant="outline" size="md">Ver Agendamentos</Button>
      </div>
    </>
  );
};

export default HomePage;
