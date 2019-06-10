/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Triangle.AbstractSyntaxTrees;

import Triangle.AbstractSyntaxTrees.CharacterLiteral;
import Triangle.AbstractSyntaxTrees.Expression;
import Triangle.SyntacticAnalyzer.SourcePosition;

/**
 *
 * @author vicnb
 */
public class LitCharacterExpression extends Expression {
   
  public LitCharacterExpression (CharacterLiteral clAST, SourcePosition thePosition) {
    super (thePosition);
    CL = clAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitLiteralCharacterExpression(this, o);
  }

  public CharacterLiteral CL;
    
}
