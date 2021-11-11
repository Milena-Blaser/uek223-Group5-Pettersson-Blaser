package com.example.demo.domain.ListEntry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListEntryServiceImpl implements ListEntryService {

    @Autowired
    private final ListEntryRepository listEntryRepository;

    public ListEntryServiceImpl(ListEntryRepository listEntryRepository) {
        this.listEntryRepository = listEntryRepository;
    }
    
    @Override
    public ListEntry addListEntry(ListEntry listEntry) {
      return listEntryRepository.save(listEntry);
    }

}
