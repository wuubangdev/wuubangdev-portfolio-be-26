package com.wuubangdev.portfolio.modules.contact.application.service;

import com.wuubangdev.portfolio.infrastructure.global.exception.ResourceNotFoundException;
import com.wuubangdev.portfolio.infrastructure.global.mail.MailService;
import com.wuubangdev.portfolio.modules.contact.application.dto.ContactRequest;
import com.wuubangdev.portfolio.modules.contact.application.dto.ContactResponse;
import com.wuubangdev.portfolio.modules.contact.domain.model.Contact;
import com.wuubangdev.portfolio.modules.contact.domain.port.ContactRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepositoryPort contactRepositoryPort;
    private final MailService mailService;
    private final MessageSource messageSource;

    @Value("${app.admin.email:admin@example.com}")
    private String adminEmail;

    @Override @Transactional
    public ContactResponse submit(ContactRequest request) {
        Contact contact = Contact.builder()
                .name(request.name()).email(request.email()).subject(request.subject())
                .message(request.message()).read(false).build();
        Contact saved = contactRepositoryPort.save(contact);
        Locale locale = LocaleContextHolder.getLocale();

        mailService.sendContactAutoReply(
                request.email(),
                getMessage("contact.mail.user.subject", locale, request.name()),
                getMessage("contact.mail.user.body", locale, request.name(), request.subject(), request.message())
        );

        mailService.sendContactNotificationToAdmin(
                adminEmail,
                getMessage("contact.mail.admin.subject", locale, request.name()),
                getMessage("contact.mail.admin.body", locale, request.name(), request.email(), request.subject(), request.message())
        );

        return toResponse(saved);
    }

    @Override
    public List<ContactResponse> getAll() {
        return contactRepositoryPort.findAll().stream().map(this::toResponse).toList();
    }

    @Override @Transactional
    public ContactResponse markAsRead(Long id) {
        return toResponse(contactRepositoryPort.markAsRead(id));
    }

    @Override @Transactional
    public ContactResponse update(Long id, ContactRequest request) {
        Contact contact = contactRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact", id));
        contact.setName(request.name());
        contact.setEmail(request.email());
        contact.setSubject(request.subject());
        contact.setMessage(request.message());
        return toResponse(contactRepositoryPort.save(contact));
    }

    @Override @Transactional
    public ContactResponse changeStatus(Long id, String status) {
        Contact contact = contactRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact", id));
        contact.setStatus(status);
        return toResponse(contactRepositoryPort.save(contact));
    }

    private ContactResponse toResponse(Contact c) {
        return new ContactResponse(c.getId(), c.getName(), c.getEmail(), c.getSubject(), c.getMessage(), c.getRead(), c.getStatus(), c.getCreatedAt());
    }

    private String getMessage(String key, Locale locale, Object... args) {
        try {
            return messageSource.getMessage(key, args, locale);
        } catch (Exception e) {
            return key;
        }
    }
}
