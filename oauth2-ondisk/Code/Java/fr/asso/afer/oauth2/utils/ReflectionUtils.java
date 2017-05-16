package fr.asso.afer.oauth2.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import fr.asso.afer.oauth2.utils.JsonUtils.JsonName;

public class ReflectionUtils {

	/**
	 * Retourne la m�thode pour �crire dans une propri�t�
	 * @param cl la classe
	 * @param prop le nom de la propri�t�
	 * @return la m�thode pour �crire dans la propri�t�
	 * @throws IntrospectionException 
	 */
	public static final Method getWriteMethod(Class<?> cl, String prop) throws IntrospectionException {
		BeanInfo beanInfo = Introspector.getBeanInfo(cl);
		PropertyDescriptor[] descs = beanInfo.getPropertyDescriptors();
		for( PropertyDescriptor desc : descs ) {
			if( desc.getName().equals(prop) )
				return desc.getWriteMethod();
			
			Method read = desc.getReadMethod();
			if( read == null )
				continue;
			JsonName ann = read.getAnnotation(JsonName.class);
			if( ann != null && ann.value().equals(prop) )
				return desc.getWriteMethod();
		}
		return null;
	}
	
	/**
	 * Retourne le type d'une propri�t�
	 * @param cl la classe
	 * @param prop la propri�t�
	 * @return le type de la propri�t�
	 * @throws IntrospectionException 
	 */
	public static final Class<?> getPropType(Class<?> cl, String prop) throws IntrospectionException {
		BeanInfo beanInfo = Introspector.getBeanInfo(cl);
		PropertyDescriptor[] descs = beanInfo.getPropertyDescriptors();
		for( PropertyDescriptor desc : descs ) {
			if( desc.getName().equals(prop) )
				return desc.getPropertyType();
			
			Method read = desc.getReadMethod();
			if( read == null )
				continue;
			JsonName ann = read.getAnnotation(JsonName.class);
			if( ann != null && ann.value().equals(prop) )
				return desc.getPropertyType();
		}
		return null;
	}
}
