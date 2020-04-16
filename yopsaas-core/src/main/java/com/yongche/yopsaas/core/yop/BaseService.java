package com.yongche.yopsaas.core.yop;

import com.yongche.yopsaas.core.config.YopProperties;

public class BaseService {
    private YopProperties properties;

    public YopProperties getProperties() {
        return properties;
    }

    public void setProperties(YopProperties properties) {
        this.properties = properties;
    }
}
