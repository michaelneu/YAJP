package de.othr.mocker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * A basic mocking library implementation. Supports basic mocking, JUnit 
 * method call verification and object spying. 
 * 
 * @author Michael Neu
 */
public final class Mocker {
	/**
	 * Get a mock of the class. The mock will sink all method calls and return 
	 * default values, `0` for every number-based method return type, `false`
	 * for every boolean return type. Any other return type will be discarded 
	 * thus returning `null`. 
	 * 
	 * @param clazz Which class to mock
	 * @return A mock
	 */
	public static <T> T mock(Class<T> clazz) {
		return ProxyFactory.getProxy(clazz, new MockInvocationHandler());
	}
	
	/**
	 * Get a spy for the object. The spy will act just like the original 
	 * object, but it also allows to perform `verify` tasks on it. 
	 * 
	 * @param object Which object to create a spy on
	 * @return A spy
	 */
	public static <T> T spy(T object) {
		return ProxyFactory.getProxy(object, new SpyInvocationHandler(object));
	}
	
	/**
	 * Verify that the next called method was called exactly 1 time. This 
	 * method is an overloaded version of `verify(T object, RepeatCount count)`.
	 * 
	 * @param mockedObject Which object's call history to verify. 
	 * @return A verification mock
	 */
	public static <T> T verify(T mockedObject) {
		return verify(mockedObject, times(1));
	}

	/**
	 * Verify that the next called method was called exactly n times. n is
	 * given by a {@link de.othr.mocker.RepeatCount} object which can be retrieved by calling
	 * `never()`, `times(i)`, `atLeast(i)` and `atMost(i)` with appropriate
	 * parameters. 
	 * 
	 * @param mockedObject Which object's call history to verify.
	 * @param count  
	 * @return A verification mock
	 */
	public static <T> T verify(T object, RepeatCount count) {
		if (object instanceof Proxy) {
			InvocationHandler handler = Proxy.getInvocationHandler(object);
			MockInvocationHandler mock = ((MockInvocationHandler)handler),
					verifier = mock.toVerifyInvocationHandler(count);
					
			return ProxyFactory.getProxy(object, verifier);
		} else {
			// in case we didn't get passed a Proxy-object we'll just hand 
			// throw an AssertionError because verify will not work at all
			
			throw new AssertionError("Non-mock-object passed to Mocker.verify");
		}
	}
	
	/**
	 * Get a {@link de.othr.mocker.RepeatCount} object to verify a method was
	 * never called. 
	 * 
	 * @return Information to check how often a method was called
	 */
	public static RepeatCount never() {
		return times(0);
	}

	/**
	 * Get a {@link de.othr.mocker.RepeatCount} object to verify a method was
	 * called exactly `i` times. 
	 * 
	 * @param i How often the method should have been called exactly
	 * @return Information to check how often a method was called
	 */
	public static RepeatCount times(int i) {
		return new RepeatCount(i);
	}

	/**
	 * Get a {@link de.othr.mocker.RepeatCount} object to verify a method was
	 * called at least `i` times. 
	 * 
	 * @param i How often the method should have been called at least
	 * @return Information to check how often a method was called
	 */
	public static RepeatCount atLeast(int i) {
		return new RepeatCount(i, RepeatCount.Operator.ATLEAST);
	}

	/**
	 * Get a {@link de.othr.mocker.RepeatCount} object to verify a method was
	 * called at most `i` times. 
	 * 
	 * @param i How often the method should have been called at most
	 * @return Information to check how often a method was called
	 */
	public static RepeatCount atMost(int i) {
		return new RepeatCount(i, RepeatCount.Operator.ATMOST);
	}
}
