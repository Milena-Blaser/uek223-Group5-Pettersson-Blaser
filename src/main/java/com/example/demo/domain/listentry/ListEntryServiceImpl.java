package com.example.demo.domain.listentry;

import com.example.demo.domain.appUser.User;
import com.example.demo.domain.appUser.UserServiceImpl;
import com.example.demo.domain.listentry.dto.ListEntryDTO;
import com.example.demo.domain.listentry.dto.ListEntryDTOForUpdateAdmin;
import com.example.demo.domain.listentry.dto.ListEntryDTOForUpdateUser;
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
        User user = userService.findById(UUID.fromString(listEntry.getUserID())).orElseGet(null);

        return listEntryRepository.save(new ListEntry(null,listEntry.getTitle(),listEntry.getText(), LocalDate.parse(listEntry.getCreationDate()),listEntry.getImportance().getNumVal(),user));
    }

    @Override
    public ListEntry updateListEntryAsUser(ListEntryDTOForUpdateUser newListEntry) throws InstanceNotFoundException {
        Optional<ListEntry> oldListEntryOptional;
        if ((oldListEntryOptional = listEntryRepository.findById(UUID.fromString(newListEntry.getId()))).isPresent()) {
            ListEntry oldListEntry = oldListEntryOptional.get();
            oldListEntry.setTitle(newListEntry.getTitle());
            oldListEntry.setText(newListEntry.getText());
            oldListEntry.setCreationDate(LocalDate.parse(newListEntry.getCreationDate()));
            oldListEntry.setImportance(newListEntry.getImportance().getNumVal());

            return listEntryRepository.save(oldListEntry);
        } else {
            throw new InstanceNotFoundException("List Entry doesn't exist");
        }
    }

    @Override
    public ListEntry updateListEntryAsAdmin(ListEntryDTOForUpdateAdmin newListEntry) throws InstanceNotFoundException {
        Optional<ListEntry> oldListEntryOptional;
        Optional<User> newUser;
        if ((newUser = userService.findById(UUID.fromString(newListEntry.getUserID()))).isEmpty())
            throw new InstanceNotFoundException("User to assign entry to doesn't exist");
        if ((oldListEntryOptional = listEntryRepository.findById(UUID.fromString(newListEntry.getId()))).isPresent()) {
            ListEntry oldListEntry = oldListEntryOptional.get();
            oldListEntry.setTitle(newListEntry.getTitle());
            oldListEntry.setText(newListEntry.getText());
            oldListEntry.setCreationDate(LocalDate.parse(newListEntry.getCreationDate()));
            oldListEntry.setImportance(newListEntry.getImportance().getNumVal());
            oldListEntry.setUser(newUser.get());
            return listEntryRepository.save(oldListEntry);
        } else {
            throw new InstanceNotFoundException("List Entry doesn't exist");
        }
    }


}
