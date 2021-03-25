package com.petriuk.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.petriuk.dao.HibernateRoleDao;
import com.petriuk.dao.HibernateUserDao;
import com.petriuk.dao.RoleDao;
import com.petriuk.dao.UserDao;
import com.petriuk.dto.UserDto;
import com.petriuk.entity.Role;
import com.petriuk.entity.User;
import com.petriuk.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserService {
    private static final Logger LOG = LogManager.getLogger();

    public Optional<User> checkUser(String login, String password) {
        Optional<User> userOptional = getUserByLogin(login);
        if (userOptional.isPresent() && verifyPassword(password,
            userOptional.get().getPassword())) {
            return userOptional;
        }
        return Optional.empty();
    }

    public Optional<User> getUserByLogin(String login) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            UserDao dao = new HibernateUserDao(session);
            return dao.findByLogin(login);
        }
    }

    public List<UserDto> getAllUsersAsDto() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            UserDao dao = new HibernateUserDao(session);
            List<User> userList = dao.findAll();
            return userList.stream().map(UserDto::new)
                .collect(Collectors.toList());
        }
    }

    public boolean addUser(User user){
        user.setPassword(encodePassword(user.getPassword()));
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            RoleDao roleDao = new HibernateRoleDao(session);
            UserDao userDao = new HibernateUserDao(session);
            Transaction transaction = session.beginTransaction();
            try {
                Role role = roleDao.findByName(user.getRole().getName()).orElseThrow(
                    PersistenceException::new);
                user.setRole(role);
                userDao.create(user);
            } catch (PersistenceException e) {
                transaction.rollback();
                LOG.error("Transaction failed", e);
                return false;
            }
            transaction.commit();
            return true;
        }
    }

    public boolean updateUser(User editedUser, String userLogin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            UserDao userDao = new HibernateUserDao(session);
            RoleDao roleDao = new HibernateRoleDao(session);

            Transaction transaction = session.beginTransaction();
            try {
                User user = userDao.findByLogin(userLogin)
                    .orElseThrow(PersistenceException::new);
                user.setLogin(editedUser.getLogin());
                user.setEmail(editedUser.getEmail());
                user.setFirstName(editedUser.getFirstName());
                user.setLastName(editedUser.getLastName());
                user.setBirthday(editedUser.getBirthday());
                if (!editedUser.getRole().equals(user.getRole())) {
                    Role role = roleDao
                        .findByName(editedUser.getRole().getName())
                        .orElseThrow(PersistenceException::new);
                    user.setRole(role);
                }
                if (editedUser.getPassword() != null && !editedUser
                    .getPassword().isEmpty()) {
                    user.setPassword(encodePassword(editedUser.getPassword()));
                }
                userDao.update(user);
            } catch (PersistenceException e) {
                transaction.rollback();
                LOG.error("Transaction failed", e);
                return false;
            }
            transaction.commit();
            return true;
        }
    }

    public boolean deleteUser(String login) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            UserDao userDao = new HibernateUserDao(session);
            Transaction transaction = session.beginTransaction();
            try {
                Optional<User> user = userDao.findByLogin(login);
                user.ifPresent(userDao::remove);
            } catch (PersistenceException e) {
                transaction.rollback();
                LOG.error("Transaction failed", e);
                return false;
            }
            transaction.commit();
            return true;
        }
    }

    private String encodePassword(String password) {
        return BCrypt.withDefaults().hashToString(10, password.toCharArray());
    }

    private boolean verifyPassword(String password, String encryptedPassword) {
        BCrypt.Result result = BCrypt.verifyer()
            .verify(password.toCharArray(), encryptedPassword);
        return result.verified;
    }
}
