package test;

import com.oneandone.iocunit.analyzer.annotations.ExcludedClasses;
import com.oneandone.iocunit.IocUnitRunner;
import org.junit.runner.RunWith;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

/**
 * @author aschoerk
 */
@RunWith(IocUnitRunner.class)
@ExcludedClasses(AbstractExcludeTest.ToInclude.class)
public abstract class AbstractExcludeTest {

    static class ToInclude {
        public static int count;

        @PostConstruct
        public void postConstruct() {
            count++;
        }
    }

    @Inject
    ToInclude toInclude;
    @Produces
    ToInclude tmp = new ToInclude(); // no produces clash with excluded ToExclude

}
