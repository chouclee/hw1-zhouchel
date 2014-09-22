package edu.cmu.lti.f14.hw1.zhouchel;

import java.util.Map;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

/**
 * This annotator uses Stanford coreNLP to annotate all gene mentions.
 * 
 * @author zhouchel
 * 
 */
public class GeneAnnotator extends JCasAnnotator_ImplBase {
  /**
   * Use <br>
   * {@link edu.cmu.lti.f14.hw1.zhouchel.PosTagNamedEntityRecognizer#getGeneSpans(String)}<br>
   * to detect Gene named, then updated JCas
   * 
   * @param aJCas
   *          CAS containing TextTag annotation added in previous phrase, and to which GeneTag
   *          annotations are to be written.
   * 
   * @see org.apache.uima.analysis_component.JCasAnnotator_ImplBase#process(JCas)
   */
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    PosTagNamedEntityRecognizer ner = null;
    try {
      ner = new PosTagNamedEntityRecognizer();
    } catch (ResourceInitializationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    String id, text, gene; // sentence id, sentence text and gene name
    int begin, end; // original(i.e. no white space elimination) gene name position in text

    // declare a feature structure(TextTag type) iterator
    FSIterator<Annotation> iter = aJCas.getAnnotationIndex(TextTag.type).iterator();

    // use this iterator to traverse all TextTag annotation and extract gene names.
    while (iter.hasNext()) {
      // get TextTag annotation
      TextTag annotation = (TextTag) iter.next();

      id = annotation.getId();            // get sentence ID
      text = annotation.getText();        // get sentence text
      Map<Integer, Integer> begin2end = ner.getGeneSpans(text);
      for (Map.Entry<Integer, Integer> entry : begin2end.entrySet()) {
        begin = entry.getKey();           // get begin position
        end = entry.getValue();           // get end position
        gene = text.substring(begin, end);// get gene name

        GeneTag geneAnnotation = new GeneTag(aJCas);
        geneAnnotation.setBegin(begin);   // set begin position
        geneAnnotation.setEnd(end);       // set end position
        geneAnnotation.setId(id);         // set sentence ID
        geneAnnotation.setText(text);     // set original text
        geneAnnotation.setGeneName(gene); // set gene name
        geneAnnotation.addToIndexes();    // add this FeatureStructure to Cas index
      }
    }
  }
}
