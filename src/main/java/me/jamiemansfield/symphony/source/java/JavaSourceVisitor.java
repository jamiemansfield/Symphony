//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.source.java;

import static com.github.javaparser.utils.PositionUtils.sortByBeginPosition;
import static com.github.javaparser.utils.Utils.isNullOrEmpty;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeArguments;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.printer.PrettyPrintVisitor;
import com.github.javaparser.printer.PrettyPrinterConfiguration;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * An extension of {@link PrettyPrintVisitor} for Symphony, with
 * improved formatting.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class JavaSourceVisitor extends PrettyPrintVisitor {

    public JavaSourceVisitor(final PrettyPrinterConfiguration config) {
        super(config);
    }

    private void printModifiers(final EnumSet<Modifier> modifiers) {
        if (modifiers.size() > 0) {
            printer.print(modifiers.stream().map(Modifier::asString).collect(Collectors.joining(" ")) + " ");
        }
    }

    private void printMembers(final NodeList<BodyDeclaration<?>> members, final Void arg) {
        for (int i = 0; i < members.size(); i++) {
            final BodyDeclaration<?> member = members.get(i);

            if (i == 0 || !member.isFieldDeclaration()) {
                printer.println();
            }
            member.accept(this, arg);
            printer.println();
        }
    }

    private void printMemberAnnotations(final NodeList<AnnotationExpr> annotations, final Void arg) {
        if (annotations.isEmpty()) {
            return;
        }
        for (final AnnotationExpr a : annotations) {
            a.accept(this, arg);
            printer.println();
        }
    }

    private void printAnnotations(final NodeList<AnnotationExpr> annotations, boolean prefixWithASpace,
                                  final Void arg) {
        if (annotations.isEmpty()) {
            return;
        }
        if (prefixWithASpace) {
            printer.print(" ");
        }
        for (AnnotationExpr annotation : annotations) {
            annotation.accept(this, arg);
            printer.print(" ");
        }
    }

    private void printTypeArgs(final NodeWithTypeArguments<?> nodeWithTypeArguments, final Void arg) {
        NodeList<Type> typeArguments = nodeWithTypeArguments.getTypeArguments().orElse(null);
        if (!isNullOrEmpty(typeArguments)) {
            printer.print("<");
            for (final Iterator<Type> i = typeArguments.iterator(); i.hasNext(); ) {
                final Type t = i.next();
                t.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
            printer.print(">");
        }
    }

    private void printTypeParameters(final NodeList<TypeParameter> args, final Void arg) {
        if (!isNullOrEmpty(args)) {
            printer.print("<");
            for (final Iterator<TypeParameter> i = args.iterator(); i.hasNext(); ) {
                final TypeParameter t = i.next();
                t.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
            printer.print(">");
        }
    }

    private void printArguments(final NodeList<Expression> args, final Void arg) {
        printer.print("(");
        if (!isNullOrEmpty(args)) {
            boolean columnAlignParameters = (args.size() > 1) && configuration.isColumnAlignParameters();
            if (columnAlignParameters) {
                printer.indentWithAlignTo(printer.getCursor().column);
            }
            for (final Iterator<Expression> i = args.iterator(); i.hasNext(); ) {
                final Expression e = i.next();
                e.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(",");
                    if (columnAlignParameters) {
                        printer.println();
                    } else {
                        printer.print(" ");
                    }
                }
            }
            if (columnAlignParameters) {
                printer.unindent();
            }
        }
        printer.print(")");
    }

    private void printPrePostFixOptionalList(final NodeList<? extends Visitable> args, final Void arg, String prefix, String separator, String postfix) {
        if (!args.isEmpty()) {
            printer.print(prefix);
            for (final Iterator<? extends Visitable> i = args.iterator(); i.hasNext(); ) {
                final Visitable v = i.next();
                v.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(separator);
                }
            }
            printer.print(postfix);
        }
    }

    private void printPrePostFixRequiredList(final NodeList<? extends Visitable> args, final Void arg, String prefix, String separator, String postfix) {
        printer.print(prefix);
        if (!args.isEmpty()) {
            for (final Iterator<? extends Visitable> i = args.iterator(); i.hasNext(); ) {
                final Visitable v = i.next();
                v.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(separator);
                }
            }
        }
        printer.print(postfix);
    }

    private void printComment(final Optional<Comment> comment, final Void arg) {
        comment.ifPresent(c -> c.accept(this, arg));
    }

    private void printOrphanCommentsEnding(final Node node) {
        if (configuration.isIgnoreComments()) return;

        List<Node> everything = new LinkedList<>();
        everything.addAll(node.getChildNodes());
        sortByBeginPosition(everything);
        if (everything.isEmpty()) {
            return;
        }

        int commentsAtEnd = 0;
        boolean findingComments = true;
        while (findingComments && commentsAtEnd < everything.size()) {
            Node last = everything.get(everything.size() - 1 - commentsAtEnd);
            findingComments = (last instanceof Comment);
            if (findingComments) {
                commentsAtEnd++;
            }
        }
        for (int i = 0; i < commentsAtEnd; i++) {
            everything.get(everything.size() - commentsAtEnd + i).accept(this, null);
        }
    }

    @Override
    public void visit(final ClassOrInterfaceDeclaration n, final Void arg) {
        printComment(n.getComment(), arg);
        printMemberAnnotations(n.getAnnotations(), arg);
        printModifiers(n.getModifiers());

        if (n.isInterface()) {
            printer.print("interface ");
        } else {
            printer.print("class ");
        }

        n.getName().accept(this, arg);

        printTypeParameters(n.getTypeParameters(), arg);

        if (!n.getExtendedTypes().isEmpty()) {
            printer.print(" extends ");
            for (final Iterator<ClassOrInterfaceType> i = n.getExtendedTypes().iterator(); i.hasNext(); ) {
                final ClassOrInterfaceType c = i.next();
                c.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }

        if (!n.getImplementedTypes().isEmpty()) {
            printer.print(" implements ");
            for (final Iterator<ClassOrInterfaceType> i = n.getImplementedTypes().iterator(); i.hasNext(); ) {
                final ClassOrInterfaceType c = i.next();
                c.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }

        printer.println(" {");
        printer.indent();
        if (!isNullOrEmpty(n.getMembers())) {
            printMembers(n.getMembers(), arg);
        }

        printOrphanCommentsEnding(n);

        printer.unindent();
        printer.print("}");
    }

    @Override
    public void visit(final EnumDeclaration n, final Void arg) {
        printComment(n.getComment(), arg);
        printMemberAnnotations(n.getAnnotations(), arg);
        printModifiers(n.getModifiers());

        printer.print("enum ");
        n.getName().accept(this, arg);

        if (!n.getImplementedTypes().isEmpty()) {
            printer.print(" implements ");
            for (final Iterator<ClassOrInterfaceType> i = n.getImplementedTypes().iterator(); i.hasNext(); ) {
                final ClassOrInterfaceType c = i.next();
                c.accept(this, arg);
                if (i.hasNext()) {
                    printer.print(", ");
                }
            }
        }

        printer.println(" {");
        printer.indent();
        if (n.getEntries().isNonEmpty()) {
            final boolean alignVertically =
                    // Either we hit the constant amount limit in the configurations, or...
                    n.getEntries().size() > configuration.getMaxEnumConstantsToAlignHorizontally() ||
                            // any of the constants has a comment.
                            n.getEntries().stream().anyMatch(e -> e.getComment().isPresent());
            printer.println();
            for (final Iterator<EnumConstantDeclaration> i = n.getEntries().iterator(); i.hasNext(); ) {
                final EnumConstantDeclaration e = i.next();
                e.accept(this, arg);
                if (i.hasNext()) {
                    if (alignVertically) {
                        printer.println(",");
                    } else {
                        printer.print(", ");
                    }
                }
            }
        }
        if (!n.getMembers().isEmpty()) {
            printer.println(";");
            printMembers(n.getMembers(), arg);
        } else {
            if (!n.getEntries().isEmpty()) {
                printer.println();
            }
        }
        printer.unindent();
        printer.print("}");
    }

}
