package com.sam.repo.tests;

import java.util.Map;
import java.util.Optional;

public interface Commandable {
    public Optional<Map<String, String>> exec(Map params);
}
