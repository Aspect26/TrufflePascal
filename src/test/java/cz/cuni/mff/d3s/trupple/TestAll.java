package cz.cuni.mff.d3s.trupple;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ WriteTest.class, ArithmeticTest.class, IfTest.class, CaseTest.class, LoopsTest.class,
		SubroutinesTest.class, LogicOperatorsTest.class, EnumTest.class, LogicNegationTest.class, 
		ArrayTest.class, NestedSubroutinesTest.class, GlobalVariablesTest.class, ReadTest.class,
		ConstantsTest.class, StringTest.class, UnitTest.class, TypeTest.class, ReferencePassingTest.class,
		SetTypeTest.class, SetOperationsTest.class, LongerProgramsTest.class, RecordTest.class, BuiltinTest.class,
        PointersTest.class, FilesTest.class, StringsUnitTest.class, GotoTest.class, ProgramArgumentsTest.class,
        SubroutineAsArgumentTest.class, VariableAccessTest.class })
public class TestAll {

}
