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
public class ElseCase extends Command{
    public ElseCase (Command c1AST, SourcePosition thePosition) {
    super (thePosition);
    C1 = c1AST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitElseCase(this, o);
  }

  public Command C1;
    
}
