
package Triangle.TreeXml;

import Triangle.AbstractSyntaxTrees.Program;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
 
public class XmlGenerator {
    
    //could be a dialog
    public static final String xmlFilePath = "C:\\Users\\nikos7\\Desktop\\files\\xmlfile.xml";
    
    private Program theAst;
    DocumentBuilderFactory documentFactory;
    DocumentBuilder documentBuilder;

    public XmlGenerator(Program ast) throws ParserConfigurationException{
        theAst = ast;

        documentFactory = DocumentBuilderFactory.newInstance();
        documentBuilder = documentFactory.newDocumentBuilder();

        
    }



    public Document getXmlAstRepresentation(){
        Document savedDocument = documentBuilder.newDocument();
        XmlVisitor visitor = new XmlVisitor(savedDocument); 
        Element root = (Element)theAst.visit(visitor,null);
        savedDocument.appendChild(root);
        return savedDocument;
       

  
    }

    public void SaveXmlRepresentation(String savePath){
        Document savedDocument = getXmlAstRepresentation();

        //saving code
        try{
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(savedDocument);
            File saveFile = new File(savePath);
            StreamResult streamResult = new StreamResult(saveFile);
            transformer.transform(domSource, streamResult);
            System.out.println("Done saving ast to " + savePath);
        }catch(Exception e){
            System.out.println("Error saving ast to " + savePath + " "+e.toString());
        }
        
    }

}