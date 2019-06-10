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
public class VarADeclaration extends Declaration{
      public VarADeclaration (Identifier iAST, Expression tAST,
                         SourcePosition thePosition) {
    super (thePosition);
    I = iAST;
    E = tAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitVarADeclaration(this, o);
  }

  public Identifier I;
  public Expression E;
}
