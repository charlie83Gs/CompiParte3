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
public class CaseLiteral extends Expression {

    public Expression caselite2;
    public Expression caselite;
  
    
    public CaseLiteral(Expression eAST,Expression cslAST,SourcePosition thePosition) {
        super(thePosition);
         caselite=cslAST;
         caselite2=eAST;
         
    }


    public Object visit(Visitor v, Object o) {
        return v.visitCaseLiteral(this, o);
    }
    

    
}
