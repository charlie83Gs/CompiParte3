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
public class ChooseCommand extends Command{
    
    public ChooseCommand (Expression eAST,Command c1AST, SourcePosition thePosition) {
    super (thePosition);
    C = c1AST;
    E=eAST;
  }
        

  public Object visit(Visitor v, Object o) {
    return v.visitChooseCommand(this, o);
  }

  
  public Command C;
  public Expression E;
    
    
}
