package siet.lcis;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({TestConditionBool.class,TestConditionInt.class,TestConditionString.class,TestComposedCondition.class})
public class AllTests {

}
