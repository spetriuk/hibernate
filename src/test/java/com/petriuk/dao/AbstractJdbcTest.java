package com.petriuk.dao;

import com.petriuk.entity.Role;
import com.petriuk.entity.User;
import org.dbunit.DataSourceBasedDBTestCase;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.h2.jdbcx.JdbcDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class AbstractJdbcTest extends DataSourceBasedDBTestCase {
    private static final String PROPERTIES_FILE = "config.properties";
    public static final String DATASET_XML_FILE = "dbunit/data.xml";
    protected static SessionFactory sessionFactory;

    @Override
    protected DataSource getDataSource() {
        Properties props = new Properties();
        try {
            props.load(AbstractJdbcTest.class.getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(props.getProperty("db.url"));
        dataSource.setUser(props.getProperty("db.username"));
        dataSource.setPassword(props.getProperty("db.password"));
        return dataSource;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Properties properties = new Properties();
        properties.put(Environment.DRIVER, "org.h2.Driver");
        properties.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
        properties.put(Environment.DATASOURCE, getDataSource());

        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Role.class);
        configuration.addAnnotatedClass(User.class);
        configuration.setProperties(properties);
        configuration.configure();

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
            .applySettings(configuration.getProperties())
            .build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(
            getClass().getClassLoader().getResourceAsStream(DATASET_XML_FILE));
    }

    @Override
    protected void setUpDatabaseConfig(DatabaseConfig config) {
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
            new H2DataTypeFactory());
    }

    protected ITable getTableFromXML(String path) throws Exception {
        try (InputStream is = getClass().getClassLoader()
            .getResourceAsStream(path)) {
            IDataSet dataSet = new FlatXmlDataSetBuilder().build(is);
            return dataSet.getTable(getTableName());
        }
    }

    protected ITable getTableFromDataBase() throws Exception {
        IDataSet dataSet = getConnection().createDataSet();
        return dataSet.getTable(getTableName());
    }

    abstract String getTableName();
}
