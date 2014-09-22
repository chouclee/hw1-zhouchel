package edu.cmu.lti.f14.hw1.zhouchel;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;

/**
 * This annotator would find out all sentences in the sentences in a document.<br>
 * Each line represents a sentence.
 * 
 * @author zhouchel
 */
public class TextAnnotator extends JCasAnnotator_ImplBase {
  /**
   * This method here would split documents into lines.
   * For each line, find the first occurrence of white space, set the left part of<br>
   * this white space as sentence ID, the right part as sentence text.
   * 
   * @param aJCas
   *          CAS containing document text, and to which new annotations are to be written.
   * 
   * @see org.apache.uima.analysis_component.JCasAnnotator_ImplBase#process(JCas)
   */
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    String documentText = aJCas.getDocumentText(); // get document text
    System.out.println("Entering textannotator");
    String[] textOfEachLine = documentText.split("\\r?\\n");
    for (int i = 0; i < textOfEachLine.length; i++) {
      // find the index where first space character appears
      int splitIdx = textOfEachLine[i].indexOf(" ");
      
      // set sentence ID
      String id = textOfEachLine[i].substring(0, splitIdx);
      
      // set sentence text
      String text = textOfEachLine[i].substring(splitIdx + 1).trim();
      
      // add feature structure to Cas index
      TextTag annotation = new TextTag(aJCas);
      annotation.setId(id);
      annotation.setText(text);
      annotation.addToIndexes();
      getContext().getLogger().log(Level.FINEST, "Found: " + annotation);
    }
  }
}
