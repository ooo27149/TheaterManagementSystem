package com.avan.movie.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class MD5Utils {

    /**
     * MD5加密类
     * @param str 要加密的字符串
     * @return    加密后的字符串
     */
    public static String code(String str){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[]byteDigest = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < byteDigest.length; offset++) {
                i = byteDigest[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Boolean duplicate(List<Integer> dup, List<Integer> input) {
        Set<Integer> tempSet = new LinkedHashSet<>();
        for(Integer i : input) {
            if(!tempSet.add(i)) {
                dup.add(i);
            }
        }
        return dup.isEmpty();
    }

    public static void solution(Integer[] input){
        List<Integer> list = new ArrayList<>(Arrays.asList(input));
        List<Integer> duplicateList = new ArrayList<>();

        while (!duplicate(duplicateList, list)){
            Collections.sort(duplicateList);
            list.remove(duplicateList.get(0));
            list.set(list.indexOf(duplicateList.get(0)), duplicateList.get(0) * 2);
            duplicateList.clear();
        }
        System.out.println(list);
        System.out.println(list.size());
    }


    public static void main(String[] args) {
        Integer[] input = {3,4,1,2,2,1,1};
        solution(input);
        Integer[] input1 = {10,40,20,50,30};
        solution(input1);
        Integer[] input2 = {1,1,3,1,1};
        solution(input2);
        Integer[] input3 = {1,1,4,3,1,5,1,8,4};
        solution(input3);

    }
}
