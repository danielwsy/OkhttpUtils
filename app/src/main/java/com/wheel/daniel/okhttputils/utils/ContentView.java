package com.wheel.daniel.okhttputils.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author danielwang
 * @Description:
 * @date 2018/9/14 11:15
 * <p>
 * 因为这个注释我们要运在Activity类上，
 * 所以我们要申明@Targrt(ElementType.TYPE)。
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ContentView {
    int value();
}
