package com.example.demo.domain.ListEntry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ListEntryRepository extends JpaRepository<ListEntry, UUID> {
}
