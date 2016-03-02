package org.zentorwie.datamining;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;

public class AssociationRule implements Comparable<AssociationRule> {
  private Set<String> left;
  private Set<String> right;
  private double support;
  private double confidence;

  AssociationRule(Set<String> left, Set<String> right) {
    this.left = left;
    this.right = right;
  }

  public int compareTo(AssociationRule other) {
    return Double.compare(support, other.support);
  }

  public void setSupport(double support) {
    this.support = support;
  }
  public double getSupport() {
    return this.support;
  }

  public void setConfidence(double confidence) {
    this.confidence = confidence;
  }
  public double getConfidence(double confidence) {
    return this.confidence;
  }
}
