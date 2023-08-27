package com.southpurity.apicore.service;

import com.southpurity.apicore.dto.ContactRequest;
import com.southpurity.apicore.persistence.model.UserDocument;

public interface EmailService {

    void sendRestorePasswordEmail(UserDocument userDocument, String code);

    void sendWelcomeEmail(UserDocument userDocument);

    void sendPurchaseEmail(String saleOrderId);

    void sendContactEmail(ContactRequest contactRequest);

}
