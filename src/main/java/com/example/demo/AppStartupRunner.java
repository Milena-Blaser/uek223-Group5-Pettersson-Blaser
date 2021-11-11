package com.example.demo;
import com.example.demo.domain.ListEntry.ListEntry;
import com.example.demo.domain.ListEntry.ListEntryRepository;
import com.example.demo.domain.appUser.User;
import com.example.demo.domain.appUser.UserService;
import com.example.demo.domain.authority.Authority;
import com.example.demo.domain.authority.AuthorityRepository;
import com.example.demo.domain.role.Role;
import com.example.demo.domain.role.RoleRepository;
import com.example.demo.domain.role.RoleServiceImpl;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
    private final ListEntryRepository listEntryRepository;

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
        Role adminRole = new Role(null, "ADMIN", allAuthorities);
        Role userRole = new Role(null, "USER", List.of(readListEntryAuth));
        roleRepository.save(adminRole);
        roleRepository.save(userRole);

        userService.saveUser(new User(null, "james","james.bond@mi6.com","bond", Set.of(userRole), Arrays.asList()));
        userService.saveUser(new User(null, "admin", "admin@mail.ch", "adm1n!", Set.of(adminRole), List.of()));
        userService.addRoleToUser("james", "USER");
        userService.addRoleToUser("admin", "ADMIN");

    }
}

