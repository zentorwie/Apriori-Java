package org.zentorwie.datamining;

import java.util.Set;

public class AssociationRule implements Comparable<AssociationRule> {
  public final Set<String> left;
  public final Set<String> right;
  public final double support;
  public final double confidence;

  AssociationRule(Set<String> left, Set<String> right, double support, double confidence) {
    this.left = left;
    this.right = right;
    this.support = support;
    this.confidence = confidence;
  }

  public int compareTo(AssociationRule other) {
    return Double.compare(other.support, support);
  }

  public String toString() {
    return String.format("%s => %s : %.3f, %.3f", left, right, support * 100, confidence * 100);
  }
}
