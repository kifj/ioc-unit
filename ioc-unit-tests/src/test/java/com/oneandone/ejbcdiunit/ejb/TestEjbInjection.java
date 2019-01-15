package com.oneandone.ejbcdiunit.ejb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.lang.reflect.Field;

import javax.ejb.EJB;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.oneandone.iocunit.analyzer.annotations.TestClasses;
import com.oneandone.iocunit.IocUnitRunner;
import com.oneandone.iocunit.ejb.persistence.TestPersistenceFactory;
import com.oneandone.ejbcdiunit.ejbs.StatelessEJB;
import com.oneandone.ejbcdiunit.helpers.LoggerGenerator;

/**
 * @author aschoerk
 */
@RunWith(IocUnitRunner.class)
@TestClasses({ StatelessEJB.class, LoggerGenerator.class, TestPersistenceFactory.class })
public class TestEjbInjection {

    @EJB(name = "StatelessEJB")
    protected StatelessEJB statelessEJB;

    @Test
    public void testEjbName() {
        boolean fieldFound = false;
        for (Field f : this.getClass().getDeclaredFields()) {
            if (f.getName().equals("statelessEJB")) {
                fieldFound = true;
                EJB ann = f.getAnnotation(EJB.class);
                assertThat(ann, notNullValue());
                assertThat(ann.name(), is("StatelessEJB"));
            }
        }
        Assert.assertTrue(fieldFound);
    }


}
