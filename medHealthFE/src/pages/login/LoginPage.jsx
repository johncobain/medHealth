import { useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import styles from './LoginPage.module.css';
import Button from '../../components/button/Button';
import { useState } from 'react';

const LoginPage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const from = location.state?.from?.pathname || '/';

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      await login(email, password);
      navigate(from, { replace: true });
    } catch (error) {
      setError('Falha no login. Verifique suas credenciais e tente novamente.');
      console.error(error);
    }
  };

  return (
    <div className={styles.container}>
      <form onSubmit={handleSubmit} className={styles.form}>
        <h1 className={styles.title}>MedHealth</h1>
        <h2 className={styles.subtitle}>Login</h2>
        {error && <p className={styles.error}>{error}</p>}

        <div className='mt-md'>
          <label htmlFor="email" className={styles.inputlabel}>Email</label>
          <input
            type="email"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            className={styles.input}
          />
        </div>

        <div className='mt-md'>
          <label htmlFor="password" className={styles.inputlabel}>Senha</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            className={styles.input}
          />
        </div>

        <Button type="submit">Entrar</Button>
      </form>
    </div>
  );
};

export default LoginPage;
