/**
 * @author Karan V Shah
 * @filename AprioriMainClass.java
 * @purpose Implementation of Apriori algorithm to compute frequent itemsets.
 * @CommandLine_input: unclean_filepath correctedmamapFilePath minimumSupport outputFilePath
 */

package com.apriori.kvs32;

import java.io.File;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.io.*;

public class AprioriMainClass {

    static String[] dataSet;
    static String outFileName;
    static List <String> dataSets;
    static Map<String, Integer> candidateSet1 = new HashMap<String, Integer>();
    static File inputFile, outputFile, correctFile, result;
    static double support, confidence, suppRows;
    static int dataRows = 0, setValues = 1;

    static FileOutputStream fos;
    static BufferedWriter bw;

    public static void main(String[] args) {

        AprioriMainClass mainClass = new AprioriMainClass();
        Scanner sc = new Scanner(System.in);

        //String fileName;
        int argsLength = args.length;
        switch(argsLength) {
            /*case 0:
                System.out.println("Enter the file name along with full path.");
                //fileName = sc.next();
                inputFile = new File(sc.next());
                System.out.println("Enter minimum support: ");
                support = sc.nextDouble();
                System.out.println("Enter minimum confidence");
                confidence = sc.nextDouble();
                break;
            case 1:
                //fileName = args[0];
                inputFile = new File(args[0]);
                support = 0.8;
                confidence = 0.6;
                break;
            case 2:
                //fileName = args[0];
                inputFile = new File(args[0]);
                support = Double.parseDouble(args[1]);
                confidence = 0.6;
                break;*/
            case 3:
                //fileName = args[0];
                inputFile   = new File(args[0]);
                support     = Double.parseDouble(args[1]);
                //confidence = Double.parseDouble(args[2]);
                outFileName = args[2];
                break;
            case 4:
                File temp   = new File(args[0]);
                String name = args[0];
                correctFile = new File(args[1]);
                support     = Double.parseDouble(args[2]);
                //confidence = Double.parseDouble(args[2]);
                outFileName = args[3];
                try {
                    inputFile = mainClass.cleanFormat(temp, name, correctFile);
                } catch(Exception e) {
                    System.out.println("Exception"+e);
                }
                break;
            default:
                System.out.println("Filename is missing. Terminating the Program.");
                break;
        }
        mainClass.calcSupport(); //To calculate the minimun count required. (Support)
        outputFile = new File("cs634_karanshah_apriori_"+suppRows+".txt");

        try {
            fos = new FileOutputStream(outputFile);
            bw  = new BufferedWriter(new OutputStreamWriter(fos));
        } catch (Exception e) {
            System.out.println("File error: "+e);
        }

        mainClass.readFile(sc, inputFile);  //Reading file and adding rows to dataSet array

        candidateSet1   = mainClass.countDistinctItems(dataSets);
        int count       = mainClass.allowedCandidate();
        System.out.println("No. rows: "+dataRows+" support: "+ suppRows);

        while(count != 0) {
            setValues++;
             candidateSet1  = mainClass.computeSet();
             count          = mainClass.allowedCandidate();
        }

        try {
            bw.close();
        } catch(Exception e){
            System.out.println("Close: "+e);
        }
    }

