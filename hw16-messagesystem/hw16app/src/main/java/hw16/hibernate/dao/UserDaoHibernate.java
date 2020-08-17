package hw16.hibernate.dao;


import hw16.core.dao.UserDao;
import hw16.core.dao.UserDaoException;
import hw16.core.model.User;
import hw16.core.sessionmanager.SessionManager;
import hw16.hibernate.sessionmanager.DatabaseSessionHibernate;
import hw16.hibernate.sessionmanager.SessionManagerHibernate;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserDaoHibernate implements UserDao {
    private static Logger logger = LoggerFactory.getLogger(UserDaoHibernate.class);
    private static final String ID_COLUMN_NAME = "id";
    private final SessionManagerHibernate sessionManager;

    @Override
    public Optional<User> findById(long id) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            CriteriaBuilder cb = currentSession.getHibernateSession().getCriteriaBuilder();
            CriteriaQuery<User> cr = cb.createQuery(User.class);
            Root<User> root = cr.from(User.class);
            cr.select(root).distinct(true).where(cb.equal(root.get(ID_COLUMN_NAME), id));
            Query<User> query = currentSession.getHibernateSession().createQuery(cr);
            User result = query.getSingleResult();
            return Optional.ofNullable(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            CriteriaBuilder cb = currentSession.getHibernateSession().getCriteriaBuilder();
            CriteriaQuery<User> cr = cb.createQuery(User.class);
            Root<User> root = cr.from(User.class);
            cr.select(root).distinct(true);
            Query<User> query = currentSession.getHibernateSession().createQuery(cr);
            return query.getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public int deleteUserById(long id) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            CriteriaBuilder cb = currentSession.getHibernateSession().getCriteriaBuilder();
            CriteriaDelete<User> criteriaDelete = cb.createCriteriaDelete(User.class);
            Root<User> root = criteriaDelete.from(User.class);
            criteriaDelete.where(cb.equal(root.get(ID_COLUMN_NAME), id));
            return hibernateSession.createQuery(criteriaDelete).executeUpdate();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void insertOrUpdate(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            if (user.getId() > 0) {
                hibernateSession.merge(user);
            } else {
                hibernateSession.persist(user);
                hibernateSession.flush();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
