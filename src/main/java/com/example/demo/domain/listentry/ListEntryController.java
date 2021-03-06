package com.example.demo.domain.listentry;

import com.example.demo.domain.listentry.dto.ListEntryDTOForUpdateAdmin;
import com.example.demo.domain.listentry.dto.ListEntryDTOForUpdateUser;
import com.example.demo.domain.listentry.dto.ListEntryDTOForOutput;
import com.example.demo.exception.NotTheOwnerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.demo.domain.listentry.dto.ListEntryDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.InstanceNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/list/")
public class ListEntryController {

    private ListEntryService listEntryService;
    private static final Logger log = LoggerFactory.getLogger(ListEntryServiceImpl.class);
    @Autowired
    public ListEntryController(ListEntryServiceImpl listEntryService) {
        this.listEntryService = listEntryService;
    }

    /**
     * This is the Endpoint that is responsible for adding a new listEntry to the data base. Before this method is
     * executed it checks with @PreAuthorize whether the user has the authority to create a listEntry. Calls on the matching
     * method in the ListEntryServiceImpl.
     *
     * @param listEntry the information that is needed to create a new listEntry that is given through the requestBody
     *                  ing JSON-format. The ListEntryDTO is used.
     * @return either returns the newly created listEntry or the error message that the userID given in the requestBody
     * does not belong to an existing user
     */
    @PostMapping("add")
    public ResponseEntity addListEntry(@RequestBody ListEntryDTO listEntry, @RequestHeader("Authorization") String authHeader) {
        log.trace("Accessed the AddListEntry Endpoint");
        ListEntryDTOForOutput returnedListEntry = null;
        try {
            returnedListEntry = listEntryService.addListEntry(listEntry, decodeCredentials(authHeader)[0]);
        } catch (InstanceNotFoundException e) {
            log.error("User was not found");
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (NullPointerException e) {
            log.trace("Admin didn't enter userID");
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            log.trace("Unexpected error: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(returnedListEntry);
    }

    /**
     * This is the Endpoint that is responsible for getting all the listEntries belonging to a specific user. Before this
     * method is executed it checks with @PreAuthorize whether the user has the authority to read the list of listEntries.
     * Calls on the matching method in the ListEntryServiceImpl.
     *
     * @param username the username of the user who's listEntries should be displayed
     * @return either the list of all the listEntries belonging to the user or an error message that user does not exist
     */
    @GetMapping("{username}")
    @PreAuthorize("hasAuthority('READ_LIST_ENTRY')")
    public ResponseEntity getAllListEntries(@PathVariable("username") String username) {
        log.trace("Accessed the getAllListEntries Endpoint");
        List<ListEntryDTOForOutput> returnedList = null;
        try {
            returnedList = listEntryService.getAllListEntries(username);
        } catch (InstanceNotFoundException e) {
            log.error("User was not found");
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            log.trace("Unexpected error: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(returnedList);
    }

    /**
     * This is the Endpoint responsible for getting one specific listEntry. Before this method is executed it checks
     * with @PreAuthorize if the user has the authority to read this listEntry. Calls on the matching method in the
     * ListEntryServiceImpl.
     *
     * @param id the id of the listEntry that should be displayed
     * @return either the listEntry or the error message that this listEntry does not exist.
     */
    @GetMapping("get/{listEntryID}")
    @PreAuthorize("hasAuthority('READ_LIST_ENTRY')")
    public ResponseEntity getListEntry(@PathVariable("listEntryID") UUID id) {
        log.trace("Accessed the getListEntry Endpoint");
        Optional<ListEntryDTOForOutput> returnedListEntry = null;
        try {
            returnedListEntry = Optional.ofNullable(listEntryService.getListEntry(id));
        } catch (InstanceNotFoundException e) {
            log.error("ListEntry was not found");
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            log.trace("Unexpected error: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(returnedListEntry);
    }

    /**
     * This is the Endpoint that is responsible for updating an already existing listEntry when the currently logged in
     * user is a normal user. Calls on the matching method in the ListEntryServiceImpl.
     *
     * @param inputEntry          the updated information that is given through the requestBody in JSON-format to update the existing
     *                            listEntry
     * @param authorizationHeader the requestHeader returns the username and the password of the currently logged in user which
     *                            needs to be decoded first so that the username can be used in the method in the listEntryServiceImpl
     * @return either the updated ListEntryDTOForOutput or the error messages for a non existing listEntry or the missing authority to
     * update the listEntry because it does not belong to the logged in user
     */
    @PutMapping("update")
    public ResponseEntity updateListEntryAsUser(@RequestBody ListEntryDTOForUpdateUser inputEntry,
                                                @RequestHeader("Authorization") String authorizationHeader) {
        log.trace("Accessed the updateListEntryAsUser Endpoint");
        try {
            return ResponseEntity.ok(new ListEntryDTOForOutput(listEntryService.updateListEntryAsUser(inputEntry,
                    decodeCredentials(authorizationHeader)[0])));
        } catch (InstanceNotFoundException e) {
            log.error("ListEntry was not found");
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (NotTheOwnerException e) {
            log.error("User was not the owner or did not have the authority to execute transaction");
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            log.trace("Unexpected error: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * This is the Endpoint that is responsible for updating an already existing listEntry when the currently logged in
     * user is an admin. Calls on the matching method in the ListEntryServiceImpl. Checks with @PreAuthorize if the user
     * has the authority to update a listEntry
     *
     * @param inputEntry the updated information that is given through the requestBody in JSON-format to update the existing
     *                   listEntry
     * @return either the updated ListEntryDTOForOutput or the error messages for a non existing listEntry
     */
    @PutMapping("admin/update")
    @PreAuthorize("hasAuthority('UPDATE_LIST_ENTRY')")
    public ResponseEntity updateListEntryAsAdmin(@RequestBody ListEntryDTOForUpdateAdmin inputEntry) {
        log.trace("Accessed the updateListEntryAsUser");
        try {
            return ResponseEntity.ok(new ListEntryDTOForOutput(listEntryService.updateListEntryAsAdmin(inputEntry)));
        } catch (InstanceNotFoundException e) {
            log.error("ListEntry was not found");
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            log.trace("Unexpected error: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * This is the Endpoint that is responsible for deleting one specific listEntry. Calls on the matching method in the ListEntryServiceImpl.
     *
     * @param id                  of the listEntry that needs to be deleted
     * @param authorizationHeader the requestHeader returns the username and the password of the currently logged in user which
     *                            needs to be decoded first so that the username can be used in the method in the listEntryServiceImpl
     * @return either a string that confirms that the listEntry has been deleted or the error message for a non existing listEntry or if the user is
     * not the owner of the listEntry and does not have the authority to delete a listEntry
     */
    @DeleteMapping("delete/{id}")
    public ResponseEntity deleteListEntry(@PathVariable UUID id,
                                          @RequestHeader("Authorization") String authorizationHeader) {
        log.trace("Accessed the deleteListEntry Endpoint");
        try {
            listEntryService.deleteListEntry(id, decodeCredentials(authorizationHeader)[0]);
            return ResponseEntity.ok("deleted");
        } catch (InstanceNotFoundException e) {
            log.error("ListEntry was not found");
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (NotTheOwnerException e) {
            log.error("User was not the owner or did not have the authority to execute transaction");
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            log.trace("Unexpected error: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * This is the Endpoint that is responsible for deleting all the existing ListEntries of one specific user.
     * Calls on the matching method in the ListEntryServiceImpl.
     *
     * @param username         is the userID of the user who's listEntries should be deleted
     * @param authHeader the requestHeader returns the username and the password of the currently logged in user which
     *                   needs to be decoded first so that the username can be used in the method in the listEntryServiceImpl
     * @return either a String that confirms that listEntries have been deleted or the error messages that either the user
     * does not exist or the list does not belong to the currently logged in user
     */
    @DeleteMapping("{username}")
    public ResponseEntity deleteAllListEntries(@PathVariable("username") String username, @RequestHeader("Authorization") String authHeader) {
        log.trace("Accessed the deleteAllEntries Endpoint");
        try {
            listEntryService.deleteAllListEntries(username, decodeCredentials(authHeader)[0]);
            return ResponseEntity.ok().body("All list entries were deleted");
        } catch (InstanceNotFoundException e) {
            log.error("User was not found");
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (NotTheOwnerException e) {
            log.error("User was not the owner or did not have the authority to execute transaction");
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            log.trace("Unexpected error: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * This method is responsible for decoding the Authorization Header and splitting the decoded String into username
     * and password.
     *
     * @param authorizationHeader the user information found in the request header under the authorization tag
     * @return the decoded and split into two String array containing the username and the password
     */
    private String[] decodeCredentials(String authorizationHeader) {
        String base64Credentials = authorizationHeader.substring("Basic".length()).trim();
        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded, StandardCharsets.UTF_8);
        // format is "username:password"
        return credentials.split(":", 2);
    }
}

