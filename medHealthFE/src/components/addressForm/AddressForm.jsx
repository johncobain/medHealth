import { formatZipCode } from '../../utils/formatters';
import styles from './AddressForm.module.css';

/**
 * Componente de formulário de endereço reutilizável
 * @param {Object} props
 * @param {Object} props.address - Objeto de endereço
 * @param {Function} props.onChange - Callback de mudança
 * @param {boolean} props.required - Se os campos são obrigatórios
 */
const AddressForm = ({ address, onChange, required = false }) => {
  const handleChange = (e) => {
    const { name, value } = e.target;
    const field = name.replace('address.', '');
    
    let formattedValue = value;
    if (field === 'zipCode') {
      formattedValue = formatZipCode(value);
    }
    
    onChange({
      target: {
        name,
        value: formattedValue,
      },
    });
  };

  return (
    <div className={styles.container}>
      <label className={styles.label}>Endereço{required && ' *'}</label>
      <div className={styles.grid}>
        <input
          type="text"
          name="address.state"
          className="input"
          placeholder={`Estado${required ? '*' : ''}`}
          value={address.state}
          onChange={handleChange}
          required={required}
        />
        <input
          type="text"
          name="address.city"
          className="input"
          placeholder={`Cidade${required ? '*' : ''}`}
          value={address.city}
          onChange={handleChange}
          required={required}
        />
        <input
          type="text"
          name="address.neighborhood"
          className="input"
          placeholder={`Bairro${required ? '*' : ''}`}
          value={address.neighborhood}
          onChange={handleChange}
          required={required}
        />
        <input
          type="text"
          name="address.street"
          className="input"
          placeholder={`Rua${required ? '*' : ''}`}
          value={address.street}
          onChange={handleChange}
          required={required}
        />
        <div className={styles.row}>
          <input
            type="text"
            name="address.number"
            className="input"
            placeholder="Número"
            value={address.number}
            onChange={handleChange}
          />
          <input
            type="text"
            name="address.zipCode"
            className="input"
            placeholder={`CEP${required ? '*' : ''} 99999-999`}
            value={address.zipCode}
            onChange={handleChange}
            maxLength={9}
            required={required}
          />
        </div>
        <input
          type="text"
          name="address.complement"
          className="input"
          placeholder="Complemento"
          value={address.complement}
          onChange={handleChange}
          style={{ gridColumn: '1 / -1' }}
        />
      </div>
    </div>
  );
};

export default AddressForm;