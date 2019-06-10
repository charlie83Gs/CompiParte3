/*
 * @(#)Parser.java                        2.1 2003/10/07
 *
 * Copyright (C) 1999, 2003 D.A. Watt and D.F. Brown
 * Dept. of Computing Science, University of Glasgow, Glasgow G12 8QQ Scotland
 * and School of Computer and Math Sciences, The Robert Gordon University,
 * St. Andrew Street, Aberdeen AB25 1HG, Scotland.
 * All rights reserved.
 *
 * This software is provided free for educational use only. It may
 * not be used for commercial purposes without the prior written permission
 * of the authors.
 */

package Triangle.SyntacticAnalyzer;

import Triangle.ErrorReporter;
import Triangle.AbstractSyntaxTrees.ActualParameter;
import Triangle.AbstractSyntaxTrees.ActualParameterSequence;
import Triangle.AbstractSyntaxTrees.ArrayAggregate;
import Triangle.AbstractSyntaxTrees.ArrayExpression;
import Triangle.AbstractSyntaxTrees.ArrayTypeDenoter;
import Triangle.AbstractSyntaxTrees.AssignCommand;
import Triangle.AbstractSyntaxTrees.BinaryExpression;
import Triangle.AbstractSyntaxTrees.CallCommand;
import Triangle.AbstractSyntaxTrees.CallExpression;
import Triangle.AbstractSyntaxTrees.CaseLiteral;
import Triangle.AbstractSyntaxTrees.CaseRange;
import Triangle.AbstractSyntaxTrees.CharacterExpression;
import Triangle.AbstractSyntaxTrees.CharacterLiteral;
import Triangle.AbstractSyntaxTrees.ChooseCommand;
import Triangle.AbstractSyntaxTrees.ComCase;
import Triangle.AbstractSyntaxTrees.Command;
import Triangle.AbstractSyntaxTrees.ConstActualParameter;
import Triangle.AbstractSyntaxTrees.ConstDeclaration;
import Triangle.AbstractSyntaxTrees.ConstFormalParameter;
import Triangle.AbstractSyntaxTrees.Declaration;
import Triangle.AbstractSyntaxTrees.DoUntilCommand;
import Triangle.AbstractSyntaxTrees.DoWhileCommand;
import Triangle.AbstractSyntaxTrees.DotVname;
import Triangle.AbstractSyntaxTrees.ElseCase;
import Triangle.AbstractSyntaxTrees.EmptyActualParameterSequence;
import Triangle.AbstractSyntaxTrees.EmptyCommand;
import Triangle.AbstractSyntaxTrees.EmptyFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.Expression;
import Triangle.AbstractSyntaxTrees.FieldTypeDenoter;
import Triangle.AbstractSyntaxTrees.ForCommand;
import Triangle.AbstractSyntaxTrees.ForWhileCommand;
import Triangle.AbstractSyntaxTrees.FormalParameter;
import Triangle.AbstractSyntaxTrees.FormalParameterSequence;
import Triangle.AbstractSyntaxTrees.FuncActualParameter;
import Triangle.AbstractSyntaxTrees.FuncDeclaration;
import Triangle.AbstractSyntaxTrees.FuncFormalParameter;
import Triangle.AbstractSyntaxTrees.Identifier;
import Triangle.AbstractSyntaxTrees.IfCommand;
import Triangle.AbstractSyntaxTrees.IfExpression;
import Triangle.AbstractSyntaxTrees.IntegerExpression;
import Triangle.AbstractSyntaxTrees.IntegerLiteral;
import Triangle.AbstractSyntaxTrees.LetCommand;
import Triangle.AbstractSyntaxTrees.LetExpression;
import Triangle.AbstractSyntaxTrees.LitIntegerExpression;
import Triangle.AbstractSyntaxTrees.LitCharacterExpression;
import Triangle.AbstractSyntaxTrees.LongIdentifier;
import Triangle.AbstractSyntaxTrees.MultipleActualParameterSequence;
import Triangle.AbstractSyntaxTrees.MultipleArrayAggregate;
import Triangle.AbstractSyntaxTrees.MultipleFieldTypeDenoter;
import Triangle.AbstractSyntaxTrees.MultipleFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.MultipleRecordAggregate;
import Triangle.AbstractSyntaxTrees.Operator;
import Triangle.AbstractSyntaxTrees.PackageDeclaration;
import Triangle.AbstractSyntaxTrees.PackageIdentifier;
import Triangle.AbstractSyntaxTrees.ParDeclaration;
import Triangle.AbstractSyntaxTrees.ProcActualParameter;
import Triangle.AbstractSyntaxTrees.ProcDeclaration;
import Triangle.AbstractSyntaxTrees.ProcFormalParameter;
import Triangle.AbstractSyntaxTrees.Program;
import Triangle.AbstractSyntaxTrees.RecordAggregate;
import Triangle.AbstractSyntaxTrees.RecordExpression;
import Triangle.AbstractSyntaxTrees.RecordTypeDenoter;
import Triangle.AbstractSyntaxTrees.SCase;
import Triangle.AbstractSyntaxTrees.SequentialCommand;
import Triangle.AbstractSyntaxTrees.SequentialDeclaration;
import Triangle.AbstractSyntaxTrees.SequentialPackageDeclaration;
import Triangle.AbstractSyntaxTrees.SimpleTypeDenoter;
import Triangle.AbstractSyntaxTrees.SimpleVname;
import Triangle.AbstractSyntaxTrees.SingleActualParameterSequence;
import Triangle.AbstractSyntaxTrees.SingleArrayAggregate;
import Triangle.AbstractSyntaxTrees.SingleFieldTypeDenoter;
import Triangle.AbstractSyntaxTrees.SingleFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.SinglePackageDeclaration;
import Triangle.AbstractSyntaxTrees.SingleRecordAggregate;
import Triangle.AbstractSyntaxTrees.SubscriptVname;
import Triangle.AbstractSyntaxTrees.TypeDeclaration;
import Triangle.AbstractSyntaxTrees.TypeDenoter;
import Triangle.AbstractSyntaxTrees.UnaryExpression;
import Triangle.AbstractSyntaxTrees.UntilCommand;
import Triangle.AbstractSyntaxTrees.VarActualParameter;
import Triangle.AbstractSyntaxTrees.VarDeclaration;
import Triangle.AbstractSyntaxTrees.VarFormalParameter;
import Triangle.AbstractSyntaxTrees.Vname;
import Triangle.AbstractSyntaxTrees.VnameExpression;
import Triangle.AbstractSyntaxTrees.WhileCommand;
import Triangle.AbstractSyntaxTrees.PrivateDeclaration;
import Triangle.AbstractSyntaxTrees.ProcFuncDeclaration;
import Triangle.AbstractSyntaxTrees.RecursiveDeclaration;
import Triangle.AbstractSyntaxTrees.VarADeclaration;

