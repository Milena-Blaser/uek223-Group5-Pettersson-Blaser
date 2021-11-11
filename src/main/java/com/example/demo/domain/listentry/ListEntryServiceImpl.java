package com.example.demo.domain.listentry;

import com.example.demo.domain.appUser.User;
import com.example.demo.domain.appUser.UserServiceImpl;
import com.example.demo.domain.listentry.dto.ListEntryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class ListEntryServiceImpl implements ListEntryService {

    private final ListEntryRepository listEntryRepository;
    private final UserServiceImpl userService;

    @Autowired
    public ListEntryServiceImpl(ListEntryRepository listEntryRepository, UserServiceImpl userService) {
        this.listEntryRepository = listEntryRepository;
        this.userService = userService;
    }

    @Override
    public ListEntry addListEntry(ListEntryDTO listEntry) throws InstanceNotFoundException{
        User user = userService.findById(UUID.fromString(listEntry.getUserID())).orElseGet(null);

        return listEntryRepository.save(new ListEntry(null,listEntry.getTitle(),listEntry.getText(), LocalDate.parse(listEntry.getCreationDate()),listEntry.getImportance().getNumVal(),user));
    }

    @Override
    public ListEntry getListEntry(UUID id) {
        return listEntryRepository.getById(id);
    }
}
