package com.fckawe.engine;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Runs all FckaweEngine tests.
 * 
 * @author fckawe
 */
@RunWith(Suite.class)
@SuiteClasses({ com.fckawe.engine.core.ConfigurationTest.class,
		com.fckawe.engine.utils.BreadcrumbTest.class,
		com.fckawe.engine.utils.NLSTest.class })
public class FckaweEngineTestSuite {

}
