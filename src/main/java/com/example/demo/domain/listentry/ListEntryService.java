package com.example.demo.domain.listentry;


import com.example.demo.domain.listentry.dto.ListEntryDTO;
import com.example.demo.domain.listentry.dto.ListEntryDTOForUpdateAdmin;
import com.example.demo.domain.listentry.dto.ListEntryDTOForUpdateUser;

import javax.management.InstanceNotFoundException;

public interface ListEntryService {
    ListEntry addListEntry(ListEntryDTO listEntry) throws InstanceNotFoundException;
    ListEntry updateListEntryAsUser(ListEntryDTOForUpdateUser newListEntry) throws InstanceNotFoundException;
    ListEntry updateListEntryAsAdmin(ListEntryDTOForUpdateAdmin newListEntry) throws InstanceNotFoundException;
}
