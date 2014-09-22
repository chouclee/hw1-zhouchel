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
 * This CollectionReader will read entire document into single CAS
 * @author zhouchel
 * 
 * @see org.apache.uima.collection.CollectionReader_ImplBase#initialize()
 */
public class TextCollectionReader extends CollectionReader_ImplBase {
  /**
   * Name of configuration parameter that must be set to the path of input file.
   */
  public static final String PARAM_INPUT = "InputFile";
  
  private File file;
  private boolean hasRead;
  @Override
  public void initialize() throws ResourceInitializationException {
    hasRead = false;
    file = new File(((String)getConfigParameterValue(PARAM_INPUT)).trim());
   /* try {
      LineNumberReader lnr = new LineNumberReader(new FileReader(
              ((String)getConfigParameterValue(PARAM_INPUT)).trim()));
      lnr.skip(Long.MAX_VALUE);
      this.lineNum = lnr.getLineNumber();
      lnr.close();
      in = new BufferedReader(new FileReader(
              ((String)getConfigParameterValue(PARAM_INPUT)).trim()));
    } catch (IOException e) {
      e.printStackTrace();
    }*/
  }

  @Override
  /**
   * getNext would be executed only once 
   * 
   * @see org.apache.uima.collection.CollectionReader#getNext(org.apache.uima.cas.CAS)
   */
  public void getNext(CAS aCas) throws IOException, CollectionException {
    // TODO Auto-generated method stub
    JCas jcas;
    try {
      // Create a JCas object from a CAS object by calling
      // the getJCas() method on the CAS object. (By SDK)
      jcas = aCas.getJCas();
    } catch (CASException e) {
      throw new CollectionException(e);
    }
    
    // read all file content into a string
    String text = FileUtils.file2String(file);
    // feed file context to jcas
    jcas.setDocumentText(text);
    hasRead = true;
    //lineIdx++;
  }

  @Override
  /**
   * @see org.apache.uima.collection.base_cpm.BaseCollectionReader#close()
   */
  public void close() throws IOException {
    // TODO Auto-generated method stub

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
    // if the input file has been processed return false
    if (file != null && !hasRead)
      return true;
    return false;
  }

}
