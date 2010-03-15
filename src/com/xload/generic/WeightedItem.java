/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.xload.generic;

/**
 *
 * @author madrax
 */
public class WeightedItem implements Comparable {

  private String key;
  private Integer weight;

  public WeightedItem() {
    
  }

  public WeightedItem(String key, Integer weight) {
    this.key = key;
    this.weight = weight;
  }

  @Override
  public String toString() {
    return this.getKey()+" ("+this.getWeight()+")";
  }
  

  @Override
  public boolean equals(Object obj) {
    try {
      WeightedItem wlist = (WeightedItem) obj;
      return this.getKey().equals(wlist.getKey());
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 29 * hash + (this.key != null ? this.key.hashCode() : 0);
    return hash;
  }

  public int compareTo(Object obj) {
    try {
      WeightedItem wlist = (WeightedItem) obj;
      return this.getWeight() - wlist.getWeight();
    } catch (Exception e) {
      return -1;
    }
  }

  /**
   * @return the key
   */
  public String getKey() {
    return key;
  }

  /**
   * @param key the key to set
   */
  public void setKey(String key) {
    this.key = key;
  }

  /**
   * @return the weight
   */
  public Integer getWeight() {
    return weight;
  }

  /**
   * @param weight the weight to set
   */
  public void setWeight(Integer weight) {
    this.weight = weight;
  }

  
}
