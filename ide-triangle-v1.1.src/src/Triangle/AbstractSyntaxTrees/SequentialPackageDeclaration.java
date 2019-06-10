


package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class SequentialPackageDeclaration extends PackageDeclaration {

  public SequentialPackageDeclaration (PackageDeclaration dAST,PackageDeclaration d2AST,SourcePosition thePosition) {
    super (thePosition);
    D = dAST;
    D2 = d2AST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitSequentialPackageDeclaration(this, o);
  }
  
  public PackageDeclaration D;
  public PackageDeclaration D2;
}