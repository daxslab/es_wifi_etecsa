package com.daxslab.eswifi_etecsa.utils;

import java.util.LinkedList;
import java.util.List;

public class TraceRouteUtils {

    public static List<List<String>> getJumps(List<String> originalList, int chunkSize){
        List<List<String>> partitions = new LinkedList<List<String>>();
        for (int i = 0; i < originalList.size(); i += chunkSize) {
            partitions.add(originalList.subList(i,
                    Math.min(i + chunkSize, originalList.size())));
        }
        return partitions;
    }


    public static int countKnownjumps(List<List<String>> jumps){
        int counter = 0;
        for (List<String> jump:jumps) {
            if (!jump.get(1).equals("")){
                counter++;
            }
        }
        return counter;
    }


}