public class Parser {

  private Scanner lexicalAnalyser;
  private ErrorReporter errorReporter;
  private Token currentToken;
  private SourcePosition previousTokenPosition;

  public Parser(Scanner lexer, ErrorReporter reporter) {
    lexicalAnalyser = lexer;
    errorReporter = reporter;
    previousTokenPosition = new SourcePosition();
  }

// accept checks whether the current token matches tokenExpected.
// If so, fetches the next token.
// If not, reports a syntactic error.

  void accept (int tokenExpected) throws SyntaxError {
    if (currentToken.kind == tokenExpected) {
      previousTokenPosition = currentToken.position;
      currentToken = lexicalAnalyser.scan();
    } else {
      syntacticError("\"%\" expected here", Token.spell(tokenExpected));
    }
  }

  void acceptIt() {
    previousTokenPosition = currentToken.position;
    currentToken = lexicalAnalyser.scan();
  }

// start records the position of the start of a phrase.
// This is defined to be the position of the first
// character of the first token of the phrase.

  void start(SourcePosition position) {
    position.start = currentToken.position.start;
  }

// finish records the position of the end of a phrase.
// This is defined to be the position of the last
// character of the last token of the phrase.

  void finish(SourcePosition position) {
    position.finish = previousTokenPosition.finish;
  }

  void syntacticError(String messageTemplate, String tokenQuoted) throws SyntaxError {
    SourcePosition pos = currentToken.position;
    errorReporter.reportError(messageTemplate, tokenQuoted, pos);
    throw(new SyntaxError());
  }

///////////////////////////////////////////////////////////////////////////////
//
// PROGRAMS
//
///////////////////////////////////////////////////////////////////////////////

  public Program parseProgram() {
   //System.out.println("Parsing program...");
    Program programAST = null;

    previousTokenPosition.start = 0;
    previousTokenPosition.finish = 0;
    currentToken = lexicalAnalyser.scan();
    
    try {
      //System.out.println("Parsing program...");
      PackageDeclaration programPackage = null;
      if(currentToken.kind == Token.PACKAGE){
          programPackage = parsePackageDeclaration();
          System.out.println("valid package: " + (programPackage == null));
          
      }
      Command cAST = parseCommand();
      programAST = new Program(programPackage,cAST, previousTokenPosition);
      if (currentToken.kind != Token.EOT) {
        syntacticError("\"%\" not expected after end of program",
          currentToken.spelling);
      }
    }
    catch (SyntaxError s) { 
        System.out.println("Error parsing program " + s.toString());
        return null;
    }catch(Exception e){
        System.out.println("Error parsing program " + e.toString());
        return null;
    }
    
    return programAST;
  }
  
  public PackageDeclaration parsePackageDeclaration() throws SyntaxError {
    PackageDeclaration packageAST = null;      

    SourcePosition commandPos = new SourcePosition();

    start(commandPos);
    packageAST = parseSinglePackageDeclaration();
    while (currentToken.kind == Token.PACKAGE) {
      //acceptIt();
      //do not consume package token is needed in parse single package
      PackageDeclaration secondPackageDeclaration = parseSinglePackageDeclaration();
      finish(commandPos);
      packageAST = new SequentialPackageDeclaration(packageAST, secondPackageDeclaration, commandPos);
    }
      return packageAST;
  }
  
   public SinglePackageDeclaration parseSinglePackageDeclaration() throws SyntaxError {
      SinglePackageDeclaration packageAST = null;
      SourcePosition packagePos = new SourcePosition();
      start(packagePos);
      accept(Token.PACKAGE);
      PackageIdentifier piAst = parsePackageIdentifier();
      accept(Token.IS);
      Declaration dAST = parseDeclaration();
      accept(Token.END);
      accept(Token.SEMICOLON);
      finish(packagePos);
      packageAST = new SinglePackageDeclaration(piAst,dAST, packagePos);
      
      
      return packageAST;
  }


///////////////////////////////////////////////////////////////////////////////
//
// LITERALS
//
///////////////////////////////////////////////////////////////////////////////

// parseIntegerLiteral parses an integer-literal, and constructs
// a leaf AST to represent it.

