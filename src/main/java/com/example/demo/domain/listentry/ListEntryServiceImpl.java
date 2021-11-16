package com.example.demo.domain.listentry;

import com.example.demo.domain.appUser.User;
import com.example.demo.domain.appUser.UserServiceImpl;
import com.example.demo.domain.listentry.dto.ListEntryDTO;
import com.example.demo.domain.listentry.dto.ListEntryDTOForUpdateAdmin;
import com.example.demo.domain.listentry.dto.ListEntryDTOForUpdateUser;

import com.example.demo.domain.listentry.dto.ListEntryDTOForOutput;
import com.example.demo.domain.role.RoleServiceImpl;
import com.example.demo.exception.NotTheOwnerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ListEntryServiceImpl implements ListEntryService {

    private final ListEntryRepository listEntryRepository;
    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;

    @Autowired
    public ListEntryServiceImpl(ListEntryRepository listEntryRepository, UserServiceImpl userService, RoleServiceImpl roleService) {
        this.listEntryRepository = listEntryRepository;
        this.userService = userService;
        this.roleService = roleService;
    }

    /**
     * This method creates a new ListEntry and calls the save method from the ListEntryRepository. If the UUID that is given in the JSON-Body does
     * not match an existing user a InstanceNotFoundException is thrown.
     *
     * @param listEntry is the combined ListEntry information given in the RequestBody
     * @return the newly created ListEntry is returned
     * @throws InstanceNotFoundException if the UserID does not match an existing User
     */
    @Override
    public ListEntry addListEntry(ListEntryDTO listEntry) throws InstanceNotFoundException {
        Optional<User> optionalUser;
        if ((optionalUser = userService.findById(UUID.fromString(listEntry.getUserID()))).isEmpty()) {
            throw new InstanceNotFoundException("User does not exist");
        }
        User user = optionalUser.get();
        return listEntryRepository.save(new ListEntry(null, listEntry.getTitle(), listEntry.getText(), LocalDate.parse(listEntry.getCreationDate()), listEntry.getImportance().getNumVal(), user));
    }

    /**
     * This method is responsible for updating a listEntry as a user and calls the method in the listEntryRepository that
     * saves the updates to the data base.
     *
     * @param newListEntry     the information that is needed to update the existing listEntry
     * @param loggedInUsername the username of the currently logged in user
     * @return the updated listEntry
     * @throws InstanceNotFoundException is thrown when the listEntry that needs to be updated does not exist
     * @throws NotTheOwnerException      is thrown when the listEntry does not belong to the logged in user or the user does
     *                                   not have the authority to update listEntries
     */
    @Override
    public ListEntry updateListEntryAsUser(ListEntryDTOForUpdateUser newListEntry,
                                           String loggedInUsername) throws InstanceNotFoundException, NotTheOwnerException {
        Optional<ListEntry> oldListEntryOptional;
        if ((oldListEntryOptional = listEntryRepository.findById(UUID.fromString(newListEntry.getId()))).isPresent()) {
            ListEntry oldListEntry = oldListEntryOptional.get();
            oldListEntry.setTitle(newListEntry.getTitle());
            oldListEntry.setText(newListEntry.getText());
            oldListEntry.setCreationDate(LocalDate.parse(newListEntry.getCreationDate()));
            oldListEntry.setImportance(newListEntry.getImportance().getNumVal());
            if (!isOwner(loggedInUsername, oldListEntry.getUser().getId()) &&
                    !hasCertainAuthority(userService.getUser(loggedInUsername), "UPDATE_LIST_ENTRY"))
                throw new NotTheOwnerException("You're not the owner of this entry and you do not have the authority to edit it");
            return listEntryRepository.save(oldListEntry);
        } else {
            throw new InstanceNotFoundException("List Entry doesn't exist");
        }
    }

    /**
     * This method is responsible for updating a listEntry as an admin and calls the method in the listEntryRepository that
     * saves the updates to the data base. The user can assign any user to the listEntry.
     *
     * @param newListEntry the information that is needed to update the existing listEntry
     * @return the updated listEntry
     * @throws InstanceNotFoundException is thrown when the listEntry that needs to be updated is non existing
     */
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

    /**
     * This method is responsible for getting a specific ListEntry. Calls the method in the listEntryRepository that
     * gets the listEntry with the given ID from the data base.
     *
     * @param id the ID of the ListEntry that should be displayed
     * @return the ListEntry according to the given ID
     * @throws InstanceNotFoundException if the given ID does not match an existing ListEntry
     */
    @Override
    public ListEntryDTOForOutput getListEntry(UUID id) throws InstanceNotFoundException {
        Optional<ListEntry> optionalListEntry;

        if ((optionalListEntry = listEntryRepository.findById(id)).isEmpty()) {
            throw new InstanceNotFoundException("Element does not exist");
        }
        ListEntry listEntry = optionalListEntry.get();

        return new ListEntryDTOForOutput(listEntry);
    }

    /**
     * This method will get all ListEntries that belong to the given user. Calls the matching method in the listEntryRepository
     * and changes the listEntries into listEntryDTOForOutputs.
     * @param id the UUID of the user that is being searched for
     * @return the list of all the ListEntries that belong to the user
     * @throws InstanceNotFoundException if the given userID does not exist
     */
    @Override
    public List<ListEntryDTOForOutput> getAllListEntries(UUID id) throws InstanceNotFoundException {
        List<ListEntry> listEntries;
        if (!(listEntries = listEntryRepository.findByUserID(id)).isEmpty()) {
            List<ListEntryDTOForOutput> listEntryDTOForOutputs = new ArrayList<>();
            for (int i = 0; i < listEntries.size(); i++) {
                ListEntry listEntry = listEntries.get(i);
                ListEntryDTOForOutput listEntryDTOForOutput = new ListEntryDTOForOutput(listEntry);
                listEntryDTOForOutputs.add(listEntryDTOForOutput);
            }
            return listEntryDTOForOutputs;
        } else {
            throw new InstanceNotFoundException("User does not exist");
        }

    }

    /**
     * This method is responsible for deleting a specific listEntry. Calls the method in the listEntryRepository that
     * will remove the listEntry from the data base.
     * @param id of the listEntry that should be deleted
     * @param username of the currently logged in username
     * @throws InstanceNotFoundException is thrown when the to be deleted listEntry does not exist
     * @throws NotTheOwnerException is thrown if the logged in user is not the owner of the listEntry and does not have
     * the authority to delete a listEntry
     */

    @Override
    public void deleteListEntry(UUID id, String username) throws InstanceNotFoundException, NotTheOwnerException {
        Optional<ListEntry> optionalListEntry = listEntryRepository.findById(id);
        if (optionalListEntry.isEmpty())
            throw new InstanceNotFoundException("List Entry doesn't exist");
        if (!isOwner(username, optionalListEntry.get().getUser().getId()) && !hasCertainAuthority(userService.getUser(username),
                "DELETE_LIST_ENTRY"))
            throw new NotTheOwnerException("You're not the owner of this entry and you do not have the authority to delete it");
        listEntryRepository.delete(optionalListEntry.get());

    }

    /**
     * This method will delete all ListEntries that belong to one user. Depending on the role that the currently logged
     * in user has the behaviour changes. If the user is an admin he is allowed to delete all the Entries without
     * having to check if they have the correct authority or if they are the owner of the list.
     *
     * @param id of the user who's all listEntries need to be deleted
     * @param username the username of the currently logged in user
     * @throws InstanceNotFoundException if the user does not exist
     * @throws NotTheOwnerException      if the logged in user is not the owner of the list and/or has not the authority to
     *                                   delete ListEntries
     */
    @Override
    public void deleteAllListEntries(UUID id, String username) throws InstanceNotFoundException, NotTheOwnerException {
        List<ListEntry> listEntries = listEntryRepository.findByUserID(id);
        if (listEntries.isEmpty()) {
            throw new InstanceNotFoundException("User does not exist");
        }
        if (!userService.getUser(username).getRoles().contains(roleService.getRoleByRolename("ADMIN"))) {
            if (!isOwner(username, listEntries.get(1).getUser().getId()) && !hasCertainAuthority(userService.getUser(username), "DELETE-LIST")) {
                throw new NotTheOwnerException("You do not own this list of entries and do not have the authority to delete those entries");
            }
        }
        for (int i = 0; i < listEntries.size(); i++) {
            listEntryRepository.delete(listEntries.get(i));

        }
    }

    /**
     * This method checks whether the currently logged in user is the owner of the listEntry in question.
     * @param username of the currently logged in user
     * @param userID that is saved in the listEntry under userID
     * @return true if the logged in user is the owner of the listEntry
     */
    private boolean isOwner(String username, UUID userID) {
        return userService.getUser(username).getId().equals(userID);
    }

    /**
     * This method checks whether the user has a specific authority.
     * @param user the currently logged in user
     * @param searchedAuthority the authority that is needed to execute a task
     * @return true if the user has the needed authority
     */
    private boolean hasCertainAuthority(User user, String searchedAuthority) {
        AtomicBoolean result = new AtomicBoolean(false);
        user.getRoles().forEach(role -> role.getAuthorities().forEach(authority -> {
            if (searchedAuthority.equals(authority.getName()))
                result.set(true);
        }));
        return result.get();

    }

}