package com.petriuk.web;

import com.petriuk.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppServletContextListener implements ServletContextListener {
    private static final Logger LOG = LogManager.getLogger();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOG.debug("Context initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateUtil.closeSessionFactory();

        LOG.debug("SessionFactory closed");
        LOG.debug("Context destroyed");
    }
}
