/*
 * @(#)LayoutVisitor.java                        2.1 2003/10/07
 *
 * Copyright (C) 1999, 2003 D.A. Watt and D.F. Brown
 * Dept. of Computing Science, University of Glasgow, Glasgow G12 8QQ Scotland
 * and School of Computer and Math Sciences, The Robert Gordon University,
 * St. Andrew Street, Aberdeen AB25 1HG, Scotland.
 * All rights reserved.
 *
 * This software is provided free for educational use only. It may
 * not be used for commercial purposes without the prior written permission
 * of the authors.
 */

package Triangle.TreeXml;

import Triangle.AbstractSyntaxTrees.AST;
import Triangle.AbstractSyntaxTrees.AnyTypeDenoter;
import Triangle.AbstractSyntaxTrees.ArrayExpression;
import Triangle.AbstractSyntaxTrees.ArrayTypeDenoter;
import Triangle.AbstractSyntaxTrees.AssignCommand;
import Triangle.AbstractSyntaxTrees.BinaryExpression;
import Triangle.AbstractSyntaxTrees.BinaryOperatorDeclaration;
import Triangle.AbstractSyntaxTrees.BoolTypeDenoter;
import Triangle.AbstractSyntaxTrees.CallCommand;
import Triangle.AbstractSyntaxTrees.CallExpression;
import Triangle.AbstractSyntaxTrees.CaseLiteral;
import Triangle.AbstractSyntaxTrees.CaseRange;
import Triangle.AbstractSyntaxTrees.CharTypeDenoter;
import Triangle.AbstractSyntaxTrees.CharacterExpression;
import Triangle.AbstractSyntaxTrees.CharacterLiteral;
import Triangle.AbstractSyntaxTrees.ChooseCommand;
import Triangle.AbstractSyntaxTrees.ComCase;
import Triangle.AbstractSyntaxTrees.ConstActualParameter;
import Triangle.AbstractSyntaxTrees.ConstDeclaration;
import Triangle.AbstractSyntaxTrees.ConstFormalParameter;
import Triangle.AbstractSyntaxTrees.DoUntilCommand;
import Triangle.AbstractSyntaxTrees.DoWhileCommand;
import Triangle.AbstractSyntaxTrees.DotVname;
import Triangle.AbstractSyntaxTrees.ElseCase;
import Triangle.AbstractSyntaxTrees.EmptyActualParameterSequence;
import Triangle.AbstractSyntaxTrees.EmptyCommand;
import Triangle.AbstractSyntaxTrees.EmptyExpression;
import Triangle.AbstractSyntaxTrees.EmptyFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.ErrorTypeDenoter;
import Triangle.AbstractSyntaxTrees.ForCommand;
import Triangle.AbstractSyntaxTrees.ForUntilCommand;
import Triangle.AbstractSyntaxTrees.ForWhileCommand;
import Triangle.AbstractSyntaxTrees.FuncActualParameter;
import Triangle.AbstractSyntaxTrees.FuncDeclaration;
import Triangle.AbstractSyntaxTrees.FuncFormalParameter;
import Triangle.AbstractSyntaxTrees.Identifier;
import Triangle.AbstractSyntaxTrees.IfCommand;
import Triangle.AbstractSyntaxTrees.IfExpression;
import Triangle.AbstractSyntaxTrees.IntTypeDenoter;
import Triangle.AbstractSyntaxTrees.IntegerExpression;
import Triangle.AbstractSyntaxTrees.IntegerLiteral;
import Triangle.AbstractSyntaxTrees.LetCommand;
import Triangle.AbstractSyntaxTrees.LetExpression;
import Triangle.AbstractSyntaxTrees.LitIntegerExpression;
import Triangle.AbstractSyntaxTrees.LitCharacterExpression;
import Triangle.AbstractSyntaxTrees.LongIdentifier;
import Triangle.AbstractSyntaxTrees.MultipleActualParameterSequence;
import Triangle.AbstractSyntaxTrees.MultipleArrayAggregate;
import Triangle.AbstractSyntaxTrees.MultipleFieldTypeDenoter;
import Triangle.AbstractSyntaxTrees.MultipleFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.MultipleRecordAggregate;
import Triangle.AbstractSyntaxTrees.Operator;
import Triangle.AbstractSyntaxTrees.PackageDeclaration;
import Triangle.AbstractSyntaxTrees.PackageIdentifier;
import Triangle.AbstractSyntaxTrees.ProcActualParameter;
import Triangle.AbstractSyntaxTrees.ProcDeclaration;
import Triangle.AbstractSyntaxTrees.ProcFormalParameter;
import Triangle.AbstractSyntaxTrees.Program;
import Triangle.AbstractSyntaxTrees.RecordExpression;
import Triangle.AbstractSyntaxTrees.RecordTypeDenoter;
import Triangle.AbstractSyntaxTrees.SCase;
import Triangle.AbstractSyntaxTrees.SequentialCommand;
import Triangle.AbstractSyntaxTrees.SequentialDeclaration;
import Triangle.AbstractSyntaxTrees.SequentialPackageDeclaration;
import Triangle.AbstractSyntaxTrees.SimpleTypeDenoter;
import Triangle.AbstractSyntaxTrees.SimpleVname;
import Triangle.AbstractSyntaxTrees.SingleActualParameterSequence;
import Triangle.AbstractSyntaxTrees.SingleArrayAggregate;
import Triangle.AbstractSyntaxTrees.SingleFieldTypeDenoter;
import Triangle.AbstractSyntaxTrees.SingleFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.SinglePackageDeclaration;
import Triangle.AbstractSyntaxTrees.SingleRecordAggregate;
import Triangle.AbstractSyntaxTrees.SubscriptVname;
import Triangle.AbstractSyntaxTrees.TypeDeclaration;
import Triangle.AbstractSyntaxTrees.UnaryExpression;
import Triangle.AbstractSyntaxTrees.UnaryOperatorDeclaration;
import Triangle.AbstractSyntaxTrees.UntilCommand;
import Triangle.AbstractSyntaxTrees.VarADeclaration;
import Triangle.AbstractSyntaxTrees.VarActualParameter;
import Triangle.AbstractSyntaxTrees.VarDeclaration;
import Triangle.AbstractSyntaxTrees.VarFormalParameter;
import Triangle.AbstractSyntaxTrees.Visitor;
import Triangle.AbstractSyntaxTrees.VnameExpression;
import Triangle.AbstractSyntaxTrees.WhileCommand;

