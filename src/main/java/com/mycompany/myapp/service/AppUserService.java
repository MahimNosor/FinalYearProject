package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.AppUser;
import com.mycompany.myapp.repository.AppUserRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.repository.search.AppUserSearchRepository;
import com.mycompany.myapp.service.dto.AppUserDTO;
import com.mycompany.myapp.service.mapper.AppUserMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.AppUser}.
 */
@Service
@Transactional
public class AppUserService {

    private static final Logger LOG = LoggerFactory.getLogger(AppUserService.class);

    private final AppUserRepository appUserRepository;

    private final AppUserMapper appUserMapper;

    private final AppUserSearchRepository appUserSearchRepository;

    private final UserRepository userRepository;

    public AppUserService(
        AppUserRepository appUserRepository,
        AppUserMapper appUserMapper,
        AppUserSearchRepository appUserSearchRepository,
        UserRepository userRepository
    ) {
        this.appUserRepository = appUserRepository;
        this.appUserMapper = appUserMapper;
        this.appUserSearchRepository = appUserSearchRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save a appUser.
     *
     * @param appUserDTO the entity to save.
     * @return the persisted entity.
     */
    public AppUserDTO save(AppUserDTO appUserDTO) {
        LOG.debug("Request to save AppUser : {}", appUserDTO);
        AppUser appUser = appUserMapper.toEntity(appUserDTO);

        // Link the AppUser to the JHipster User
        if (appUserDTO.getUserId() != null) {
            userRepository.findById(appUserDTO.getUserId()).ifPresent(appUser::setUser);
        }

        appUser = appUserRepository.save(appUser);
        appUserSearchRepository.index(appUser);
        return appUserMapper.toDto(appUser);
    }

    /**
     * Update a appUser.
     *
     * @param appUserDTO the entity to save.
     * @return the persisted entity.
     */
    public AppUserDTO update(AppUserDTO appUserDTO) {
        LOG.debug("Request to update AppUser : {}", appUserDTO);
        AppUser appUser = appUserMapper.toEntity(appUserDTO);

        // Link the AppUser to the JHipster User
        if (appUserDTO.getUserId() != null) {
            userRepository.findById(appUserDTO.getUserId()).ifPresent(appUser::setUser);
        }

        appUser = appUserRepository.save(appUser);
        appUserSearchRepository.index(appUser);
        return appUserMapper.toDto(appUser);
    }

    /**
     * Partially update a appUser.
     *
     * @param appUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AppUserDTO> partialUpdate(AppUserDTO appUserDTO) {
        LOG.debug("Request to partially update AppUser : {}", appUserDTO);

        return appUserRepository
            .findById(appUserDTO.getId())
            .map(existingAppUser -> {
                appUserMapper.partialUpdate(existingAppUser, appUserDTO);

                return existingAppUser;
            })
            .map(appUserRepository::save)
            .map(savedAppUser -> {
                appUserSearchRepository.index(savedAppUser);
                return savedAppUser;
            })
            .map(appUserMapper::toDto);
    }

    /**
     * Get all the appUsers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AppUserDTO> findAll() {
        LOG.debug("Request to get all AppUsers");
        return appUserRepository.findAll().stream().map(appUserMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the appUsers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AppUserDTO> findAllWithEagerRelationships(Pageable pageable) {
        return appUserRepository.findAllWithEagerRelationships(pageable).map(appUserMapper::toDto);
    }

    /**
     * Get one appUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AppUserDTO> findOne(Long id) {
        LOG.debug("Request to get AppUser : {}", id);
        return appUserRepository.findOneWithEagerRelationships(id).map(appUserMapper::toDto);
    }

    /**
     * Delete the appUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AppUser : {}", id);
        appUserRepository.deleteById(id);
        appUserSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the appUser corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AppUserDTO> search(String query) {
        LOG.debug("Request to search AppUsers for query {}", query);
        try {
            return StreamSupport.stream(appUserSearchRepository.search(query).spliterator(), false).map(appUserMapper::toDto).toList();
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public Optional<AppUserDTO> findOneWithUser(Long id) {
        LOG.debug("Request to get AppUser with User : {}", id);
        return appUserRepository
            .findById(id)
            .map(appUser -> {
                appUser.setUser(userRepository.findById(appUser.getUser().getId()).orElse(null));
                return appUserMapper.toDto(appUser);
            });
    }
}
