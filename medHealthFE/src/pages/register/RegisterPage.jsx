import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import Button from '../../components/button/Button';
import AddressForm from '../../components/addressForm/AddressForm';
import authService from '../../services/authService';
import doctorService from '../../services/doctorService';
import { formatPhone, formatCPF, formatZipCode, formatCRM } from '../../utils/formatters';
import { validatePhone, validateCPF, validateEmail, validateCRM } from '../../utils/validators';
import { extractErrorMessage } from '../../utils/errorHandler';
import styles from './RegisterPage.module.css';

const emptyPatientForm = () => ({
  fullName: '',
  email: '',
  phone: '',
  cpf: '',
  address: {
    state: '',
    city: '',
    neighborhood: '',
    street: '',
    number: '',
    complement: '',
    zipCode: '',
  },
});

const emptyDoctorForm = () => ({
  fullName: '',
  email: '',
  phone: '',
  cpf: '',
  crm: '',
  specialty: '',
  state: '',
  city: '',
  neighborhood: '',
  street: '',
  number: '',
  complement: '',
  zipCode: '',
});

const RegisterPage = () => {
  const navigate = useNavigate();
  const [registerType, setRegisterType] = useState('patient'); // 'patient' ou 'doctor'
  const [patientForm, setPatientForm] = useState(emptyPatientForm());
  const [doctorForm, setDoctorForm] = useState(emptyDoctorForm());
  const [loading, setLoading] = useState(false);
  const [specialties, setSpecialties] = useState([]);

  useEffect(() => {
    const fetchSpecialties = async () => {
      try {
        const data = await doctorService.getSpecialties();
        setSpecialties(data);
      } catch (err) {
        console.error('Erro ao buscar especialidades:', err);
      }
    };
    fetchSpecialties();
  }, []);

  const handlePatientChange = (e) => {
    const { name, value } = e.target;
    
    if (name.startsWith('address.')) {
      const field = name.replace('address.', '');
      setPatientForm((prev) => ({
        ...prev,
        address: {
          ...prev.address,
          [field]: value,
        },
      }));
    } else {
      let formattedValue = value;
      if (name === 'phone') formattedValue = formatPhone(value);
      if (name === 'cpf') formattedValue = formatCPF(value);
      
      setPatientForm((prev) => ({
        ...prev,
        [name]: formattedValue,
      }));
    }
  };

  const handleDoctorChange = (e) => {
    const { name, value } = e.target;
    
    let formattedValue = value;
    if (name === 'phone') formattedValue = formatPhone(value);
    if (name === 'cpf') formattedValue = formatCPF(value);
    if (name === 'zipCode') formattedValue = formatZipCode(value);
    if (name === 'crm') formattedValue = formatCRM(value);
    
    setDoctorForm((prev) => ({
      ...prev,
      [name]: formattedValue,
    }));
  };

  const validatePatientForm = () => {
    if (!patientForm.fullName.trim()) {
      toast.error('Campo obrigatório: Nome completo');
      return false;
    }
    if (!validateEmail(patientForm.email)) {
      toast.error('Campo inválido: Email (formato esperado: exemplo@email.com)');
      return false;
    }
    if (!validatePhone(patientForm.phone)) {
      toast.error('Campo inválido: Telefone (formato esperado: (99) 99999-9999)');
      return false;
    }
    if (!validateCPF(patientForm.cpf)) {
      toast.error('Campo inválido: CPF (formato esperado: 999.999.999-99)');
      return false;
    }
    if (!patientForm.address.state) {
      toast.error('Campo obrigatório: Estado (Endereço)');
      return false;
    }
    if (!patientForm.address.city) {
      toast.error('Campo obrigatório: Cidade (Endereço)');
      return false;
    }
    if (!patientForm.address.neighborhood) {
      toast.error('Campo obrigatório: Bairro (Endereço)');
      return false;
    }
    if (!patientForm.address.street) {
      toast.error('Campo obrigatório: Rua (Endereço)');
      return false;
    }
    if (!patientForm.address.zipCode) {
      toast.error('Campo obrigatório: CEP (Endereço)');
      return false;
    }
    return true;
  };

  const validateDoctorForm = () => {
    if (!doctorForm.fullName.trim()) {
      toast.error('Campo obrigatório: Nome completo');
      return false;
    }
    if (!validateEmail(doctorForm.email)) {
      toast.error('Campo inválido: Email (formato esperado: exemplo@email.com)');
      return false;
    }
    if (!validatePhone(doctorForm.phone)) {
      toast.error('Campo inválido: Telefone (formato esperado: (99) 99999-9999)');
      return false;
    }
    if (!validateCPF(doctorForm.cpf)) {
      toast.error('Campo inválido: CPF (formato esperado: 999.999.999-99)');
      return false;
    }
    if (!validateCRM(doctorForm.crm)) {
      toast.error('Campo inválido: CRM (formato esperado: CRM-UF-NUMERO (ex: CRM-SP-12345))');
      return false;
    }
    if (!doctorForm.crm.trim()) {
      toast.error('Campo obrigatório: CRM');
      return false;
    }
    if (!doctorForm.specialty.trim()) {
      toast.error('Campo obrigatório: Especialidade');
      return false;
    }
    if (!doctorForm.state) {
      toast.error('Campo obrigatório: Estado (Endereço)');
      return false;
    }
    if (!doctorForm.city) {
      toast.error('Campo obrigatório: Cidade (Endereço)');
      return false;
    }
    if (!doctorForm.neighborhood) {
      toast.error('Campo obrigatório: Bairro (Endereço)');
      return false;
    }
    if (!doctorForm.street) {
      toast.error('Campo obrigatório: Rua (Endereço)');
      return false;
    }
    if (!doctorForm.zipCode) {
      toast.error('Campo obrigatório: CEP (Endereço)');
      return false;
    }
    return true;
  };

  const handlePatientSubmit = async (e) => {
    e.preventDefault();
    if (!validatePatientForm()) return;

    setLoading(true);
    try {
      await authService.register(patientForm);
      toast.success('Paciente cadastrado com sucesso! Uma senha provisória foi enviada para o email.');
      navigate('/login');
    } catch (err) {
      toast.error(extractErrorMessage(err, 'Erro ao cadastrar paciente.'));
    } finally {
      setLoading(false);
    }
  };

  const handleDoctorSubmit = async (e) => {
    e.preventDefault();
    if (!validateDoctorForm()) return;

    setLoading(true);
    try {
      await authService.requestRegister(doctorForm);
      toast.success('Pedido de registro enviado com sucesso! Aguarde aprovação do administrador.');
      navigate('/login');
    } catch (err) {
      toast.error(extractErrorMessage(err, 'Erro ao enviar pedido de registro.'));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.wrapper}>
        <h1 className={styles.title}>MedHealth</h1>
        <h2 className={styles.subtitle}>Cadastro</h2>

        <div className={styles.tabs}>
          <button
            type="button"
            className={`${styles.tab} ${registerType === 'patient' ? styles.active : ''}`}
            onClick={() => setRegisterType('patient')}
          >
            Cadastrar Paciente
          </button>
          <button
            type="button"
            className={`${styles.tab} ${registerType === 'doctor' ? styles.active : ''}`}
            onClick={() => setRegisterType('doctor')}
          >
            Solicitar Registro Médico
          </button>
        </div>

        {registerType === 'patient' ? (
          <form onSubmit={handlePatientSubmit} className={styles.form}>
            <div className={styles.formSection}>
              <h3 className={styles.sectionTitle}>Dados Pessoais</h3>
              
              <div className={styles.inputGroup}>
                <label htmlFor="patient-fullName" className={styles.label}>
                  Nome Completo *
                </label>
                <input
                  type="text"
                  id="patient-fullName"
                  name="fullName"
                  value={patientForm.fullName}
                  onChange={handlePatientChange}
                  required
                  className="input"
                />
              </div>

              <div className={styles.inputGroup}>
                <label htmlFor="patient-email" className={styles.label}>
                  Email *
                </label>
                <input
                  type="email"
                  id="patient-email"
                  name="email"
                  value={patientForm.email}
                  onChange={handlePatientChange}
                  required
                  className="input"
                />
              </div>

              <div className={styles.row}>
                <div className={styles.inputGroup}>
                  <label htmlFor="patient-phone" className={styles.label}>
                    Telefone *
                  </label>
                  <input
                    type="tel"
                    id="patient-phone"
                    name="phone"
                    value={patientForm.phone}
                    onChange={handlePatientChange}
                    placeholder="(99) 99999-9999"
                    required
                    className="input"
                  />
                </div>

                <div className={styles.inputGroup}>
                  <label htmlFor="patient-cpf" className={styles.label}>
                    CPF *
                  </label>
                  <input
                    type="text"
                    id="patient-cpf"
                    name="cpf"
                    value={patientForm.cpf}
                    onChange={handlePatientChange}
                    placeholder="999.999.999-99"
                    required
                    className="input"
                  />
                </div>
              </div>
            </div>

            <div className={styles.formSection}>
              <AddressForm
                address={patientForm.address}
                onChange={handlePatientChange}
                required
              />
            </div>

            <div className={styles.actions}>
              <Button
                type="button"
                variant="secondary"
                onClick={() => navigate('/login')}
                disabled={loading}
              >
                Cancelar
              </Button>
              <Button type="submit" disabled={loading}>
                {loading ? 'Cadastrando...' : 'Cadastrar Paciente'}
              </Button>
            </div>
          </form>
        ) : (
          <form onSubmit={handleDoctorSubmit} className={styles.form}>
            <div className={styles.formSection}>
              <h3 className={styles.sectionTitle}>Dados Pessoais</h3>
              
              <div className={styles.inputGroup}>
                <label htmlFor="doctor-fullName" className={styles.label}>
                  Nome Completo *
                </label>
                <input
                  type="text"
                  id="doctor-fullName"
                  name="fullName"
                  value={doctorForm.fullName}
                  onChange={handleDoctorChange}
                  required
                  className="input"
                />
              </div>

              <div className={styles.inputGroup}>
                <label htmlFor="doctor-email" className={styles.label}>
                  Email *
                </label>
                <input
                  type="email"
                  id="doctor-email"
                  name="email"
                  value={doctorForm.email}
                  onChange={handleDoctorChange}
                  required
                  className="input"
                />
              </div>

              <div className={styles.row}>
                <div className={styles.inputGroup}>
                  <label htmlFor="doctor-phone" className={styles.label}>
                    Telefone *
                  </label>
                  <input
                    type="tel"
                    id="doctor-phone"
                    name="phone"
                    value={doctorForm.phone}
                    onChange={handleDoctorChange}
                    placeholder="(99) 99999-9999"
                    required
                    className="input"
                  />
                </div>

                <div className={styles.inputGroup}>
                  <label htmlFor="doctor-cpf" className={styles.label}>
                    CPF *
                  </label>
                  <input
                    type="text"
                    id="doctor-cpf"
                    name="cpf"
                    value={doctorForm.cpf}
                    onChange={handleDoctorChange}
                    placeholder="999.999.999-99"
                    required
                    className="input"
                  />
                </div>
              </div>

              <div className={styles.row}>
                <div className={styles.inputGroup}>
                  <label htmlFor="doctor-crm" className={styles.label}>
                    CRM *
                  </label>
                  <input
                    type="text"
                    id="doctor-crm"
                    name="crm"
                    value={doctorForm.crm}
                    onChange={handleDoctorChange}
                    placeholder="CRM-UF-123456"
                    required
                    className="input"
                  />
                </div>

                <div className={styles.inputGroup}>
                  <label htmlFor="doctor-specialty" className={styles.label}>
                    Especialidade *
                  </label>
                  <select
                    id="doctor-specialty"
                    name="specialty"
                    value={doctorForm.specialty}
                    onChange={handleDoctorChange}
                    required
                    className="input"
                  >
                    <option value="">Selecione...</option>
                    {specialties.map((spec) => (
                      <option key={spec.specialty} value={spec.specialty}>
                        {spec.specialtyDescription}
                      </option>
                    ))}
                  </select>
                </div>
              </div>
            </div>

            <div className={styles.formSection}>
              <h3 className={styles.sectionTitle}>Endereço</h3>
              
              <div className={styles.row}>
                <div className={styles.inputGroup}>
                  <label htmlFor="doctor-state" className={styles.label}>
                    Estado *
                  </label>
                  <input
                    type="text"
                    id="doctor-state"
                    name="state"
                    value={doctorForm.state}
                    onChange={handleDoctorChange}
                    required
                    className="input"
                  />
                </div>

                <div className={styles.inputGroup}>
                  <label htmlFor="doctor-city" className={styles.label}>
                    Cidade *
                  </label>
                  <input
                    type="text"
                    id="doctor-city"
                    name="city"
                    value={doctorForm.city}
                    onChange={handleDoctorChange}
                    required
                    className="input"
                  />
                </div>
              </div>

              <div className={styles.inputGroup}>
                <label htmlFor="doctor-neighborhood" className={styles.label}>
                  Bairro *
                </label>
                <input
                  type="text"
                  id="doctor-neighborhood"
                  name="neighborhood"
                  value={doctorForm.neighborhood}
                  onChange={handleDoctorChange}
                  required
                  className="input"
                />
              </div>

              <div className={styles.inputGroup}>
                <label htmlFor="doctor-street" className={styles.label}>
                  Rua *
                </label>
                <input
                  type="text"
                  id="doctor-street"
                  name="street"
                  value={doctorForm.street}
                  onChange={handleDoctorChange}
                  required
                  className="input"
                />
              </div>

              <div className={styles.row}>
                <div className={styles.inputGroup}>
                  <label htmlFor="doctor-number" className={styles.label}>
                    Número
                  </label>
                  <input
                    type="text"
                    id="doctor-number"
                    name="number"
                    value={doctorForm.number}
                    onChange={handleDoctorChange}
                    className="input"
                  />
                </div>

                <div className={styles.inputGroup}>
                  <label htmlFor="doctor-zipCode" className={styles.label}>
                    CEP *
                  </label>
                  <input
                    type="text"
                    id="doctor-zipCode"
                    name="zipCode"
                    value={doctorForm.zipCode}
                    onChange={handleDoctorChange}
                    placeholder="99999-999"
                    maxLength={9}
                    required
                    className="input"
                  />
                </div>
              </div>

              <div className={styles.inputGroup}>
                <label htmlFor="doctor-complement" className={styles.label}>
                  Complemento
                </label>
                <input
                  type="text"
                  id="doctor-complement"
                  name="complement"
                  value={doctorForm.complement}
                  onChange={handleDoctorChange}
                  className="input"
                />
              </div>
            </div>

            <div className={styles.actions}>
              <Button
                type="button"
                variant="secondary"
                onClick={() => navigate('/login')}
                disabled={loading}
              >
                Cancelar
              </Button>
              <Button type="submit" disabled={loading}>
                {loading ? 'Enviando...' : 'Solicitar Registro'}
              </Button>
            </div>
          </form>
        )}
      </div>
    </div>
  );
};

export default RegisterPage;
