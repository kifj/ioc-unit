package com.oneandone.iocunit.ejb.persistence;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.eclipse.persistence.internal.jpa.deployment.SEPersistenceUnitInfo;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oneandone.iocunit.analyzer.annotations.TestClasses;
import com.oneandone.iocunit.ejb.EjbExtensionExtended;
import com.oneandone.iocunit.ejb.SessionContextFactory;

/**
 * This Persistencefactory should allow to create tests with in an h2 database very fast.
 * No persistence.xml for the testconfig is necessary. Default persistenceunitname is test. If the persistenceunit "test"
 * is not found in persistence.xml some defaults are used: Db-Driver: H2, username SA, password empty. "drop all objects"
 * before first connection.
 * To let it handle a defaultschema: subclass and override getSchema but: if subclassing all Producers produceEntityManager()
 * and produceDataSource() must be overridden as well.
 * If Entity-Beans are found by the EjbExtension, they are automatically added to the persistence-context.
 * This can be controlled by getEntityBeanRegex();
 * If PersistenceProvider is Hibernate and hibernate.default_schema is set --> that Schema is created in H2 at start.
 * Uses also eclipseLink if only that PersistenceProvider is found.
 *
 * @author aschoerk
 */
@ApplicationScoped
@TestClasses({ SessionContextFactory.class })
public class TestPersistenceFactory extends PersistenceFactory {

    public static Set<String> notFoundPersistenceUnits = new HashSet<>();
    static Logger logger = LoggerFactory.getLogger("TestPersistenceFactory");
    @Inject
    private EjbExtensionExtended ejbExtensionExtended;

    @Override
    protected String getPersistenceUnitName() {
        if (getFilenamePrefix() == null)
            return "test";
        else
            return getFilenamePrefix();
    }

    /**
     * Schema to be created when starting this PersistenceFactory
     * @return Name of the schema to be created
     */
    protected String getSchema()  {
        String hibernateSchema = System.getProperty("hibernate.default_schema");
        return hibernateSchema;
    }

    /**
     * returns EntityManager, to be injected and used so that the current threadSpecific context is correctly handled
     *
     * @return the EntityManager as it is returnable by producers.
     */
    @Produces
    @Default
    @Override
    public EntityManager produceEntityManager() {
        return super.produceEntityManager();
    }

    public String getEntityBeanRegex() {
        return null;
    }
    /**
     * create a jdbc-Datasource using the same driver url user and password as the entityManager
     *
     * @return a jdbc-Datasource using the same driver url user and password as the entityManager
     */
    @Produces
    @Default
    @Override
    public DataSource produceDataSource() {
        return super.produceDataSource();
    }

    boolean justConstructed = true;
    List<String> initStatements = new ArrayList<>();

    public EjbExtensionExtended getEjbExtensionExtended() {
        return ejbExtensionExtended;
    }

    protected PersistenceUnitInfo getHibernatePersistenceUnitInfo(final HashMap<String, Object> properties) {

        return new PersistenceUnitInfo() {
            @Override
            public String getPersistenceUnitName() {
                return "TestPersistenceUnit";
            }

            @Override
            public String getPersistenceProviderClassName() {
                return "org.hibernate.ejb.HibernatePersistence";
            }

            @Override
            public PersistenceUnitTransactionType getTransactionType() {
                return PersistenceUnitTransactionType.RESOURCE_LOCAL;
            }

            @Override
            public DataSource getJtaDataSource() {
                return null;
            }



            @Override
            public DataSource getNonJtaDataSource() {
                BasicDataSource bds = new BasicDataSource();
                bds.setDriverClassName(getProperty(properties,"javax.persistence.jdbc.driver", "hibernate.connection.driverclass", "org.h2.Driver"));
                bds.setUrl(getProperty(properties,"javax.persistence.jdbc.url","hibernate.connection.url","jdbc:h2:mem:test;MODE=MySQL;DB_CLOSE_ON_EXIT=TRUE;DB_CLOSE_DELAY=0;LOCK_MODE=0;LOCK_TIMEOUT=10000"));
                bds.setUsername(getProperty(properties,"javax.persistence.jdbc.user", "hibernate.connection.username", "sa"));
                bds.setPassword(getProperty(properties,"javax.persistence.jdbc.password", "hibernate.connection.password", ""));
                handleJustConstructed(bds);
                return bds;
            }

            @Override
            public List<String> getMappingFileNames() {
                return Collections.emptyList();
            }

            @Override
            public List<URL> getJarFileUrls() {
                return TestPersistenceFactory.this.getJarFileUrls();
            }

            @Override
            public URL getPersistenceUnitRootUrl() {
                return TestPersistenceFactory.this.getPersistenceUnitRootUrl("META-INF/persistence.xml");
            }

            @Override
            public List<String> getManagedClassNames() {
                return TestPersistenceFactory.this.getManagedClassNames();
            }

            @Override
            public boolean excludeUnlistedClasses() {
                return false;
            }

            @Override
            public SharedCacheMode getSharedCacheMode() {
                return null;
            }

            @Override
            public ValidationMode getValidationMode() {
                return null;
            }

            @Override
            public Properties getProperties() {
                return new Properties();
            }

            @Override
            public String getPersistenceXMLSchemaVersion() {
                return null;
            }

            @Override
            public ClassLoader getClassLoader() {
                return Thread.currentThread().getContextClassLoader();
            }

            @Override
            public void addTransformer(ClassTransformer transformer) {

            }

            @Override
            public ClassLoader getNewTempClassLoader() {
                return this.getClassLoader();
            }
        };
    }


