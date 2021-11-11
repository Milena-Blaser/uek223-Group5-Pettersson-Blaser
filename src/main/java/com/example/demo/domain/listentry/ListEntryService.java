package com.example.demo.domain.listentry;


import com.example.demo.domain.listentry.dto.ListEntryDTO;

import javax.management.InstanceNotFoundException;
import java.util.UUID;

public interface ListEntryService {
    ListEntry addListEntry(ListEntryDTO listEntry) throws InstanceNotFoundException;
    ListEntry getListEntry(UUID listEntryID);
}
