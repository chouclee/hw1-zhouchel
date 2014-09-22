package edu.cmu.lti.f14.hw1.zhouchel;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import com.aliasi.chunk.Chunk;

public class GeneAnnotatorWithLingPipe extends JCasAnnotator_ImplBase {
  private LingPipeGeneNamedEntityRecognizer ner;
  @Override
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    //String model = ((String)aContext.getConfigParameterValue(PARAM_MODEL)).trim();
   this.ner = null;
   try {
     String model = "src/main/resources/ne-en-bio-genetag.HmmChunker";
     ner = new LingPipeGeneNamedEntityRecognizer(model, 15, 0.65);
   } catch (ResourceInitializationException e) {
     // TODO Auto-generated catch block
     System.out.println("Failed to load Model");
     e.printStackTrace();
   }
  }
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
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

        GeneTag geneAnnotation = new GeneTag(aJCas);
        geneAnnotation.setBegin(begin);
        geneAnnotation.setEnd(end);
        geneAnnotation.setId(id);
        geneAnnotation.setGeneName(gene);
        geneAnnotation.setText(text);
        geneAnnotation.addToIndexes();
      }
    }
  }
}
