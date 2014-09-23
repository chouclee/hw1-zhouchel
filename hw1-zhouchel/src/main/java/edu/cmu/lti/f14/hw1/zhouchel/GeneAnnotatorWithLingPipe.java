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

/**
 * This annotator uses LingPipe toolkit to annotate all gene mentions.
 * 
 * @author zhouchel
 */
public class GeneAnnotatorWithLingPipe extends JCasAnnotator_ImplBase {
  /** Map from paramConfig */
  public static StringMapResource mMap;

  private LingPipeGeneNamedEntityRecognizer ner;

  @Override
  /**
   * Provides access to external resources (other than the CAS)<br>
   * Load parameters configuration from file paramConfig
   * 
   * @param aContext
   *          provides UIMA resources with all access to external resources (other than the CAS)
   *          
   * @see AnalysisComponent#initialize(UimaContext)
   */
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);
    this.ner = null;
    try {
      // get hashMap of all parameters set in paramConfig
      mMap = (StringMapResource) getContext().getResourceObject("paramConfig");

      // get the file name of selected model
      String model = mMap.get("model");
      
      String useNBest = mMap.get("N-Best_NER");
      if (useNBest.equalsIgnoreCase("true")) {
        // get parameter for MAX_N_BEST_CHUNKS
        int MAX_N_BEST_CHUNKS = Integer.parseInt(mMap.get("MAX_N_BEST_CHUNKS"));

        // get parameter for threshold
        double threshold = Double.parseDouble(mMap.get("threshold"));

        // initialize LingPipeGeneNamedEntityRecognizer, use ConfidenceChunker
        ner = new LingPipeGeneNamedEntityRecognizer(model, MAX_N_BEST_CHUNKS, threshold);
      } else {
        // initialize LingPipeGeneNamedEntityRecognizer, use First-BestChunker
        ner = new LingPipeGeneNamedEntityRecognizer(model);
      }
    } catch (ResourceAccessException e) {
      // TODO Auto-generated catch block
      System.out.println("Failed to load Model");
      e.printStackTrace();
    }
  }

  /**
   * Use {@link edu.cmu.lti.f14.hw1.zhouchel.LingPipeGeneNamedEntityRecognizer#chunk(String)}
   * to detect Gene named, then updated JCas
   * 
   * @param aJCas
   *          CAS containing TextTag annotation added in previous phrase, and to which GeneTag
   *          annotations are to be written.
   * 
   * @see org.apache.uima.analysis_component.JCasAnnotator_ImplBase#process(JCas)
   */
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    String id, text, gene; // sentence id, sentence text and gene name
    int begin, end; // original(i.e. no white space elimination) gene name position in text

    // declare a feature structure(TextTag type) iterator
    FSIterator<Annotation> iter = aJCas.getAnnotationIndex(TextTag.type).iterator();
    while (iter.hasNext()) {

      TextTag annotation = (TextTag) iter.next();
      id = annotation.getId(); // get sentence ID
      text = annotation.getText(); // get sentence text
      for (Chunk chunk : ner.chunk(text)) {
        begin = chunk.start(); // get begin position of this chunk
        end = chunk.end(); // get end position of this chunk
        gene = text.substring(begin, end);// get gene name

        GeneTag geneAnnotation = new GeneTag(aJCas);
        geneAnnotation.setBegin(begin); // set begin position
        geneAnnotation.setEnd(end); // set end position
        geneAnnotation.setId(id); // set sentence ID
        geneAnnotation.setText(text); // set original text
        geneAnnotation.setGeneName(gene); // set gene name
        geneAnnotation.addToIndexes(); // add this FeatureStructure to Cas index
      }
    }
  }
}
