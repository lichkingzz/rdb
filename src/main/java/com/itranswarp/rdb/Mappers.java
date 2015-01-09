package com.itranswarp.rdb;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class Mappers {

    static Map<String, BeanMapper> cache = new ConcurrentHashMap<String, BeanMapper>();

    static BeanMapper getMapper(Class<?> beanClass) {
        String key = beanClass.getName();
        BeanMapper mapper = cache.get(key);
        if (mapper == null) {
            mapper = new BeanMapper(beanClass);
            cache.put(key, mapper);
        }
        return mapper;
    }

}

class BeanMapper {

    final String table;
    final String primaryKey;
    final Class<?> beanClass;
    final Map<String, Property> map;

    Log log = LogFactory.getLog(getClass());

    static final Field[] EMPTY_FIELDS = new Field[0];
    static final Method[] EMPTY_METHODS = new Method[0];

    BeanMapper(Class<?> clazz) {
        this.table = getTableName(clazz);
        this.beanClass = clazz;
        log.info("Found bean: " + clazz.getName());
        Map<String, Property> map = new HashMap<String, Property>();

        Map<String, Method> getters = PropertyUtils.getAllGetters(clazz);
        Map<String, Method> setters = PropertyUtils.getAllSetters(clazz);
        Map<String, Field> fields = PropertyUtils.getAllFields(clazz);

        Set<String> possiblePropertyNames = new HashSet<String>();
        possiblePropertyNames.addAll(getters.keySet());
        possiblePropertyNames.addAll(setters.keySet());
        possiblePropertyNames.addAll(fields.keySet());

        String primaryKey = null;
        for (String propertyName : possiblePropertyNames) {
            Method getter = getters.get(propertyName);
            Method setter = setters.get(propertyName);
            Field f = fields.get(propertyName);
            if (getter!=null && getter.isAnnotationPresent(Transient.class)) {
                log.info("Ignore property \"" + propertyName + "\".");
                continue;
            }
            if (f!=null && f.isAnnotationPresent(Transient.class)) {
                log.info("Ignore property \"" + propertyName + "\".");
                continue;
            }
            if (f == null) {
                if (getter == null || setter == null) {
                    throw new IllegalArgumentException("Property \"" + propertyName + "\" must have both getter and setters.");
                }
                if (! getter.getReturnType().equals(setter.getParameterTypes()[0])) {
                    throw new IllegalArgumentException("Property \"" + propertyName + "\" must have same type of getter and setter.");
                }
            }
            else {
                // field exist:
                if (getter==null && setter!=null && !f.getType().equals(setter.getParameterTypes()[0])) {
                    throw new IllegalArgumentException("Property \"" + propertyName + "\" must have same type of field and setter.");
                }
                if (setter==null && getter!=null && !f.getType().equals(getter.getReturnType())) {
                    throw new IllegalArgumentException("Property \"" + propertyName + "\" must have same type of field and getter.");
                }
            }
            boolean isPrimaryKey = false;
            if (getter!=null && getter.isAnnotationPresent(Id.class)) {
                isPrimaryKey = true;
            }
            else if (f!=null && f.isAnnotationPresent(Id.class)) {
                isPrimaryKey = true;
            }
            if (isPrimaryKey && primaryKey!=null) {
                throw new IllegalArgumentException("Multiple @Id found in class: " + beanClass.getName());
            }
            if (isPrimaryKey) {
                primaryKey = propertyName;
                log.info("Found primary key: " + propertyName);
            }
            Class<?> propertyType = f!=null ? f.getType() : (getter!=null ? getter.getReturnType() : setter.getParameterTypes()[0]);
            log.info("Found property on bean \"" + clazz.getName() + "\": \"" + propertyName + "\", type: " + propertyType.getName());
            boolean isSimpleType = isSimpleType(propertyType);
            map.put(propertyName, new Property() {
                public boolean isSimpleType() {
                    return isSimpleType;
                }
                public Class<?> getPropertyType() {
                    return propertyType;
                }
                public Object getProperty(Object bean) throws Exception {
                    return getter == null ? f.get(bean) : getter.invoke(bean);
                }
                public void setProperty(Object bean, Object value) throws Exception {
                    if (setter == null) {
                        f.set(bean, value);
                    }
                    else {
                        setter.invoke(bean, value);
                    }
                }
            });
        }
        this.map = map;
        if (primaryKey == null && map.containsKey("id")) {
            primaryKey = "id";
            log.info("Automatically detect primary key: id");
        }
        if (primaryKey == null) {
            throw new IllegalArgumentException("No primary key found in class: " + beanClass.getName());
        }
        this.primaryKey = primaryKey;
    }

    static final Set<String> SIMPLE_TYPES = new HashSet<String>(Arrays.asList(
            boolean.class.getName(),
            Boolean.class.getName(),
            byte.class.getName(),
            Byte.class.getName(),
            short.class.getName(),
            Short.class.getName(),
            int.class.getName(),
            Integer.class.getName(),
            long.class.getName(),
            Long.class.getName(),
            float.class.getName(),
            Float.class.getName(),
            double.class.getName(),
            Double.class.getName(),
            String.class.getName()
    ));

    boolean isSimpleType(Class<?> propertyType) {
        return SIMPLE_TYPES.contains(propertyType.getName());
    }

    String getTableName(Class<?> beanClass) {
        Table t = beanClass.getAnnotation(Table.class);
        return t==null ? beanClass.getSimpleName() : t.value();
    }

    /**
     * Return a modifiable, new set contains property names.
     * 
     * @return A safe-to-modify set.
     */
    Set<String> getProperyNames() {
        return new HashSet<String>(map.keySet());
    }

    Property getProperty(String name) {
        return this.map.get(name);
    }
}

interface Property {

    boolean isSimpleType();

    Class<?> getPropertyType();

    Object getProperty(Object bean) throws Exception;

    void setProperty(Object bean, Object value) throws Exception;

}
