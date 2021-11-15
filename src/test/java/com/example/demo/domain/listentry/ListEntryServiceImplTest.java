package com.example.demo.domain.listentry;

import com.example.demo.domain.appUser.User;
import com.example.demo.domain.appUser.UserService;
import com.example.demo.domain.authority.Authority;
import com.example.demo.domain.authority.AuthorityRepository;
import com.example.demo.domain.listentry.dto.ListEntryDTO;
import com.example.demo.domain.role.Role;
import com.example.demo.domain.role.RoleRepository;
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        Assertions.assertNotNull(listEntryRepository.getById(uuid));
    }

    @Test
    void updateListEntryAsUser() {
    }

    @Test
    void updateListEntryAsAdmin() {
    }

    @Test
    void getListEntry() {
    }

    @Test
    void getAllListEntries() {
    }

    @Test
    void deleteListEntry() {
    }
}