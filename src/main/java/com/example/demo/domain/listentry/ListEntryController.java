package com.example.demo.domain.listentry;

import com.example.demo.domain.listentry.dto.ListEntryDTOForUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.demo.domain.listentry.dto.ListEntryDTO;

import javax.management.InstanceNotFoundException;

@RestController
@RequestMapping("/list")
public class ListEntryController {

    private ListEntryService listEntryService;

    @Autowired
    public ListEntryController(ListEntryServiceImpl listEntryService){
        this.listEntryService = listEntryService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('CREATE_LIST_ENTRY')")
    public ResponseEntity<ListEntry> addListEntry(@RequestBody ListEntryDTO listEntry){
        ListEntry returnedListEntry = null;
        try {
            returnedListEntry = listEntryService.addListEntry(listEntry);
        } catch (InstanceNotFoundException e) {
            return  ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok().body(returnedListEntry);
    }

    @PutMapping("update")
    @PreAuthorize("hasAuthority('UPDATE_LIST_ENTRY')")
    public ResponseEntity<ListEntry> updateListEntry(@RequestBody ListEntryDTOForUpdate inputEntry) {

    }
}
