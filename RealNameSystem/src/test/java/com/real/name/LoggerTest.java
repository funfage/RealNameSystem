package com.real.name;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerTest extends BaseTest {

    private Logger logger = LoggerFactory.getLogger(LoggerTest.class);

    @Test
    public void logTest() {
        logger.info("info测试");
        logger.warn("warn测试");
        logger.error("error测试");
    }

}
