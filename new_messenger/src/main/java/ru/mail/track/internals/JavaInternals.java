package ru.mail.track.internals;

/*
 * Copyright 2015 Odnoklassniki Ltd, Mail.Ru Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import sun.misc.Unsafe;

public final class JavaInternals {
    public static final Unsafe unsafe = getUnsafe();
    public static final long byteArrayOffset = unsafe.arrayBaseOffset(byte[].class);

    public static Unsafe getUnsafe() {
        try {
            return (Unsafe) getField(Unsafe.class, "theUnsafe").get(null);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Field getField(Class<?> cls, String name) {
        try {
            Field f = cls.getDeclaredField(name);
            f.setAccessible(true);
            return f;
        } catch (Exception e) {
            return null;
        }
    }

    public static Field getField(String cls, String name) {
        try {
            return getField(Class.forName(cls), name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Field findFieldRecursively(Class<?> cls, String name) {
        for (; cls != null; cls = cls.getSuperclass()) {
            Field f = getField(cls, name);
            if (f != null) {
                return f;
            }
        }
        return null;
    }

    public static Method getMethod(Class<?> cls, String name, Class... params) {
        try {
            Method m = cls.getDeclaredMethod(name, params);
            m.setAccessible(true);
            return m;
        } catch (Exception e) {
            return null;
        }
    }

    public static Method getMethod(String cls, String name, Class... params) {
        try {
            return getMethod(Class.forName(cls), name, params);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Method findMethodRecursively(Class<?> cls, String name, Class... params) {
        for (; cls != null; cls = cls.getSuperclass()) {
            Method m = getMethod(cls, name, params);
            if (m != null) {
                return m;
            }
        }
        return null;
    }

    public static Constructor getConstructor(Class<?> cls, Class... params) {
        try {
            Constructor c = cls.getDeclaredConstructor(params);
            c.setAccessible(true);
            return c;
        } catch (Exception e) {
            return null;
        }
    }

    public static Constructor getConstructor(String cls, Class... params) {
        try {
            return getConstructor(Class.forName(cls), params);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static long fieldOffset(Class<?> cls, String name) {
        try {
            return unsafe.objectFieldOffset(cls.getDeclaredField(name));
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(e);
        }
    }

    public static long fieldOffset(String cls, String name) {
        try {
            return fieldOffset(Class.forName(cls), name);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
}

