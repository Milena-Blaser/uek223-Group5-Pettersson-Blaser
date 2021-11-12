package com.example.demo.domain.listentry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ListEntryRepository extends JpaRepository<ListEntry, UUID> {
    @Modifying
    @Query(
         value = "SELECT * FROM list_entry l WHERE l.id = :id SORT ASC"
    )
    List<ListEntry>getAllListEntries(@Param("id") UUID id);

}
