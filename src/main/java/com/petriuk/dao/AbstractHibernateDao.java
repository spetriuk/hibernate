package com.petriuk.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

public class AbstractHibernateDao<T> {

    protected void createUpdate(Session session, T t) {
        if(session.getTransaction().getStatus().equals(TransactionStatus.ACTIVE)){
            session.persist(t);
        } else {
            Transaction transaction = session.beginTransaction();
            session.persist(t);
            transaction.commit();
        }
    }

    protected void deleteEntity(Session session, T t) {
        if(session.getTransaction().getStatus().equals(TransactionStatus.ACTIVE)){
            session.delete(t);
        } else {
            Transaction transaction = session.beginTransaction();
            session.delete(t);
            transaction.commit();
        }
    }

}
