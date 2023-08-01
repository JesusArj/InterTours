package com.intertours.intertoursapp.utils;

public class AppUtils {

    public static String buildStopsDescription(String ... stops){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<stops.length; i++){
            if(i != stops.length - 1){
                sb.append(i+1).append(". ").append(stops[i]).append("\n");
            }
            else{
                sb.append(i+1).append(". ").append(stops[i]);
            }
        }
        return sb.toString();
    }
}
