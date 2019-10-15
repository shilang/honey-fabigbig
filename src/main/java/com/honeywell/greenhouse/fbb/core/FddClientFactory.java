package com.honeywell.greenhouse.fbb.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.fadada.sdk.client.common.FddClient;
import com.honeywell.greenhouse.fbb.properties.FddProperties;

@Component
@EnableConfigurationProperties(FddProperties.class)
public class FddClientFactory {

    @Autowired
    private FddProperties fddProperties;

    public <T extends FddClient> T create(Class<T> fddClientType) throws FddException {
        try {
            Constructor<T> declaredConstructor = fddClientType.getDeclaredConstructor(String.class, String.class, String.class, String.class);
            return declaredConstructor.newInstance(fddProperties.getAppId(), fddProperties.getSecret(), fddProperties.getVersion(), fddProperties.getVerifyCaUrl());
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new FddException(e.getMessage(), e);
        }
    }
}
