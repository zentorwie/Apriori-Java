package org.zentorwie.datamining;

import java.util.*;

/**
 * The Apriori Algorithm implemented in Java for association rule mining.
 * @author Dongyu Zeng
 * @version 1.1
 */

public class Apriori {
  private ArrayList<Set<String>> dataSet;
  private double minSupport;

  private ArrayList<Set<String>> frequentSet;

  /**
   * Initialization method to import dataset.
   * @param dataSet The list of item set.
   */
  Apriori(ArrayList<Set<String>> dataSet) {
    this.dataSet = dataSet;
  }

  /**
   * Get association rules according to the dataset imported.
   * @param minSupport The minimum support
   * @return List of association rules.
   */
  public ArrayList<AssociationRule> getAssociationRules(double minSupport) {
    this.minSupport = minSupport;
    this.frequentSet = getFrequentSet();

    return generateRules();
  }

  private ArrayList<Set<String>> getFrequentSet() {
    ArrayList<Set<Set<String>>> L = new ArrayList<>();

    // 0-Set
    L.add(null);

    // Get the set that contains all item
    Set<String> allItems = new HashSet<>();
    for (Set<String> transaction : dataSet) {
      for (String item : transaction) {
        allItems.add(item);
      }
    }

    // System.out.printf("AllItems: %s\n", allItems);

    // Get large 1-itemset
    L.add(new HashSet<>());
    for (String item : allItems) {
      Set<String> oneSet = new HashSet<>();
      oneSet.add(item);
      double support = getSupport(oneSet);
      if (support >= minSupport) {
        L.get(1).add(oneSet);
      }
    }

    int k = 2;
    while (L.get(k-1).size() != 0) {
      // System.out.printf("L[k-1]: %s\n", L.get(k-1));
      L.add(new HashSet<>());

      Set<Set<String>> C = new HashSet<>();

      for (Set<String> a : L.get(k-1)) {
        for (String b : allItems) {
          if (a.contains(b)) continue;
          Set<String> c = new HashSet<>();
          c.addAll(a);
          c.add(b);
          // System.out.println(c);
          if (C.contains(c)) continue;
          if (!checkSet(c, L.get(k-1))) continue;
          C.add(c);
        }
      }

      for (Set<String> c : C) {
        if (getSupport(c) < minSupport) continue;
        L.get(k).add(c);
      }
      k++;
    }

    ArrayList<Set<String>> frequentSet = new ArrayList<>();
    for (int i = 1; i < k; i++) {
      frequentSet.addAll(L.get(i));
    }

    return frequentSet;
  }

  private boolean checkSet(Set<String> S, Set<Set<String>> L) {
    Set<String> S2 = new HashSet<>();
    S2.addAll(S);

    for (String item : S) {
      S2.remove(item);
      if (!L.contains(S2)) return false;
      S2.add(item);
    }

    return true;
  }

  private double getSupport(Set<String> itemSet) {
    int count = 0;

    for (Set<String> transaction : dataSet) {
      if (transaction.containsAll(itemSet)) {
        count++;
      }
    }
    return (double)count / dataSet.size();
  }

  private ArrayList<AssociationRule> generateRules() {
    ArrayList<AssociationRule> rules = new ArrayList<>();
    for (Set<String> base : frequentSet) {
      if (base.size() == 1) continue;

      double baseSupport = getSupport(base);
      ArrayList<String> baseList = new ArrayList<>(base);

      class Inner {
        void generateRulesDFS(int cur, Set<String> left, Set<String> right) {
          if (cur == baseList.size()) {
            if (left.size() == 0 || right.size() == 0) return;

            double leftSupport = getSupport(left);
            double rightSupport = getSupport(right);
            if (leftSupport < minSupport || rightSupport < minSupport) return;

            AssociationRule rule = new AssociationRule(
                new HashSet<>(left), new HashSet<>(right), baseSupport, baseSupport / leftSupport);
            rules.add(rule);
          }
          else {
            String ele = baseList.get(cur);
            left.add(ele);
            generateRulesDFS(cur + 1, left, right);
            left.remove(ele);
            right.add(ele);
            generateRulesDFS(cur + 1, left, right);
            right.remove(ele);
          }
        }
      }

      // use depth-first search to generate all subset of base
      Inner dfs = new Inner();
      Set<String> left = new HashSet<>();
      Set<String> right = new HashSet<>();
      dfs.generateRulesDFS(0, left, right);
    }

    return rules;
  }


  private static void runTest(String[][] input) {
    ArrayList<Set<String>> dataSet = new ArrayList<>();
    for (String[] trans : input) {
      dataSet.add(new HashSet<>(Arrays.asList(trans)));
    }
    // System.out.println(dataSet);
    Apriori apirori = new Apriori(dataSet);
    ArrayList<AssociationRule> res = apirori.getAssociationRules(3.0/7.0);
  }

  public static void main(String[] args) throws Exception {
    runTest(new String[][] {
        {"1", "2", "3", "4"},
        {"1", "2", "4"},
        {"1", "2"},
        {"2", "3", "4"},
        {"2", "3"},
        {"3", "4"},
        {"2", "4"}});
  }

}
