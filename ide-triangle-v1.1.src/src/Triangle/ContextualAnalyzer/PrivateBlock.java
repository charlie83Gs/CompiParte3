/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Triangle.ContextualAnalyzer;

/**
 *
 * @author Charlie
 * 
 */
public class PrivateBlock {
    //latest declaration previous to private block
    IdEntry begin;
    //first declaration made after private block
    IdEntry end;

    public PrivateBlock(IdEntry begin) {
        this.begin = begin;
    }
    /**
     * 
     * @param newEnd first declaration after private
     */
    public void setEnd(IdEntry newEnd){
        this.end = newEnd; 
    }
    
    public void finishPrivate(){
        //System.out.println("Private Begin: " + begin.id + "-- Private End: " + end.id);
        //zip tie end and begin
        //extracts private block from code
        end.previous = begin;
    }
    
    

    
}
