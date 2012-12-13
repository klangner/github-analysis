package com.matrobot.gha;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ParamParserTest {

	@Test
	public void testCommand() {
		String[] args = {"my_command", "-data=path"};
		ParamParser parser = new ParamParser(args);
		
		assertEquals("my_command", parser.getCommand());
	}


	@Test
	public void testData() {
		String[] args = {"my_command", "-data=path"};
		ParamParser parser = new ParamParser(args);
		
		assertEquals("path", parser.getDataPath());
	}


	@Test
	public void testMonths() {
		String[] args = {"my_command", "-data=path", "-from=2011-10", "-to=2012-2"};
		ParamParser parser = new ParamParser(args);
		
		assertEquals("2011-10", parser.getStartDate());
		assertEquals("2012-2", parser.getEndDate());
	}


	@Test
	public void testRepo() {
		String[] args = {"my_command", "-data=path", "-repo=ala/ola"};
		ParamParser parser = new ParamParser(args);
		
		assertEquals("ala/ola", parser.getRepositoryName());
	}


	@Test
	public void testAllParams() {
		String[] args = {"my_command", "-data=path", "-from=2011-10", "-to=2012-2", "-repo=ala/ola"};
		ParamParser parser = new ParamParser(args);
		
		assertTrue(parser.hasAllParams());
	}

}
