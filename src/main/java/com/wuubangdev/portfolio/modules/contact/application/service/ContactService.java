package com.wuubangdev.portfolio.modules.contact.application.service;

import com.wuubangdev.portfolio.modules.contact.application.dto.ContactRequest;
import com.wuubangdev.portfolio.modules.contact.application.dto.ContactResponse;

import java.util.List;

public interface ContactService {
    ContactResponse submit(ContactRequest request);
    List<ContactResponse> getAll();
    ContactResponse markAsRead(Long id);
}
