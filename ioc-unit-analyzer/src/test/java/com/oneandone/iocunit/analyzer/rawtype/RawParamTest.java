package com.oneandone.iocunit.analyzer.rawtype;


import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.oneandone.iocunit.analyzer.BaseTest;

/**
 * @author aschoerk
 */
public class RawParamTest extends BaseTest {

    void testAndCheckProducer(Class<?> testClass, Class<?> producerClass) {
        createTest(testClass);
        assertTrue(toBeStarted.size() == 2);
        assertTrue(toBeStarted.contains(producerClass));
        assertTrue(toBeStarted.contains(testClass));

    }

    @Test
    public void analyzeRawListContainerRawListSubExcluded() {
        testAndCheckProducer(RawListContainerRawListSubExcluded.class, RawProducer.class);
    }

    @Test
    public void analyzeRawListContainerRawProducerExcluded() {
        testAndCheckProducer(RawListContainerRawProducerExcluded.class, RawListSubProducer.class);
    }
    @Test
    public void analyzeParameterizedListContainerStringListExcluded() {
        testAndCheckProducer(ParameterizedListContainerStringListExcluded.class, ParameterizedProducer.class);
    }
    @Test
    public void analyzeStringListContainerNotItself() {
        testAndCheckProducer(StringListContainerNotItself.class, StringListProducer.class);
    }
    @Test
    public void analyzeStringListContainerNoProducer() {
        testAndCheckProducer(StringListContainerNoProducer.class, StringList.class);
    }
    @Test
    public void analyzeRawListSubContainerNotItself() {
        testAndCheckProducer(RawListSubContainerNotItself.class, RawListSubProducer.class);
    }
    @Test
    public void analyzeRawListSubContainerNoProducer() {
        testAndCheckProducer(RawListSubContainerNoProducer.class, RawListSub.class);
    }
}

