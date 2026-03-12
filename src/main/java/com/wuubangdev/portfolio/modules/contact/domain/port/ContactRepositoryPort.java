package com.wuubangdev.portfolio.modules.contact.domain.port;

import com.wuubangdev.portfolio.modules.contact.domain.model.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactRepositoryPort {
    Contact save(Contact contact);
    List<Contact> findAll();
    Optional<Contact> findById(Long id);
    Contact markAsRead(Long id);
}