  IntegerLiteral parseIntegerLiteral() throws SyntaxError {
    IntegerLiteral IL = null;

    if (currentToken.kind == Token.INTLITERAL) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      IL = new IntegerLiteral(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      IL = null;
      syntacticError("integer literal expected here", "");
    }
    return IL;
  }

// parseCharacterLiteral parses a character-literal, and constructs a leaf
// AST to represent it.

  CharacterLiteral parseCharacterLiteral() throws SyntaxError {
    CharacterLiteral CL = null;

    if (currentToken.kind == Token.CHARLITERAL) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      CL = new CharacterLiteral(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      CL = null;
      syntacticError("character literal expected here", "");
    }
    return CL;
  }

// parseIdentifier parses an identifier, and constructs a leaf AST to
// represent it.

  Identifier parseIdentifier() throws SyntaxError {
    Identifier I = null;
    SourcePosition identifierPos = new SourcePosition();
    start(identifierPos);
    if (currentToken.kind == Token.IDENTIFIER) {
      String spelling = currentToken.spelling;
      System.out.println("current Token "+ currentToken.kind );
      acceptIt();
      System.out.println("current Token - "+ currentToken.kind );
      if(currentToken.kind == Token.DOLAR){
          System.out.println("Long Identifier");
          acceptIt();
          PackageIdentifier piAST = parsePackageIdentifier();
          finish(identifierPos);
          I = new LongIdentifier(piAST,spelling, identifierPos);
          System.out.println("Current Token "+ currentToken.kind );
      }else{
          System.out.println("Normal Identifier "+ currentToken.kind );
          finish(identifierPos);
          I = new Identifier(spelling, identifierPos);
      }
    } else {
      I = null;
      syntacticError("identifier expected here", "");
    }
    return I;
  }
  
 Identifier parseSingleIdentifier() throws SyntaxError {
    Identifier I = null;

      if (currentToken.kind == Token.IDENTIFIER) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      I = new Identifier(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      I = null;
      syntacticError("identifier expected here", "");
    }
    
    return I;
  }
  
  PackageIdentifier parsePackageIdentifier() throws SyntaxError {
    PackageIdentifier I = null;


      if (currentToken.kind == Token.IDENTIFIER) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      I = new PackageIdentifier(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      I = null;
      syntacticError("identifier expected here", "");
    }
    
    return I;
  }
// parseOperator parses an operator, and constructs a leaf AST to
// represent it.

  Operator parseOperator() throws SyntaxError {
    Operator O = null;

    if (currentToken.kind == Token.OPERATOR) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      O = new Operator(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      O = null;
      syntacticError("operator expected here", "");
    }
    return O;
  }

///////////////////////////////////////////////////////////////////////////////
//
// COMMANDS
//
///////////////////////////////////////////////////////////////////////////////

// parseCommand parses the command, and constructs an AST
// to represent its phrase structure.

