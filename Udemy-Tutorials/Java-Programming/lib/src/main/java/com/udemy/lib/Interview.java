package com.udemy.lib;

public class Interview {

    public static void main(String[] args) {
        String statement = "My name is Nilesh and I am learning Kotlin Language";

        String[] split = statement.split(" ");

        String[] capitalStringList = new String[split.length];

        for (int i = 0; i < split.length; i++) {
            char[] chars = split[i].toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            capitalStringList[i] = new String(chars);
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (String s : capitalStringList) {
            stringBuilder.append(s).append(" ");
        }

        String capitalString = stringBuilder.toString().trim();
        System.out.println("Capital String is: " + capitalString);
    }
}
