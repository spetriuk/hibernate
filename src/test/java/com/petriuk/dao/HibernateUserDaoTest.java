package com.petriuk.dao;

import com.petriuk.entity.Role;
import com.petriuk.entity.User;
import org.dbunit.Assertion;
import org.dbunit.dataset.ITable;
import org.hibernate.Session;

import javax.persistence.PersistenceException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class HibernateUserDaoTest extends AbstractJdbcTest {
    private static final String USERS_TABLE_NAME = "user_accounts";
    private UserDao dao;
    private Role userRole;
    private Role adminRole;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        Session session = sessionFactory.openSession();
        userRole = (Role) session.get(Role.class, 1L);
        adminRole = (Role) session.get(Role.class, 2L);
        dao = new HibernateUserDao(session);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        sessionFactory.close();
    }

    public void testInitialDBContentShouldMatchInitialXMLContent()
        throws Exception {
        ITable expected = getTableFromXML(DATASET_XML_FILE);
        ITable actual = getTableFromDataBase();
        Assertion.assertEquals(expected, actual);
    }

    public void testCreateShouldCreateNewUser() throws Exception {
        ITable expected = getTableFromXML("dbunit/expected_users_new_user.xml");
        User user = new User();
        user.setLogin("sam");
        user.setPassword("pass");
        user.setEmail("e@mail");
        user.setFirstName("Sam");
        user.setLastName("Winchester");
        user.setBirthday(Date.valueOf("1970-11-11"));
        user.setRole(userRole);
        dao.create(user);
        ITable actual = getTableFromDataBase();

        Assertion
            .assertEqualsIgnoreCols(expected, actual, new String[] { "id" });
    }

    public void testCreateNullShouldThrowIAE() throws SQLException {
        try {
            dao.create(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("attempt to create event with null entity",
                e.getMessage());
        }
    }

    public void testShouldUpdateCorrectly() throws Exception {
        User user = dao.findByLogin("adm").get();
        ITable expected = getTableFromXML(
            "dbunit/expected_users_update_user.xml");
        user.setLastName("White");
        dao.update(user);
        ITable actual = getTableFromDataBase();
        Assertion.assertEquals(expected, actual);
    }

    public void testUpdateNullShouldThrowIAE() throws SQLException {
        try {
            dao.update(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("attempt to create event with null entity",
                e.getMessage());
        }
    }

    public void testShouldRemoveCorrectly() throws Exception {
        ITable expected = getTableFromXML(
            "dbunit/expected_users_remove_user.xml");
        User user = dao.findByLogin("adm").get();
        dao.remove(user);
        ITable actual = getTableFromDataBase();
        Assertion.assertEquals(expected, actual);
    }

    public void testRemoveNullShouldThrowIAE() throws SQLException {
        try {
            dao.remove(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("attempt to create delete event with null entity",
                e.getMessage());
        }
    }

    public void testFindAllShouldReturnTheSameNumberOfRecords()
        throws Exception {
        ITable recordsFromDB = getTableFromDataBase();
        List<User> userList = dao.findAll();
        assertEquals(recordsFromDB.getRowCount(), userList.size());
    }

    public void testFindAllShouldReturnValidRecords() throws Exception {
        ITable recordsFromDB = getTableFromDataBase();
        List<User> userList = dao.findAll();
        assertEquals(recordsFromDB.getValue(0, "email"),
            userList.get(0).getEmail());
    }

    public void testFindByLoginShouldReturnValidRecord() throws Exception {
        User user = dao.findByLogin("usr").get();
        ITable dbTable = getTableFromDataBase();
        String userLogin = dbTable.getValue(0, "login").toString();
        String userPassword = dbTable.getValue(0, "password").toString();

        assertEquals(userLogin, user.getLogin());
        assertEquals(userPassword, user.getPassword());
    }

    public void testFindByLoginShouldReturnNullIfNotFound() {
        Optional<User> user = dao.findByLogin("invalidLogin");
        assertFalse(user.isPresent());
    }

    public void testFindByLoginIfArgumentNullShouldBeEmpty() {
        Optional<User> optionalUser = dao.findByLogin(null);
        assertTrue(optionalUser.isEmpty());
    }

    public void testFindByEmailShouldReturnValidRecord() throws Exception {
        User user = dao.findByEmail("u@ser.com").get();
        ITable dbTable = getTableFromDataBase();
        String userLogin = dbTable.getValue(0, "login").toString();
        String userPassword = dbTable.getValue(0, "password").toString();

        assertEquals(userLogin, user.getLogin());
        assertEquals(userPassword, user.getPassword());
    }

    public void testFindByEmailShouldReturnNullIfNotFound() {
        Optional<User> user = dao.findByEmail("invalidLogin");
        assertFalse(user.isPresent());
    }

    public void testFindByEmailIfArgumentNullShouldReturnEmpty() {
        Optional<User> optionalUser = dao.findByEmail(null);
        assertTrue(optionalUser.isEmpty());
    }

    @Override
    String getTableName() {
        return USERS_TABLE_NAME;
    }
}
