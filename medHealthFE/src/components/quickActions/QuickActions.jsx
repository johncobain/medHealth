import styles from './QuickActions.module.css';
import Button from '../button/Button';

const QuickActions = ({ actions }) => {
  return (
    <section className={styles.container}>
      <h2 className={styles.title}>Ações Rápidas</h2>
      <div className={styles.actions}>
        {actions.map((action, index) => (
          <Button
            key={index}
            variant={action.variant || 'primary'}
            size="md"
            onClick={action.onClick}
          >
            <span className={styles.iconWrapper}>{action.icon}</span>
            {action.label}
          </Button>
        ))}
      </div>
    </section>
  );
};

export default QuickActions;