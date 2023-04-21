package com.deador.restshop.service;

import com.deador.restshop.entity.User;

public interface MailSenderService {
    void sendVerificationMessage(User user);
}
