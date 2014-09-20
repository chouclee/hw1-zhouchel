package edu.cmu.lti.f14.hw1.zhouchel;

import java.util.Map;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

public class GeneAnnotator extends JCasAnnotator_ImplBase {
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    PosTagNamedEntityRecognizer ner = null;
    try {
      ner = new PosTagNamedEntityRecognizer();
    } catch (ResourceInitializationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    String id, text;
    int begin, end;
    String gene;

    FSIterator<Annotation> iter = aJCas.getAnnotationIndex(TextTag.type).iterator();
    while (iter.hasNext()) {

      TextTag annotation = (TextTag) iter.next();
      id = annotation.getId();
      text = annotation.getText();
      Map<Integer, Integer> begin2end = ner.getGeneSpans(text);
      for (Map.Entry<Integer, Integer> entry : begin2end.entrySet()) {
        begin = entry.getKey();
        end = entry.getValue();
        gene = text.substring(begin, end);
        begin = begin - countWhiteSpaces(text.substring(0, begin));
        end = begin + gene.length() - countWhiteSpaces(gene) - 1;

        GeneTag geneAnnotation = new GeneTag(aJCas);
        geneAnnotation.setBegin(begin);
        geneAnnotation.setEnd(end);
        geneAnnotation.setId(id);
        geneAnnotation.setGeneName(gene);
        geneAnnotation.addToIndexes();
      }
    }
  }

  private int countWhiteSpaces(String str) {
    int cnt = 0;
    for (int i = 0; i < str.length(); i++) {
      if (str.charAt(i) == ' ')
        cnt++;
    }
    return cnt;
  }
}
