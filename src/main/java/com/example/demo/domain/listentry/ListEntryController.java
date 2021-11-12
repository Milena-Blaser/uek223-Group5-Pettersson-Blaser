package com.example.demo.domain.listentry;

import com.example.demo.domain.listentry.dto.ListEntryDTOForUpdateAdmin;
import com.example.demo.domain.listentry.dto.ListEntryDTOForUpdateUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.demo.domain.listentry.dto.ListEntryDTO;

import javax.management.InstanceNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/list/")
public class ListEntryController {

    private ListEntryService listEntryService;

    @Autowired
    public ListEntryController(ListEntryServiceImpl listEntryService) {
        this.listEntryService = listEntryService;
    }

    @PostMapping("/add")
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

    @PutMapping("update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateListEntryAsUser(@RequestBody ListEntryDTOForUpdateUser inputEntry) {
        try {
            return ResponseEntity.ok(listEntryService.updateListEntryAsUser(inputEntry));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("admin/update")
    @PreAuthorize("hasAuthority('UPDATE_LIST_ENTRY')")
    public ResponseEntity updateListEntryAsUser(@RequestBody ListEntryDTOForUpdateAdmin inputEntry) {
        try {
            return ResponseEntity.ok(listEntryService.updateListEntryAsAdmin(inputEntry));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }

    }

    public String[] decodeCredentials(String authorizationHeader) {
        String base64Credentials = authorizationHeader.substring("Basic".length()).trim();
        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded, StandardCharsets.UTF_8);
        // format is "username:password"
        return credentials.split(":", 2);
    }
}