  Command parseCommand() throws SyntaxError {
    Command commandAST = null; // in case there's a syntactic error

    SourcePosition commandPos = new SourcePosition();

    start(commandPos);
    commandAST = parseSingleCommand();
    while (currentToken.kind == Token.SEMICOLON) {
      acceptIt();
      Command c2AST = parseSingleCommand();
      finish(commandPos);
      commandAST = new SequentialCommand(commandAST, c2AST, commandPos);
    }
    return commandAST;
  }

  
  Command parseSingleCommand() throws SyntaxError {
    Command commandAST = null; // in case there's a syntactic error

    SourcePosition commandPos = new SourcePosition();
    start(commandPos);

    switch (currentToken.kind) {

    case Token.IDENTIFIER:
      {
        System.out.println("CHOOSE ENTRO A IDENTIFIER");
        Identifier iAST = parseIdentifier();
        if (currentToken.kind == Token.LPAREN) {
          acceptIt();
          ActualParameterSequence apsAST = parseActualParameterSequence();
          accept(Token.RPAREN);
          finish(commandPos);
          commandAST = new CallCommand(iAST, apsAST, commandPos);

        } else {

          Vname vAST = parseRestOfVname(iAST);
          accept(Token.BECOMES);
          Expression eAST = parseExpression();
          finish(commandPos);
          commandAST = new AssignCommand(vAST, eAST, commandPos);
        }
      }
      break;

    case Token.PASS:
    {
        acceptIt();
        commandAST= new EmptyCommand(commandPos);
        break;
    }
    
    case Token.LOOP:
    {
        acceptIt();
        switch(currentToken.kind){
            case Token.WHILE:
            {
                acceptIt();
                Expression eAST = parseExpression();
                accept(Token.DO);
                Command cAST = parseCommand();
                accept(Token.END);
                commandAST = new WhileCommand(eAST, cAST, commandPos);
                break;
                        
            }
            case Token.UNTIL:
                {
                acceptIt();
                Expression eAST = parseExpression();
                accept(Token.DO);
                Command cAST = parseCommand();
                accept(Token.END);
                
                commandAST = new UntilCommand(eAST, cAST, commandPos);
                break;
                        
            }
            case Token.DO:
            {
                acceptIt();
                Command cAST = parseCommand();
                switch(currentToken.kind){
                    case Token.WHILE:
                    {
                        acceptIt();
                        Expression eAST = parseExpression();
                        accept(Token.END);
                        commandAST = new DoWhileCommand(cAST,eAST, commandPos);
                        break;
                    }
                    case Token.UNTIL:
                    {
                        acceptIt();
                        Expression eAST = parseExpression();
                        accept(Token.END);
                        commandAST = new DoUntilCommand(cAST,eAST, commandPos);
                        break;
                    }
                }
                
                        
             
                break;
            }
            case Token.FOR:
            {
                acceptIt();
                Identifier iAST=parseIdentifier();
                accept(Token.FROM);
                Expression eAST = parseExpression();
                accept(Token.TO);
                Expression eAST1 = parseExpression();
                switch(currentToken.kind){
                    case Token.DO:{
                        acceptIt();
                        Command cAST = parseCommand();
                        accept(Token.END);
                        commandAST = new ForCommand(iAST,eAST,eAST1,cAST, commandPos);
                        break;
                    }
                    case Token.WHILE:{
                        acceptIt();
                        Expression eAST2= parseExpression();
                        accept(Token.DO);
                        Command c1AST = parseCommand();
                        accept(Token.END);
                        commandAST = new ForWhileCommand(iAST,eAST,eAST1,eAST2,c1AST, commandPos);
                        break;
                    }
                    case Token.UNTIL:{
                        acceptIt();
                        Expression eAST2 =parseExpression();
                        accept(Token.DO);
                        Command c2AST = parseCommand();
                        accept(Token.END);
                        commandAST = new ForWhileCommand(iAST,eAST,eAST1,eAST2,c2AST, commandPos);
                    }
                            
                      
                }
                break;
                
            }
                    
            
        }
        break;
    }
    case Token.LET:
    {
        acceptIt();
        System.out.println("Entra a LET");
        Declaration dAST = parseDeclaration();
        
        accept(Token.IN);
        Command cAST = parseCommand();
        accept(Token.END);
        commandAST = new LetCommand(dAST, cAST, commandPos);
        break;
    }
    case Token.IF:
    {
        acceptIt();
        Expression eAST= parseExpression();
        accept(Token.THEN);
        Command cAST= parseCommand();
        accept(Token.ELSE);
        Command c1AST = parseCommand();
        accept(Token.END);
        commandAST = new IfCommand(eAST, cAST,c1AST, commandPos);
        break;
    }
    
    case Token.CHOOSE:
    {
        System.out.println("ENTROOOOOOO");
        acceptIt();
        Expression eAST=parseExpression();
        accept(Token.FROM);
        //falta ParserCases
        Command casAST=parseCases();
        accept(Token.END);
        commandAST=new ChooseCommand(eAST, casAST, commandPos);
        break;
    }
//    case Token.BEGIN:
//      acceptIt();
//      commandAST = parseCommand();
//      accept(Token.END);
//      break;

//    case Token.LET:
//      {
//        acceptIt();
//        Declaration dAST = parseDeclaration();
//        accept(Token.IN);
//        Command cAST = parseSingleCommand();
//        finish(commandPos);
//        commandAST = new LetCommand(dAST, cAST, commandPos);
//      }
//      break;

//    case Token.IF:
//      {
//        acceptIt();
//        Expression eAST = parseExpression();
//        accept(Token.THEN);
//        Command c1AST = parseSingleCommand();
//        accept(Token.ELSE);
//        Command c2AST = parseSingleCommand();
//        finish(commandPos);
//        commandAST = new IfCommand(eAST, c1AST, c2AST, commandPos);
//      }
//      break;

//    case Token.WHILE:
//      {
//        acceptIt();
//        Expression eAST = parseExpression();
//        accept(Token.DO);
//        Command cAST = parseSingleCommand();
//        finish(commandPos);
//        commandAST = new WhileCommand(eAST, cAST, commandPos);
//      }
//      break;

  //  case Token.SEMICOLON:
  //  case Token.END:
  //  case Token.ELSE:
  //  case Token.IN:
   //   case Token.EOT:

   //   finish(commandPos);
   //   commandAST = new EmptyCommand(commandPos);
   //   break;

    default:
      syntacticError("\"%\" cannot start a command",
        currentToken.spelling);
      break;

    }

    return commandAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// CASES
//
///////////////////////////////////////////////////////////////////////////////3
  
  Command parseCases()throws SyntaxError{
      Command casesAST=null;
      SourcePosition expressionPos = new SourcePosition();
      start (expressionPos);
      casesAST=parseCase();
      while(currentToken.kind==Token.WHEN){
          Command csAST=parseCase();
          finish(expressionPos);
          casesAST = new SCase(casesAST, csAST, expressionPos);
          
      }
      if(currentToken.kind==Token.ELSE){
          Command elsAST=parseElseCase();
          finish(expressionPos);
          casesAST=new ElseCase(elsAST, expressionPos);
      }
      
      return casesAST;
  }
  
  Command parseCase()throws SyntaxError{
      Command caseAST=null;
      SourcePosition expressionPos = new SourcePosition();
      start (expressionPos);
      accept(Token.WHEN);
      Expression cLAST=parseCaseLiterals();
      accept(Token.THEN);
      Command cAST=parseCommand();
      finish(expressionPos);
      caseAST=new ComCase(cLAST,cAST,expressionPos);
      return caseAST;
  }
  
  Command parseElseCase()throws SyntaxError{
      Command elseCase=null;
      SourcePosition expressionPos = new SourcePosition();
      start (expressionPos);
      accept(Token.ELSE);
      Command cAST=parseCommand();
      finish(expressionPos);
      elseCase=new ElseCase(cAST, expressionPos);
      return elseCase;
  }
  
  Expression parseCaseLiterals()throws SyntaxError{
      Expression caseLiterals=null;
      SourcePosition expressionPos = new SourcePosition();
      start (expressionPos);
      caseLiterals=parseCaseRange();
      while(currentToken.kind==Token.PIPE){
          acceptIt();
          Expression cr2AST=parseCaseRange();
          finish(expressionPos);
          caseLiterals=new CaseRange(caseLiterals, cr2AST, expressionPos);
      }
      return caseLiterals;
  }
  
  Expression parseCaseRange()throws SyntaxError{
      Expression caseRange=null;
      SourcePosition expressionPos = new SourcePosition();
      start (expressionPos);
      caseRange= parseCaseLiteral();
      if(currentToken.kind==Token.DOTDOT){
          acceptIt();
          Expression eAST=parseCaseLiteral();
          finish(expressionPos);
          caseRange=new CaseLiteral(caseRange, eAST, expressionPos);
      }
      else{
          finish(expressionPos);
          caseRange=new CaseLiteral(caseRange,null,expressionPos);
      }
      return caseRange;
  }
    
  Expression parseCaseLiteral()throws SyntaxError{
      Expression caseLit = null; // in case there's a syntactic error

    SourcePosition expressionPos = new SourcePosition();
    start (expressionPos);
    switch(currentToken.kind){
        case Token.INTLITERAL:{
            IntegerLiteral ilAST = parseIntegerLiteral();
            finish(expressionPos);
            caseLit = new LitIntegerExpression(ilAST,expressionPos);
            
            
        }
        break;
        case Token.CHARLITERAL:{
            CharacterLiteral clAST= parseCharacterLiteral();
            finish(expressionPos);
            caseLit = new LitCharacterExpression(clAST, expressionPos);
            
        }
        break;
        default:{
            syntacticError("\"%\" cannot start a formal parameter",
        currentToken.spelling);
         
        }
        break;
    }
    
    return caseLit;
}

///////////////////////////////////////////////////////////////////////////////
//
// EXPRESSIONS
//
///////////////////////////////////////////////////////////////////////////////

  Expression parseExpression() throws SyntaxError {
    Expression expressionAST = null; // in case there's a syntactic error

    SourcePosition expressionPos = new SourcePosition();

    start (expressionPos);

    switch (currentToken.kind) {

    case Token.LET:
      {
        acceptIt();
        Declaration dAST = parseDeclaration();
        accept(Token.IN);
        Expression eAST = parseExpression();
        finish(expressionPos);
        expressionAST = new LetExpression(dAST, eAST, expressionPos);
      }
      break;

    case Token.IF:
      {
        acceptIt();
        Expression e1AST = parseExpression();
        accept(Token.THEN);
        Expression e2AST = parseExpression();
        accept(Token.ELSE);
        Expression e3AST = parseExpression();
        finish(expressionPos);
        expressionAST = new IfExpression(e1AST, e2AST, e3AST, expressionPos);
      }
      break;

    default:
      expressionAST = parseSecondaryExpression();
      break;
    }
    return expressionAST;
  }

  Expression parseSecondaryExpression() throws SyntaxError {
    Expression expressionAST = null; // in case there's a syntactic error

    SourcePosition expressionPos = new SourcePosition();
    start(expressionPos);

    expressionAST = parsePrimaryExpression();
    while (currentToken.kind == Token.OPERATOR) {
      Operator opAST = parseOperator();
      Expression e2AST = parsePrimaryExpression();
      expressionAST = new BinaryExpression (expressionAST, opAST, e2AST,
        expressionPos);
    }
    return expressionAST;
  }

  Expression parsePrimaryExpression() throws SyntaxError {
    Expression expressionAST = null; // in case there's a syntactic error

    SourcePosition expressionPos = new SourcePosition();
    start(expressionPos);

    switch (currentToken.kind) {

    case Token.INTLITERAL:
      {
        IntegerLiteral ilAST = parseIntegerLiteral();
        finish(expressionPos);
        expressionAST = new IntegerExpression(ilAST, expressionPos);
      }
      break;

    case Token.CHARLITERAL:
      {
        CharacterLiteral clAST= parseCharacterLiteral();
        finish(expressionPos);
        expressionAST = new CharacterExpression(clAST, expressionPos);
      }
      break;

    case Token.LBRACKET:
      {
        acceptIt();
        ArrayAggregate aaAST = parseArrayAggregate();
        accept(Token.RBRACKET);
        finish(expressionPos);
        expressionAST = new ArrayExpression(aaAST, expressionPos);
      }
      break;

    case Token.LCURLY:
      {
        acceptIt();
        RecordAggregate raAST = parseRecordAggregate();
        accept(Token.RCURLY);
        finish(expressionPos);
        expressionAST = new RecordExpression(raAST, expressionPos);
      }
      break;

    case Token.IDENTIFIER:
      {
        Identifier iAST= parseIdentifier();
        if (currentToken.kind == Token.LPAREN) {
          acceptIt();
          ActualParameterSequence apsAST = parseActualParameterSequence();
          accept(Token.RPAREN);
          finish(expressionPos);
          expressionAST = new CallExpression(iAST, apsAST, expressionPos);

        } else {
            
          /*if(currentToken.kind == Token.DOLAR){
              acceptIt();
          }*/
          Vname vAST = parseRestOfVname(iAST);
          finish(expressionPos);
          expressionAST = new VnameExpression(vAST, expressionPos);
        }
      }
      break;

    case Token.OPERATOR:
      {
        Operator opAST = parseOperator();
        Expression eAST = parsePrimaryExpression();
        finish(expressionPos);
        expressionAST = new UnaryExpression(opAST, eAST, expressionPos);
      }
      break;

    case Token.LPAREN:
      acceptIt();
      expressionAST = parseExpression();
      accept(Token.RPAREN);
      break;

    default:
      syntacticError("\"%\" cannot start an expression",
        currentToken.spelling);
      break;

    }
    return expressionAST;
  }

  RecordAggregate parseRecordAggregate() throws SyntaxError {
    RecordAggregate aggregateAST = null; // in case there's a syntactic error

    SourcePosition aggregatePos = new SourcePosition();
    start(aggregatePos);

    Identifier iAST = parseIdentifier();
    accept(Token.IS);
    Expression eAST = parseExpression();

    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      RecordAggregate aAST = parseRecordAggregate();
      finish(aggregatePos);
      aggregateAST = new MultipleRecordAggregate(iAST, eAST, aAST, aggregatePos);
    } else {
      finish(aggregatePos);
      aggregateAST = new SingleRecordAggregate(iAST, eAST, aggregatePos);
    }
    return aggregateAST;
  }

