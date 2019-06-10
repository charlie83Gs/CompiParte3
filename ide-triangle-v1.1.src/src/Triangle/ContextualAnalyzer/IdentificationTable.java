/*
 * @(#)IdentificationTable.java                2.1 2003/10/07
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

package Triangle.ContextualAnalyzer;

import Triangle.AbstractSyntaxTrees.Declaration;
import java.util.ArrayList;
import java.util.Iterator;

public final class IdentificationTable {

  private int level;
  private String pack;
  private IdEntry latest;
  ArrayList<PrivateBlock> privateList;
  
  //ugly flag to complete private stuff
  private boolean EndingPrivate;
  //ugly flag to activate paralel declarations
  private boolean isParalel;

  public IdentificationTable () {
    level = 0;
    latest = null;
    privateList = new ArrayList<>();
    EndingPrivate = false;
    isParalel = false;
  }

  // Opens a new level in the identification table, 1 higher than the
  // current topmost level.

  public void openScope () {

    level ++;
  }

  // Closes the topmost level in the identification table, discarding
  // all entries belonging to that level.

  public void closeScope () {

    IdEntry entry, local;

    // Presumably, idTable.level > 0.
    entry = this.latest;
    while (entry!=null &&entry.level == this.level) {
      local = entry;
      entry = local.previous;
    }
    this.level--;
    this.latest = entry;
  }

  // Makes a new entry in the identification table for the given identifier
  // and attribute. The new entry belongs to the current level.
  // duplicated is set to to true iff there is already an entry for the
  // same identifier at the current level.

  public void enter (String id, Declaration attr) {
    if(isParalel) return;
      
    //if package add package to id
    //System.out.println("Declared: " + id);
    IdEntry entry = this.latest;
    boolean present = false, searching = true;
    
     
     
     
    String fullId = id;
    if(pack != null)fullId = pack+"$"+id;
    // Check for duplicate entry ...
    while (searching) {
      if (entry == null || entry.level < this.level)
        searching = false;
      else if (entry.compareId(fullId)) {
        present = true;
        searching = false;
       } else
       entry = entry.previous;
    }

    attr.duplicated = present;
    // Add new entry ...
    if(pack != null){ 
         entry = new IdEntry(pack, id, attr, this.level, this.latest);
    }else{
         entry = new IdEntry(id, attr, this.level, this.latest);
    }
    
    //System.out.println("Declared: " + id);    
    
   
    
    
    this.latest = entry;
    
    //private should be updated after new entry is acepted
    if(EndingPrivate) completePrivate();
  }
  
  public boolean enterLiteral(String id) {

        IdEntry entry = this.latest;
        boolean present = false, searching = true;

        // Check for duplicate entry ...
        while (searching) {
            if (entry == null || entry.level < this.level) {
                searching = false;
            } else if (entry.id.equals(id)) {
                present = true;
                searching = false;
            } else {
                entry = entry.previous;
            }
        }
        // Add new entry ...
        entry = new IdEntry(id, this.level, this.latest);
        this.latest = entry;
        
        
        return present;
    }

  // Finds an entry for the given identifier in the identification table,
  // if any. If there are several entries for that identifier, finds the
  // entry at the highest level, in accordance with the scope rules.
  // Returns null iff no entry is found.
  // otherwise returns the attribute field of the entry found.

  public Declaration retrieve (String id) {
    

    IdEntry entry;
    Declaration attr = null;
    boolean present = false, searching = true;

    
    
    entry = this.latest;
    while (searching) {
      if (entry == null)
        searching = false;
      else if (entry.compareId(id)) {
        present = true;
        searching = false;
        attr = entry.attr;
      }else if (pack != null && entry.compareId(pack+"$"+id)) {
        present = true;
        searching = false;
        attr = entry.attr;
      } else
        entry = entry.previous;
      
      //if(entry != null) System.out.println(pack+"$"+id+"----"+ entry.id);
    }

    return attr;
  }
  
  
  public void startPackage(String pack){
      this.pack = pack;
  
  }
  
  public void endPackage(){
      this.pack = null;
  }
  
  //starts a private block
  public void startPrivate(){
      PrivateBlock newPrivate = new PrivateBlock(latest);
      privateList.add(newPrivate);
  }
  
  //ends a private block

  public void endPrivate(){
      EndingPrivate = true;
  }
  
  //private should be updated after new entry is acepted
  private void completePrivate(){
      EndingPrivate = false;
      privateList.get(privateList.size()-1).end = latest;
      
  }
  
  //removes the last inserted private block
  public void flushPrivate(){
      //remove block from list
      PrivateBlock lastBlock = privateList.remove(privateList.size()-1);
      //remove private variables from identification table
      lastBlock.finishPrivate();
  }
  
  
  public void startParalel(){
      isParalel = true;
  }
  
  public void endParalel(){
      isParalel = false;

      
  }
}
