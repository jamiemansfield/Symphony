//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.tab.code;

import org.fxmisc.richtext.CodeArea;
import spoon.reflect.code.CtAnnotationFieldAccess;
import spoon.reflect.code.CtArrayRead;
import spoon.reflect.code.CtArrayWrite;
import spoon.reflect.code.CtAssert;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtBreak;
import spoon.reflect.code.CtCase;
import spoon.reflect.code.CtCatch;
import spoon.reflect.code.CtCatchVariable;
import spoon.reflect.code.CtCodeSnippetExpression;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtComment;
import spoon.reflect.code.CtConditional;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtContinue;
import spoon.reflect.code.CtDo;
import spoon.reflect.code.CtExecutableReferenceExpression;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtFieldRead;
import spoon.reflect.code.CtFieldWrite;
import spoon.reflect.code.CtFor;
import spoon.reflect.code.CtForEach;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtJavaDoc;
import spoon.reflect.code.CtJavaDocTag;
import spoon.reflect.code.CtLambda;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtNewArray;
import spoon.reflect.code.CtNewClass;
import spoon.reflect.code.CtOperatorAssignment;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatementList;
import spoon.reflect.code.CtSuperAccess;
import spoon.reflect.code.CtSwitch;
import spoon.reflect.code.CtSwitchExpression;
import spoon.reflect.code.CtSynchronized;
import spoon.reflect.code.CtThisAccess;
import spoon.reflect.code.CtThrow;
import spoon.reflect.code.CtTry;
import spoon.reflect.code.CtTryWithResource;
import spoon.reflect.code.CtTypeAccess;
import spoon.reflect.code.CtUnaryOperator;
import spoon.reflect.code.CtVariableRead;
import spoon.reflect.code.CtVariableWrite;
import spoon.reflect.code.CtWhile;
import spoon.reflect.code.CtYieldStatement;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtAnnotationMethod;
import spoon.reflect.declaration.CtAnnotationType;
import spoon.reflect.declaration.CtAnonymousExecutable;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtCompilationUnit;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtEnum;
import spoon.reflect.declaration.CtEnumValue;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtImport;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtModule;
import spoon.reflect.declaration.CtModuleRequirement;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtPackageDeclaration;
import spoon.reflect.declaration.CtPackageExport;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.CtProvidedService;
import spoon.reflect.declaration.CtTypeParameter;
import spoon.reflect.declaration.CtUsedService;
import spoon.reflect.reference.CtArrayTypeReference;
import spoon.reflect.reference.CtCatchVariableReference;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtIntersectionTypeReference;
import spoon.reflect.reference.CtLocalVariableReference;
import spoon.reflect.reference.CtModuleReference;
import spoon.reflect.reference.CtPackageReference;
import spoon.reflect.reference.CtParameterReference;
import spoon.reflect.reference.CtTypeMemberWildcardImportReference;
import spoon.reflect.reference.CtTypeParameterReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.reference.CtUnboundVariableReference;
import spoon.reflect.reference.CtWildcardReference;
import spoon.reflect.visitor.CtVisitor;

import java.lang.annotation.Annotation;

