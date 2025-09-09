package com.southpurity.apicore.service.impl;

import com.southpurity.apicore.dto.ContactRequest;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import com.southpurity.apicore.persistence.repository.SaleOrderRepository;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private SaleOrderRepository saleOrderRepository;

    @Mock
    private Configuration freemarkerConfiguration;

    @InjectMocks
    private EmailServiceImpl emailService;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String SENDER_EMAIL = "noreply@purezadelsur.com";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "purezaDelSurGmail", SENDER_EMAIL);
    }

    @Test
    void sendRestorePasswordEmail_Success() throws Exception {
        // Arrange
        UserDocument userDocument = mock(UserDocument.class);
        when(userDocument.getEmail()).thenReturn(TEST_EMAIL);
        String code = "123456";

        MimeMessage mimeMessage = mock(MimeMessage.class);
        Template template = mock(Template.class);

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(freemarkerConfiguration.getTemplate(anyString())).thenReturn(template);

        // Act
        emailService.sendRestorePasswordEmail(userDocument, code);

        // Assert
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(any(MimeMessage.class));
    }

    @Test
    void sendRestorePasswordEmail_NullUserDocument() {
        // Act & Assert
        assertThrows(NullPointerException.class, () ->
            emailService.sendRestorePasswordEmail(null, "123456"));
    }

    @Test
    void sendRestorePasswordEmail_NullCode() {
        UserDocument userDocument = mock(UserDocument.class);
        // Act & Assert
        assertThrows(NullPointerException.class, () ->
            emailService.sendRestorePasswordEmail(userDocument, null));
    }

    @Test
    void sendRestorePasswordEmail_TemplateNotFound() throws Exception {
        // Arrange
        UserDocument userDocument = mock(UserDocument.class);
        when(userDocument.getEmail()).thenReturn(TEST_EMAIL);
        when(freemarkerConfiguration.getTemplate(anyString()))
            .thenThrow(new IOException("Template not found"));

        // Act & Assert
        EmailServiceException exception = assertThrows(EmailServiceException.class,
            () -> emailService.sendRestorePasswordEmail(userDocument, "123456"));
        assertEquals("Email template not found", exception.getMessage());
    }

    @Test
    void sendRestorePasswordEmail_MailError() throws Exception {
        // Arrange
        UserDocument userDocument = mock(UserDocument.class);
        when(userDocument.getEmail()).thenReturn(TEST_EMAIL);

        MimeMessage mimeMessage = mock(MimeMessage.class);
        Template template = mock(Template.class);

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(freemarkerConfiguration.getTemplate(anyString())).thenReturn(template);
        doThrow(new MailSendException("Failed to send"))
            .when(javaMailSender).send(any(MimeMessage.class));

        // Act & Assert
        EmailServiceException exception = assertThrows(EmailServiceException.class,
            () -> emailService.sendRestorePasswordEmail(userDocument, "123456"));
        assertEquals("Failed to send email", exception.getMessage());
    }

    @Test
    void sendWelcomeEmail_Success() throws Exception {
        // Arrange
        UserDocument userDocument = mock(UserDocument.class);
        when(userDocument.getEmail()).thenReturn(TEST_EMAIL);

        MimeMessage mimeMessage = mock(MimeMessage.class);
        Template template = mock(Template.class);

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(freemarkerConfiguration.getTemplate(anyString())).thenReturn(template);

        // Act
        emailService.sendWelcomeEmail(userDocument);

        // Assert
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(any(MimeMessage.class));
    }

    @Test
    void sendWelcomeEmail_NullUserDocument() {
        // Act & Assert
        assertThrows(NullPointerException.class, () ->
            emailService.sendWelcomeEmail(null));
    }

    @Test
    void sendWelcomeEmail_NullEmail() {
        // Arrange
        UserDocument userDocument = mock(UserDocument.class);
        when(userDocument.getEmail()).thenReturn(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
            emailService.sendWelcomeEmail(userDocument));
    }

    @Test
    void sendPurchaseEmail_Success() throws Exception {
        // Arrange
        String orderId = "test-order-id";
        SaleOrderDocument saleOrder = mock(SaleOrderDocument.class);
        UserDocument client = mock(UserDocument.class);

        when(client.getEmail()).thenReturn(TEST_EMAIL);
        when(saleOrder.getClient()).thenReturn(client);
        when(saleOrder.getKeys()).thenReturn(Collections.emptyList());
        when(saleOrderRepository.findById(orderId)).thenReturn(Optional.of(saleOrder));

        MimeMessage mimeMessage = mock(MimeMessage.class);
        Template template = mock(Template.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(freemarkerConfiguration.getTemplate(anyString())).thenReturn(template);

        // Act
        emailService.sendPurchaseEmail(orderId);

        // Assert
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(any(MimeMessage.class));
    }

    @Test
    void sendPurchaseEmail_OrderNotFound() {
        // Arrange
        String orderId = "non-existent-id";
        when(saleOrderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            emailService.sendPurchaseEmail(orderId));
        verify(javaMailSender, never()).createMimeMessage();
    }

    @Test
    void sendContactEmail_Success() throws Exception {
        // Arrange
        ContactRequest contactRequest = mock(ContactRequest.class);
        Map<String, Object> model = new HashMap<>();
        model.put("name", "Test User");
        model.put("email", TEST_EMAIL);
        model.put("message", "Test message");

        when(contactRequest.getModel()).thenReturn(model);

        MimeMessage mimeMessage = mock(MimeMessage.class);
        Template template = mock(Template.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(freemarkerConfiguration.getTemplate(anyString())).thenReturn(template);

        // Act
        emailService.sendContactEmail(contactRequest);

        // Assert
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(any(MimeMessage.class));
    }

    @Test
    void sendContactEmail_NullRequest() {
        // Act & Assert
        assertThrows(NullPointerException.class, () ->
            emailService.sendContactEmail(null));
    }

    @Test
    void sendContactEmail_NullModel() {
        // Arrange
        ContactRequest contactRequest = mock(ContactRequest.class);
        when(contactRequest.getModel()).thenReturn(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
            emailService.sendContactEmail(contactRequest));
    }

    @Test
    void sendTestEmail_Success() throws Exception {
        // Arrange
        MimeMessage mimeMessage = mock(MimeMessage.class);
        Template template = mock(Template.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(freemarkerConfiguration.getTemplate(anyString())).thenReturn(template);

        // Act
        emailService.sendTestEmail(TEST_EMAIL);

        // Assert
        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(any(MimeMessage.class));
    }

    @Test
    void sendTestEmail_NullEmail() {
        // Act & Assert
        assertThrows(NullPointerException.class, () ->
            emailService.sendTestEmail(null));
    }

    @Test
    void sendTestEmail_MailError() throws Exception {
        // Arrange
        MimeMessage mimeMessage = mock(MimeMessage.class);
        Template template = mock(Template.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(freemarkerConfiguration.getTemplate(anyString())).thenReturn(template);
        doThrow(new MailSendException("Failed to send"))
            .when(javaMailSender).send(any(MimeMessage.class));

        // Act & Assert
        EmailServiceException exception = assertThrows(EmailServiceException.class,
            () -> emailService.sendTestEmail(TEST_EMAIL));
        assertEquals("Failed to send email", exception.getMessage());
    }

    @Nested
    class PrivateMethodsTests {
        @Test
        void createMimeMessage_Success() {
            // Arrange
            MimeMessage mimeMessage = mock(MimeMessage.class);
            when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

            // Act
            MimeMessage result = ReflectionTestUtils.invokeMethod(emailService,
                "createMimeMessage",
                TEST_EMAIL, "Test Subject", "Test Content");

            // Assert
            assertNotNull(result);
            verify(javaMailSender).createMimeMessage();
        }

        @Test
        void createMimeMessageWithCC_Success() {
            // Arrange
            MimeMessage mimeMessage = mock(MimeMessage.class);
            when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

            // Act
            MimeMessage result = ReflectionTestUtils.invokeMethod(emailService,
                "createMimeMessageWithCC",
                TEST_EMAIL, "Test Subject", "Test Content", "cc@example.com");

            // Assert
            assertNotNull(result);
            verify(javaMailSender).createMimeMessage();
        }

        @Test
        void processTemplate_Success() throws Exception {
            // Arrange
            Template template = mock(Template.class);
            when(freemarkerConfiguration.getTemplate("test-template.flth")).thenReturn(template);
            doAnswer(invocation -> {
                StringWriter writer = invocation.getArgument(1);
                writer.write("Processed content");
                return null;
            }).when(template).process(any(), any(StringWriter.class));

            // Act
            String result = ReflectionTestUtils.invokeMethod(emailService,
                "processTemplate",
                "test-template.flth", Collections.emptyMap());

            // Assert
            assertNotNull(result);
            assertEquals("Processed content", result);
        }

        @Test
        void processTemplate_TemplateNotFound() throws Exception {
            // Arrange
            when(freemarkerConfiguration.getTemplate(anyString()))
                .thenThrow(new IOException("Template not found"));

            // Act & Assert
            EmailServiceException exception = assertThrows(EmailServiceException.class,
                () -> ReflectionTestUtils.invokeMethod(emailService,
                    "processTemplate",
                    "non-existent.flth", Collections.emptyMap()));
            assertEquals("Email template not found", exception.getMessage());
        }

        @Test
        void processTemplate_ProcessingError() throws Exception {
            // Arrange
            Template template = mock(Template.class);
            when(freemarkerConfiguration.getTemplate(anyString())).thenReturn(template);
            doThrow(new TemplateException("Processing error", null))
                .when(template).process(any(), any(StringWriter.class));

            // Act & Assert
            EmailServiceException exception = assertThrows(EmailServiceException.class,
                () -> ReflectionTestUtils.invokeMethod(emailService,
                    "processTemplate",
                    "template.flth", Collections.emptyMap()));
            assertEquals("Failed to process email template", exception.getMessage());
        }

        @Test
        void sendEmail_Success() {
            // Arrange
            MimeMessage mimeMessage = mock(MimeMessage.class);

            // Act
            ReflectionTestUtils.invokeMethod(emailService, "sendEmail", mimeMessage);

            // Assert
            verify(javaMailSender).send(mimeMessage);
        }

        @Test
        void sendEmail_ThrowsException() {
            // Arrange
            MimeMessage mimeMessage = mock(MimeMessage.class);
            doThrow(new MailSendException("Failed to send"))
                .when(javaMailSender).send(any(MimeMessage.class));

            // Act & Assert
            EmailServiceException exception = assertThrows(EmailServiceException.class,
                () -> ReflectionTestUtils.invokeMethod(emailService, "sendEmail", mimeMessage));
            assertEquals("Failed to send email", exception.getMessage());
        }
    }
}
