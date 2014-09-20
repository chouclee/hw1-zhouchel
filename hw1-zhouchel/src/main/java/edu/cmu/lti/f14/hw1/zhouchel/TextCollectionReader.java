package edu.cmu.lti.f14.hw1.zhouchel;

import java.io.*;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.jcas.JCas;
//import org.apache.uima.jcas.tcas.DocumentAnnotation;
import org.apache.uima.resource.ResourceInitializationException;
//import org.apache.uima.util.FileUtils;
import org.apache.uima.util.Progress;

public class TextCollectionReader extends CollectionReader_ImplBase {
  /**
   * Name of configuration parameter that must be set to the path of the input file.
   */
  public static final String PARAM_INPUT = "InputFile";
  private BufferedReader in;
  private int lineNum;
  private int lineIdx;
  @Override
  public void initialize() throws ResourceInitializationException {
    this.lineIdx = 0;
    try {
      LineNumberReader lnr = new LineNumberReader(new FileReader(
              ((String)getConfigParameterValue(PARAM_INPUT)).trim()));
      lnr.skip(Long.MAX_VALUE);
      this.lineNum = lnr.getLineNumber();
      lnr.close();
      this.in = new BufferedReader(new FileReader(
              ((String)getConfigParameterValue(PARAM_INPUT)).trim()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void getNext(CAS arg0) throws IOException, CollectionException {
    // TODO Auto-generated method stub
    JCas jcas;
    try {
      jcas = arg0.getJCas();
    } catch (CASException e) {
      throw new CollectionException(e);
    }
    
    // feed next line to jcas
    jcas.setDocumentText(in.readLine().trim());
    lineIdx++;
  }

  @Override
  public void close() throws IOException {
    // TODO Auto-generated method stub

  }

  @Override
  public Progress[] getProgress() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean hasNext() throws IOException, CollectionException {
    // TODO Auto-generated method stub
    if (in != null && lineIdx < lineNum)
      return true;
    return false;
  }

}
