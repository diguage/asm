package com.diguage.truman;

import org.objectweb.asm.ClassWriter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.*;

public class GenerateClassTest {

    public static byte[] generateClass() {
        ClassWriter cw = new ClassWriter(0);
        cw.visit(V1_5, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE,
                "pkg/Comparable", null,
                "java/lang/Object", new String[]{"pkg/Mesurable"});
        cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "LESS",
                "I", null, -1).visitEnd();
        cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "EQUAL",
                "I", null, 0).visitEnd();
        cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "GREATER",
                "I", null, 1).visitEnd();
        cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "compareTo",
                "(Ljava/lang/Object;)I", null, null).visitEnd();
        cw.visitEnd();
        byte[] bytes = cw.toByteArray();
        return bytes;
    }

    public static byte[] generateInterface() {
        ClassWriter cw = new ClassWriter(0);
        cw.visit(V1_5, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE,
                "pkg/Mesurable", null,
                "java/lang/Object", null);
        cw.visitEnd();
        byte[] bytes = cw.toByteArray();
        return bytes;
    }

    public static void main(String[] args) {
        AsmClassLoader loader = new AsmClassLoader();
        Class mClazz = loader.defineClass("pkg.Mesurable", generateInterface());
        Class clazz = loader.defineClass("pkg.Comparable", generateClass());
        for (Field field : clazz.getDeclaredFields()) {
            System.out.println(field.getName());
        }
        for (Method method : clazz.getDeclaredMethods()) {
            System.out.println(method.getName());
        }
    }

    public static class AsmClassLoader extends ClassLoader {
        public Class defineClass(String name, byte[] bytes) {
            return defineClass(name, bytes, 0, bytes.length);
        }
    }

    public static class StubClassLoader extends ClassLoader {
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            if (name.endsWith("_Stub")) {
                ClassWriter cw = new ClassWriter(0);
                // TODO generate the class
                byte[] bytes = cw.toByteArray();
                return defineClass(name, bytes, 0, bytes.length);
            }
            return super.findClass(name);
        }
    }
}