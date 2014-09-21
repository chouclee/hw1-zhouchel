package edu.cmu.lti.f14.hw1.zhouchel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceProcessException;

public class GeneTagCasConsumer extends CasConsumer_ImplBase {
  
  /**
   * Name of configuration parameter that  be set to the path of output file(optional)
   */
  public static final String PARAM_OUTPUT = "OutputFile";

  private BufferedWriter writer;

  @Override
  public void initialize() {
    writer = null;
    String output = ((String)getConfigParameterValue(PARAM_OUTPUT)).trim();
    File file = new File(output);
    try {
      writer = new BufferedWriter(new FileWriter(file, false));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void processCas(CAS aCas) throws ResourceProcessException {
    // TODO Auto-generated method stub
    JCas jcas;
    try {
      // You can create a JCas object from a CAS object by calling
      // the getJCas() method on the CAS object. (By SDK)
      jcas = aCas.getJCas();
    } catch (CASException e) {
      throw new ResourceProcessException(e);
    }
    String id, geneName;
    int begin, end;
    FSIterator<Annotation> iter = jcas.getAnnotationIndex(GeneTag.type).iterator();
    while (iter.hasNext()) {
      GeneTag geneAnnotation = (GeneTag) iter.next();
      id = geneAnnotation.getId();
      geneName = geneAnnotation.getGeneName();
      begin = geneAnnotation.getBegin();
      end = geneAnnotation.getEnd();
      try {
        writer.write(id + "|" + begin + " " + end + "|" + geneName + "\n");
      } catch (IOException e) {
        throw new ResourceProcessException(e);
      }
    }

    try {
      writer.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
