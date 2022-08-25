package com.diguage.truman;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ASM9;

public class RemoveClassMemberTest {
    public static class RemoveDebugAdapter extends ClassVisitor {
        public RemoveDebugAdapter(ClassVisitor classVisitor) {
            super(ASM9, classVisitor);
        }

        @Override
        public void visitSource(String source, String debug) {
        }

        @Override
        public void visitOuterClass(String owner, String name, String descriptor) {
        }

        @Override
        public void visitInnerClass(String name, String outerName, String innerName, int access) {
        }
    }

    public static class RemoveMethodAdapter extends ClassVisitor {
        private String mName;
        private String mDesc;

        protected RemoveMethodAdapter(ClassVisitor classVisitor, String mName, String mDesc) {
            super(ASM9, classVisitor);
            this.mName = mName;
            this.mDesc = mDesc;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            if (name.endsWith(mName) && descriptor.equals(mDesc)) {
                // TODO
            }
            return cv.visitMethod(access, name, descriptor, signature, exceptions);
        }
    }

}