/**
 * A {@link CtVisitor visitor} to decorate a {@link CodeTab code tab}'s
 * view with selectable members.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class SourceDecoratorVisitor implements CtVisitor {

    private final CodeArea codeArea;

    public SourceDecoratorVisitor(final CodeArea codeArea) {
        this.codeArea = codeArea;
    }

    @Override
    public <A extends Annotation> void visitCtAnnotation(final CtAnnotation<A> annotation) {
    }

    @Override
    public <T> void visitCtCodeSnippetExpression(final CtCodeSnippetExpression<T> expression) {
    }

    @Override
    public void visitCtCodeSnippetStatement(final CtCodeSnippetStatement statement) {
    }

    @Override
    public <A extends Annotation> void visitCtAnnotationType(final CtAnnotationType<A> annotationType) {
    }

    @Override
    public void visitCtAnonymousExecutable(final CtAnonymousExecutable anonymousExec) {
    }

    @Override
    public <T> void visitCtArrayRead(final CtArrayRead<T> arrayRead) {
    }

    @Override
    public <T> void visitCtArrayWrite(final CtArrayWrite<T> arrayWrite) {
    }

    @Override
    public <T> void visitCtArrayTypeReference(final CtArrayTypeReference<T> reference) {
    }

    @Override
    public <T> void visitCtAssert(final CtAssert<T> asserted) {
    }

    @Override
    public <T, A extends T> void visitCtAssignment(final CtAssignment<T, A> assignement) {
    }

    @Override
    public <T> void visitCtBinaryOperator(final CtBinaryOperator<T> operator) {
    }

    @Override
    public <R> void visitCtBlock(final CtBlock<R> block) {
    }

    @Override
    public void visitCtBreak(final CtBreak breakStatement) {
    }

    @Override
    public <S> void visitCtCase(final CtCase<S> caseStatement) {
    }

    @Override
    public void visitCtCatch(final CtCatch catchBlock) {
    }

    @Override
    public <T> void visitCtClass(final CtClass<T> ctClass) {
    }

    @Override
    public void visitCtTypeParameter(final CtTypeParameter typeParameter) {
    }

    @Override
    public <T> void visitCtConditional(final CtConditional<T> conditional) {
    }

    @Override
    public <T> void visitCtConstructor(final CtConstructor<T> c) {
    }

    @Override
    public void visitCtContinue(final CtContinue continueStatement) {
    }

    @Override
    public void visitCtDo(final CtDo doLoop) {
    }

    @Override
    public <T extends Enum<?>> void visitCtEnum(final CtEnum<T> ctEnum) {
    }

    @Override
    public <T> void visitCtExecutableReference(final CtExecutableReference<T> reference) {
    }

    @Override
    public <T> void visitCtField(final CtField<T> f) {
        f.getDocComment();
    }

    @Override
    public <T> void visitCtEnumValue(final CtEnumValue<T> enumValue) {
    }

    @Override
    public <T> void visitCtThisAccess(final CtThisAccess<T> thisAccess) {
    }

    @Override
    public <T> void visitCtFieldReference(final CtFieldReference<T> reference) {
    }

    @Override
    public <T> void visitCtUnboundVariableReference(final CtUnboundVariableReference<T> reference) {
    }

    @Override
    public void visitCtFor(final CtFor forLoop) {
    }

    @Override
    public void visitCtForEach(final CtForEach foreach) {
    }

    @Override
    public void visitCtIf(final CtIf ifElement) {
    }

    @Override
    public <T> void visitCtInterface(final CtInterface<T> intrface) {
    }

    @Override
    public <T> void visitCtInvocation(final CtInvocation<T> invocation) {
    }

    @Override
    public <T> void visitCtLiteral(final CtLiteral<T> literal) {
    }

    @Override
    public <T> void visitCtLocalVariable(final CtLocalVariable<T> localVariable) {
    }

    @Override
    public <T> void visitCtLocalVariableReference(final CtLocalVariableReference<T> reference) {
    }

    @Override
    public <T> void visitCtCatchVariable(final CtCatchVariable<T> catchVariable) {
    }

    @Override
    public <T> void visitCtCatchVariableReference(final CtCatchVariableReference<T> reference) {
    }

    @Override
    public <T> void visitCtMethod(final CtMethod<T> m) {
    }

    @Override
    public <T> void visitCtAnnotationMethod(final CtAnnotationMethod<T> annotationMethod) {
    }

    @Override
    public <T> void visitCtNewArray(final CtNewArray<T> newArray) {
    }

    @Override
    public <T> void visitCtConstructorCall(final CtConstructorCall<T> ctConstructorCall) {
    }

    @Override
    public <T> void visitCtNewClass(final CtNewClass<T> newClass) {
    }

    @Override
    public <T> void visitCtLambda(final CtLambda<T> lambda) {
    }

    @Override
    public <T, E extends CtExpression<?>> void visitCtExecutableReferenceExpression(final CtExecutableReferenceExpression<T, E> expression) {
    }

    @Override
    public <T, A extends T> void visitCtOperatorAssignment(final CtOperatorAssignment<T, A> assignment) {
    }

    @Override
    public void visitCtPackage(final CtPackage ctPackage) {
        System.out.println(ctPackage.getQualifiedName());
    }

    @Override
    public void visitCtPackageReference(final CtPackageReference reference) {
    }

    @Override
    public <T> void visitCtParameter(final CtParameter<T> parameter) {
    }

    @Override
    public <T> void visitCtParameterReference(final CtParameterReference<T> reference) {
    }

    @Override
    public <R> void visitCtReturn(final CtReturn<R> returnStatement) {
    }

    @Override
    public <R> void visitCtStatementList(final CtStatementList statements) {
    }

    @Override
    public <S> void visitCtSwitch(final CtSwitch<S> switchStatement) {
    }

    @Override
    public <T, S> void visitCtSwitchExpression(final CtSwitchExpression<T, S> switchExpression) {
    }

    @Override
    public void visitCtSynchronized(final CtSynchronized synchro) {
    }

    @Override
    public void visitCtThrow(final CtThrow throwStatement) {
    }

    @Override
    public void visitCtTry(final CtTry tryBlock) {
    }

    @Override
    public void visitCtTryWithResource(final CtTryWithResource tryWithResource) {
    }

    @Override
    public void visitCtTypeParameterReference(final CtTypeParameterReference ref) {
    }

    @Override
    public void visitCtWildcardReference(final CtWildcardReference wildcardReference) {
    }

    @Override
    public <T> void visitCtIntersectionTypeReference(final CtIntersectionTypeReference<T> reference) {
    }

    @Override
    public <T> void visitCtTypeReference(final CtTypeReference<T> reference) {
    }

    @Override
    public <T> void visitCtTypeAccess(final CtTypeAccess<T> typeAccess) {
    }

    @Override
    public <T> void visitCtUnaryOperator(final CtUnaryOperator<T> operator) {
    }

    @Override
    public <T> void visitCtVariableRead(final CtVariableRead<T> variableRead) {
    }

    @Override
    public <T> void visitCtVariableWrite(final CtVariableWrite<T> variableWrite) {
    }

    @Override
    public void visitCtWhile(final CtWhile whileLoop) {
    }

    @Override
    public <T> void visitCtAnnotationFieldAccess(final CtAnnotationFieldAccess<T> annotationFieldAccess) {
    }

    @Override
    public <T> void visitCtFieldRead(final CtFieldRead<T> fieldRead) {
    }

    @Override
    public <T> void visitCtFieldWrite(final CtFieldWrite<T> fieldWrite) {
    }

    @Override
    public <T> void visitCtSuperAccess(final CtSuperAccess<T> f) {
    }

    @Override
    public void visitCtComment(final CtComment comment) {
    }

    @Override
    public void visitCtJavaDoc(final CtJavaDoc comment) {
    }

    @Override
    public void visitCtJavaDocTag(final CtJavaDocTag docTag) {
    }

    @Override
    public void visitCtImport(final CtImport ctImport) {
    }

    @Override
    public void visitCtModule(final CtModule module) {
    }

    @Override
    public void visitCtModuleReference(final CtModuleReference moduleReference) {
    }

    @Override
    public void visitCtPackageExport(final CtPackageExport moduleExport) {
    }

    @Override
    public void visitCtModuleRequirement(final CtModuleRequirement moduleRequirement) {
    }

    @Override
    public void visitCtProvidedService(final CtProvidedService moduleProvidedService) {
    }

    @Override
    public void visitCtUsedService(final CtUsedService usedService) {
    }

    @Override
    public void visitCtCompilationUnit(final CtCompilationUnit compilationUnit) {
    }

    @Override
    public void visitCtPackageDeclaration(final CtPackageDeclaration packageDeclaration) {
    }

    @Override
    public void visitCtTypeMemberWildcardImportReference(final CtTypeMemberWildcardImportReference wildcardReference) {
    }

    @Override
    public void visitCtYieldStatement(final CtYieldStatement statement) {
    }

}
