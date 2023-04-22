package com.deador.restshop.service;

import com.deador.restshop.model.User;

public interface MailSenderService {
    void sendVerificationMessage(User user);
}
