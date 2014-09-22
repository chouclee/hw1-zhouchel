package edu.cmu.lti.f14.hw1.zhouchel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

/**
 * 
 * 
 */
public class StringMapResource_impl implements StringMapResource, SharedResourceObject {
  private HashMap<String,String> mMap = new HashMap<String, String>();

  /**
   * @see org.apache.uima.resource.SharedResourceObject#load(DataResource)
   */
  public void load(DataResource aData) throws ResourceInitializationException {
    InputStream inStr = null;
    try {
      // open input stream to data
      inStr = aData.getInputStream();
      // read each line
      BufferedReader reader = new BufferedReader(new InputStreamReader(inStr));
      String line;
      while ((line = reader.readLine()) != null) {
        // the first tab on each line separates key from value.
        // Keys cannot contain whitespace.
        String[] splited = line.split("=");
        String key = splited[0];
        String val = splited[1];
        mMap.put(key, val);
      }
    } catch (IOException e) {
      throw new ResourceInitializationException(e);
    } finally {
      if (inStr != null) {
        try {
          inStr.close();
        } catch (IOException e) {
        }
      }
    }

  }

  /**
   * @see StringMapResource#get(String)
   */
  public String get(String aKey) {
    String result = mMap.get(aKey);
    return result;
  }

}

