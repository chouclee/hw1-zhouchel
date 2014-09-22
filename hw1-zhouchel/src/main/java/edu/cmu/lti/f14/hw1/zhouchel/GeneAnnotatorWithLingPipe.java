package edu.cmu.lti.f14.hw1.zhouchel;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceAccessException;
import org.apache.uima.resource.ResourceInitializationException;

import com.aliasi.chunk.Chunk;

public class GeneAnnotatorWithLingPipe extends JCasAnnotator_ImplBase {
  /** Map from paramConfig */
  private StringMapResource mMap;
  private LingPipeGeneNamedEntityRecognizer ner;
  @Override
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    //String model = ((String)aContext.getConfigParameterValue(PARAM_MODEL)).trim();
   super.initialize(aContext);
   this.ner = null;
   try {
     //String model = "src/main/resources/ne-en-bio-genetag.HmmChunker";
      mMap = (StringMapResource) getContext().getResourceObject("paramConfig");
      String model = mMap.get("model");
      int MAX_N_BEST_CHUNKS = Integer.parseInt(mMap.get("MAX_N_BEST_CHUNKS"));
      double threshold = Double.parseDouble(mMap.get("threshold"));
      ner = new LingPipeGeneNamedEntityRecognizer(model, MAX_N_BEST_CHUNKS, threshold);
    } catch (ResourceAccessException e) {
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
