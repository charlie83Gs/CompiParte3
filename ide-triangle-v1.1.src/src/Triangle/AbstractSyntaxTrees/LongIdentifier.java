
package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class LongIdentifier extends Identifier {

  //pAST means packageIdentifier AST
  public LongIdentifier (PackageIdentifier pAST,String theSpelling, SourcePosition thePosition) {
    super (theSpelling, thePosition);
    P = pAST;
    type = null;
    decl = null;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitLongIdentifier(this, o);
  }

  public TypeDenoter type;
  public AST decl; // Either a Declaration or a FieldTypeDenoter
  public PackageIdentifier P; // Either a Declaration or a FieldTypeDenoter
}
