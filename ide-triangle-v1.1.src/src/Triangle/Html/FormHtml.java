/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Triangle.Html;

/**
 *
 * @author reese
 */
public class FormHtml {
    
    public String header(String titulo){
        String htmlString = 
            "<html>\n" +
            "    <header>\n" +
            "        <title>" + titulo + "</title>\n" +
            "        <link rel=\"stylesheet\" href=\"/css/style.css\">\n" +
            "    </header>\n" +
            "\n" +
            "    <body BGCOLOR=\"B9CFFF\">\n" +
            "   <font face=\"Courier\" size:1em>\n" +
            "   <h1>TRI</h1><br>\n";
        
        return htmlString;
    }
    
    public String operadores(String word){
        String htmlString = 
            "       <font color=BLACK> " + word + "</font>";
        return htmlString;
    }
    
    public String reservado(String word){
        String htmlString = 
            "       <b> " + word + "</b>";
        return htmlString;
    }
    
    public String texto(String word){
        String htmlString =
            "       <font color=BLUE> " + word + "</font>";
        return htmlString;
    }
    
    public String comentarios(String texto){
        String htmlString =
            "       <font color=\"3CB371\"> " + texto + "</font>\n";
        return htmlString;
    }
    
    public String cambioLinea(){
        String htmlString =
            "<br>\n";
        return htmlString;
    }
    
    public String footer(){
        String htmlString = 
            "   </font>\n" +
            "    </body>\n" +
            "</html>";
        
        return htmlString;
    }
}
