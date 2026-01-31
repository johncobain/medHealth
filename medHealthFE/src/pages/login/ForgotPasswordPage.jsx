import { useState } from 'react';
import { toast } from 'react-toastify';
import authService from '../../services/authService';
import styles from './LoginPage.module.css';
import { Link } from 'react-router-dom';
import Button from '../../components/button/Button';

const ForgotPasswordPage = () => {
  const [email, setEmail] = useState('');
  const [sent, setSent] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      await authService.forgotPassword(email);
      toast.success(`Um link de recuperação foi enviado para ${email}.`);
      setSent(true);
    } catch (err) {
      const errorMessage =
        err.response?.data?.reason || 'Erro ao conectar com o servidor. Tente novamente.';
      toast.error(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.container}>
      <form onSubmit={handleSubmit} className={styles.form}>
        <h2 className={styles.subtitle}>Recuperar Senha</h2>

        {!sent && (
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
        {sent && (
          <p className="text-center text-sm text-light mt-md">
            Verifique sua caixa de entrada. Se não receber em alguns minutos, confira o spam.
          </p>
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
