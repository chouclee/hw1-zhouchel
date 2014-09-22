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

public class LingPipeGeneNamedEntityRecognizer {
  private Chunker chunker;

  private ConfidenceChunker cfdchunker;

  private int MAX_N_BEST_CHUNKS;
  
  private double threshold;
  
  private ClassLoader loader = LingPipeGeneNamedEntityRecognizer.class.getClassLoader();

  public LingPipeGeneNamedEntityRecognizer(String modelFile) throws ResourceInitializationException {
    
    URL modelURL = loader.getResource(modelFile);
    File model = new File(modelURL.getPath());
    System.out.println("Reading chunker from file=" + model);
    try {
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

  public LingPipeGeneNamedEntityRecognizer(String modelFile, int MAX_N_BEST_CHUNKS, double threshold)
          throws ResourceInitializationException {
    URL modelURL = loader.getResource(modelFile);
    File model = new File(modelURL.getPath());
    System.out.println("Reading chunker from file=" + model);
    try {
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

  public Iterable<Chunk> chunk(String text) {
    if (this.chunker == null && this.cfdchunker == null)
      return null;
    else if (this.chunker != null)
      return this.chunker.chunk(text).chunkSet();
    else {
      Iterator<Chunk> it = cfdchunker.nBestChunks(text.toCharArray(), 0, text.length(),
              MAX_N_BEST_CHUNKS);
      List<Chunk> result = new ArrayList<Chunk>();
      Chunk chunk;
      while (it.hasNext()) {
        chunk = it.next();
        if (Math.pow(2.0, chunk.score()) > this.threshold)
          result.add(chunk);
      }
      return result;
    }
  }
}
