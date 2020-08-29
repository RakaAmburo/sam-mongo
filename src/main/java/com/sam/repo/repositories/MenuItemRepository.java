package com.sam.repo.repositories;

import com.sam.repo.entities.MenuItem;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MenuItemRepository extends ReactiveMongoRepository<MenuItem, String> {}
