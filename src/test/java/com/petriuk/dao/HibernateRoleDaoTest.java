package com.petriuk.dao;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.petriuk.entity.Role;
import org.dbunit.Assertion;
import org.dbunit.dataset.ITable;
import org.hibernate.Session;

import javax.persistence.PersistenceException;
import java.util.Optional;

public class HibernateRoleDaoTest extends AbstractJdbcTest {
    private static final String ROLES_TABLE_NAME = "user_roles";
    private RoleDao dao;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        Session session = sessionFactory.openSession();
        dao = new HibernateRoleDao(session);
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

    public void testCreateNewRoleShouldCreateNewRecordInDB() throws Exception {
        ITable expected = getTableFromXML("dbunit/expected_roles_new_role.xml");
        dao.create(new Role("new_role"));
        ITable actual = getTableFromDataBase();
        Assertion.assertEquals(expected, actual);
    }

    public void testCreateNewRoleShouldThrowPersistenceExceptionIfRoleExists() {
        try {
            dao.create(new Role("user"));
            fail();
        } catch (PersistenceException e) {
            assertTrue(e.getCause().toString()
                .contains("ConstraintViolationException"));
        }
    }

    public void testAddNullShouldThrowIAE() {
        try {
            dao.create(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("attempt to create event with null entity",
                e.getMessage());
        }
    }

    public void testUpdateShouldUpdateCorrectly() throws Exception {
        ITable expected = getTableFromXML(
            "dbunit/expected_roles_update_role.xml");
        Role adminRole = dao.findByName("admin").get();
        adminRole.setName("superadmin");
        dao.update(adminRole);
        ITable actual = getTableFromDataBase();
        Assertion.assertEquals(expected, actual);
    }

    public void testUpdateShouldThrowIAEIfNullArgument() {
        try {
            dao.update(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("attempt to create event with null entity",
                e.getMessage());
        }
    }

    public void testRemoveShouldDeleteCorrectly() throws Exception {
        ITable expected = getTableFromXML(
            "dbunit/expected_roles_remove_role.xml");
        Role adminRole = dao.findByName("admin").get();
        dao.remove(adminRole);
        ITable actual = getTableFromDataBase();
        Assertion.assertEquals(expected, actual);
    }

    public void testRemoveShouldThrowIAEIfNullArgument() {
        try {
            dao.remove(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("attempt to create delete event with null entity",
                e.getMessage());
        }
    }

    public void testFindByNameShouldReturnValidRole() throws Exception {
        ITable dbContent = getTableFromDataBase();
        String adminNameFromDB = dbContent.getValue(1, "name").toString();
        Role adminRole = dao.findByName("admin").get();
        assertEquals(adminNameFromDB, adminRole.getName());
    }

    public void testFindByNameShouldReturnEmptyIfNameNull() {
        Optional<Role> optionalRole = dao.findByName(null);
        assertTrue(optionalRole.isEmpty());
    }

    public void testFindByNameShouldReturnNullIfNotFound() {
        Optional<Role> roleNotExisting = dao.findByName("role_not_exist");
        assertFalse(roleNotExisting.isPresent());
    }

    @Override
    String getTableName() {
        return ROLES_TABLE_NAME;
    }
}

