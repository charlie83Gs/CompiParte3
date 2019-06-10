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
public class RecursiveDeclaration extends Declaration{
    public RecursiveDeclaration (Declaration d1AST, SourcePosition thePosition) {
    super (thePosition);
    D1 = d1AST;
  
  }

  public Object visit(Visitor v, Object o) {
    return v.RecursiveDeclaration(this, o);
  }

  public Declaration D1;
    
    
}
