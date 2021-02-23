package com.sam.repo.tests;

import com.sam.commons.entities.MenuItemDTO;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class MongoTest {



  public static void main(String[] args) throws IOException {

    LogManager.getLogManager().reset();
    Logger rootLogger = Logger.getLogger("");
    ConsoleHandler consoleHandler = new ConsoleHandler();
    consoleHandler.setFormatter(new SimpleFormatter());
    consoleHandler.setLevel(Level.OFF);
    rootLogger.addHandler(consoleHandler);

    Logger logger = Logger.getLogger("MyLog");
    FileHandler fh;
    System.setProperty("com.couchbase.client.deps.io.netty.noUnsafe", "true");
    // C:\repos\mongoTest\logs\testlog.log

    fh = new FileHandler("C:/repos/mongoTest/logs/testlog.log", 2*1024*1024, 5, true);
    logger.addHandler(fh);
    SimpleFormatter formatter = new SimpleFormatter();
    fh.setFormatter(formatter);

    // the following statement is used to log any messages
    logger.info("My first log");

    Scanner scanner = new Scanner(System.in);

    // java -cp classes;C:\repos\sam\sam-commons\target\classes;C:\repos\mongoTest\lib\*
    // com.sam.repo.tests.MongoTest

    String input = "";
    System.out.println("compilado:");
    while (!"quit".equalsIgnoreCase(input)) {

      System.out.print("Enter command: ");

      input = scanner.nextLine();

      if (input.equalsIgnoreCase("call")) {
        restCall();
      }

      // System.out.println("input : " + input);
    }

    System.out.println("Closed!");
  }

  private static void restCall() {

    WebClient client = WebClient.create("http://localhost:8080/sam-repo/menuItems");
    List<MenuItemDTO> lista =
        client
            .get()
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<MenuItemDTO>>() {})
            .block();
    System.out.println(lista.isEmpty());
  }
}
