package de.othr.mocker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * An implementation of InvocationHandler which passes all methods to the 
 * object it spies on, but keeps track of what was called already. 
 * 
 * @author Michael Neu
 */
class SpyInvocationHandler extends MockInvocationHandler {
	private final Object target;
	
	/**
	 * Initialize the spy
	 * 
	 * @param object Which object to spy on
	 */
	public SpyInvocationHandler(Object object) {
		target = object;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object defaultReturnValue = super.invoke(proxy, method, args);
		
		try {
			defaultReturnValue = method.invoke(target, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// don't throw any exception as we'll just return the default value then 
		}
		
		return defaultReturnValue;
	}
}
