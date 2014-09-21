package edu.cmu.lti.f14.hw1.zhouchel;

import java.util.*;
import java.io.*;

public class CalcPreRecall {
  private int goldNum;
  private int predNum;
  private int truePositive;
  private HashSet<String> goldStandardSet;
  
  public  CalcPreRecall(String goldStandard, String output) {
     goldStandardSet = new HashSet<String>();
     BufferedReader in = null;
     try {
       // get total number of 
       LineNumberReader lnr = new LineNumberReader(new FileReader(goldStandard));
       lnr.skip(Long.MAX_VALUE);
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
  
  public double precision() {
    return (double)truePositive / predNum;
  }
  
  public double recall() {
    return (double)truePositive / goldNum;
  }
  
  public double f1score() {
    return 2*(double)truePositive / (predNum + goldNum);
  }
  
}
