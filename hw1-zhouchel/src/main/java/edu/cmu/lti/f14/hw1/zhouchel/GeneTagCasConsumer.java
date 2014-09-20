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
  private final String filePath = "hw1-zhouchel.out";

  private BufferedWriter writer;

  @Override
  public void initialize() {
    writer = null;
    File file = new File(filePath);
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
