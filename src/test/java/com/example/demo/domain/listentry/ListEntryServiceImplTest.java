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
import org.springframework.transaction.annotation.Transactional;

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
    private ListEntryRepository listEntryRepository;
    @Autowired
    private ListEntryService listEntryService;
    private User admin;
    private User user;
    @BeforeEach
    void setUp() {
        admin = userService.getUser("admin");
        user = userService.getUser("james");
        listEntryRepository.save(new ListEntry(null, "TEST TITLE", "TEST DESCRIPTION", LocalDate.now(),
                Importance.NEUTRAL.getNumVal(), user));
        listEntryRepository.save(new ListEntry(null, "TEST TITLE 2", "TEST DESCRIPTION 2", LocalDate.now(),
                Importance.NEUTRAL.getNumVal(), user));
        listEntryRepository.save(new ListEntry(null, "TEST ADMIN TITLE", "TEST ADMIN DESCRIPTION", LocalDate.now(),
                Importance.NEUTRAL.getNumVal(), admin));
        listEntryRepository.save(new ListEntry(null, "TEST ADMIN TITLE 2", "TEST ADMIN DESCRIPTION 2", LocalDate.now(),
                Importance.NEUTRAL.getNumVal(), admin));
    }

   /* @Test
    void addListEntryAsAdmin() {
        UUID uuid = admin.getId();
        try {
            listEntryService.addListEntry(new ListEntryDTO(uuid.toString(), "TEST TITLE", "TEST DESC",
                    "2020-11-20", Importance.IMPORTANT), admin.getUsername());
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
                    "2020-11-20", Importance.IMPORTANT), user.getUsername());
        } catch (InstanceNotFoundException e) {
            Assertions.fail(e.getMessage());
        }
        Assertions.assertNotNull(listEntryRepository.getById(uuid));
    }*/

    @Transactional
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

    @Transactional
    @Test
    void updateOtherListEntryAsUser() {
        ListEntry listEntry = listEntryRepository.findByUserID(admin.getId()).get(0);
        ListEntryDTOForUpdateUser updateData = new ListEntryDTOForUpdateUser(listEntry.getId().toString(),
                "UPDATED TITLE", "UPDATED TEXT", "2021-11-13", Importance.LESS_IMPORTANT);
        try {
            listEntryService.updateListEntryAsUser(updateData, user.getUsername());
            Assertions.fail("User was able to update post without necessary rights");
        } catch (InstanceNotFoundException | NotTheOwnerException e) {
            Assertions.assertTrue(e instanceof NotTheOwnerException);
        }
    }

    @Transactional
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

    @Transactional
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

    @Transactional
    @Test
    void getListEntry() {
        try {
            Assertions.assertEquals(listEntryService.getListEntry(listEntryRepository.findByUserID(
                    user.getId()).get(0).getId()).getUsername(), user.getUsername());
        } catch (InstanceNotFoundException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Transactional
    @Test
    void getAllListEntries() {
        listEntryRepository.save(new ListEntry(null, "TEST TITLE", "TEST DESCRIPTION", LocalDate.now(),
                Importance.NEUTRAL.getNumVal(), user));
        listEntryRepository.save(new ListEntry(null, "TEST TITLE 2", "TEST DESCRIPTION 2", LocalDate.now(),
                Importance.NEUTRAL.getNumVal(), user));
        List<ListEntryDTOForOutput> listEntries = new ArrayList<>();
        try {
            listEntries = listEntryService.getAllListEntries(user.getUsername());
        } catch (InstanceNotFoundException e) {
            Assertions.fail(e.getMessage());
        }
        Assertions.assertTrue(!listEntries.isEmpty() && listEntries.size() > 1);
    }

    @Test
    void deleteOwnListEntryAsUser() {
        ListEntry listEntry = listEntryRepository.findByUserID(user.getId()).get(0);
        try {
            listEntryService.deleteListEntry(listEntry.getId(), user.getUsername());
        } catch (InstanceNotFoundException | NotTheOwnerException e) {
            Assertions.fail(e.getMessage());
        }
        Assertions.assertFalse(listEntryRepository.findById(listEntry.getId()).isPresent());
    }

    @Test
    void deleteOtherListEntryAsUser() {
        ListEntry listEntry = listEntryRepository.findByUserID(admin.getId()).get(0);
        try {
            listEntryService.deleteListEntry(listEntry.getId(), user.getUsername());
            Assertions.fail("deleted without the necessary rights");
        } catch (InstanceNotFoundException | NotTheOwnerException e) {
            Assertions.assertTrue(e instanceof NotTheOwnerException);
        }
    }

    @Test
    void deleteOwnListEntryAsAdmin() {
        ListEntry listEntry = listEntryRepository.findByUserID(admin.getId()).get(0);
        try {
            listEntryService.deleteListEntry(listEntry.getId(), admin.getUsername());
        } catch (InstanceNotFoundException | NotTheOwnerException e) {
            Assertions.fail(e.getMessage());
        }
        Assertions.assertFalse(listEntryRepository.findById(listEntry.getId()).isPresent());
    }

    @Test
    void deleteOtherListEntryAsAdmin() {
        ListEntry listEntry = listEntryRepository.findByUserID(user.getId()).get(0);
        try {
            listEntryService.deleteListEntry(listEntry.getId(), admin.getUsername());
        } catch (InstanceNotFoundException | NotTheOwnerException e) {
            Assertions.fail(e.getMessage());
        }
        Assertions.assertFalse(listEntryRepository.findById(listEntry.getId()).isPresent());
    }

    @Test
    void deleteAllOwnListEntryAsUser() {
        try {
            listEntryService.deleteAllListEntries(user.getUsername(), user.getUsername());
        } catch (InstanceNotFoundException | NotTheOwnerException e) {
            Assertions.fail(e.getMessage());
        }
        Assertions.assertTrue(listEntryRepository.findByUserID(user.getId()).isEmpty());
    }

    @Test
    void deleteAllOtherListEntryAsUser() {
        try {
            listEntryService.deleteAllListEntries(admin.getUsername(), user.getUsername());
            Assertions.fail("deleted without the necessary rights");
        } catch (InstanceNotFoundException | NotTheOwnerException e) {
            Assertions.assertTrue(e instanceof NotTheOwnerException);
        }
    }

    @Test
    void deleteAllOwnListEntryAsAdmin() {
        try {
            listEntryService.deleteAllListEntries(admin.getUsername(), admin.getUsername());
        } catch (InstanceNotFoundException | NotTheOwnerException e) {
            Assertions.fail(e.getMessage());
        }
        Assertions.assertTrue(listEntryRepository.findByUserID(admin.getId()).isEmpty());
    }

    @Test
    void deleteAllOtherListEntryAsAdmin() {
        try {
            listEntryService.deleteAllListEntries(user.getUsername(), admin.getUsername());
            Assertions.assertTrue(listEntryRepository.findByUserID(user.getId()).isEmpty());
        } catch (InstanceNotFoundException | NotTheOwnerException e) {
            Assertions.fail(e.getMessage());
        }
    }


}