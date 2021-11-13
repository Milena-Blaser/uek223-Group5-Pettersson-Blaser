package com.example.demo.domain.listentry;

import com.example.demo.domain.listentry.dto.ListEntryDTOForUpdateAdmin;
import com.example.demo.domain.listentry.dto.ListEntryDTOForUpdateUser;
import com.example.demo.domain.listentry.dto.ListEntryDTOForOutput;
import com.example.demo.exception.NotTheOwnerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.demo.domain.listentry.dto.ListEntryDTO;

import javax.management.InstanceNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/list/")
public class ListEntryController {

    private ListEntryService listEntryService;

    @Autowired
    public ListEntryController(ListEntryServiceImpl listEntryService) {
        this.listEntryService = listEntryService;
    }

    @PostMapping("add")
    @PreAuthorize("hasAuthority('CREATE_LIST_ENTRY')")
    public ResponseEntity addListEntry(@RequestBody ListEntryDTO listEntry) {
        ListEntry returnedListEntry = null;
        try {
            returnedListEntry = listEntryService.addListEntry(listEntry);
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
        return ResponseEntity.ok().body(returnedListEntry);
    }

    @GetMapping("get/{id}")
    @PreAuthorize("hasAuthority('READ_LIST_ENTRY')")
    public ResponseEntity getListEntry(@PathVariable("id") UUID id) {
        Optional<ListEntryDTOForOutput> returnedListEntry = null;
        try {
            returnedListEntry = Optional.ofNullable(listEntryService.getListEntry(id));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
        return ResponseEntity.ok().body(returnedListEntry);
    }

    @PutMapping("update")
    public ResponseEntity updateListEntryAsUser(@RequestBody ListEntryDTOForUpdateUser inputEntry,
                                                @RequestHeader("Authorization") String authorizationHeader) {
        try {
            return ResponseEntity.ok(new ListEntryDTOForOutput(listEntryService.updateListEntryAsUser(inputEntry,
                    decodeCredentials(authorizationHeader)[0])));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (NotTheOwnerException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @PutMapping("admin/update")
    public ResponseEntity updateListEntryAsAdmin (@RequestBody ListEntryDTOForUpdateAdmin inputEntry) {
        try {
            return ResponseEntity.ok(new ListEntryDTOForOutput(listEntryService.updateListEntryAsAdmin(inputEntry)));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }

    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity deleteListEntry (@PathVariable UUID id,
                                           @RequestHeader("Authorization") String authorizationHeader) {
        try {
            listEntryService.deleteListEntry(id, decodeCredentials(authorizationHeader)[0]);
            return ResponseEntity.ok("deleted");
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (NotTheOwnerException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    private String[] decodeCredentials(String authorizationHeader) {
        String base64Credentials = authorizationHeader.substring("Basic".length()).trim();
        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded, StandardCharsets.UTF_8);
        // format is "username:password"
        return credentials.split(":", 2);
    }
}

