package de.othr.test;

import java.util.*;
import org.junit.Test;
import junit.framework.TestCase;
import static de.othr.mocker.Mocker.*;

@SuppressWarnings("unchecked")
public class MockerTest extends TestCase {
	@Test
	public void testMockMethodCallSink() {
		List<Character> mockedObject = mock(ArrayList.class);
		
		for (char letter = 'a'; letter <= 'z'; letter++) {
			mockedObject.add(letter);
		}
		
		assertEquals("List.size()", 0, mockedObject.size());
	}
	
	@Test
	public void testMockSpying() {
		List<Character> list = new ArrayList<>(),
				spyList = spy(list);
		
		for (char letter = 'a'; letter <= 'z'; letter++) {
			spyList.add(letter);
		}

		assertEquals(26, list.size());
		assertEquals(list.size(), spyList.size());
		
		verify(spyList).add('a');
	}

	@Test
	public void testMockMethodCallCounting() {
		List<Character> mockedObject = mock(ArrayList.class);

		mockedObject.add('a');
		mockedObject.clear();
		mockedObject.clear();
		mockedObject.clear();

		verify(mockedObject).add('a');
		verify(mockedObject, never()).add('b');
		verify(mockedObject, atLeast(2)).clear();
		verify(mockedObject, atMost(3)).clear();
	}
}
