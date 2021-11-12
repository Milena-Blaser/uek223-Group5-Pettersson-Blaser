package com.example.demo.domain.listentry;


import com.example.demo.domain.listentry.dto.ListEntryDTO;
import com.example.demo.domain.listentry.dto.ListEntryDTOForUpdate;

import javax.management.InstanceNotFoundException;

public interface ListEntryService {
    ListEntry addListEntry(ListEntryDTO listEntry) throws InstanceNotFoundException;
    ListEntry updateListEntry(ListEntryDTOForUpdate newListEntry) throws InstanceNotFoundException;
}
