package com.petriuk.dao;


import com.petriuk.entity.Role;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.sql.SQLException;
import java.util.Optional;

public class HibernateRoleDao extends AbstractHibernateDao<Role> implements RoleDao{
    private final Session session;

    public HibernateRoleDao(Session session) {
        this.session = session;
    }

    @Override
    public void create(Role role) {
        createUpdate(session, role);
    }

    @Override
    public void update(Role role) {
        createUpdate(session, role);
    }

    @Override
    public void remove(Role role) {
        deleteEntity(session, role);
    }

    @Override
    public Optional<Role> findByName(String name) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Role> criteriaQuery = builder.createQuery(Role.class);
        Root<Role> roleRoot = criteriaQuery.from(Role.class);
        criteriaQuery.select(roleRoot).where(builder.equal(roleRoot.get("name"), name));
        Query<Role> query = session.createQuery(criteriaQuery);
        return query.getResultList().stream().findFirst();
    }
}
