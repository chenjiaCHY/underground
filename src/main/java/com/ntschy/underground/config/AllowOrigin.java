package com.ntschy.underground.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "allow")
public class AllowOrigin {

    private List<String> originList;

    public List<String> getOriginList() {
        return originList;
    }

    public void setOriginList(List<String> originList) {
        this.originList = originList;
    }
}
