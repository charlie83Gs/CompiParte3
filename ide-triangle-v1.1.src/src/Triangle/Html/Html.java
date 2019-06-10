/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Triangle.Html;

import Triangle.SyntacticAnalyzer.Parser;
import Triangle.SyntacticAnalyzer.Scanner;
import Triangle.SyntacticAnalyzer.Token;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 *
 * @author reese
 */
public class Html {
    
    FormHtml form = new FormHtml();
    String programa;
    File f;

    public Html(String nombre) {
        this.programa = nombre;
        this.f = new File(nombre+".html");
    }
    
    public void generateHtml(Scanner scanner){
        try {
            Token currentToken;
            currentToken = scanner.scanWithComm();
            header();
            while(currentToken.getKind() != Token.EOT){
                if(currentToken.getKind() == Token.COMMENT){
                    //System.out.println(currentToken.getSpelling());
                    //System.out.println(currentToken.getKind());
                    comentarios(currentToken.getSpelling());
                    cambioLinea();
                    currentToken = scanner.scanWithComm();
                }
                if(currentToken.getKind() >= Token.ARRAY && currentToken.getKind() <= Token.WHILE){
                    reservada(currentToken.getSpelling());
                    currentToken = scanner.scanWithComm();
                }
                if(currentToken.getKind() == Token.IDENTIFIER || currentToken.getKind() == Token.OPERATOR ||
                        (currentToken.getKind() >= Token.DOT && currentToken.getKind() <= Token.DOTDOT)){
                    operador(currentToken.getSpelling());
                    currentToken = scanner.scanWithComm();
                }
                if(currentToken.getKind() == Token.SEMICOLON){
                    operador(currentToken.getSpelling());
                    cambioLinea();
                    currentToken = scanner.scanWithComm();
                }
                else{
                    //System.out.println(currentToken.getKind());
                    texto(currentToken.getSpelling());
                    currentToken = scanner.scanWithComm();
                }
            }
            footer();
            System.out.println("Done saving html to " + f);
        } catch (Exception e) {
            System.out.println("Error saving html to " + f + " "+ e.toString());
        }
    }
    
    public void header(){    
        try {
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write(form.header(programa));
            bw.close();
            
        } catch (Exception e) {
            System.out.println("Error al crear al crear o escribir el archivo");
        }
    }
    
    public void operador(String word){    
        try {
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            bw.append(form.operadores(word));
            bw.close();
            
        } catch (Exception e) {
            System.out.println("Error al crear al crear o escribir el archivo");
        }
    }
    
    public void reservada(String word){    
        try {
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            bw.append(form.reservado(word));
            bw.close();
            
        } catch (Exception e) {
            System.out.println("Error al crear al crear o escribir el archivo");
        }
    }
    
    public void texto(String word){    
        try {
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            bw.append(form.texto(word));
            bw.close();
            
        } catch (Exception e) {
            System.out.println("Error al crear al crear o escribir el archivo");
        }
    }
    
    public void comentarios(String word){    
        try {
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            bw.append(form.comentarios(word));
            bw.close();
            
        } catch (Exception e) {
            System.out.println("Error al crear al crear o escribir el archivo");
        }
    }
    
    public void cambioLinea(){    
        try {
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            bw.append(form.cambioLinea());
            bw.close();
            
        } catch (Exception e) {
            System.out.println("Error al crear al crear o escribir el archivo");
        }
    }
    
    public void footer(){    
        try {
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            bw.append(form.footer());
            bw.close();
            
        } catch (Exception e) {
            System.out.println("Error al crear al crear o escribir el archivo");
        }
    }
        
}
