package nl.amis.jsf.model;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.sql.Timestamp;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;


public class EntityLovModelUtil {
    
    public EntityLovModelUtil() {
        super();
    }

    public static boolean isFieldAccessible(Field field, Class entityClass) {
        if (Modifier.isPublic(field.getModifiers())) {
            return true;
        } else if (existsAccessorMethod("get", field.getName(), entityClass)) {
            return true;
        } else if (existsAccessorMethod("is", field.getName(), entityClass)) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean existsAccessorMethod(String prefix, String fieldName, Class entityClass) {
        return null != getAccessorMethod(prefix, fieldName, entityClass);
    }

    public static Method getAccessorMethod(String prefix, String fieldName, Class entityClass) {
        StringBuilder methodName = new StringBuilder();
        methodName.append(prefix);
        methodName.append(fieldName.substring(0, 1).toUpperCase());
        methodName.append(fieldName.substring(1));
        try {
            return entityClass.getDeclaredMethod(methodName.toString(), new Class[] { });
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static boolean isDate(Class clazz) {
        return Date.class.equals(clazz) || java.sql.Date.class.equals(clazz) || Timestamp.class.equals(clazz) || Calendar.class.equals(clazz);
    }

    public static boolean isNumber(Class clazz) {
        return clazz.isPrimitive() || Number.class.equals(clazz) || Short.class.equals(clazz) || Long.class.equals(clazz) || Integer.class.equals(clazz) ||
            Float.class.equals(clazz) || Double.class.equals(clazz) || Byte.class.equals(clazz) || BigInteger.class.equals(clazz) ||
            BigDecimal.class.equals(clazz) || AtomicLong.class.equals(clazz) || AtomicInteger.class.equals(clazz);
    }

    public static String logObject(Object object) {
        if (null == object) {
            return "null";
        }
        StringBuilder result = new StringBuilder();
        result.append(object.getClass().getName());
        result.append(":");
        result.append(object);
        return result.toString();
    }

    public static Object getKeyFromEntity(Method keyGetter, Object entity) {
        Object key = null;        
        if (null == keyGetter) {
            return null;
        }
        if (null == entity) {
            return null;
        }
        try {
            key = keyGetter.invoke(entity, null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return key;
    }
}
