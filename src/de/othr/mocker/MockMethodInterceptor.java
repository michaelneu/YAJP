package de.othr.mocker;

import java.lang.reflect.*;
import java.util.HashMap;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * An implementation of InvocationHandler which acts as a method call sink. 
 * 
 * @author Michael Neu
 */
class MockMethodInterceptor implements MethodInterceptor {
	protected HashMap<String, Integer> methodCallCount;
	private RepeatCount repeatCount;
	
	/**
	 * Initialize the invocation handler
	 */
	public MockMethodInterceptor() {
		methodCallCount = new HashMap<>();
	}
	
	/**
	 * Convert a method to a String representation. 
	 * 
	 * @param method Which method was called
	 * @param args Wich arguments were passed to the method
	 * @return The string representation of this call
	 */
	protected String getMethodStringRepresentation(Method method, Object[] args) {
		String parameterString = "";
		
		if (args != null) {
			String[] parameters = new String[args.length];
			
			for (int i = 0; i < args.length; i++) {
				parameters[i] = args[i].hashCode() + "";
			}
			
			parameterString = String.join(", ", parameters);
		}
		
		String representation = String.format(
				"%s(%s)", 
				method.getName(), 
				parameterString
			);
		
		return getSimilarStringRepresentation(representation);
	}
	
	/**
	 * Get the key of the `methodCallCount` hashmap to prevent double insertion. 
	 * 
	 * @param representation A string representation of the called method
	 * @return Either the similar key of the hashmap or the original string
	 */
	private String getSimilarStringRepresentation(String representation) {
		for (String key : methodCallCount.keySet()) {
			if (key.equals(representation)) {
				return key;
			}
		}
		
		return representation;
	}
	
	/**
	 * Increment the count of how often the given method was called already. 
	 * 
	 * @param method Which method was called
	 * @param args Which arguments were passed
	 */
	private void incrementCallCount(Method method, Object[] args) {
		String representation = getMethodStringRepresentation(method, args);
		
		// add key to the map if necessary
		if (!methodCallCount.containsKey(representation)) {
			methodCallCount.put(representation, 0);
		}
		
		// actually increment the value
		methodCallCount.put(
			representation,
			methodCallCount.get(representation) + 1
		);
	}
	
	public void setVerificationFlag(RepeatCount count) {
		repeatCount = count;
	}

	@Override
	public Object intercept(Object object, Method method, Object[] args, MethodProxy proxy) 
			throws Throwable {
		if (repeatCount != null) {
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
			
			// reset the "verification mode flag"
			repeatCount = null;
		} else {
			// no flag set --> add the method to the call counter
			incrementCallCount(method, args);
		}

		// return default values based on the method's return type
		String type = method.getGenericReturnType()
		                    .getTypeName();
		
		switch (type) {
			case "int":
			case "long": 
			case "double": 
			case "float": 
				return 0;
				
			case "boolean": 
				return false;
				
			default: 
				return null;
		}
	}
}
