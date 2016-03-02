package org.zentorwie.datamining;

import java.util.*;

/**
 * The Java Implement of Apriori Algorithm for association rule mining.
 * @author Dongyu Zeng
 * @version 1.0
 */

public class Apriori {

  public static ArrayList<AssociationRule> getAssociationRules(
      ArrayList<Set<String>> dataSet,
      double minSupport,
      int maxSetSize) {

    ArrayList<Set<String>> frequentSet = getFrequentSet(dataSet, minSupport, maxSetSize);

    System.out.println("frequentset:");
    System.out.println(frequentSet);

    ArrayList<AssociationRule> res = new ArrayList<>();
    // TODO
    return res;
  }

  public static ArrayList<Set<String>> getFrequentSet(
      ArrayList<Set<String>> dataSet,
      double minSupport,
      int maxSetSize) {

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

    System.out.printf("AllItems: %s\n", allItems);

    // Get large 1-itemset
    L.add(new HashSet<>());
    for (String item : allItems) {
      Set<String> oneSet = new HashSet<>();
      oneSet.add(item);
      double support = getSupport(oneSet, dataSet);
      if (support >= minSupport) {
        L.get(1).add(oneSet);
      }
    }

    int k = 2;
    while (L.get(k-1).size() != 0) {
      System.out.printf("L[k-1]: %s\n", L.get(k-1));
      L.add(new HashSet<>());

      Set<Set<String>> C = new HashSet<>();

      for (Set<String> a : L.get(k-1)) {
        for (String b : allItems) {
          if (a.contains(b)) continue;
          Set<String> c = new HashSet<>();
          c.addAll(a);
          c.add(b);
          System.out.println(c);
          if (C.contains(c)) continue;
          if (!checkSet(c, L.get(k-1))) continue;
          C.add(c);
        }
      }

      for (Set<String> c : C) {
        if (getSupport(c, dataSet) < minSupport) continue;
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

  public static boolean checkSet(Set<String> S, Set<Set<String>> L) {
    Set<String> S2 = new HashSet<>();
    S2.addAll(S);

    for (String item : S) {
      S2.remove(item);
      if (!L.contains(S2)) return false;
      S2.add(item);
    }

    return true;
  }

  public static double getSupport(Set<String> itemSet, ArrayList<Set<String>> dataSet) {
    int count = 0;

    for (Set<String> transaction : dataSet) {
      if (transaction.containsAll(itemSet)) {
        count++;
      }
    }
    return (double)count / dataSet.size();
  }

  public static ArrayList<AssociationRule> generateRules(
      ArrayList<Set<String>> frequentSet, double minSupport) {
    // TODO
    return new ArrayList<>();
  }


 public static void runTest(String[][] input) {
   ArrayList<Set<String>> dataSet = new ArrayList<>();
   for (String[] trans : input) {
     dataSet.add(new HashSet<>(Arrays.asList(trans)));
   }
   System.out.println(dataSet);
   ArrayList<AssociationRule> res = getAssociationRules(dataSet, 3.0/7.0, 10);
 }

  public static void main(String[] args) {
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