    /**
     * should work without needing a persistence.xml create it using
     *
     * @return
     */
    @Override
    protected EntityManagerFactory createEntityManagerFactory() {
        Throwable possiblyToThrow = null;
        if(!notFoundPersistenceUnits.contains(getPersistenceUnitName())) {
            try {
                EntityManagerFactory result = super.createEntityManagerFactory();
                if (result != null)
                    return result;
                notFoundPersistenceUnits.add(getPersistenceUnitName());
            } catch (Throwable e) {
                possiblyToThrow = e;
                notFoundPersistenceUnits.add(getPersistenceUnitName());
            }
        }

        EntityManagerFactory res = createEntityManagerFactoryWOPersistenceXml();
        return res;
    }

    private EntityManagerFactory createEntityManagerFactoryWOPersistenceXml() {
        PersistenceProvider persistenceProvider = getPersistenceProvider();
        HashMap<String, Object> propertiesL = new HashMap<>();
        EntityManagerFactory result;
        initStatements.add("drop all objects");
        if (getSchema() != null)
            initStatements.add("create schema " + getSchema());
        if(getRecommendedProvider().equals(Provider.HIBERNATE)) {
            initHibernateProperties(propertiesL);
            // possibly override properties using system properties
            for (Map.Entry<Object, Object> p : System.getProperties().entrySet()) {
                propertiesL.put((String) p.getKey(), p.getValue());
            }
            final PersistenceUnitInfo persistenceUnitInfo = getHibernatePersistenceUnitInfo(propertiesL);
            try {
                result = new EntityManagerFactoryBuilderImpl(new PersistenceUnitInfoDescriptor(persistenceUnitInfo), propertiesL).build();
            } catch (Throwable thw) {
                throw new RuntimeException(thw);
            }

        } else {
            initEclipseLinkProperties(propertiesL);
            for (Map.Entry<Object, Object> p : System.getProperties().entrySet()) {
                propertiesL.put((String) p.getKey(), p.getValue());
            }
            propertiesL.put("eclipselink.se-puinfo", new SEPersistenceUnitInfo() {
                @Override
                public String getPersistenceUnitName() {
                    return "TestPersistenceUnit";
                }
                @Override
                public DataSource getJtaDataSource() {
                    return null;
                }

                /**
                 * take the first URL found, which supports Entities
                 * @return the first URL found, depending on EntityClasses
                 */
                @Override
                public URL getPersistenceUnitRootUrl() {
                    return TestPersistenceFactory.this.getPersistenceUnitRootUrl(".");
                }

                /**
                 * @return
                 */
                @Override
                public List<URL> getJarFileUrls() {
                    return TestPersistenceFactory.this.getJarFileUrls();
                }

                @Override
                public List<String> getManagedClassNames() {
                    return TestPersistenceFactory.this.getManagedClassNames();
                }

                @Override
                public DataSource getNonJtaDataSource() {
                    DataSource ds = this.nonJtaDataSource;
                    if (ds == null) {
                        BasicDataSource bds = new BasicDataSource() {
                            @Override
                            public Connection getConnection(final String user, final String pass) throws SQLException {
                                return super.getConnection();
                            }
                        };
                        bds.setDriverClassName(getProperty(propertiesL,"javax.persistence.jdbc.driver", "hibernate.connection.driverclass", "org.h2.Driver"));
                        bds.setUrl(getProperty(propertiesL,"javax.persistence.jdbc.url","hibernate.connection.url","jdbc:h2:mem:test;MODE=MySQL;DB_CLOSE_ON_EXIT=TRUE;DB_CLOSE_DELAY=0;LOCK_MODE=0;LOCK_TIMEOUT=10000"));
                        bds.setUsername(getProperty(propertiesL,"javax.persistence.jdbc.user", "hibernate.connection.username", "sa"));
                        bds.setPassword(getProperty(propertiesL,"javax.persistence.jdbc.password", "hibernate.connection.password", ""));
                        handleJustConstructed(bds);
                        return bds;
                    }
                    handleJustConstructed(ds);
                    return ds;
                }

                @Override
                public boolean excludeUnlistedClasses() {
                    return false;
                }
                @Override
                public ClassLoader getClassLoader() {
                    return Thread.currentThread().getContextClassLoader();
                }

            });
            result = persistenceProvider.createEntityManagerFactory(getPersistenceUnitName(), propertiesL);
        }

        return result;
    }