    File cleanFormat (File temp, String name, File clean) throws IOException{
        result              = new File("resultFile.txt");
        Map<Integer, String> tempResult = new HashMap<Integer, String>();
        List<String> lines  = new ArrayList<String>();
        List <String> datas = new ArrayList<String>();

        try {
            String currentLine;
            Scanner cleanF = new Scanner(new FileReader(clean));
            while (cleanF.hasNextLine()) {
                currentLine = cleanF.nextLine();
                lines.add(currentLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> correctData = new HashMap<String, String>();
        Iterator <String> tester = lines.iterator();
        String temps[];
        String currentLine;
        while(tester.hasNext()) {
            temps = tester.next().split(" ");
            correctData.put(temps[0], temps[1]);
        }

        //to replace or clear te data
        //String content = new String(File. readAllBytes(temp), charset);
        FileOutputStream fos1   = new FileOutputStream(result);
        BufferedWriter bw1      = new BufferedWriter(new OutputStreamWriter(fos1));
        Scanner sc              = new Scanner(new FileReader(temp));
        int rowLines            = 0;

        for (Map.Entry<String, String> allowed : correctData.entrySet()) {
            while(sc.hasNextLine()) {
                currentLine = sc.nextLine();
                String str  = currentLine.replaceAll(";", " ");
                if(str.length() == 0) {
                    //bw1.write(currentLine + "\n");
                    tempResult.put(rowLines, currentLine);
                } else {
                    //bw1.write(str + "\n");
                    tempResult.put(rowLines, str);
                }
                rowLines++;
            }
        }
        bw1.flush();

        //System.out.println("yes: "+sc1.hasNextLine());
        Scanner sc1 = new Scanner(result);
        int resLines;
        for (Map.Entry<String, String> allowed : correctData.entrySet()) {
            String key = allowed.getKey();
            String value = allowed.getValue();
            /*while (sc1.hasNextLine()) {
                currentLine = sc1.nextLine();
                String str = currentLine.replaceAll(value, key);
                System.out.println("cc: " + str);
                System.out.println("Cur: " + currentLine);
                if (str.length() == 0)
                    bw1.write(currentLine + "\n");
                else
                    bw1.write(str + "\n");
                bw1.flush();
            }*/
            for(Map.Entry<Integer, String> tempRes : tempResult.entrySet()) {
                currentLine = tempRes.getValue();
                String str  = currentLine.replaceAll(value, key);/*
                System.out.println("cc: " + Arrays.toString(str));
                System.out.println("Cur: " + currentLine);*/
                if (str.length() == 0)
                    tempResult.put(tempRes.getKey(), currentLine);
                else
                    tempResult.put(tempRes.getKey(), str);
            }
        }
        for(Map.Entry<Integer, String> tempRes : tempResult.entrySet()) {
            currentLine = tempRes.getValue();
            /*String str1[] = currentLine.split("(?<=\\G...)"); //currentLine.replaceAll("(?<=\\D......)(?=\\d)"," ");
            String str = String.join(" ", str1);*/

            String str1[] = currentLine.split(" ");
            //String tt[] = new String[str1.length + 100];
            List<String> tt = new ArrayList<String>();
            for ( int i = 0 ; i < str1.length; i++ ) {
                if (str1[i].length() == 6) {
                    //System.out.println(str1[i].substring(0,3)+" -- "+str1[i].substring(3));
                    tt.add(str1[i].substring(0,3));
                    tt.add(str1[i].substring(3));
                } else {
                     tt.add(str1[i]);
                }
            }
            String str = String.join(" ", tt );
            bw1.write(str+"\n");
        }

        bw1.close();
        return result;
    }

    Map computeSet() {
        Map<String, Integer> candidateSet   = new HashMap<String, Integer>();
        Iterator <String> distinct          = dataSets.iterator();
        String keys, temp[];
        StringBuilder key;

        while(distinct.hasNext()) {
            /*for(int i = 0; i < dataRows.lenth; i++){
                for(int j = i+1; j < dataRows.length; j++){
                    if()
                }
            }*/
            temp = distinct.next().split(" ");
            switch (setValues) {
                case 2:
                    for (int i = 0; i < temp.length; i++) {
                        for (int j = i + 1; j < temp.length; j++) {
                            //key = new StringBuilder(temp[i]).append(" ").append(temp[j]);
                            keys = temp[i].concat(" ").concat(temp[j]);
                            candidateSet.merge(keys, 1, Integer::sum);
                        }
                    }
                    break;
                case 3:
                    for (int i = 0; i < temp.length; i++) {
                        for (int j = i + 1; j < temp.length; j++) {
                            for (int k = j + 1; k < temp.length; k++) {
                                //key = new StringBuilder(temp[i]).append(" ").append(temp[j]);
                                keys = temp[i].concat(" ").concat(temp[j]).concat(" ").concat(temp[k]);
                                candidateSet.merge(keys, 1, Integer::sum);
                            }
                        }
                    }
                    break;
                case 4:
                    for (int i = 0; i < temp.length; i++) {
                        for (int j = i + 1; j < temp.length; j++) {
                            for (int k = j + 1; k < temp.length; k++) {
                                for (int l = k + 1; l < temp.length; l++) {
                                    //key = new StringBuilder(temp[i]).append(" ").append(temp[j]);
                                    keys = temp[i].concat(" ").concat(temp[j]).concat(" ").concat(temp[k]).concat(" ").concat(temp[l]);
                                    candidateSet.merge(keys, 1, Integer::sum);
                                }
                            }
                        }
                    }
                    break;
            }
        }
        return candidateSet;
    }

    void readFile(Scanner sc, File inputFile){
        try {
            String currentLine;
            sc = new Scanner(new FileReader(inputFile));
            //System.out.println("File: "+sc.next());
            dataSets = new ArrayList<>();
            while (sc.hasNextLine()) {
                currentLine = sc.nextLine();
                dataSets.add(currentLine);
                dataRows++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("File read successful.");
    }

    // Generating Candidate Set
    Map countDistinctItems(List dataSet){
        Iterator <String> distinct = dataSet.iterator();
        /*while(distinct.hasNext()){
            String temp[] = distinct.next().split(" ");
            for(int i = 0; i < temp.length; i++){
                */ /*int count = candidateSet.containsKey(temp[i]) ? Integer.parseInt(candidateSet.get(temp[i])) : 0;
                candidateSet.put(temp[i], count + 1);*//*
                Integer count = candidateSet1.get(temp[i]);
                if (count == null) {
                    candidateSet1.put(temp[i], 1);
                } else {
                    candidateSet1.put(temp[i], count + 1);
                }
            }
        }*/
        while (distinct.hasNext() ) {
            String temp[] = distinct.next().split(" ");
            for ( int i = 0; i < temp.length; i++ ) {
                candidateSet1.merge(temp[i], 1, Integer::sum);
            }
        }

        System.out.println(candidateSet1);
        /*System.out.println(Arrays.asList(candidateSet)); // method 1
        System.out.println(Collections.singletonList(candidateSet)); // method 2*/
        System.out.println("Added the distinct values");
        return candidateSet1;
    }

    int allowedCandidate() {
        List <String> removeKey = new ArrayList<>();
        for (Map.Entry<String, Integer> allowed : candidateSet1.entrySet()) {
            String key      = allowed.getKey();
            Integer value   = allowed.getValue();
            if(value < suppRows) {
                //candidateSet1.remove(key);
                removeKey.add(key);
            }
            System.out.println(key+" -- "+value);
        }
        Iterator <String> removable = removeKey.iterator();
        while( removable.hasNext() ) {
            String temp = removable.next();
            candidateSet1.remove(temp);
        }

        // have to remove this iterator and copy keyset to new allowed map
        System.out.println(candidateSet1);

        if (candidateSet1.size() > 0) {
            AprioriMainClass mainClass = new AprioriMainClass();
            try {
                mainClass.writeData();
            } catch (Exception e) {
                System.out.println("Exception : " + e);
            }
            return candidateSet1.size();
        } else {
            return 0;
        }
    }

    void writeData() throws IOException{
        
        bw.append("Candidate set: "+setValues+"\n");
        for (Map.Entry<String, Integer> allowed : candidateSet1.entrySet()) {
            String key = allowed.getKey();
            Integer value = allowed.getValue();
            bw.append(key+"\t\t\t\t\t"+value+"\n");
        }
        bw.append("--------------------------------------------------------"+"\n");
    }

    void calcSupport() {
        //suppRows = Math.round(dataRows * support);
        suppRows = Math.round(support);
    }

    //Generating Frequent Item List
    void checkConfidence(){

    }

    //Check Minium support
    void checkSupport() {}

    /*public void findFile(String name,File file) {
        File[] list = file.listFiles();
        if(list!=null)
            for (File fil : list) {
                if (fil.isDirectory()) {
                    findFile(name,fil);
                } else if (name.equalsIgnoreCase(fil.getName())) {
                    System.out.println(fil.getParentFile());
                }
            }
    }*/
}
