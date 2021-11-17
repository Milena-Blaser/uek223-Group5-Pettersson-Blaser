package com.example.demo.domain.listentry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ListEntryRepository extends JpaRepository<ListEntry, UUID> {
    /**
     * This method's task is defined by the query that will get all the listEntries that have a specific userID and sort
     * them by the importance level, starting with the highest level of importance.
     * @param id is the userID of the user who's list should be displayed
     * @return List<ListEntry> of all the listEntries with this specific id
     */
    @Query(value = "select * from list_entry  where list_entry.user_id = :id order by list_entry.importance desc", nativeQuery = true)
    List<ListEntry> findByUserID(@Param("id") UUID id);

}
