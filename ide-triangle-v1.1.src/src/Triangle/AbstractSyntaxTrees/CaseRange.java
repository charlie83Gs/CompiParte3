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
public class CaseRange extends Expression {
        public Expression caseRange;
    public Expression caseRange2;
  
    
    public CaseRange(Expression cr1AST,Expression cr2AST,SourcePosition thePosition) {
        super(thePosition);
         caseRange=cr1AST;
         caseRange2=cr2AST;
         
    }


    public Object visit(Visitor v, Object o) {
        return v.visitCaseRange(this, o);
    }
    

}
