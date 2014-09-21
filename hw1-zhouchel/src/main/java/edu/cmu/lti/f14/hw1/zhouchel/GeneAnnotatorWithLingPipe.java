package edu.cmu.lti.f14.hw1.zhouchel;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import com.aliasi.chunk.Chunk;

public class GeneAnnotatorWithLingPipe extends JCasAnnotator_ImplBase {

  public void process(JCas aJCas) throws AnalysisEngineProcessException {

    LingPipeGeneNamedEntityRecognizer ner = null;
    try {
      String model = "ne-en-bio-genetag.HmmChunker";
      ner = new LingPipeGeneNamedEntityRecognizer(model, 15, 0.65);
    } catch (ResourceInitializationException e) {
      // TODO Auto-generated catch block
      System.out.println("Failed to load Model");
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
      for (Chunk chunk : ner.chunk(text)) {
        begin = chunk.start();
        end = chunk.end();
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