//xml related libraries
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class XmlVisitor implements Visitor {


  private Document document;

  public  XmlVisitor (Document pDocument) {
    this.document = pDocument;
  }

  // Commands
  public Object visitAssignCommand(AssignCommand ast, Object obj) {
    return xmlBinary("AssignCommand", ast.V, ast.E);
  }

  public Object visitCallCommand(CallCommand ast, Object obj) {
    return xmlBinary("CallCommand", ast.I, ast.APS);
   }

  public Object visitEmptyCommand(EmptyCommand ast, Object obj) {
    return xmlNullary("EmptyCommand");
  }

  public Object visitIfCommand(IfCommand ast, Object obj) {
    return xmlTernary("IfCommand", ast.E, ast.C1, ast.C2);
  }

  public Object visitLetCommand(LetCommand ast, Object obj) {
    return xmlBinary("LetCommand", ast.D, ast.C);
  }

  public Object visitSequentialCommand(SequentialCommand ast, Object obj) {
    return xmlBinary("SequentialCommand", ast.C1, ast.C2);
  }

  public Object visitWhileCommand(WhileCommand ast, Object obj) {
    return xmlBinary("WhileCommand", ast.E, ast.C);
  }
  
  public Object visitUntilCommand(UntilCommand ast, Object obj) {
    return xmlBinary("UntilCommand", ast.E, ast.C);
  }
  
  public Object visitDoUntilCommand(DoUntilCommand ast, Object obj) {
    return xmlBinary("DoUntilCommand", ast.E, ast.C);
  }
  
  public Object visitDoWhileCommand(DoWhileCommand ast, Object obj) {
    return xmlBinary("DoWhileCommand", ast.E, ast.C);
  }
  
  
  public Object visitForCommand(ForCommand ast, Object obj) {
    return xmlQuaternary("UntilCommand",ast.I, ast.E, ast.E2, ast.C);
  }
  
 public Object visitForUntilCommand(ForUntilCommand ast, Object obj) {
    return xmlFive("UntilCommand",ast.I, ast.E, ast.E2, ast.E2, ast.C);
  }
 
  public Object visitForWhileCommand(ForWhileCommand ast, Object obj) {
    return xmlFive("UntilCommand",ast.I, ast.E, ast.E2, ast.E2, ast.C);
  }
  


  // Expressions
  public Object visitArrayExpression(ArrayExpression ast, Object obj) {
    return xmlUnary("ArrayExpression", ast.AA);
  }

  public Object visitBinaryExpression(BinaryExpression ast, Object obj) {
    return xmlTernary("BinaryExpression", ast.E1, ast.O, ast.E2);
  }

  public Object visitCallExpression(CallExpression ast, Object obj) {
    return xmlBinary("CallExpression", ast.I, ast.APS);
  }

  public Object visitCharacterExpression(CharacterExpression ast, Object obj) {
    return xmlUnary("CharacterExpression", ast.CL);
  }

  public Object visitEmptyExpression(EmptyExpression ast, Object obj) {
    return xmlNullary("EmptyExpression");
  }

  public Object visitIfExpression(IfExpression ast, Object obj) {
    return xmlTernary("IfExpression", ast.E1, ast.E2, ast.E3);
  }

  public Object visitIntegerExpression(IntegerExpression ast, Object obj) {
    return xmlUnary("IntegerExpression", ast.IL);
  }

  public Object visitLetExpression(LetExpression ast, Object obj) {
    return xmlBinary("LetExpression", ast.D, ast.E);
  }

  public Object visitRecordExpression(RecordExpression ast, Object obj) {
    return xmlUnary("RecordExpression", ast.RA);
  }

  public Object visitUnaryExpression(UnaryExpression ast, Object obj) {
    return xmlBinary("UnaryExpression", ast.O, ast.E);
  }

  public Object visitVnameExpression(VnameExpression ast, Object obj) {
    return xmlUnary("VnameExpression", ast.V);
  }


  // Declarations
  public Object visitBinaryOperatorDeclaration(BinaryOperatorDeclaration ast, Object obj) {
    return xmlQuaternary("BinaryOperatorDeclaration.", ast.O, ast.ARG1, ast.ARG2, ast.RES);
  }

  public Object visitConstDeclaration(ConstDeclaration ast, Object obj) {
    return xmlBinary("ConstDeclaration", ast.I, ast.E);
  }

  public Object visitFuncDeclaration(FuncDeclaration ast, Object obj) {
    return xmlQuaternary("FuncDeclaration", ast.I, ast.FPS, ast.T, ast.E);
  }

  public Object visitProcDeclaration(ProcDeclaration ast, Object obj) {
    return xmlTernary("ProcDeclaration", ast.I, ast.FPS, ast.C);
  }

  public Object visitSequentialDeclaration(SequentialDeclaration ast, Object obj) {
    return xmlBinary("SequentialDeclaration", ast.D1, ast.D2);
  }

  public Object visitTypeDeclaration(TypeDeclaration ast, Object obj) {
    return xmlBinary("TypeDeclaration", ast.I, ast.T);
  }

  public Object visitUnaryOperatorDeclaration(UnaryOperatorDeclaration ast, Object obj) {
    return xmlTernary("UnaryOperatorDeclaration", ast.O, ast.ARG, ast.RES);
  }

  public Object visitVarDeclaration(VarDeclaration ast, Object obj) {
    return xmlBinary("VarDeclaration", ast.I, ast.T);
  }


  // Array Aggregates
  public Object visitMultipleArrayAggregate(MultipleArrayAggregate ast, Object obj) {
    return xmlBinary("MultipleArrayAggregate", ast.E, ast.AA);
  }

  public Object visitSingleArrayAggregate(SingleArrayAggregate ast, Object obj) {
    return xmlUnary("SingleArrayAggregate", ast.E);
  }


  // Record Aggregates
  public Object visitMultipleRecordAggregate(MultipleRecordAggregate ast, Object obj) {
    return xmlTernary("MultipleRecordAggregate", ast.I, ast.E, ast.RA);
  }

  public Object visitSingleRecordAggregate(SingleRecordAggregate ast, Object obj) {
    return xmlBinary("SingleRecordAggregate", ast.I, ast.E);
  }


  // Formal Parameters
  public Object visitConstFormalParameter(ConstFormalParameter ast, Object obj) {
    return xmlBinary("ConstFormalParameter", ast.I, ast.T);
  }

  public Object visitFuncFormalParameter(FuncFormalParameter ast, Object obj) {
    return xmlTernary("FuncFormalParameter", ast.I, ast.FPS, ast.T);
  }

  public Object visitProcFormalParameter(ProcFormalParameter ast, Object obj) {
    return xmlBinary("ProcFormalParameter", ast.I, ast.FPS);
  }

  public Object visitVarFormalParameter(VarFormalParameter ast, Object obj) {
    return xmlBinary("VarFormalParameter", ast.I, ast.T);
  }


  public Object visitEmptyFormalParameterSequence(EmptyFormalParameterSequence ast, Object obj) {
    return xmlNullary("EmptyFormalParameterSequence");
  }

  public Object visitMultipleFormalParameterSequence(MultipleFormalParameterSequence ast, Object obj) {
    return xmlBinary("MultipleFormalParameterSequence", ast.FP, ast.FPS);
  }

  public Object visitSingleFormalParameterSequence(SingleFormalParameterSequence ast, Object obj) {
    return xmlUnary("SingleFormalParameterSequence", ast.FP);
  }


  // Actual Parameters
  public Object visitConstActualParameter(ConstActualParameter ast, Object obj) {
    return xmlUnary("ConstActualParameter", ast.E);
  }

  public Object visitFuncActualParameter(FuncActualParameter ast, Object obj) {
    return xmlUnary("FuncActualParameter", ast.I);
  }

  public Object visitProcActualParameter(ProcActualParameter ast, Object obj) {
    return xmlUnary("ProcActualParameter", ast.I);
  }

  public Object visitVarActualParameter(VarActualParameter ast, Object obj) {
    return xmlUnary("VarActualParameter", ast.V);
  }


  public Object visitEmptyActualParameterSequence(EmptyActualParameterSequence ast, Object obj) {
    return xmlNullary("EmptyActualParameterSequence");
  }

  public Object visitMultipleActualParameterSequence(MultipleActualParameterSequence ast, Object obj) {
    return xmlBinary("MultipleActualParameterSequence", ast.AP, ast.APS);
  }

  public Object visitSingleActualParameterSequence(SingleActualParameterSequence ast, Object obj) {
    return xmlUnary("SingleActualParameterSequence", ast.AP);
  }


  // Type Denoters
  public Object visitAnyTypeDenoter(AnyTypeDenoter ast, Object obj) {
    return xmlNullary("AnyTypeDenoter");
  }

  public Object visitArrayTypeDenoter(ArrayTypeDenoter ast, Object obj) {
    return xmlBinary("ArrayTypeDenoter", ast.IL, ast.T);
  }

  public Object visitBoolTypeDenoter(BoolTypeDenoter ast, Object obj) {
    return xmlNullary("BoolTypeDenoter");
  }

  public Object visitCharTypeDenoter(CharTypeDenoter ast, Object obj) {
    return xmlNullary("CharTypeDenoter");
  }

  public Object visitErrorTypeDenoter(ErrorTypeDenoter ast, Object obj) {
    return xmlNullary("ErrorTypeDenoter");
  }

  public Object visitSimpleTypeDenoter(SimpleTypeDenoter ast, Object obj) {
    return xmlUnary("SimpleTypeDenoter", ast.I);
  }

  public Object visitIntTypeDenoter(IntTypeDenoter ast, Object obj) {
    return xmlNullary("IntTypeDenoter");
  }

  public Object visitRecordTypeDenoter(RecordTypeDenoter ast, Object obj) {
    return xmlUnary("RecordTypeDenoter", ast.FT);
  }


  public Object visitMultipleFieldTypeDenoter(MultipleFieldTypeDenoter ast, Object obj) {
    return xmlTernary("MultipleFieldTypeDenoter", ast.I, ast.T, ast.FT);
  }

  public Object visitSingleFieldTypeDenoter(SingleFieldTypeDenoter ast, Object obj) {
    return xmlBinary("SingleFieldTypeDenoter", ast.I, ast.T);
  }


  // Literals, Identifiers and Operators
  public Object visitCharacterLiteral(CharacterLiteral ast, Object obj) {
    return xmlNullary("Character",ast.spelling);
  }

  public Object visitIdentifier(Identifier ast, Object obj) {
    return xmlNullary("Identifier",ast.spelling);
 }
  
  public Object visitLongIdentifier(LongIdentifier ast, Object obj) {
    return xmlNullary("LongIdentifier",ast.P.spelling +"$"+ast.spelling);
 }
  
  public Object visitPackageIdentifier(PackageIdentifier ast, Object obj) {
    return xmlNullary("PackageIdentifier",ast.spelling);
 }
  public Object visitIntegerLiteral(IntegerLiteral ast, Object obj) {
    //System.out.println(ast.spelling);
    return xmlNullary("Integer",ast.spelling);
  }

  public Object visitOperator(Operator ast, Object obj) {
    return xmlNullary("Operator",ast.spelling);
  }


  // Value-or-variable names
  public Object visitDotVname(DotVname ast, Object obj) {
    return xmlBinary("DotVname", ast.I, ast.V);
  }

  public Object visitSimpleVname(SimpleVname ast, Object obj) {
    return xmlUnary("SimpleVname", ast.I);
  }

  public Object visitSubscriptVname(SubscriptVname ast, Object obj) {
    return xmlBinary("SubscriptVname",
        ast.V, ast.E);
  }


  // Programs
  public Object visitProgram(Program ast, Object obj) {
    if(ast.D == null)
      return xmlUnary("Program", ast.C);
    return xmlBinary("Program",ast.D, ast.C);
  }

  // Package
  public Object visitSinglePackageDeclaration(SinglePackageDeclaration ast, Object obj) {
    return xmlBinary("SinglePackageDeclaration",ast.I, ast.D);
  }
   
   public Object visitSequentialPackageDeclaration(SequentialPackageDeclaration ast, Object obj) {
    return xmlBinary("SequentialPackageDeclaration", ast.D, ast.D2);
  }
  
  //xml element generating functions
  private Element xmlCaption(String name){
       Element generatedElement = document.createElement(name);
       return generatedElement;
  }

  private Element xmlNullary(String name, String value){
       Element generatedElement = document.createElement(name);
       generatedElement.setTextContent(value);
       return generatedElement;
  }
  
  private Element xmlNullary(String name){
       Element generatedElement = document.createElement(name);
       return generatedElement;
  }

  private Element xmlUnary(String name, AST child1){
       Element generatedElement = xmlCaption(name);
       Element childElmenet = (Element) child1.visit(this, null);
       generatedElement.appendChild(childElmenet);
       return generatedElement;
  }

  private Element xmlBinary(String name, AST child1, AST child2){
       Element generatedElement = xmlCaption(name);
       Element childElmenet = (Element) child1.visit(this, null);
       Element childElmenet2 = (Element) child2.visit(this, null);
       generatedElement.appendChild(childElmenet);
       generatedElement.appendChild(childElmenet2);
       return generatedElement;
  }

  private Element xmlTernary(String name, AST child1, AST child2,
                                     AST child3){
       Element generatedElement = xmlCaption(name);
       Element childElmenet = (Element) child1.visit(this, null);
       Element childElmenet2 = (Element) child2.visit(this, null);
       Element childElmenet3 = (Element) child3.visit(this, null);
       generatedElement.appendChild(childElmenet);
       generatedElement.appendChild(childElmenet2);
       generatedElement.appendChild(childElmenet3);
       return generatedElement;
  }

    private Element xmlQuaternary(String name, AST child1, AST child2,
                                     AST child3, AST child4){
       Element generatedElement = xmlCaption(name);
       Element childElmenet = (Element) child1.visit(this, null);
       Element childElmenet2 = (Element) child2.visit(this, null);
       Element childElmenet3 = (Element) child3.visit(this, null);
       Element childElmenet4 = (Element) child4.visit(this, null);
       generatedElement.appendChild(childElmenet);
       generatedElement.appendChild(childElmenet2);
       generatedElement.appendChild(childElmenet3);
       generatedElement.appendChild(childElmenet4);
       return generatedElement;
  }
    
    private Element xmlFive(String name, AST child1, AST child2,
                                     AST child3, AST child4, AST child5){
       Element generatedElement = xmlCaption(name);
       Element childElmenet = (Element) child1.visit(this, null);
       Element childElmenet2 = (Element) child2.visit(this, null);
       Element childElmenet3 = (Element) child3.visit(this, null);
       Element childElmenet4 = (Element) child4.visit(this, null);
       Element childElmenet5 = (Element) child5.visit(this, null);
       generatedElement.appendChild(childElmenet);
       generatedElement.appendChild(childElmenet2);
       generatedElement.appendChild(childElmenet3);
       generatedElement.appendChild(childElmenet4);
       generatedElement.appendChild(childElmenet5);
       return generatedElement;
  }

        @Override
    public Object visitCaseLiteral(CaseLiteral aThis, Object o) {
        if(aThis.caselite != null)
            return xmlBinary("CaseLiteral", aThis.caselite,aThis.caselite2);
        return xmlUnary("CaseLiteral",aThis.caselite2);
    }
        @Override
    public Object ProcFuncDeclaration(Triangle.AbstractSyntaxTrees.ProcFuncDeclaration aThis, Object o) {
        if(aThis.D2!=null) 
            return xmlBinary("ProcFuncDeclaration", aThis.D1, aThis.D2);
        return xmlUnary("ProcFuncDeclaration", aThis.D1);
    }

    @Override
    public Object visitChooseCommand(ChooseCommand aThis, Object o) {
        return xmlBinary("ChooseCommand", aThis.E, aThis.C);
    }

    @Override
    public Object visitCaseRange(CaseRange aThis, Object o) {
       return xmlBinary("CaseRange", aThis.caseRange,aThis.caseRange2);
    }

    @Override
    public Object visitComCase(ComCase aThis, Object o) {
        return xmlBinary("Case", aThis.CL, aThis.C);
    }

    @Override
    public Object visitElseCase(ElseCase aThis, Object o) {
        return xmlUnary("ElseCase", aThis.C1);
    }

    @Override
    public Object visitSCase(SCase aThis, Object o) {
        return xmlBinary("SequencialCase", aThis.C1, aThis.C2);
    }
    @Override
    public Object privateDeclaration(Triangle.AbstractSyntaxTrees.PrivateDeclaration aThis, Object o) {
        return xmlBinary("PrivateDeclaration", aThis.D1, aThis.D2);
        
    }

    @Override
    public Object ParDeclaration(Triangle.AbstractSyntaxTrees.ParDeclaration aThis, Object o) {
        return xmlBinary("ParDeclaration", aThis.D1, aThis.D2);
        
    }



    @Override
    public Object RecursiveDeclaration(Triangle.AbstractSyntaxTrees.RecursiveDeclaration aThis, Object o) {
        return xmlUnary("ProcFuncDeclaration", aThis.D1);
    }
  @Override
    public Object visitVarADeclaration(VarADeclaration aThis, Object o) {
        return xmlBinary("VarAsDeclaration", aThis.I, aThis.E);
    }

    @Override
    public Object visitLitIntegerExpression(LitIntegerExpression aThis, Object o) {
        return xmlUnary("litIntegerExpression", aThis.IL);
    }


    @Override
    public Object visitLiteralCharacterExpression(LitCharacterExpression aThis, Object o) {
        return xmlUnary("litCharacterExpression", aThis.CL);
    }
}