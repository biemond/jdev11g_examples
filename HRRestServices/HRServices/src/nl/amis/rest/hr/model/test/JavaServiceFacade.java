package nl.amis.rest.hr.model.test;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import nl.amis.rest.hr.model.entities.Departments;

public class JavaServiceFacade {
    private static final boolean isAutoCommit = true;
    private final EntityManagerHelper entityManagerHelper;

    public JavaServiceFacade() {
        entityManagerHelper = new EntityManagerHelper("HRLocal", isAutoCommit);
    }

    public List<Departments> getAllDepartments(){
        return entityManagerHelper.getEntityManager().createNamedQuery("Departments.findAll").getResultList();
    }


    public Object queryByRange(String jpqlStmt, int firstResult, int maxResults) {
        Query query = entityManagerHelper.getEntityManager().createQuery(jpqlStmt);
        if (firstResult > 0) {
            query = query.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
            query = query.setMaxResults(maxResults);
        }
        return query.getResultList();
    }

    private class EntityManagerHelper {
        final private EntityManagerFactory _entityManagerFactory;
        final private boolean _isAutoCommit;

        private EntityManager _entityManager;

        EntityManagerHelper(String persistenceUnit, boolean isAutoCommit) {
            _entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
            _isAutoCommit = isAutoCommit;
        }

        public EntityManager getEntityManager() {
            if (_entityManager == null) {
                _entityManager = _entityManagerFactory.createEntityManager();
            }

            return _entityManager;
        }

        public EntityTransaction getEntityTransaction() {
            return getEntityManager().getTransaction();
        }

        public void commitTransaction() {
            final EntityTransaction entityTransaction = getEntityTransaction();
            if (entityTransaction.isActive()) {
                entityTransaction.commit();
            }

            _closeEntityManager();
        }

        public void rollbackTransaction() {
            final EntityTransaction entityTransaction = getEntityTransaction();
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }

            _closeEntityManager();
        }

        public boolean isTransactionDirty() {
            return (!_isAutoCommit && getEntityTransaction().isActive());
        }

        public Object persistEntity(Object entity) {
            _beginTransactionIfNeeded();
            _entityManager.persist(entity);
            _commitTransactionIfNeeded();

            return entity;
        }

        public Object mergeEntity(Object entity) {
            _beginTransactionIfNeeded();
            entity = _entityManager.merge(entity);
            _commitTransactionIfNeeded();

            return entity;
        }

        public void removeEntity(Object entity) {
            _beginTransactionIfNeeded();
            _entityManager.remove(entity);
            _commitTransactionIfNeeded();
        }

        private void _beginTransactionIfNeeded() {
            final EntityTransaction entityTransaction = getEntityTransaction();
            if (!entityTransaction.isActive()) {
                entityTransaction.begin();
            }
        }

        private void _commitTransactionIfNeeded() {
            if (_isAutoCommit) {
                commitTransaction();
            }
        }

        private void _closeEntityManager() {
            if (_entityManager != null && _entityManager.isOpen()) {
                _entityManager.close();
            }

            _entityManager = null;
        }
    }
}
