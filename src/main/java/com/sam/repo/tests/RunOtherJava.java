package com.sam.repo.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RunOtherJava {
  public static void main(String[] args) throws IOException, InterruptedException {
    // Process proc = Runtime.getRuntime().exec("java com.sam.repo.tests.RunThis");

    final File f =
        new File(RunOtherJava.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    String path = f.getAbsolutePath();
    System.out.println(
        RunOtherJava.class.getProtectionDomain().getCodeSource().getLocation().getPath());

    String command = String.format("java -cp %s com.sam.repo.tests.RunThis", path);
    System.out.println(command);
    Process proc = Runtime.getRuntime().exec(command);
    InputStream stderr = proc.getErrorStream();
    InputStreamReader isr = new InputStreamReader(stderr);
    BufferedReader br = new BufferedReader(isr);
    String line = null;
    System.out.println("<ERROR>");
    while ((line = br.readLine()) != null) System.out.println(line);
    System.out.println("</ERROR>");
    proc.destroyForcibly();
    int exitVal = proc.waitFor();
    System.out.println("Process exitValue: " + exitVal);
  }
}
