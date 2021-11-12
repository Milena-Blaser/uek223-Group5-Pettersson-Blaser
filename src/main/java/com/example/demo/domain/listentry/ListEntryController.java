package com.example.demo.domain.listentry;

import com.example.demo.domain.listentry.dto.ListEntryDTOForOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.demo.domain.listentry.dto.ListEntryDTO;

import javax.management.InstanceNotFoundException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/list/")
public class ListEntryController {

    private ListEntryServiceImpl listEntryService;

    @Autowired
    public ListEntryController(ListEntryServiceImpl listEntryService) {
        this.listEntryService = listEntryService;
    }

    @PostMapping("add")
    @PreAuthorize("hasAuthority('CREATE_LIST_ENTRY')")
    public ResponseEntity<ListEntry> addListEntry(@RequestBody ListEntryDTO listEntry) {
        ListEntry returnedListEntry = null;
        try {
            returnedListEntry = listEntryService.addListEntry(listEntry);
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok().body(returnedListEntry);
    }

    @GetMapping("get/{id}")
    @PreAuthorize("hasAuthority('READ_LIST_ENTRY')")
    public ResponseEntity<Optional<ListEntryDTOForOutput>> getListEntry(@PathVariable("id") UUID id) {
        Optional<ListEntryDTOForOutput> returnedListEntry = null;
        try {
            returnedListEntry = Optional.ofNullable(listEntryService.getListEntry(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok().body(returnedListEntry);
    }
}
