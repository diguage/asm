package com.diguage.truman;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

import java.util.Objects;

import static org.objectweb.asm.Opcodes.ASM9;

/**
 * TODO 如果在 visitField 方法中增加新属性，会每次遇到一个 field 都去执行一遍吗？如何解决这个问题？
 */
public class AddClassMemberTest {
    public static class AddFieldAdapter extends ClassVisitor {
        private int fAcc;
        private String fName;
        private String fDesc;
        private boolean isFieldPresent;

        public AddFieldAdapter(ClassVisitor classVisitor,
                               int fAcc, String fName, String fDesc) {
            super(ASM9, classVisitor);
            this.fAcc = fAcc;
            this.fName = fName;
            this.fDesc = fDesc;
        }

        @Override
        public FieldVisitor visitField(int access, String name,
                                       String descriptor, String signature, Object value) {
            if (name.equals(fName)) {
                isFieldPresent = true;
            }
            return super.visitField(access, name, descriptor, signature, value);
        }

        @Override
        public void visitEnd() {
            if (!isFieldPresent) {
                FieldVisitor fv = cv.visitField(fAcc, fName, fDesc, null, null);
                if (Objects.nonNull(fv)) {
                    fv.visitEnd();
                }
            }
            cv.visitEnd();
        }
    }
}
