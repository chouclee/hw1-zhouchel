package edu.cmu.lti.f14.hw1.zhouchel;

import org.apache.uima.resource.ResourceInitializationException;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunker;
import com.aliasi.chunk.ConfidenceChunker;
import com.aliasi.util.AbstractExternalizable;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Statistical Gene Named Entity Recognizer using LingPipe toolkit.<br>
 * This recognizer support two chunking methods:<br>
 * First-Best Named Entity Chunking and N-Best Named Entity Chunking
 * 
 * @author zhouchel
 *
 */
public class LingPipeGeneNamedEntityRecognizer {
  private Chunker chunker;

  private ConfidenceChunker cfdchunker;
  
  /** the maximum number of the best chunks returned by ConfidenceChunker*/
  private int MAX_N_BEST_CHUNKS; 
  
  /** threshold to decide which chuck would be added to final results*/
  private double threshold; 
  
  private ClassLoader loader = LingPipeGeneNamedEntityRecognizer.class.getClassLoader();

  /**
   * Constructor for LingPipeGeneNamedEntityRecognizer using First-Best Chunker.
   * 
   * @param modelFile
   *          file name of the model
   * @throws ResourceInitializationException
   */
  public LingPipeGeneNamedEntityRecognizer(String modelFile) throws ResourceInitializationException {
    URL modelURL = loader.getResource(modelFile); // get model filename
    
    File model = new File(modelURL.getPath());    // get file path to the model
    System.out.println("Reading chunker from file=" + model);
    try {
      // initialize a First BestChunker
      chunker = (Chunker) AbstractExternalizable.readObject(model);
      cfdchunker = null;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Constructor for LingPipeGeneNamedEntityRecognizer using ConfidenceChunker. 
   * 
   * @param modelFile
   *          file name of the model
   * @param MAX_N_BEST_CHUNKS
   *          The maximum number of the best chunks returned by ConfidenceChunker 
   * @param threshold
   *          Confident threshold for accepting chunks 
   * @throws ResourceInitializationException
   */
  public LingPipeGeneNamedEntityRecognizer(String modelFile, int MAX_N_BEST_CHUNKS, double threshold)
          throws ResourceInitializationException {
    URL modelURL = loader.getResource(modelFile); // get model filename
    
    File model = new File(modelURL.getPath());    // get file path to the model
    System.out.println("Reading chunker from file=" + model);
    try {
      // initialize a ConfidenceChunker
      cfdchunker = (ConfidenceChunker) AbstractExternalizable.readObject(model);
      chunker = null;
      this.MAX_N_BEST_CHUNKS = MAX_N_BEST_CHUNKS;
      this.threshold = threshold;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  /**
   * Chuck String into Gene name chunks.
   * This method can be used for both First-BestChunker and ConfidenceChunker.<br>
   * Call {@link com.aliasi.chunk.Chunker#chunk(CharSequence)} for First-BestChunker.<br>
   * Call {@link com.aliasi.chunk.ConfidenceChunker#nBestChunks(char[], int, int, int)}
   * for ConfidenceChunker.
   * @param text
   *          String to be analyzed by Chunker
   * @return
   *          Chunk set of all extracted chunks
   */
  public Iterable<Chunk> chunk(String text) {
    if (this.chunker == null && this.cfdchunker == null)
      return null;
    else if (this.chunker != null) // First-BestChunker
      return this.chunker.chunk(text).chunkSet();
    else { // ConfidenceChunker
      Iterator<Chunk> it = cfdchunker.nBestChunks(text.toCharArray(), 0, text.length(),
              MAX_N_BEST_CHUNKS); // return first MAX_N_BEST_CHUNKS number of best chunks
      List<Chunk> result = new ArrayList<Chunk>();
      Chunk chunk;
      while (it.hasNext()) {
        chunk = it.next();
        // calculate the confidence, add chunks whose confidence are larger than threshold
        // into a Set, return the Set
        if (Math.pow(2.0, chunk.score()) > this.threshold)
          result.add(chunk);
      }
      return result;
    }
  }
}
