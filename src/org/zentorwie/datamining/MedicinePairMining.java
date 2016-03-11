package org.zentorwie.datamining;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * Use Apriori Algorithm to mining medicine pairs.
 */
public class MedicinePairMining {
  public static void main(String[] args) {
    long startTime, ioTime = 0, algoTime;
    startTime = System.currentTimeMillis();
    try {
      String line;
      Scanner in = new Scanner(new FileInputStream(new File("data.csv")));
      ArrayList<Set<String>> dataSet = new ArrayList<>();
      while (in.hasNext()) {
        line = in.nextLine();
        String[] row = line.split(", ");
        dataSet.add(new HashSet<>(Arrays.asList(row)));
      }
      ioTime = System.currentTimeMillis() - startTime;
      Apriori apriori = new Apriori(dataSet);
      ArrayList<AssociationRule> rules = apriori.getAssociationRules(0.01);
      Collections.sort(rules);

      PrintWriter out = new PrintWriter(new FileOutputStream(new File("result.txt")));

      for (AssociationRule r : rules) {
        if (r.left.size() == 1 && r.right.size() == 1) {
          out.println(r);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    algoTime = System.currentTimeMillis() - ioTime - startTime;
    System.out.printf("Done.\nIO time: %s ms\nAlgorithm running time: %s ms\nTotal time: %s ms\n",
        ioTime, algoTime, System.currentTimeMillis() - startTime);
    System.out.printf("The result has been saved into 'result.txt'\n");
  }
}
