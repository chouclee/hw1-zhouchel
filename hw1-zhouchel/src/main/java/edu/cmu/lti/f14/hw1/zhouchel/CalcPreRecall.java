package edu.cmu.lti.f14.hw1.zhouchel;

import java.util.*;
import java.io.*;

/**
 * Evaluations for generated output, compared to golden standard output
 * 
 * @author zhouchel
 * 
 */
public class CalcPreRecall {
  /** Number of entries in golden standard output file */
  private int goldNum;

  /** Number of entries in generated output file */
  private int predNum;

  /** Number of true positive entries in generated output file */
  private int truePositive;

  private HashSet<String> goldStandardSet;

  /**
   * Constructor for CalcPreRecall
   * 
   * @param goldStandard
   *          standard output file path
   * @param output
   *          generated output for evaluation
   */
  public CalcPreRecall(String goldStandard, String output) {
    goldStandardSet = new HashSet<String>();
    BufferedReader in = null;
    try {
      // get total number of
      LineNumberReader lnr = new LineNumberReader(new FileReader(goldStandard));
      lnr.skip(Long.MAX_VALUE); // skip the entir file, reach the end of file
      goldNum = lnr.getLineNumber();
      lnr.close();

      // build gold standard set
      in = new BufferedReader(new FileReader(goldStandard));
      for (int i = 0; i < goldNum; i++) {
        goldStandardSet.add(in.readLine().trim());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    truePositive = 0;
    initialize(output);
  }

  /*
   * (non-javadoc) compare the results with golden standard output
   */
  private void initialize(String output) {
    BufferedReader in = null;

    try {
      // get total number of extracted gene names
      LineNumberReader lnr;
      lnr = new LineNumberReader(new FileReader(output));
      lnr.skip(Long.MAX_VALUE);
      predNum = lnr.getLineNumber();
      lnr.close();

      // iterate through results to see how many names are TP/FP
      in = new BufferedReader(new FileReader(output));
      for (int i = 0; i < predNum; i++) {
        if (goldStandardSet.contains(in.readLine().trim()))
          truePositive++;
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Calculate precision, which equals to true positive in predicted <br>
   * results over predicated results
   * 
   * @return Precision of this prediction
   */
  public double precision() {
    return (double) truePositive / predNum;
  }

  /**
   * Calculate recall, which equals to true positive in predicted results<br>
   * over standard results
   * 
   * @return Recall of this prediction
   */
  public double recall() {
    return (double) truePositive / goldNum;
  }

  /**
   * Calculate f1 Score, which equals to 2*precision * recall&#47(precision + recall)<br>
   * 
   * @return Recall of this prediction
   */
  public double f1score() {
    return 2 * (double) truePositive / (predNum + goldNum);
  }

}
