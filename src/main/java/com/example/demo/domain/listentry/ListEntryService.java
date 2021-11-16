package com.example.demo.domain.listentry;


import com.example.demo.domain.listentry.dto.ListEntryDTO;
import com.example.demo.domain.listentry.dto.ListEntryDTOForUpdateAdmin;
import com.example.demo.domain.listentry.dto.ListEntryDTOForUpdateUser;

import com.example.demo.domain.listentry.dto.ListEntryDTOForOutput;
import com.example.demo.exception.NotTheOwnerException;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.UUID;

public interface ListEntryService {
    ListEntry addListEntry(ListEntryDTO listEntry) throws InstanceNotFoundException;

    ListEntry updateListEntryAsUser(ListEntryDTOForUpdateUser newListEntry, String loggedInUsername) throws InstanceNotFoundException, NotTheOwnerException;

    ListEntry updateListEntryAsAdmin(ListEntryDTOForUpdateAdmin newListEntry) throws InstanceNotFoundException;

    ListEntryDTOForOutput getListEntry(UUID id, String username) throws InstanceNotFoundException;

    List<ListEntryDTOForOutput> getAllListEntries(UUID id) throws InstanceNotFoundException;

    void deleteListEntry(UUID id, String username) throws InstanceNotFoundException, NotTheOwnerException;

    void deleteAllListEntries(UUID id, String username) throws InstanceNotFoundException, NotTheOwnerException;

}
