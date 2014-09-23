package edu.cmu.lti.f14.hw1.zhouchel;

import java.io.*;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.jcas.JCas;
//import org.apache.uima.jcas.tcas.DocumentAnnotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.FileUtils;
import org.apache.uima.util.Progress;

/**
 * This CollectionReader will read document line by line.
 * @author zhouchel
 * 
 * @see org.apache.uima.collection.CollectionReader_ImplBase#initialize()
 */
public class TextCollectionReader extends CollectionReader_ImplBase {
  /**
   * Name of configuration parameter that must be set to the path of input file.
   */
  public static final String PARAM_INPUT = "InputFile";
  
  private BufferedReader in;
  @Override
  public void initialize() throws ResourceInitializationException {
    File file = new File(((String)getConfigParameterValue(PARAM_INPUT)).trim());
    try {
      in =  new BufferedReader(new FileReader(file));
    } catch (IOException e) {
      System.out.println("Failed to load input file");
    }
  }

  @Override
  /**
   * Get next line of the document, find the first occurrence of white space, set 
   * the left part of this white space as sentence ID, the right part as sentence text.
   * 
   * @see org.apache.uima.collection.CollectionReader#getNext(org.apache.uima.cas.CAS)
   */
  public void getNext(CAS aCas) throws IOException, CollectionException {
    // TODO Auto-generated method stub
    JCas jcas;
    try {
      // Create a JCas object from a CAS object by calling
      // the getJCas() method on the CAS object.
      jcas = aCas.getJCas();
    } catch (CASException e) {
      throw new CollectionException(e);
    }
    // read line
    String line = in.readLine().trim();
    
    // find the index where first space character appears
    int splitIdx = line.indexOf(" ");
    
    // get sentence ID
    String id = line.substring(0, splitIdx);
    
    // get sentence text
    String text = line.substring(splitIdx + 1).trim();
    
    TextTag annotation = new TextTag(jcas);
    annotation.setId(id);       // set sentence ID                     
    annotation.setText(text);   // set sentence text
    annotation.addToIndexes();  // add feature structure to Cas index
  }

  @Override
  /**
   * @see org.apache.uima.collection.base_cpm.BaseCollectionReader#close()
   */
  public void close() throws IOException {
    // TODO Auto-generated method stub
    try {
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  /**
   * @see org.apache.uima.collection.base_cpm.BaseCollectionReader#getProgress()
   */
  public Progress[] getProgress() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  /**
   * @see org.apache.uima.collection.base_cpm.BaseCollectionReader#hasNext()
   */
  public boolean hasNext() throws IOException, CollectionException {
    // TODO Auto-generated method stub
    // if the input file has a next line, return true
    if (in != null && in.ready())
      return true;
    return false;
  }

}
