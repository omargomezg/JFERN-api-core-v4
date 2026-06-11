package com.southpurity.apicore.service;

import com.southpurity.apicore.dto.ContactRequest;
import com.southpurity.apicore.persistence.model.UserDocument;
import org.springframework.lang.NonNull;

public interface EmailService {

    void sendRestorePasswordEmail(@NonNull UserDocument userDocument,@NonNull String code);

    void sendWelcomeEmail(@NonNull UserDocument userDocument);

    void sendPurchaseEmail(String saleOrderId);

    void sendContactEmail(ContactRequest contactRequest);

    void sendTestEmail(String email);

}
