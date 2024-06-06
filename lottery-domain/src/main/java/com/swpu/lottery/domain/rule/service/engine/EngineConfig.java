package com.swpu.lottery.domain.rule.service.engine;

import com.swpu.lottery.domain.rule.service.logic.LogicFilter;
import com.swpu.lottery.domain.rule.service.logic.impl.UserAgeFilter;
import com.swpu.lottery.domain.rule.service.logic.impl.UserGenderFilter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EngineConfig {
    protected static Map<String, LogicFilter> logicFilterMap = new ConcurrentHashMap<>();
    @Resource
    private UserAgeFilter userAgeFilter;
    @Resource
    private UserGenderFilter userGenderFilter;
    @PostConstruct
    public void init() {
        logicFilterMap.put("age", userAgeFilter);
        logicFilterMap.put("gender", userGenderFilter);
    }
}
