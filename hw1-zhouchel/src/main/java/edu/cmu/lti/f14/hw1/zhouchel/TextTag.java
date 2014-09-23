

/* First created by JCasGen Mon Sep 22 01:22:41 EDT 2014 */
package edu.cmu.lti.f14.hw1.zhouchel;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** extract sentence from input file
 * Updated by JCasGen Mon Sep 22 01:22:41 EDT 2014
 * XML source: src/main/resources/typeSystemDescriptor.xml
 * @generated */
public class TextTag extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(TextTag.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated  */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected TextTag() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public TextTag(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public TextTag(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public TextTag(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: id

  /** getter for id - gets 
   * @generated */
  public String getId() {
    if (TextTag_Type.featOkTst && ((TextTag_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "edu.cmu.lti.f14.hw1.zhouchel.TextTag");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TextTag_Type)jcasType).casFeatCode_id);}
    
  /** setter for id - sets  
   * @generated */
  public void setId(String v) {
    if (TextTag_Type.featOkTst && ((TextTag_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "edu.cmu.lti.f14.hw1.zhouchel.TextTag");
    jcasType.ll_cas.ll_setStringValue(addr, ((TextTag_Type)jcasType).casFeatCode_id, v);}    
   
    
  //*--------------*
  //* Feature: text

  /** getter for text - gets 
   * @generated */
  public String getText() {
    if (TextTag_Type.featOkTst && ((TextTag_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "edu.cmu.lti.f14.hw1.zhouchel.TextTag");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TextTag_Type)jcasType).casFeatCode_text);}
    
  /** setter for text - sets  
   * @generated */
  public void setText(String v) {
    if (TextTag_Type.featOkTst && ((TextTag_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "edu.cmu.lti.f14.hw1.zhouchel.TextTag");
    jcasType.ll_cas.ll_setStringValue(addr, ((TextTag_Type)jcasType).casFeatCode_text, v);}    
  }

    