package com.example.demo.domain.listentry;


import com.example.demo.domain.listentry.dto.ListEntryDTO;
import com.example.demo.domain.listentry.dto.ListEntryDTOForUpdateAdmin;
import com.example.demo.domain.listentry.dto.ListEntryDTOForUpdateUser;

import com.example.demo.domain.listentry.dto.ListEntryDTOForOutput;
import javax.management.InstanceNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ListEntryService {
    ListEntry addListEntry(ListEntryDTO listEntry) throws InstanceNotFoundException;
    ListEntry updateListEntryAsUser(ListEntryDTOForUpdateUser newListEntry) throws InstanceNotFoundException;
    ListEntry updateListEntryAsAdmin(ListEntryDTOForUpdateAdmin newListEntry) throws InstanceNotFoundException;
    ListEntryDTOForOutput getListEntry(UUID id) throws InstanceNotFoundException;
    Collection<ListEntry> getAllListEntries(String username);
}
