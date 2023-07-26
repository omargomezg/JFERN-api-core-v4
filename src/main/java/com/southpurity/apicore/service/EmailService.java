package com.southpurity.apicore.service;

import com.southpurity.apicore.persistence.model.UserDocument;

public interface EmailService {

    void sendRestorePasswordEmail(UserDocument userDocument, String code);

}
