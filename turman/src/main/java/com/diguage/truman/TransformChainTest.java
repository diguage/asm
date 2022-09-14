package com.diguage.truman;

import org.objectweb.asm.ClassVisitor;

import static org.objectweb.asm.Opcodes.ASM9;

public class TransformChainTest {
    public static class MultiClassAdapter extends ClassVisitor {
        protected ClassVisitor[] cvs;

        public MultiClassAdapter(ClassVisitor[] cvs) {
            super(ASM9);
            this.cvs = cvs;
        }

        @Override
        public void visit(int version, int access, String name,
                          String signature, String superName, String[] interfaces) {
            for (ClassVisitor cv : cvs) {
                cv.visit(version, access, name, signature, superName, interfaces);
            }
        }
    }
}
