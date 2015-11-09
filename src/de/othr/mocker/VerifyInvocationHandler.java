package de.othr.mocker;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * An implementation of InvocationHandler which tests whether the given method
 * was called the desired count. 
 * 
 * @author Michael Neu
 */
class VerifyInvocationHandler extends MockInvocationHandler {
	private final RepeatCount repeatCount;
	private final HashMap<String, Integer> callMap;
	
	/**
	 * Initialize the verifier
	 * 
	 * @param callMap A map counting how often which method was called
	 * @param repeatCount How often the called method should have been called
	 */
	public VerifyInvocationHandler(HashMap<String, Integer> callMap, RepeatCount repeatCount) {
		this.callMap = callMap;
		this.repeatCount = repeatCount;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// get a return value to not break anything later
		Object defaultReturnValue = super.invoke(proxy, method, args);
		
		// get how often the method was called already
		methodCallCount = callMap;
		String representation = getMethodStringRepresentation(method, args);
		
		int count = 0;
		
		if (methodCallCount.containsKey(representation)) {
			count = methodCallCount.get(representation);
		}
		
		if (!repeatCount.matchesCount(count)) {
			throw new AssertionError(
					String.format(
						"Verification failure: Expected number of calls %d but was %d",
						repeatCount.getCount(),
						count
					)
				);
		}
		
		return defaultReturnValue;
	}
}
