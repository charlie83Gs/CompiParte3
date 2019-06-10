/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

/**
 *
 * @author vicnb
 */
public class LitIntegerExpression extends Expression {

  public LitIntegerExpression (IntegerLiteral ilAST, SourcePosition thePosition) {
    super (thePosition);
    IL = ilAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitLitIntegerExpression(this, o);
  }

  public IntegerLiteral IL;
}
