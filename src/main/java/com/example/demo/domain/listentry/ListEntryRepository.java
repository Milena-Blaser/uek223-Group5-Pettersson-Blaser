package com.example.demo.domain.listentry;

import com.example.demo.domain.appUser.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface ListEntryRepository extends JpaRepository<ListEntry, UUID> {
    @Query(value = "select * from list_entry  where list_entry.user_id = :id order by list_entry.importance desc", nativeQuery = true)
    List<ListEntry> findByUserID(@Param("id") UUID id);

}
