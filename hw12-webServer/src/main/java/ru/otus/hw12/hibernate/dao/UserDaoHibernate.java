package ru.otus.hw12.hibernate.dao;


import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw12.core.dao.UserDao;
import ru.otus.hw12.core.dao.UserDaoException;
import ru.otus.hw12.core.model.User;
import ru.otus.hw12.core.sessionmanager.SessionManager;
import ru.otus.hw12.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.hw12.hibernate.sessionmanager.SessionManagerHibernate;

import javax.persistence.EntityGraph;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.*;

public class UserDaoHibernate implements UserDao {
    private static Logger logger = LoggerFactory.getLogger(UserDaoHibernate.class);
    private static final String ID_COLUMN_NAME = "phones";
    private static final String PHONES_COLUMN_NAME = "phones";
    private static final String ADDRESS_COLUMN_NAME = "address";
    private static final String LOGIN_COLUMN_NAME = "login";

    private final SessionManagerHibernate sessionManager;

    public UserDaoHibernate(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }


    @Override
    public Optional<User> findById(long id) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            EntityGraph entityGraph = currentSession.getHibernateSession().getEntityGraph("userEntityGraph");
            Map<String, Object> properties = new HashMap<>();
            properties.put("javax.persistence.fetchgraph", entityGraph);
            return Optional.ofNullable(currentSession.getHibernateSession().find(User.class, id, properties));
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
            root.fetch(PHONES_COLUMN_NAME, JoinType.LEFT);
            root.fetch(ADDRESS_COLUMN_NAME, JoinType.LEFT);
            cr.select(root);
            Query<User> query = currentSession.getHibernateSession().createQuery(cr);
            return query.getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<User> findByIdWithPhonesAndAddress(long id) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            CriteriaBuilder cb = currentSession.getHibernateSession().getCriteriaBuilder();
            CriteriaQuery<User> cr = cb.createQuery(User.class);
            Root<User> root = cr.from(User.class);
            root.fetch(PHONES_COLUMN_NAME, JoinType.LEFT);
            root.fetch(ADDRESS_COLUMN_NAME, JoinType.LEFT);
            cr.select(root).where(cb.equal(root.get(ID_COLUMN_NAME), id));
            Query<User> query = currentSession.getHibernateSession().createQuery(cr);
            User result = query.getSingleResult();
            return Optional.ofNullable(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public long insertUser(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.persist(user);
            hibernateSession.flush();
            return user.getId();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void updateUser(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.merge(user);
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
    public Optional<User> findRandomUser() {
        int userCount = getUserCount();
        if (userCount == 0) {
            return Optional.of(new User(-1, "NO_USER", "null", "null", null, null));
        }

        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            int randomNumber = new Random().nextInt(userCount);
            CriteriaBuilder cb = currentSession.getHibernateSession().getCriteriaBuilder();
            CriteriaQuery<User> cr = cb.createQuery(User.class);
            Root<User> root = cr.from(User.class);
            cr.select(root);
            Query<User> query = currentSession.getHibernateSession().createQuery(cr).setFirstResult(randomNumber).setMaxResults(1);
            User result = query.getResultList().get(0);
            return Optional.ofNullable(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            CriteriaBuilder cb = currentSession.getHibernateSession().getCriteriaBuilder();
            CriteriaQuery<User> cr = cb.createQuery(User.class);
            Root<User> root = cr.from(User.class);
            cr.select(root).where(cb.equal(root.get(LOGIN_COLUMN_NAME), login));
            Query<User> query = currentSession.getHibernateSession().createQuery(cr);
            User result = query.getSingleResult();
            return Optional.ofNullable(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    private int getUserCount() {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            CriteriaBuilder cb = currentSession.getHibernateSession().getCriteriaBuilder();
            CriteriaQuery<Long> cr = cb.createQuery(Long.class);
            Root<User> root = cr.from(User.class);
            cr.select(cb.count(root));
            Query<Long> query = currentSession.getHibernateSession().createQuery(cr);
            return query.getSingleResult().intValue();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
