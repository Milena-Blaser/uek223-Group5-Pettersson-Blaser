package com.example.demo.domain.listentry;

import com.example.demo.domain.appUser.User;
import com.example.demo.domain.appUser.UserServiceImpl;
import com.example.demo.domain.listentry.dto.ListEntryDTO;
import com.example.demo.domain.listentry.dto.ListEntryDTOForUpdate;
import com.example.demo.domain.listentry.dto.ListEntryDTOForOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.Optional;
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
        Optional<User> optionalUser;
        if((optionalUser = userService.findById(UUID.fromString(listEntry.getUserID()))).isEmpty()) {
          throw new InstanceNotFoundException("User does not exist");
        }
        User user = optionalUser.get();
        return listEntryRepository.save(new ListEntry(null,listEntry.getTitle(),listEntry.getText(), LocalDate.parse(listEntry.getCreationDate()),listEntry.getImportance().getNumVal(),user));
    }

    @Override
    public ListEntryDTOForOutput getListEntry(UUID id) throws InstanceNotFoundException {
        Optional<ListEntry> optionalListEntry;
        if((optionalListEntry = listEntryRepository.findById(id)).isEmpty()) {
            throw new InstanceNotFoundException("Element does not exist");
        }
        ListEntry listEntry = optionalListEntry.get();
        return  new ListEntryDTOForOutput(listEntry.getTitle(),listEntry.getText(),listEntry.getCreationDate().toString(), listEntry.getImportance(), listEntry.getUser().getUsername());
    }

}
