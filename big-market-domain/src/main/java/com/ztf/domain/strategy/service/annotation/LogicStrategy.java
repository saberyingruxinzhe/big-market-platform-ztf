package com.ztf.domain.strategy.service.annotation;

import com.ztf.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


//策略自定义枚举，这个注解放到Filter上面方便factory能够扫描进map
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogicStrategy {

    //放到Filter上面时设定这个值，同时这个值也是map中的key
    DefaultLogicFactory.LogicModel logicMode();

}