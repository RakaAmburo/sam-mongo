package com.sam.repo.tests.commandables;

import com.sam.repo.tests.Commandable;

import java.util.Map;
import java.util.Optional;

public class FirstCommands implements Commandables {

    public Map.Entry<String, Commandable> quitCommand() {

        return Map.entry(
                "print",
                map -> {
                    System.out.println("printing something!");
                    return Optional.empty();
                });
    }

}
