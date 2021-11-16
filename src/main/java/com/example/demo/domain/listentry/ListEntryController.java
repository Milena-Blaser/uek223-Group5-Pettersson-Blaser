package com.example.demo.domain.listentry;

import com.example.demo.domain.listentry.dto.ListEntryDTOForUpdateAdmin;
import com.example.demo.domain.listentry.dto.ListEntryDTOForUpdateUser;
import com.example.demo.domain.listentry.dto.ListEntryDTOForOutput;
import com.example.demo.exception.NotTheOwnerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.demo.domain.listentry.dto.ListEntryDTO;

import javax.management.InstanceNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/list/")
public class ListEntryController {

    private ListEntryService listEntryService;

    @Autowired
    public ListEntryController(ListEntryServiceImpl listEntryService) {
        this.listEntryService = listEntryService;
    }

    /**
     * This is the Endpoint that is responsible for adding a new listEntry to the data base.
     * @param listEntry the information that is needed to create a new listEntry that is given through the requestBody
     *                  ing JSON-format
     * @return either returns the newly created listEntry or the error message that the userID given in the requestBody
     * does not belong to an existing user
     */
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

    /**
     * This is the Endpoint that is responsible for getting all the listEntries belonging to a specific user. Before this
     * method is executed it checks with @PreAuthorize whether the user has the authority to read the list of listEntries.
     *
     * @param id the userID of the user who's listEntries should be displayed
     * @return either the list of all the listEntries belonging to the user or an error message that user does not exist
     */
    @GetMapping("{userID}")
    @PreAuthorize("hasAuthority('READ_LIST_ENTRY')")
    public ResponseEntity getAllListEntries(@PathVariable("userID") UUID id) {
        List<ListEntryDTOForOutput> returnedList = null;
        try {
            returnedList = listEntryService.getAllListEntries(id);
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
        return ResponseEntity.ok().body(returnedList);
    }

    /**
     * This is the Endpoint responsible for getting one specific listEntry. Before this method is executed it checks
     * with @PreAuthorize if the user has the authority to read this listEntry.
     *
     * @param id the id of the listEntry that should be displayed
     * @return either the listEntry or the error message that this listEntry does not exist.
     */
    @GetMapping("get/{listEntryID}")
    @PreAuthorize("hasAuthority('READ_LIST_ENTRY')")
    public ResponseEntity getListEntry(@PathVariable("listEntryID") UUID id) {

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
    @PreAuthorize("hasAuthority('UPDATE_LIST_ENTRY')")
    public ResponseEntity updateListEntryAsAdmin(@RequestBody ListEntryDTOForUpdateAdmin inputEntry) {
        try {
            return ResponseEntity.ok(new ListEntryDTOForOutput(listEntryService.updateListEntryAsAdmin(inputEntry)));
        } catch (InstanceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }

    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity deleteListEntry(@PathVariable UUID id,
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

    /**
     * This is the Endpoint that is responsible for deleting all the existing ListEntries of one specific user.
     *
     * @param id         is the userID of the user who's listEntries should be deleted
     * @param authHeader the requestHeader returns the username and the password of the currently logged in user which
     *                   needs to be decoded first so that the username can be used in the method in the listEntryServiceImpl
     * @return either a String that confirms that listEntries have been deleted or the error messages that either the user
     * does not exist or the list does not belong to the currently logged in user
     */
    @DeleteMapping("{id}")
    public ResponseEntity deleteAllListEntries(@PathVariable UUID id, @RequestHeader("Authorization") String authHeader) {
        try {
            listEntryService.deleteAllListEntries(id, decodeCredentials(authHeader)[0]);
            return ResponseEntity.ok().body("All list entries were deleted");
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