  ArrayAggregate parseArrayAggregate() throws SyntaxError {
    ArrayAggregate aggregateAST = null; // in case there's a syntactic error

    SourcePosition aggregatePos = new SourcePosition();
    start(aggregatePos);

    Expression eAST = parseExpression();
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      ArrayAggregate aAST = parseArrayAggregate();
      finish(aggregatePos);
      aggregateAST = new MultipleArrayAggregate(eAST, aAST, aggregatePos);
    } else {
      finish(aggregatePos);
      aggregateAST = new SingleArrayAggregate(eAST, aggregatePos);
    }
    return aggregateAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// VALUE-OR-VARIABLE NAMES
//
///////////////////////////////////////////////////////////////////////////////

Vname parseVname () throws SyntaxError {
    Vname vnameAST = null; // in case there's a syntactic error
    Identifier iAST = parseIdentifier();
    //parse $ identifier
    if(currentToken.kind == Token.DOLAR){
        acceptIt();
        iAST = new PackageIdentifier(iAST);
    }
    vnameAST = parseRestOfVname(iAST);
    return vnameAST;
  }

  Vname parseRestOfVname(Identifier identifierAST) throws SyntaxError {
    SourcePosition vnamePos = new SourcePosition();
    vnamePos = identifierAST.position;
    Vname vAST = new SimpleVname(identifierAST, vnamePos);

    while (currentToken.kind == Token.DOT ||
           currentToken.kind == Token.LBRACKET) {

      if (currentToken.kind == Token.DOT) {
        acceptIt();
        Identifier iAST = parseIdentifier();
        vAST = new DotVname(vAST, iAST, vnamePos);
      } else {
        acceptIt();
        Expression eAST = parseExpression();
        accept(Token.RBRACKET);
        finish(vnamePos);
        vAST = new SubscriptVname(vAST, eAST, vnamePos);
      }
    }
    return vAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// DECLARATIONS
//
///////////////////////////////////////////////////////////////////////////////

  
  Declaration parseDeclaration() throws SyntaxError {
    Declaration declarationAST = null; // in case there's a syntactic error

    SourcePosition declarationPos = new SourcePosition();
    start(declarationPos);
    declarationAST = parseCompoundDeclaration();
    System.out.println("Entra a Declaration");
    while (currentToken.kind == Token.SEMICOLON) {
      acceptIt();
      Declaration d2AST = parseCompoundDeclaration();
      finish(declarationPos);
      declarationAST = new SequentialDeclaration(declarationAST, d2AST,
        declarationPos);
    }
    return declarationAST;
  }
    Declaration parseCompoundDeclaration() throws SyntaxError {
    Declaration declarationAST = null; // in case there's a syntactic error
    
    SourcePosition declarationPos = new SourcePosition();
    start(declarationPos);
    System.out.println("Entra a compoundD");
    switch(currentToken.kind){
        case Token.RECURSIVE:
        {
            
            System.out.println("Entra a recursive");
            acceptIt();
            Declaration pfAST=parseProcFuncs();
            accept(Token.END);
            declarationAST=new RecursiveDeclaration(pfAST, declarationPos);          
            break;
        }
        
        case Token.PRIVATE:
        {
           acceptIt();
           Declaration d1AST=parseDeclaration();
           accept(Token.IN);
           Declaration d2AST=parseDeclaration();
           accept(Token.END);
           declarationAST= new PrivateDeclaration(d1AST, d2AST, declarationPos);
           break;
        }
        
        case Token.PAR:
        {
            acceptIt();
            Declaration d1AST= parseSingleDeclaration();
            accept(Token.PIPE);
            Declaration d2AST= parseSingleDeclaration();
            
            //simple trick to avoid undefined
            declarationAST = d1AST;
            while(currentToken.kind==Token.PIPE){
                acceptIt();
                declarationAST =new SequentialDeclaration(d1AST, d2AST, declarationPos);
                d2AST= parseSingleDeclaration();
                
            }
            accept(Token.END);
            declarationAST=new ParDeclaration(declarationAST, d2AST, declarationPos);
            break;
        }
        default:
                declarationAST = parseSingleDeclaration();
                break;
    }
    return declarationAST;
}

    private boolean flag=true;
  Declaration parseProcFuncs() throws SyntaxError{
      Declaration declarationAST = null; // in case there's a syntactic error

      SourcePosition declarationPos = new SourcePosition();
      start(declarationPos);
      Declaration pfAST=parseProcFunc();
      if(flag){
          if(currentToken.kind != Token.PIPE){
              syntacticError("| expected here", "");
          }else{
              flag=false;
          }
      } 
      if(currentToken.kind == Token.PIPE){
          acceptIt();
          declarationAST=new ProcFuncDeclaration(pfAST,parseProcFuncs(),declarationPos);
      }
      else{
          declarationAST = new ProcFuncDeclaration(pfAST,null,declarationPos);
      }
      return declarationAST;
  }
    
  Declaration parseProcFunc() throws SyntaxError{
    Declaration declarationAST = null; // in case there's a syntactic error

    SourcePosition declarationPos = new SourcePosition();
    start(declarationPos);
    switch(currentToken.kind){
        case Token.PROC:
        {
            acceptIt();
            Identifier iAST= parseIdentifier();
            
            accept(Token.LPAREN);
            FormalParameterSequence fpsAST = parseFormalParameterSequence();
            accept(Token.RPAREN);
            accept(Token.IS);
            Command cAST = parseCommand();
            accept(Token.END);
            declarationAST = new ProcDeclaration(iAST, fpsAST, cAST, declarationPos);
            break;
        }
        case Token.FUNC:
        {
            acceptIt();
            Identifier iAST = parseIdentifier();
            accept(Token.LPAREN);
            FormalParameterSequence fpsAST = parseFormalParameterSequence();
            accept(Token.RPAREN);
            accept(Token.COLON);
            TypeDenoter tAST = parseTypeDenoter();
            accept(Token.IS);
            Expression eAST = parseExpression();
            declarationAST = new FuncDeclaration(iAST, fpsAST, tAST, eAST,declarationPos);
            break;
        }
        default:
      syntacticError("\"%\" cannot start a declaration",
        currentToken.spelling);
      break;
    }
    return declarationAST;
  }
  
  
  Declaration parseSingleDeclaration() throws SyntaxError {
    Declaration declarationAST = null; // in case there's a syntactic error

    SourcePosition declarationPos = new SourcePosition();
    start(declarationPos);

    switch (currentToken.kind) {

    case Token.CONST:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.IS);
        Expression eAST = parseExpression();
        finish(declarationPos);
        declarationAST = new ConstDeclaration(iAST, eAST, declarationPos);
      }
      break;

    case Token.VAR:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        System.out.println("Var "+ currentToken.kind);
        switch(currentToken.kind){
            case(Token.COLON):{
                acceptIt();
                TypeDenoter tAST = parseTypeDenoter();
                finish(declarationPos);
                declarationAST = new VarDeclaration(iAST, tAST, declarationPos);
                
                break;
            }
            case(Token.ASSIGN):{
                acceptIt();
                Expression eAST= parseExpression();
                declarationAST =new VarADeclaration(iAST, eAST, declarationPos);
                break;
            }
        }
        
      }
      break;

