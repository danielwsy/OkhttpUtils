package com.wheel.daniel.okhttputils.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author danielwang
 * @Description:
 * @date 2018/9/14 11:16
 * 因为这个注释我们要运在类的成员变量上，
 * 所以我们要申明@Targrt(ElementType.FILELD)。
 * 类成员变量指定我们申明的控件对象
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectView {
    int value();
}

