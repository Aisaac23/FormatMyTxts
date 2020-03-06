/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formatmytxts;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import javax.swing.JOptionPane;

/**
 *
 * @author root
 */
public class txtFormatter extends Thread{
    
    String loadedText;
    File txtFile;
    String replace, with, normalize, delete, saveAs, separator;
    boolean willReplace, willDelete, willSaveAs, willCase, willGroup, willNormalize;
    boolean deleteBefore, lowercase;
    int group;
    
    public txtFormatter(File txtFile) {
        
        this.txtFile = txtFile;
        this.loadedText = new String( );
        this.delete = "";
        this.normalize = " ";
        this.saveAs = ".txt";
        this.replace = this.with = this.separator = "";
        
    }
    
    @Override
    public void run() {
        this.loadTextfromFile();
        
        this.caseWords();
        this.deleteText();
        this.normalize();
        this.replace();
        this.groupData();
        
        try {
            if( !this.saveAs.equals(".txt") )
                Files.writeString(Paths.get( this.txtFile.getAbsolutePath().
                        replace( ".txt", this.saveAs ) ), this.loadedText, 
                        StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
            else
                Files.writeString(Paths.get( this.txtFile.getAbsolutePath() ), this.loadedText, 
                        StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            
           
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Couldn't write into file: " + ex.getMessage() );
        }
        
        this.loadedText = "";
        this.delete = "";
        this.normalize = "";
        this.replace = "";
        this.txtFile = null;
        System.gc();
    }

    @Override
    public synchronized void start() {
        this.run();
    }
    
    private void caseWords()
    {
        if( this.willCase )
            if( this.lowercase )
                this.loadedText = this.loadedText.toLowerCase();
            else
                this.loadedText = this.loadedText.toUpperCase(); 
    }
    
    private void deleteText()
    {
        
        if( this.willDelete )
        {
            this.delete = this.delete.replace("\\n", "\n");
            this.delete = this.delete.replace("\\t", "\t");
            this.delete = this.delete.replace("\\r", "\r");
            if( this.loadedText.contains(this.delete) )
            {
                if( this.deleteBefore )
                    this.loadedText = this.loadedText.substring( this.loadedText.indexOf(this.delete) );
                else
                    this.loadedText = this.loadedText.substring( 0, this.loadedText.indexOf(this.delete) );
            }
            else
                JOptionPane.showMessageDialog(null, this.delete + 
                        " was not found in thid file: " + this.txtFile.getAbsolutePath());
        }
    }
    
    private void normalize()
    {
        if( this.willNormalize )
        {
            if(this.normalize.equals(","))
                this.loadedText = this.loadedText.replaceAll(this.normalize, "");
            else
                this.loadedText = this.loadedText.replaceAll("\\s{1,}", this.normalize);
            
        }
    }
    
    private void replace()
    {   
        if(this.willReplace)
        {
            this.replace = this.replace.replace("\\n", "\n");
            this.replace = this.replace.replace("\\t", "\t");
            this.replace = this.replace.replace("\\r", "\r");

            this.with = this.with.replace("\\n", "\n");
            this.with = this.with.replace("\\t", "\t");
            this.with = this.with.replace("\\r", "\r");
            this.loadedText = this.loadedText.replaceAll(this.replace, this.with);
        }
    }
    
    private void groupData()
    {
        if(this.willGroup)
        {
            String[] temp = this.loadedText.split(this.normalize);
            
            this.separator = this.separator.replace("\\n", "\n");
            this.separator = this.separator.replace("\\t", "\t");
            this.separator = this.separator.replace("\\r", "\r");
            
            long count = 1;
            this.loadedText = "";
            for(String item : temp )
            {
                if(count%this.group == 0)
                    this.loadedText = this.loadedText + item + "\n";
                else
                    this.loadedText = this.loadedText + item + this.separator;
                count++;
            }
        }

    }
    
    private void loadTextfromFile()
    {
        try {
            
            this.loadedText = new String( Files.readAllBytes( Paths.get( this.txtFile.getAbsolutePath() ) ) );
                        
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "There's something wrong with the file " + 
                    this.txtFile.getName() + ": " + ex.getMessage());
        }
        
    }

    public void setLowercase(boolean lowercase) {
        this.lowercase = lowercase;
    }
    
    public void setDeleteBefore(boolean deleteBefore) {
        this.deleteBefore = deleteBefore;
    }

    public void setReplace(String replace) {
        this.replace = replace;
    }

    public void setWith(String with) {
        this.with = with;
    }

    public void setNormalize(String normalize) {
        this.normalize = normalize;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public void setSaveAs(String saveAs) {
        this.saveAs = saveAs;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public void setWillReplace(boolean willReplace) {
        this.willReplace = willReplace;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public void setWillSaveAs(boolean willSaveAs) {
        this.willSaveAs = willSaveAs;
    }

    public void setWillDelete(boolean willDelete) {
        this.willDelete = willDelete;
    }

    public void setWillCase(boolean willCase) {
        this.willCase = willCase;
    }

    public void setWillGroup(boolean willGroup) {
        this.willGroup = willGroup;
    }

    public void setWillNormalize(boolean willNormalize) {
        this.willNormalize = willNormalize;
    }   
    
}
