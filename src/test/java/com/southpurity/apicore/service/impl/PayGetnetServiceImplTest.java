package com.southpurity.apicore.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.placetopay.java_placetopay.Entities.Models.RedirectRequest;
import com.southpurity.apicore.dto.payment.ClientDto;
import com.southpurity.apicore.dto.payment.ItemsDto;
import com.southpurity.apicore.dto.payment.PaymentRequest;
import com.southpurity.apicore.dto.payment.PlaceDto;
import com.southpurity.apicore.persistence.model.ConfigurationDocument;
import com.southpurity.apicore.persistence.model.PlaceDocument;
import com.southpurity.apicore.persistence.model.ProductDocument;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.persistence.model.constant.SaleOrderStatusEnum;
import com.southpurity.apicore.persistence.model.saleorder.PaymentDetail;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import com.southpurity.apicore.persistence.repository.ConfigurationRepository;
import com.southpurity.apicore.persistence.repository.PlaceRepository;
import com.southpurity.apicore.persistence.repository.ProductRepository;
import com.southpurity.apicore.persistence.repository.SaleOrderRepository;
import com.southpurity.apicore.persistence.repository.UserRepository;
import com.southpurity.apicore.service.ProfileService;
import com.southpurity.apicore.service.payment.PayGetnetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PayGetnetServiceImplTest {

    @InjectMocks
    PayGetnetServiceImpl payGetnetService;

    @Mock
    SaleOrderRepository saleOrderRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    ConfigurationRepository configurationRepository;

    @Mock
    PlaceRepository placeRepository;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    ProfileService profileService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(payGetnetService, "endpoint", "https://checkout.test.getnet.cl");
        ReflectionTestUtils.setField(payGetnetService, "login", "7ffbb7bf1f7361b1200b2e8d74e1d76f");
        ReflectionTestUtils.setField(payGetnetService, "trankey", "SnZP3D63n3I9dH9O");
        // GETNET_ENDPOINT=https://checkout.test.getnet.cl;
        // GETNET_LOGIN=;GETNET_TRANKEY=;JWT_KEY=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970;MONGO_URI=mongodb://localhost:27017/dbmongo
    }

    @Test
    void getPayment_success() throws JsonProcessingException {
        ConfigurationDocument configuration = mock(ConfigurationDocument.class);
        SaleOrderDocument orderDocument = mock(SaleOrderDocument.class);
        PlaceDto place = new PlaceDto();
        place.setId("123");

        List<ProductDocument> products = List.of(mock(ProductDocument.class));

        List<ItemsDto> items = List.of(
                new ItemsDto(1, 1020L, "Producto 1", 1020),
                new ItemsDto(6, 2000L, "Product two", 12000));
        PaymentRequest request = new PaymentRequest();

        ClientDto client = new ClientDto();
        client.setId("1232423423");
        ;

        request.setPlace(place);
        request.setItems(items);
        request.setClient(client);

        when(configurationRepository.findBySiteName(any())).thenReturn(Optional.of(configuration));
        when(configuration.getReturnUrl()).thenReturn("http://localhost");
        when(saleOrderRepository.save(any(SaleOrderDocument.class))).thenReturn(orderDocument);
        when(orderDocument.getId()).thenReturn("3243543535");
        when(placeRepository.findById(place.getId())).thenReturn(Optional.of(mock(PlaceDocument.class)));
        when(userRepository.findById(client.getId())).thenReturn(Optional.of(mock(UserDocument.class)));
        when(productRepository.markAsTaken(anyInt(), anyString(), anyString())).thenReturn(products);
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        var result = payGetnetService.getPayment(request);
        assert result != null;
    }

    @Test
    void scheduledTaskForPendings() {
        var sales = List.of(
                mock(SaleOrderDocument.class),
                mock(SaleOrderDocument.class));
        when(saleOrderRepository.findAllByStatusIn(SaleOrderStatusEnum.isPending()))
                .thenReturn(sales);
        when(sales.get(0).getPaymentDetail()).thenReturn(mock(PaymentDetail.class));
        when(sales.get(0).getPaymentDetail().getRequestId()).thenReturn(54);
        when(sales.get(1).getPaymentDetail()).thenReturn(mock(PaymentDetail.class));
        when(sales.get(1).getPaymentDetail().getRequestId()).thenReturn(5234);

        payGetnetService.scheduledTaskForPendings();
    }
}
