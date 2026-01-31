import styles from './StatsCard.module.css';
import Button from '../button/Button';
import { Eye, Plus } from 'lucide-react';

const StatsCard = ({
  icon,
  title,
  mainValue,
  mainLabel,
  subLabel,
  primaryAction,
  primaryActionLabel,
  secondaryAction,
  secondaryActionLabel,
}) => {
  return (
    <div className={styles.card}>
      <div className={styles.header}>
        <span className={styles.icon}>{icon}</span>
        <h3 className={styles.title}>{title}</h3>
      </div>

      <div className={styles.content}>
        <span className={styles.mainValue}>{mainValue}</span>
        <span className={styles.mainLabel}>{mainLabel}</span>
        {subLabel && <span className={styles.subLabel}>{subLabel}</span>}
      </div>

      <div className={styles.actions}>
        <button className={styles.actionBtn} onClick={primaryAction}>
          <Eye size={16} strokeWidth={2} />
          {primaryActionLabel}
        </button>
        <Button variant="outline" size="sm" onClick={secondaryAction}>
          <Plus size={16} strokeWidth={2} />
          {secondaryActionLabel}
        </Button>
      </div>
    </div>
  );
};

export default StatsCard;