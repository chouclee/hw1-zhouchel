package edu.cmu.lti.f14.hw1.zhouchel;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;

public class TextAnnotator extends JCasAnnotator_ImplBase {
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    String documentText = aJCas.getDocumentText();
    System.out.println("entering textannotator");
    String[] textOfEachLine = documentText.split("\\r?\\n");
    for (int i = 0; i < textOfEachLine.length; i++) {
      // find the index where first space character appears
      int splitIdx = textOfEachLine[i].indexOf(" ");
      String id = textOfEachLine[i].substring(0, splitIdx);
      String text = textOfEachLine[i].substring(splitIdx + 1).trim();
      TextTag annotation = new TextTag(aJCas);
      annotation.setId(id);
      annotation.setText(text);
      annotation.addToIndexes();
      getContext().getLogger().log(Level.FINEST, "Found: " + annotation);
    }
  }
}
