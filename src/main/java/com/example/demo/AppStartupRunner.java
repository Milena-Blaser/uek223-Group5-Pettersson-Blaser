package com.example.demo;
import com.example.demo.domain.listentry.Importance;
import com.example.demo.domain.listentry.ListEntry;
import com.example.demo.domain.listentry.ListEntryRepository;
import com.example.demo.domain.appUser.User;
import com.example.demo.domain.appUser.UserService;
import com.example.demo.domain.authority.Authority;
import com.example.demo.domain.authority.AuthorityRepository;
import com.example.demo.domain.role.Role;
import com.example.demo.domain.role.RoleRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;


@Component
@RequiredArgsConstructor
//ApplicationListener used to run commands after startup
class AppStartupRunner implements ApplicationRunner {
    @Autowired
    private final UserService userService;
    @Autowired
    private final RoleRepository roleRepository;
    @Autowired
    private final AuthorityRepository authorityRepository;
    @Autowired
    private ListEntryRepository listEntryRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        RUN YOUR STARTUP CODE HERE
//        e.g. to add a user or role to the DB (only for testing)

//        Authorities
        List<Authority> allAuthorities = new ArrayList<>();
        Authority readListEntryAuth = new Authority(null,"READ_LIST_ENTRY");
        Authority createListEntryAuth = new Authority(null, "CREATE_LIST_ENTRY");
        Authority updateListEntryAuth = new Authority(null, "UPDATE_LIST_ENTRY");
        Authority deleteListEntryAuth = new Authority(null, "DELETE_LIST_ENTRY");
        allAuthorities.add(readListEntryAuth);
        allAuthorities.add(createListEntryAuth);
        allAuthorities.add(updateListEntryAuth);
        allAuthorities.add(deleteListEntryAuth);
        authorityRepository.saveAll(allAuthorities);

//        Roles
        Role adminRole = new Role(null, "ROLE_ADMIN", allAuthorities);
        Role userRole = new Role(null, "USER", List.of(readListEntryAuth));
        roleRepository.save(adminRole);
        roleRepository.save(userRole);


//        Users
        User admin = new User(null, "admin", "admin@mail.ch", "adm1n!", Set.of(adminRole), List.of());
        User james = new User(null, "james","james.bond@mi6.com","bond", Set.of(userRole), List.of());
        userService.saveUser(admin);
        userService.saveUser(james);

//        List Entries
        ListEntry entry1 = new ListEntry(null, "TITLE OF ENTRY 1", "Description of entry 1",
                LocalDate.of(2004, 7, 14), Importance.VERY_IMPORTANT.getNumVal(), admin);
        ListEntry entry2 = new ListEntry(null, "TITLE OF ENTRY 2", "Description of entry 2",
                LocalDate.of(2008, 8, 16), Importance.IMPORTANT.getNumVal(), admin);
        ListEntry entry3 = new ListEntry(null, "TITLE OF ENTRY 3", "Description of entry 3",
                LocalDate.of(2012, 9, 18), Importance.LESS_IMPORTANT.getNumVal(), admin);
        listEntryRepository.saveAll(List.of(entry1, entry2, entry3));
        //admin.setMyEntryList(List.of(entry1, entry2, entry3));
        //userService.updateUser(admin);
        ListEntry entry4 = new ListEntry(null, "TITLE OF ENTRY 4", "Description of entry 4",
                LocalDate.of(2016, 10, 20), Importance.NEUTRAL.getNumVal(), james);
        ListEntry entry5 = new ListEntry(null, "TITLE OF ENTRY 5", "Description of entry 5",
                LocalDate.of(2020, 11, 22), Importance.NOT_IMPORTANT.getNumVal(), james);
        listEntryRepository.saveAll(List.of(entry4, entry5));
        //james.setMyEntryList(List.of(entry4, entry5));
        //userService.updateUser(james);
    }
}

