package com.haircutAPI.HaircutAPI.annotation;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Properties;

import javax.imageio.spi.ServiceRegistry;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;

public class GenerateIDCustome implements IdentifierGenerator, Configurable {
    private String prefix;
    private String colName;
    private String tableName;

    @Override
    public Serializable generate(
            SharedSessionContractImplementor session, Object obj)
            throws HibernateException {

        String query = String.format("select %s from %s",
                colName,
                tableName);

        var strings = session.createNativeQuery(query, String.class);
        var ids = strings.getResultStream().toList();
        long max = ids.size();

        return prefix + (max + 1);
    }

    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {
        prefix = properties.getProperty("prefix");
        colName = properties.getProperty("colName");
        tableName = properties.getProperty("tableName");
    }

}
