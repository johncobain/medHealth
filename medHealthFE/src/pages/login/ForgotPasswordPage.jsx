import { useState } from 'react';
import authService from '../../services/authService';
import styles from './LoginPage.module.css';
import { Link } from 'react-router-dom';
import Button from '../../components/button/Button';

const ForgotPasswordPage = () => {
  const [email, setEmail] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setMessage('');
    setLoading(true);

    try {
      await authService.forgotPassword(email);
      setMessage(`Um link de recuperação foi enviado para ${email}.`);
    } catch (err) {
      const errorMessage =
        err.response?.data?.reason || 'Erro ao conectar com o servidor. Tente novamente.';
      setError(`Erro: ${errorMessage}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.container}>
      <form onSubmit={handleSubmit} className={styles.form}>
        <h2 className={styles.subtitle}>Recuperar Senha</h2>

        {message && <p className={styles.success}>{message}</p>}
        {error && <p className={styles.error}>{error}</p>}

        {!message && (
          <>
            <p className="text-center text-sm text-light mb-md">
              Insira seu email para receber um link de recuperação de senha.
            </p>
            <div className="mt-md">
              <label htmlFor="email" className={styles.inputlabel}>
                Email
              </label>
              <input
                type="email"
                id="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
                className='input'
                disabled={loading}
              />
            </div>
            <Button type="submit" disabled={loading}>
              {loading ? 'Enviando...' : 'Enviar Link de Recuperação'}
            </Button>
          </>
        )}

        <div className="text-center mt-md">
          <Link to="/login" className="text-sm text-primary hover:underline">
            Voltar para Login
          </Link>
        </div>
      </form>
    </div>
  );
};

export default ForgotPasswordPage;
