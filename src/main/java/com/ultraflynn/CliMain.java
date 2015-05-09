package com.ultraflynn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CliMain {
    private final Clattered clattered = new Clattered();

    public static void main(String[] args) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String s;
            while ((s = in.readLine()) != null && s.length() != 0) {
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
