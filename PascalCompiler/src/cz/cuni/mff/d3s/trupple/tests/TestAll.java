package cz.cuni.mff.d3s.trupple.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ WriteTest.class, ArithmeticTest.class, IfTest.class, CaseTest.class, LoopsTest.class,
		SubroutinesTest.class, LogicOperatorsTest.class, EnumTest.class, LogicNegationTest.class, 
		ArrayTest.class, NestedSubroutinesTest.class, GlobalVariablesTest.class, ReadTest.class,
		ConstantsTest.class})
public class TestAll {

}
