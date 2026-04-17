package com.wuubangdev.portfolio.modules.contact.application.service;

import com.wuubangdev.portfolio.infrastructure.global.mail.MailService;
import com.wuubangdev.portfolio.modules.contact.application.dto.ContactRequest;
import com.wuubangdev.portfolio.modules.contact.application.dto.ContactResponse;
import com.wuubangdev.portfolio.modules.contact.domain.model.Contact;
import com.wuubangdev.portfolio.modules.contact.domain.port.ContactRepositoryPort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ContactServiceImplTest {

    private InMemoryContactRepository repository;
    private CapturingMailSender mailSender;
    private ContactServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryContactRepository();
        mailSender = new CapturingMailSender();
        service = new ContactServiceImpl(repository, new MailService(mailSender), messageSource());
        ReflectionTestUtils.setField(service, "adminEmail", "owner@example.com");
    }

    @AfterEach
    void tearDown() {
        LocaleContextHolder.resetLocaleContext();
    }

    @Test
    void submitShouldSendVietnameseEmailsWhenLocaleIsVi() {
        LocaleContextHolder.setLocale(Locale.forLanguageTag("vi"));

        ContactResponse response = service.submit(new ContactRequest("An", "an@example.com", "Hop tac", "Xin chao"));

        assertThat(response.email()).isEqualTo("an@example.com");
        assertThat(mailSender.messages).hasSize(2);
        assertThat(mailSender.messages.get(0).getTo()).containsExactly("an@example.com");
        assertThat(mailSender.messages.get(0).getSubject()).isEqualTo("Da nhan yeu cau lien he cua ban, An");
        assertThat(mailSender.messages.get(1).getTo()).containsExactly("owner@example.com");
        assertThat(mailSender.messages.get(1).getSubject()).isEqualTo("Co yeu cau lien he moi tu An");
    }

    @Test
    void submitShouldSendEnglishEmailsWhenLocaleIsEn() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);

        service.submit(new ContactRequest("John", "john@example.com", "Work", "Hello there"));

        assertThat(mailSender.messages).hasSize(2);
        assertThat(mailSender.messages.get(0).getSubject()).isEqualTo("We received your contact request, John");
        assertThat(mailSender.messages.get(1).getSubject()).isEqualTo("New contact request from John");
    }

    private StaticMessageSource messageSource() {
        StaticMessageSource source = new StaticMessageSource();
        source.addMessage("contact.mail.user.subject", Locale.ENGLISH, "We received your contact request, {0}");
        source.addMessage("contact.mail.user.body", Locale.ENGLISH, "Hello {0}");
        source.addMessage("contact.mail.admin.subject", Locale.ENGLISH, "New contact request from {0}");
        source.addMessage("contact.mail.admin.body", Locale.ENGLISH, "Admin email body");

        Locale vi = Locale.forLanguageTag("vi");
        source.addMessage("contact.mail.user.subject", vi, "Da nhan yeu cau lien he cua ban, {0}");
        source.addMessage("contact.mail.user.body", vi, "Chao {0}");
        source.addMessage("contact.mail.admin.subject", vi, "Co yeu cau lien he moi tu {0}");
        source.addMessage("contact.mail.admin.body", vi, "Noi dung admin");
        return source;
    }

    private static class InMemoryContactRepository implements ContactRepositoryPort {
        private final List<Contact> contacts = new ArrayList<>();

        @Override
        public Contact save(Contact contact) {
            if (contact.getId() == null) {
                contact.setId((long) (contacts.size() + 1));
            }
            if (contact.getCreatedAt() == null) {
                contact.setCreatedAt(LocalDateTime.now());
            }
            contacts.add(contact);
            return contact;
        }

        @Override
        public List<Contact> findAll() {
            return List.copyOf(contacts);
        }

        @Override
        public Optional<Contact> findById(Long id) {
            return contacts.stream().filter(contact -> id.equals(contact.getId())).findFirst();
        }

        @Override
        public Contact markAsRead(Long id) {
            Contact contact = findById(id).orElseThrow();
            contact.setRead(true);
            return contact;
        }
    }

    private static class CapturingMailSender implements JavaMailSender {
        private final List<SimpleMailMessage> messages = new ArrayList<>();

        @Override
        public jakarta.mail.internet.MimeMessage createMimeMessage(InputStream contentStream) {
            throw new UnsupportedOperationException();
        }

        @Override
        public jakarta.mail.internet.MimeMessage createMimeMessage() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void send(jakarta.mail.internet.MimeMessage mimeMessage) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void send(jakarta.mail.internet.MimeMessage... mimeMessages) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void send(org.springframework.mail.javamail.MimeMessagePreparator mimeMessagePreparator) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void send(org.springframework.mail.javamail.MimeMessagePreparator... mimeMessagePreparators) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void send(SimpleMailMessage simpleMessage) {
            messages.add(simpleMessage);
        }

        @Override
        public void send(SimpleMailMessage... simpleMessages) {
            messages.addAll(List.of(simpleMessages));
        }
    }
}