    case Token.PROC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        accept(Token.IS);
        Command cAST = parseCommand();
        accept(Token.END);
        finish(declarationPos);
        declarationAST = new ProcDeclaration(iAST, fpsAST, cAST, declarationPos);
      }
      break;

    case Token.FUNC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        accept(Token.IS);
        Expression eAST = parseExpression();
        finish(declarationPos);
        declarationAST = new FuncDeclaration(iAST, fpsAST, tAST, eAST,
          declarationPos);
      }
      break;

    case Token.TYPE:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.IS);
        TypeDenoter tAST = parseTypeDenoter();
        finish(declarationPos);
        declarationAST = new TypeDeclaration(iAST, tAST, declarationPos);
      }
      break;

    default:
      syntacticError("\"%\" cannot start a declaration",
        currentToken.spelling);
      break;

    }
    return declarationAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// PARAMETERS
//
///////////////////////////////////////////////////////////////////////////////

  FormalParameterSequence parseFormalParameterSequence() throws SyntaxError {
    FormalParameterSequence formalsAST;

    SourcePosition formalsPos = new SourcePosition();

    start(formalsPos);
    if (currentToken.kind == Token.RPAREN) {
      finish(formalsPos);
      formalsAST = new EmptyFormalParameterSequence(formalsPos);

    } else {
      formalsAST = parseProperFormalParameterSequence();
    }
    return formalsAST;
  }

  FormalParameterSequence parseProperFormalParameterSequence() throws SyntaxError {
    FormalParameterSequence formalsAST = null; // in case there's a syntactic error;

    SourcePosition formalsPos = new SourcePosition();
    start(formalsPos);
    FormalParameter fpAST = parseFormalParameter();
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      FormalParameterSequence fpsAST = parseProperFormalParameterSequence();
      finish(formalsPos);
      formalsAST = new MultipleFormalParameterSequence(fpAST, fpsAST,
        formalsPos);

    } else {
      finish(formalsPos);
      formalsAST = new SingleFormalParameterSequence(fpAST, formalsPos);
    }
    return formalsAST;
  }

  FormalParameter parseFormalParameter() throws SyntaxError {
    FormalParameter formalAST = null; // in case there's a syntactic error;

    SourcePosition formalPos = new SourcePosition();
    start(formalPos);

    switch (currentToken.kind) {

    case Token.IDENTIFIER:
      {
        Identifier iAST = parseIdentifier();
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        finish(formalPos);
        formalAST = new ConstFormalParameter(iAST, tAST, formalPos);
      }
      break;

    case Token.VAR:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        finish(formalPos);
        formalAST = new VarFormalParameter(iAST, tAST, formalPos);
      }
      break;

    case Token.PROC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        finish(formalPos);
        formalAST = new ProcFormalParameter(iAST, fpsAST, formalPos);
      }
      break;

    case Token.FUNC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        finish(formalPos);
        formalAST = new FuncFormalParameter(iAST, fpsAST, tAST, formalPos);
      }
      break;

    default:
      syntacticError("\"%\" cannot start a formal parameter",
        currentToken.spelling);
      break;

    }
    return formalAST;
  }


  ActualParameterSequence parseActualParameterSequence() throws SyntaxError {
    ActualParameterSequence actualsAST;

    SourcePosition actualsPos = new SourcePosition();

    start(actualsPos);
    if (currentToken.kind == Token.RPAREN) {
      finish(actualsPos);
      actualsAST = new EmptyActualParameterSequence(actualsPos);

    } else {
      actualsAST = parseProperActualParameterSequence();
    }
    return actualsAST;
  }

  ActualParameterSequence parseProperActualParameterSequence() throws SyntaxError {
    ActualParameterSequence actualsAST = null; // in case there's a syntactic error

    SourcePosition actualsPos = new SourcePosition();

    start(actualsPos);
    ActualParameter apAST = parseActualParameter();
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      ActualParameterSequence apsAST = parseProperActualParameterSequence();
      finish(actualsPos);
      actualsAST = new MultipleActualParameterSequence(apAST, apsAST,
        actualsPos);
    } else {
      finish(actualsPos);
      actualsAST = new SingleActualParameterSequence(apAST, actualsPos);
    }
    return actualsAST;
  }

  ActualParameter parseActualParameter() throws SyntaxError {
    ActualParameter actualAST = null; // in case there's a syntactic error

    SourcePosition actualPos = new SourcePosition();

    start(actualPos);

    switch (currentToken.kind) {

    case Token.IDENTIFIER:
    case Token.INTLITERAL:
    case Token.CHARLITERAL:
    case Token.OPERATOR:
    case Token.LET:
    case Token.IF:
    case Token.LPAREN:
    case Token.LBRACKET:
    case Token.LCURLY:
      {
        Expression eAST = parseExpression();
        finish(actualPos);
        actualAST = new ConstActualParameter(eAST, actualPos);
      }
      break;

    case Token.VAR:
      {
        acceptIt();
        Vname vAST = parseVname();
        finish(actualPos);
        actualAST = new VarActualParameter(vAST, actualPos);
      }
      break;

    case Token.PROC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        finish(actualPos);
        actualAST = new ProcActualParameter(iAST, actualPos);
      }
      break;

    case Token.FUNC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        finish(actualPos);
        actualAST = new FuncActualParameter(iAST, actualPos);
      }
      break;

    default:
      syntacticError("\"%\" cannot start an actual parameter",
        currentToken.spelling);
      break;

    }
    return actualAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// TYPE-DENOTERS
