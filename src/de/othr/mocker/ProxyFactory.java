package de.othr.mocker;

import java.lang.reflect.*;

/**
 * A simple factory to create {@link java.lang.reflect.Proxy} objects. 
 * 
 * @author Michael Neu
 */
@SuppressWarnings("unchecked")
final class ProxyFactory {
	/**
	 * Create a {@link java.lang.reflect.Proxy} with a given
	 * {@link java.lang.reflect.InvocationHandler}. 
	 * 
	 * @param object Which object to proxy
	 * @param handler Which handler to use
	 * @return
	 */
	public static <T> T getProxy(T object, InvocationHandler handler) {
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
	public static <T> T getProxy(Class<T> clazz, InvocationHandler handler) {
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
	private static <T> T createProxy(Class<T> clazz, InvocationHandler handler) {
		T proxy = (T)Proxy.newProxyInstance(
				clazz.getClassLoader(), 
				clazz.getInterfaces(), 
				handler
			);
			
		return proxy;
	}
}
