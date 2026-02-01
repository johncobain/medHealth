import { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import { useAuth } from '../../context/AuthContext';
import authService from '../../services/authService';
import patientService from '../../services/patientService';
import doctorService from '../../services/doctorService';
import Button from '../../components/button/Button';
import AddressForm from '../../components/addressForm/AddressForm';
import { formatPhone } from '../../utils/formatters';
import { validatePhone, validateAddress } from '../../utils/validators';
import { extractErrorMessage } from '../../utils/errorHandler';

const emptyAddress = () => ({
  state: '',
  city: '',
  neighborhood: '',
  street: '',
  number: '',
  complement: '',
  zipCode: '',
});

const Settings = () => {
  const { user } = useAuth();
  const isPatient = user?.role === 'ROLE_PATIENT';
  const isDoctor = user?.role === 'ROLE_DOCTOR';

  const [passwordData, setPasswordData] = useState({
    oldPassword: '',
    newPassword: '',
    confirmPassword: '',
  });
  const [passwordLoading, setPasswordLoading] = useState(false);

  const [profileData, setProfileData] = useState({
    fullName: '',
    phone: '',
    address: emptyAddress(),
  });
  const [profileLoading, setProfileLoading] = useState(false);
  const [fetchingProfile, setFetchingProfile] = useState(true);

  useEffect(() => {
    const fetchProfile = async () => {
      if (!isPatient && !isDoctor) {
        setFetchingProfile(false);
        return;
      }

      try {
        let data;
        if (isPatient) {
          data = await patientService.getMyData();
        } else if (isDoctor) {
          data = await doctorService.getMyData();
        }

        setProfileData({
          fullName: data.fullName || '',
          phone: data.phone || '',
          address: data.address || emptyAddress(),
        });
      } catch (error) {
        toast.error(extractErrorMessage(error, 'Erro ao carregar dados do perfil.'));
      } finally {
        setFetchingProfile(false);
      }
    };

    fetchProfile();
  }, [isPatient, isDoctor]);

  const handlePasswordChange = (e) => {
    setPasswordData({
      ...passwordData,
      [e.target.name]: e.target.value,
    });
  };

  const handleProfileChange = (e) => {
    const { name, value } = e.target;

    if (name === 'phone') {
      setProfileData((prev) => ({ ...prev, phone: formatPhone(value) }));
      return;
    }

    if (name.startsWith('address.')) {
      const field = name.replace('address.', '');
      setProfileData((prev) => ({
        ...prev,
        address: { ...prev.address, [field]: value },
      }));
    } else {
      setProfileData((prev) => ({ ...prev, [name]: value }));
    }
  };

  const handlePasswordSubmit = async (e) => {
    e.preventDefault();

    if (passwordData.newPassword !== passwordData.confirmPassword) {
      toast.error('A nova senha e a confirmação não coincidem.');
      return;
    }

    if (passwordData.newPassword.length < 6) {
      toast.error('A nova senha deve ter no mínimo 6 caracteres.');
      return;
    }

    setPasswordLoading(true);
    try {
      await authService.changePassword(passwordData.oldPassword, passwordData.newPassword);
      toast.success('Senha alterada com sucesso.');
      setPasswordData({ oldPassword: '', newPassword: '', confirmPassword: '' });
    } catch (error) {
      toast.error(extractErrorMessage(error, 'Erro ao alterar a senha.'));
    } finally {
      setPasswordLoading(false);
    }
  };

  const handleProfileSubmit = async (e) => {
    e.preventDefault();

    if (!profileData.fullName?.trim()) {
      toast.error('Nome completo é obrigatório.');
      return;
    }

    if (profileData.phone && !validatePhone(profileData.phone)) {
      toast.error('Telefone no formato (99) 99999-9999.');
      return;
    }

    const addressValidation = validateAddress(profileData.address);
    if (!addressValidation.valid) {
      toast.error(addressValidation.message);
      return;
    }

    setProfileLoading(true);
    try {
      const payload = {
        fullName: profileData.fullName.trim(),
        ...(profileData.phone && { phone: profileData.phone }),
      };

      const hasAddress = Object.values(profileData.address).some((v) => v?.trim());
      if (hasAddress) {
        payload.address = {
          state: profileData.address.state.trim(),
          city: profileData.address.city.trim(),
          neighborhood: profileData.address.neighborhood.trim(),
          street: profileData.address.street.trim(),
          number: profileData.address.number?.trim() || '',
          complement: profileData.address.complement?.trim() || '',
          zipCode: profileData.address.zipCode,
        };
      }

      if (isPatient) {
        await patientService.updateMyData(payload);
      } else if (isDoctor) {
        await doctorService.updateMyData(payload);
      }

      toast.success('Dados atualizados com sucesso.');
    } catch (error) {
      toast.error(extractErrorMessage(error, 'Erro ao atualizar dados.'));
    } finally {
      setProfileLoading(false);
    }
  };

  return (
    <>
      <h2 className="text-2xl text-bold mb-lg text-primary">Configurações da Conta</h2>

      {(isPatient || isDoctor) && (
        <div className="mb-xl flex flex-col gap-md pb-md bb-sm">
          <h3 className="text-lg mb-md">Dados Pessoais</h3>

          {fetchingProfile ? (
            <p className="text-light">Carregando dados...</p>
          ) : (
            <form onSubmit={handleProfileSubmit} className="flex flex-col gap-md">
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <div>
                  <label className="block text-sm mb-xs">Nome Completo *</label>
                  <input
                    type="text"
                    name="fullName"
                    value={profileData.fullName}
                    onChange={handleProfileChange}
                    required
                    className="input"
                  />
                </div>

                <div>
                  <label className="block text-sm mb-xs">Telefone</label>
                  <input
                    type="text"
                    name="phone"
                    value={profileData.phone}
                    onChange={handleProfileChange}
                    placeholder="(99) 99999-9999"
                    maxLength={15}
                    className="input"
                  />
                </div>
              </div>

              <AddressForm address={profileData.address} onChange={handleProfileChange} />

              <div className="mt-sm">
                <Button type="submit" disabled={profileLoading}>
                  {profileLoading ? 'Salvando...' : 'Atualizar Dados'}
                </Button>
              </div>
            </form>
          )}
        </div>
      )}

      <div className="mb-xl flex flex-col gap-md pt-md">
        <h3 className="text-lg mb-md">Alterar Senha</h3>

        <form onSubmit={handlePasswordSubmit} className="flex flex-col gap-md">
          <div>
            <label className="block text-sm mb-xs">Senha Atual *</label>
            <input
              type="password"
              name="oldPassword"
              value={passwordData.oldPassword}
              onChange={handlePasswordChange}
              required
              className="input"
              style={{ width: '37%' }}
            />
          </div>

          <div>
            <label className="block text-sm mb-xs">Nova Senha *</label>
            <input
              type="password"
              name="newPassword"
              value={passwordData.newPassword}
              onChange={handlePasswordChange}
              required
              className="input"
              style={{ width: '37%' }}
            />
          </div>

          <div>
            <label className="block text-sm mb-xs">Confirmar Nova Senha *</label>
            <input
              type="password"
              name="confirmPassword"
              value={passwordData.confirmPassword}
              onChange={handlePasswordChange}
              required
              className="input"
              style={{ width: '37%' }}
            />
          </div>

          <div className="mt-sm">
            <Button type="submit" disabled={passwordLoading}>
              {passwordLoading ? 'Salvando...' : 'Atualizar Senha'}
            </Button>
          </div>
        </form>
      </div>
    </>
  );
};

export default Settings;