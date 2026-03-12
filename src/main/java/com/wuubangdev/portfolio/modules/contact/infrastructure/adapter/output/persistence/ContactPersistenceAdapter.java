package com.wuubangdev.portfolio.modules.contact.infrastructure.adapter.output.persistence;

import com.wuubangdev.portfolio.infrastructure.global.exception.ResourceNotFoundException;
import com.wuubangdev.portfolio.modules.contact.domain.model.Contact;
import com.wuubangdev.portfolio.modules.contact.domain.port.ContactRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ContactPersistenceAdapter implements ContactRepositoryPort {

    private final ContactJpaRepository repo;

    private Contact toDomain(ContactJpaEntity e) {
        return Contact.builder().id(e.getId()).name(e.getName()).email(e.getEmail())
                .subject(e.getSubject()).message(e.getMessage()).read(e.getRead()).build();
    }

    private ContactJpaEntity toEntity(Contact c) {
        ContactJpaEntity e = new ContactJpaEntity();
        e.setId(c.getId()); e.setName(c.getName()); e.setEmail(c.getEmail());
        e.setSubject(c.getSubject()); e.setMessage(c.getMessage()); e.setRead(c.getRead());
        return e;
    }

    @Override public Contact save(Contact c) { return toDomain(repo.save(toEntity(c))); }
    @Override public List<Contact> findAll() { return repo.findAll().stream().map(this::toDomain).toList(); }
    @Override public Optional<Contact> findById(Long id) { return repo.findById(id).map(this::toDomain); }

    @Override
    public Contact markAsRead(Long id) {
        ContactJpaEntity e = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact", id));
        e.setRead(true);
        return toDomain(repo.save(e));
    }
}
