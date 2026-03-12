package com.wuubangdev.portfolio.modules.contact.application.service;

import com.wuubangdev.portfolio.infrastructure.global.exception.ResourceNotFoundException;
import com.wuubangdev.portfolio.modules.contact.application.dto.ContactRequest;
import com.wuubangdev.portfolio.modules.contact.application.dto.ContactResponse;
import com.wuubangdev.portfolio.modules.contact.domain.model.Contact;
import com.wuubangdev.portfolio.modules.contact.domain.port.ContactRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepositoryPort contactRepositoryPort;

    @Override @Transactional
    public ContactResponse submit(ContactRequest request) {
        Contact contact = Contact.builder()
                .name(request.name()).email(request.email()).subject(request.subject())
                .message(request.message()).read(false).build();
        return toResponse(contactRepositoryPort.save(contact));
    }

    @Override
    public List<ContactResponse> getAll() {
        return contactRepositoryPort.findAll().stream().map(this::toResponse).toList();
    }

    @Override @Transactional
    public ContactResponse markAsRead(Long id) {
        return toResponse(contactRepositoryPort.markAsRead(id));
    }

    private ContactResponse toResponse(Contact c) {
        return new ContactResponse(c.getId(), c.getName(), c.getEmail(), c.getSubject(), c.getMessage(), c.getRead());
    }
}
