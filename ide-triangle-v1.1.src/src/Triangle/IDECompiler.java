/*
 * IDE-Triangle v1.0
 * Compiler.java 
 */

package Triangle;

import Triangle.CodeGenerator.Frame;
import java.awt.event.ActionListener;
import Triangle.SyntacticAnalyzer.SourceFile;
import Triangle.SyntacticAnalyzer.Scanner;
import Triangle.AbstractSyntaxTrees.Program;
import Triangle.SyntacticAnalyzer.Parser;
import Triangle.ContextualAnalyzer.Checker;
import Triangle.CodeGenerator.Encoder;
import Triangle.Html.Html;
import Triangle.TreeXml.XmlGenerator;
import javax.xml.parsers.ParserConfigurationException;



/** 
 * This is merely a reimplementation of the Triangle.Compiler class. We need
 * to get to the ASTs in order to draw them in the IDE without modifying the
 * original Triangle code.
 *
 * @author Luis Leopoldo Pérez <luiperpe@ns.isi.ulatina.ac.cr>
 */
public class IDECompiler {

    // <editor-fold defaultstate="collapsed" desc=" Methods ">
    /**
     * Creates a new instance of IDECompiler.
     *
     */
    public IDECompiler() {
    }
    
    /**
     * Particularly the same compileProgram method from the Triangle.Compiler
     * class.
     * @param sourceName Path to the source file.
     * @return True if compilation was succesful.
     */
    public boolean compileProgram(String sourceName) {
        System.out.println("********** " +
                           "Triangle Compiler (IDE-Triangle 1.0)" +
                           " **********");
        
        System.out.println("Syntactic Analysis ...");
        SourceFile source = new SourceFile(sourceName);
        Scanner scanner = new Scanner(source);
        scanner.enableDebugging();//enable scaner debug
        report = new IDEReporter();
        Parser parser = new Parser(scanner, report);
        boolean success = false;
        
        rootAST = parser.parseProgram();
        
        
        //System.out.println("Valid command on program: " + !(rootAST.C == null));
        //generate xml
        try{
            XmlGenerator generator = new XmlGenerator(rootAST);
            //System.out.println("Tring to save file "+ sourceName + ".xml");
            String xmlPath = sourceName.replace(".tri",".xml");
            generator.SaveXmlRepresentation(xmlPath);
        }catch(ParserConfigurationException e) {
            System.out.println("Error saving file" + e.toString());
            e.printStackTrace(System.out);
        }
        catch(Exception e){
            System.out.println("Error generating xml file" + e.toString());
            //System.out.println("Error saving file" + e.toString());
           //e.printStackTrace(System.out);
        }
        
        
        if (report.numErrors == 0) {
              System.out.println("Contextual Analysis ...");
              Checker checker = new Checker(report);
              checker.check(rootAST);
            if (report.numErrors == 0) {
                //System.out.println("Code Generation ...");
                //Encoder encoder = new Encoder(report);
                //encoder.encodeRun(rootAST, false);
                
                if (report.numErrors == 0) {
             //       encoder.saveObjectProgram(sourceName.replace(".tri", ".tam"));
                    success = true;
                }
            }
        }
        
        //generate html
        try {
            SourceFile source2 = new SourceFile(sourceName);
            Html html = new Html(sourceName.replace(".tri", ""));
            Scanner scanner2 = new Scanner(source2);
            //scanner2.enableDebugging();//enable scaner debug
            //System.out.println(scanner2.scan().toString());
            html.generateHtml(scanner2);
        } 
        catch (Exception e) {
        }

        if (success)
            System.out.println("Compilation was successful.");
        else
            System.out.println("Compilation was unsuccessful.");
        
        return(success);
    }
      
    /**
     * Returns the line number where the first error is.
     * @return Line number.
     */
    public int getErrorPosition() {
        return(report.getFirstErrorPosition());
    }
        
    /**
     * Returns the root Abstract Syntax Tree.
     * @return Program AST (root).
     */
    public Program getAST() {
        return(rootAST);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Attributes ">
    private Program rootAST;        // The Root Abstract Syntax Tree.    
    private IDEReporter report;     // Our ErrorReporter class.
    // </editor-fold>
}
