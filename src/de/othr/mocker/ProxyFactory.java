package de.othr.mocker;

import java.util.HashMap;

import net.sf.cglib.proxy.Enhancer;

/**
 * A simple factory to create {@link java.lang.reflect.Proxy} objects. 
 * 
 * @author Michael Neu
 */
@SuppressWarnings("unchecked")
final class ProxyFactory {
	private static final HashMap<Object, MockMethodInterceptor> interceptors = new HashMap<>();
	
	/**
	 * Create a {@link java.lang.reflect.Proxy} with a given
	 * {@link java.lang.reflect.InvocationHandler}. 
	 * 
	 * @param object Which object to proxy
	 * @param handler Which handler to use
	 * @return
	 */
	public static <T> T getProxy(T object, MockMethodInterceptor handler) {
		return (T)createProxy(object.getClass(), handler);
	}

	/**
	 * Create a {@link java.lang.reflect.Proxy} with a given
	 * {@link java.lang.reflect.InvocationHandler}. 
	 * 
	 * @param clazz Which class to proxy
	 * @param handler Which handler to use
	 * @return
	 */
	public static <T> T getProxy(Class<T> clazz, MockMethodInterceptor handler) {
		return (T)createProxy(clazz, handler);
	}

	/**
	 * Create a {@link java.lang.reflect.Proxy} with a given
	 * {@link java.lang.reflect.InvocationHandler}. 
	 * 
	 * @param clazz Which class to proxy
	 * @param handler Which handler to use
	 * @return
	 */
	private static <T> T createProxy(Class<T> clazz, MockMethodInterceptor handler) {
		Object object = Enhancer.create(clazz, handler);
		
		// store the MethodInterceptor in a map so we can access it later
		// to set the verification mode required by `Mocker.verify`
		if (!interceptors.containsKey(object)) {
			interceptors.put(object, handler);
		}
		
		return (T)object;
	}
	
	/**
	 * Set the verification mode on the given object. 
	 * 
	 * @param proxy The proxy which should be in verification mode
	 * @param count Which value should be verified
	 */
	public static void setVerificationMode(Object proxy, RepeatCount count) {
		if (interceptors.containsKey(proxy)) {
			MockMethodInterceptor handler = interceptors.get(proxy);
			handler.setVerificationFlag(count);
		} else {
			throw new AssertionError("Non proxy-object passed to Mocker.verify");
		}
	}
}
