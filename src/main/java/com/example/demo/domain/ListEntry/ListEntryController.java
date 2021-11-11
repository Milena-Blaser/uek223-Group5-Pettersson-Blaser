package com.example.demo.domain.ListEntry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/list")
public class ListEntryController {

    private ListEntryServiceImpl listEntryService;

    @Autowired
    public ListEntryController(ListEntryServiceImpl listEntryService){
        this.listEntryService = listEntryService;
    }

    @PostMapping("/add")
    public ResponseEntity<ListEntry> addListEntry(@RequestBody ListEntry listEntry){
        return ResponseEntity.ok().body(listEntryService.addListEntry(listEntry));
    }
}
