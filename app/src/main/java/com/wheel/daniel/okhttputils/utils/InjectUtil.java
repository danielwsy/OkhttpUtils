package com.wheel.daniel.okhttputils.utils;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;

/**
 * @author danielwang
 * @Description:
 * @date 2018/9/14 11:15
 */
public class InjectUtil {
    public InjectUtil() {
    }

    public static int getContentLayoutId(Object object) {
        ContentView contentView = (ContentView) object.getClass().getAnnotation(ContentView.class);
        return contentView == null ? 0 : contentView.value();
    }

    public static void injectViews(Object injectedSource, View sourceView) {
        injectViewsFromClass(injectedSource, injectedSource.getClass(), sourceView);
        injectViewsFromClass(injectedSource, injectedSource.getClass().getSuperclass(), sourceView);
    }

    public static void injectViews(Object injectedSource, Activity activity) {
        injectViewsFromClass(injectedSource, injectedSource.getClass(), activity);
        injectViewsFromClass(injectedSource, injectedSource.getClass().getSuperclass(), activity);
    }

    public static void injectViewsFromClass(Object injectedSource, Class<?> clazz, View sourceView) {
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            Field[] arr$ = fields;
            int len$ = fields.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                Field field = arr$[i$];

                try {
                    field.setAccessible(true);
                    InjectView viewInject = (InjectView) field.getAnnotation(InjectView.class);
                    if (viewInject != null) {
                        int viewId = viewInject.value();
                        field.set(injectedSource, sourceView.findViewById(viewId));
                    }
                } catch (Exception var10) {
                    var10.printStackTrace();
                }
            }
        }

    }

    private static void injectViewsFromClass(Object injectedSource, Class<?> clazz, Activity activity) {
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            Field[] arr$ = fields;
            int len$ = fields.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                Field field = arr$[i$];

                try {
                    field.setAccessible(true);
                    InjectView viewInject = (InjectView) field.getAnnotation(InjectView.class);
                    if (viewInject != null) {
                        int viewId = viewInject.value();
                        field.set(injectedSource, activity.findViewById(viewId));
                    }
                } catch (Exception var10) {
                }
            }
        }

    }
}
