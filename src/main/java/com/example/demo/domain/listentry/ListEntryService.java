package com.example.demo.domain.listentry;


import com.example.demo.domain.listentry.dto.ListEntryDTO;
import com.example.demo.domain.listentry.dto.ListEntryDTOForUpdate;
import com.example.demo.domain.listentry.dto.ListEntryDTOForOutput;
import javax.management.InstanceNotFoundException;
import java.util.Optional;
import java.util.UUID;

public interface ListEntryService {
    ListEntry addListEntry(ListEntryDTO listEntry) throws InstanceNotFoundException;
    ListEntry updateListEntry(ListEntryDTOForUpdate newListEntry) throws InstanceNotFoundException;
    ListEntryDTOForOutput getListEntry(UUID id) throws InstanceNotFoundException;
}