//
///////////////////////////////////////////////////////////////////////////////

  TypeDenoter parseTypeDenoter() throws SyntaxError {
    TypeDenoter typeAST = null; // in case there's a syntactic error
    SourcePosition typePos = new SourcePosition();

    start(typePos);

    switch (currentToken.kind) {

    case Token.IDENTIFIER:
      {
        Identifier iAST = parseIdentifier();
        finish(typePos);
        typeAST = new SimpleTypeDenoter(iAST, typePos);
      }
      break;

    case Token.ARRAY:
      {
        acceptIt();
        IntegerLiteral ilAST = parseIntegerLiteral();
        accept(Token.OF);
        TypeDenoter tAST = parseTypeDenoter();
        finish(typePos);
        typeAST = new ArrayTypeDenoter(ilAST, tAST, typePos);
      }
      break;

    case Token.RECORD:
      {
        acceptIt();
        FieldTypeDenoter fAST = parseFieldTypeDenoter();
        accept(Token.END);
        finish(typePos);
        typeAST = new RecordTypeDenoter(fAST, typePos);
      }
      break;

    default:
      syntacticError("\"%\" cannot start a type denoter",
        currentToken.spelling);
      break;

    }
    return typeAST;
  }

  FieldTypeDenoter parseFieldTypeDenoter() throws SyntaxError {
    FieldTypeDenoter fieldAST = null; // in case there's a syntactic error

    SourcePosition fieldPos = new SourcePosition();

    start(fieldPos);
    Identifier iAST = parseIdentifier();
    accept(Token.COLON);
    TypeDenoter tAST = parseTypeDenoter();
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      FieldTypeDenoter fAST = parseFieldTypeDenoter();
      finish(fieldPos);
      fieldAST = new MultipleFieldTypeDenoter(iAST, tAST, fAST, fieldPos);
    } else {
      finish(fieldPos);
      fieldAST = new SingleFieldTypeDenoter(iAST, tAST, fieldPos);
    }
    return fieldAST;
  }
}
