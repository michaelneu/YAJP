package de.othr.mocker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;

/**
 * An implementation of InvocationHandler which passes all methods to the 
 * object it spies on, but keeps track of what was called already. 
 * 
 * @author Michael Neu
 */
class SpyMethodInterceptor extends MockMethodInterceptor {
	private final Object target;
	
	/**
	 * Initialize the spy
	 * 
	 * @param object Which object to spy on
	 */
	public SpyMethodInterceptor(Object object) {
		target = object;
	}
	
	@Override
	public Object intercept(Object object, Method method, Object[] args, MethodProxy proxy) 
			throws Throwable {
		Object defaultReturnValue = super.intercept(object, method, args, proxy);
		
		try {
			defaultReturnValue = method.invoke(target, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// don't throw any exception as we'll just return the default value then 
		}
		
		return defaultReturnValue;
	}
}
