package com.example.demo.domain.listentry;

import com.example.demo.domain.appUser.User;
import com.example.demo.domain.appUser.UserService;
import com.example.demo.domain.authority.Authority;
import com.example.demo.domain.authority.AuthorityRepository;
import com.example.demo.domain.listentry.dto.ListEntryDTO;
import com.example.demo.domain.listentry.dto.ListEntryDTOForOutput;
import com.example.demo.domain.listentry.dto.ListEntryDTOForUpdateAdmin;
import com.example.demo.domain.listentry.dto.ListEntryDTOForUpdateUser;
import com.example.demo.domain.role.Role;
import com.example.demo.domain.role.RoleRepository;
import com.example.demo.exception.NotTheOwnerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@SpringBootTest
class ListEntryServiceImplTest {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private ListEntryRepository listEntryRepository;
    @Autowired
    private ListEntryService listEntryService;
    private User admin;
    private User user;
    @BeforeEach
    void setUp() {
        admin = userService.getUser("admin");
        user = userService.getUser("james");
    }

    @Test
    void addListEntryAsAdmin() {
        UUID uuid = admin.getId();
        try {
            listEntryService.addListEntry(new ListEntryDTO(uuid.toString(), "TEST TITLE", "TEST DESC",
                    "2020-11-20", Importance.IMPORTANT));
        } catch (InstanceNotFoundException e) {
            Assertions.fail(e.getMessage());
        }
        Assertions.assertNotNull(listEntryRepository.getById(uuid));
    }

    @Test
    void addListEntryAsUser() {
        UUID uuid = user.getId();
        try {
            listEntryService.addListEntry(new ListEntryDTO(uuid.toString(), "TEST TITLE", "TEST DESC",
                    "2020-11-20", Importance.IMPORTANT));
        } catch (InstanceNotFoundException e) {
            Assertions.fail(e.getMessage());
        }
        Assertions.assertNotNull(listEntryRepository.getById(uuid));
    }

    @Test
    void updateOwnListEntryAsUser() {
        ListEntry listEntry = listEntryRepository.findByUserID(user.getId()).get(0);
        ListEntryDTOForUpdateUser updateData = new ListEntryDTOForUpdateUser(listEntry.getId().toString(),
        "UPDATED TITLE", "UPDATED TEXT", "2021-11-13", Importance.LESS_IMPORTANT);
        try {
            listEntryService.updateListEntryAsUser(updateData, user.getUsername());
        } catch (InstanceNotFoundException | NotTheOwnerException e) {
            Assertions.fail(e.getMessage());
        }
        ListEntry updatedEntry = listEntryRepository.getById(listEntry.getId());
        Assertions.assertTrue( updateData.getId().equals(updatedEntry.getId().toString()) &&
                updateData.getCreationDate().equals(updatedEntry.getCreationDate().toString()) &&
                updateData.getImportance().getNumVal() == updatedEntry.getImportance() &&
                updateData.getText().equals(updatedEntry.getText()) &&
                updateData.getTitle().equals(updatedEntry.getTitle())
                );
    }

    @Test
    void updateOtherListEntryAsUser() {
        ListEntry listEntry = listEntryRepository.findByUserID(admin.getId()).get(0);
        ListEntryDTOForUpdateUser updateData = new ListEntryDTOForUpdateUser(listEntry.getId().toString(),
                "UPDATED TITLE", "UPDATED TEXT", "2021-11-13", Importance.LESS_IMPORTANT);
        try {
            listEntryService.updateListEntryAsUser(updateData, user.getUsername());
        } catch (InstanceNotFoundException | NotTheOwnerException e) {
            Assertions.assertTrue(e instanceof NotTheOwnerException);
        }
        Assertions.fail("User was able to update post without necessary rights");
    }

    @Test
    void updateOwnListEntryAsAdmin() {
        ListEntry listEntry = listEntryRepository.findByUserID(admin.getId()).get(0);
        ListEntryDTOForUpdateAdmin updateData = new ListEntryDTOForUpdateAdmin(listEntry.getId().toString(),
                admin.getId().toString(), "UPDATED TITLE", "UPDATED TEXT", "2021-11-13",
                Importance.LESS_IMPORTANT);
        try {
            listEntryService.updateListEntryAsAdmin(updateData);
        } catch (InstanceNotFoundException e) {
            Assertions.fail(e.getMessage());
        }
        ListEntry updatedEntry = listEntryRepository.getById(listEntry.getId());
        Assertions.assertTrue( updateData.getId().equals(updatedEntry.getId().toString()) &&
                updateData.getCreationDate().equals(updatedEntry.getCreationDate().toString()) &&
                updateData.getImportance().getNumVal() == updatedEntry.getImportance() &&
                updateData.getText().equals(updatedEntry.getText()) &&
                updateData.getTitle().equals(updatedEntry.getTitle())
        );
    }

    @Test
    void updateOtherListEntryAsAdmin() {
        ListEntry listEntry = listEntryRepository.findByUserID(user.getId()).get(0);
        ListEntryDTOForUpdateAdmin updateData = new ListEntryDTOForUpdateAdmin(listEntry.getId().toString(),
                admin.getId().toString(), "UPDATED BY ADMIN", "UPDATED TEXT BY ADMIN", "2021-11-23",
                Importance.LESS_IMPORTANT);
        try {
            listEntryService.updateListEntryAsAdmin(updateData);
        } catch (InstanceNotFoundException e) {
            Assertions.fail(e.getMessage());
        }
        ListEntry updatedEntry = listEntryRepository.getById(listEntry.getId());
        Assertions.assertTrue( updateData.getId().equals(updatedEntry.getId().toString()) &&
                updateData.getCreationDate().equals(updatedEntry.getCreationDate().toString()) &&
                updateData.getImportance().getNumVal() == updatedEntry.getImportance() &&
                updateData.getText().equals(updatedEntry.getText()) &&
                updateData.getTitle().equals(updatedEntry.getTitle())
        );
    }

    @Test
    void getListEntry() {

    }

    @Test
    void getAllListEntries() {
        listEntryRepository.save(new ListEntry(null, "TEST TITLE", "TEST DESCRIPTION", LocalDate.now(),
                Importance.NEUTRAL.getNumVal(), user));
        List<ListEntryDTOForOutput> listEntries = new ArrayList<>();
        try {
            listEntries = listEntryService.getAllListEntries(user.getId());
        } catch (InstanceNotFoundException e) {
            Assertions.fail(e.getMessage());
        }
        Assertions.assertFalse(listEntries.isEmpty());
    }

    @Test
    void deleteListEntry() {
    }
}