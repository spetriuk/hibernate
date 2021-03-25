package com.petriuk.dao;

import com.petriuk.entity.User;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class HibernateUserDao extends AbstractHibernateDao<User> implements UserDao{

    private final Session session;

    public HibernateUserDao(Session session) {
        this.session = session;
    }

    @Override
    public void create(User user) {
        createUpdate(session, user);
    }

    @Override
    public void update(User user) {
        createUpdate(session, user);
    }

    @Override
    public void remove(User user) {
        deleteEntity(session, user);
    }

    @Override
    public List<User> findAll() {
        TypedQuery<User> typedQuery = session.createQuery("from User", User.class);
        return typedQuery.getResultList();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        TypedQuery<User> query = session.createQuery("from User where login = :login", User.class);
        query.setParameter("login", login);
        try {
            User user = query.getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        TypedQuery<User> query = session.createQuery("from User where email = :email", User.class);
        query.setParameter("email", email);
        try {
            User user = query.getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
