package com.diguage.truman;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import static org.objectweb.asm.Opcodes.ASM9;
import static org.objectweb.asm.Opcodes.V1_8;

public class TransformClassTest {
    public void test() {
        byte[] b1 = null;
        ClassReader cr = new ClassReader(b1);

        ClassWriter cw = new ClassWriter(0);
        ClassVisitor cv = new ChangeVersionAdapter(cw);
        cr.accept(cv, 0);
        byte[] b2 = cw.toByteArray();
    }

    public static class ChangeVersionAdapter extends ClassVisitor {
        public ChangeVersionAdapter(ClassVisitor classVisitor) {
            super(ASM9, classVisitor);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            super.visit(V1_8, access, name, signature, superName, interfaces);
        }
    }

    /**
     * 通过 Java Agent 可以修改所有类的版本号
     *
     * @param agentArgs
     * @param instrumentation
     */
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                ClassReader cr = new ClassReader(classfileBuffer);
                ClassWriter cw = new ClassWriter(cr, 0);
                ChangeVersionAdapter cv = new ChangeVersionAdapter(cw);
                cr.accept(cv, 0);
                return cw.toByteArray();
            }
        });
    }
}