    AtomicInteger count = new AtomicInteger(0);

    private void initEclipseLinkProperties(final HashMap<String, Object> properties) {
        properties.put("javax.persistence.jdbc.driver","org.h2.Driver");
        String db = getDbNameOrMem();
        properties.put("javax.persistence.jdbc.url",
                "jdbc:h2:" + db + ":test;DB_CLOSE_ON_EXIT=TRUE;DB_CLOSE_DELAY=0;LOCK_MODE=0;LOCK_TIMEOUT=10000");
        properties.put("javax.persistence.jdbc.user" , "sa");
        properties.put("javax.persistence.jdbc.password", "");
        properties.put("eclipselink.disableXmlSecurity","true");
        properties.put("eclipselink.ddl-generation", "drop-and-create-tables");
        properties.put("eclipselink.target-database", "MYSQL");
        System.clearProperty("hibernate.default_schema");
    }
    private String getProperty(final HashMap<String, Object> properties, String name, String hibernateName, String defaultValue) {
        Object propValue = properties.get(name);
        if (propValue != null) return propValue.toString();
        propValue = System.getProperty(name);
        if (propValue != null)
            return propValue.toString();
        propValue = properties.get(hibernateName);
        if (propValue != null) return propValue.toString();
        propValue = System.getProperty(hibernateName);
        if (propValue != null)
            return propValue.toString();

        return defaultValue;
    }

    private String getDbNameOrMem() {
        return getFilenamePrefix() == null ? "mem" : "file:" + System.getProperty("java.io.tmpdir")
                                                     + File.separatorChar
                                                     + getFilenamePrefix()
                                                    + count.incrementAndGet();
    }

    protected String getFilenamePrefix() {
        return null;
    }

    private void initHibernateProperties(final HashMap<String, Object> properties) {
        if (System.getProperty("hibernate.connection.driverclass") == null)
            properties.put("javax.persistence.jdbc.driver","org.h2.Driver");
        if (System.getProperty("hibernate.connection.url") == null) {
            String db = getDbNameOrMem();
            properties.put("javax.persistence.jdbc.url",
                    "jdbc:h2:" + db + ":test;DB_CLOSE_ON_EXIT=TRUE;DB_CLOSE_DELAY=0;LOCK_MODE=0;LOCK_TIMEOUT=10000");
        }
        if (System.getProperty("hibernate.connection.username") == null)
            properties.put("javax.persistence.jdbc.user" , "sa");
        if (System.getProperty("hibernate.connection.password") == null)
            properties.put("javax.persistence.jdbc.password", "");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        properties.put("hibernate.show_sql", true);
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.id.new_generator_mappings", false);
        properties.put("hibernate.archive.autodetection", "class");
    }

    /**
     * Called before the creation if the first connection.
     * Use super.createDataSource() if you want to do further inits
     */
    protected void handleJustConstructed(DataSource ds ) {
        if (ds != null && justConstructed) {
            justConstructed = false;
            if(initStatements.size() > 0) {
                try (Connection conn = ds.getConnection()) {
                    try (Statement stmt = conn.createStatement()) {
                        for (String s : initStatements) {
                            stmt.execute(s);
                        }
                        stmt.execute("commit");
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    private List<String> getManagedClassNames() {
        final String entityBeanRegex = getEntityBeanRegex();
        List<String> result = new ArrayList<>();
        for (Class<?> c : getEjbExtensionExtended().getEntityClasses()) {
            if (entityBeanRegex == null || Pattern.matches(entityBeanRegex, c.getName()))
                result.add(c.getName());
        }
        logger.info("PUName: {} getManagedClassNames: {}", TestPersistenceFactory.this.getPersistenceUnitName(), result);
        return result;
    }
    private List<URL> getJarFileUrls() {
        if (getEntityBeanRegex() == null) {
            try {
                final ArrayList<URL> jarFiles = Collections.list(this.getClass()
                        .getClassLoader()
                        .getResources(""));
                logger.info("PUName: {} getJarFileUrls: {}", TestPersistenceFactory.this.getPersistenceUnitName(), jarFiles);
                return jarFiles;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return Collections.emptyList();
        }
    }

    public URL getPersistenceUnitRootUrl(String resourcePath) {
        try {
            final Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(resourcePath);
            if (resources.hasMoreElements())
                return resources.nextElement();
            else
                return null;
        } catch (IOException e) {
            return null;
        }
    }

}
