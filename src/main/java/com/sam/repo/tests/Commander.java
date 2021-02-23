package com.sam.repo.tests;

import com.sam.repo.tests.commandables.Commandables;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.reflections.ReflectionUtils.*;

public class Commander {

  public static void main(String[] args) {

    Commander main = new Commander();
    Scanner scanner = new Scanner(System.in);

    Map<String, Commandable> commands =
        Stream.of(main.quitCommand())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    main.fillCommands(commands);

    while (main.printAndHasNext(scanner, "Enter command: ")) {

      String input = scanner.nextLine();

      Commandable cmd = commands.getOrDefault(input, main.defaultCommand());
      Optional<Map<String, String>> result = cmd.exec(Map.of());

      if (result
          .orElse(Map.of())
          .getOrDefault("doQuit", Boolean.FALSE.toString())
          .equals(Boolean.TRUE.toString())) {
        break;
      }
    }
  }

  public void fillCommands(Map<String, Commandable> commands) {
    Reflections reflections =
        new Reflections(Commander.class.getPackage().getName() + ".commandables");

    reflections.getSubTypesOf(Commandables.class).stream()
        .map(
            clazz -> {
              Object obj;
              try {
                obj = clazz.getDeclaredConstructor().newInstance();
              } catch (InstantiationException
                  | NoSuchMethodException
                  | InvocationTargetException
                  | IllegalAccessException e) {
                throw new RuntimeException(e);
              }
              final Commandables commandables = (Commandables) obj;
              return getAllMethods(
                      clazz, withReturnType(Map.Entry.class), withModifier(Modifier.PUBLIC))
                  .stream()
                  .map(
                      method -> {
                        try {
                          return (Map.Entry<String, Commandable>) method.invoke(commandables);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                          throw new RuntimeException(e);
                        }
                      })
                  .collect(Collectors.toList());
            })
        .flatMap(Collection::stream)
        .forEach(
            entry -> {
              commands.put(entry.getKey(), entry.getValue());
            });
  }

  public boolean printAndHasNext(Scanner scanner, String label) {
    System.out.print(label);
    return scanner.hasNext();
  }

  public Commandable defaultCommand() {
    return map -> {
      System.out.println("Command not recognized");
      return Optional.ofNullable(null);
    };
  }

  public Map.Entry<String, Commandable> quitCommand() {

    return Map.entry(
        "quit",
        map -> {
          System.out.println("Closing!");
          return Optional.ofNullable(Map.of("doQuit", Boolean.TRUE.toString()));
        });
  }
}
