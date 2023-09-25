package com.southpurity.apicore.service.impl;

import com.southpurity.apicore.persistence.model.constant.SaleOrderStatusEnum;
import com.southpurity.apicore.persistence.model.saleorder.PaymentDetail;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import com.southpurity.apicore.persistence.repository.ConfigurationRepository;
import com.southpurity.apicore.persistence.repository.PlaceRepository;
import com.southpurity.apicore.persistence.repository.ProductRepository;
import com.southpurity.apicore.persistence.repository.SaleOrderRepository;
import com.southpurity.apicore.persistence.repository.UserRepository;
import com.southpurity.apicore.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

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
    ProfileService profileService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(payGetnetService, "endpoint", "https://checkout.test.getnet.cl");
        ReflectionTestUtils.setField(payGetnetService, "login", "7ffbb7bf1f7361b1200b2e8d74e1d76f");
        ReflectionTestUtils.setField(payGetnetService, "trankey", "SnZP3D63n3I9dH9O");
        //GETNET_ENDPOINT=https://checkout.test.getnet.cl;
        // GETNET_LOGIN=;GETNET_TRANKEY=;JWT_KEY=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970;MONGO_URI=mongodb://localhost:27017/dbmongo
    }

    @Test
    void scheduledTaskForPendings() {
        var sales = List.of(
                mock(SaleOrderDocument.class),
                mock(SaleOrderDocument.class)
        );
        when(saleOrderRepository.findAllByStatusIn(SaleOrderStatusEnum.isPending()))
                .thenReturn(sales);
        when(sales.get(0).getPaymentDetail()).thenReturn(mock(PaymentDetail.class));
        when(sales.get(0).getPaymentDetail().getRequestId()).thenReturn(54);
        when(sales.get(1).getPaymentDetail()).thenReturn(mock(PaymentDetail.class));
        when(sales.get(1).getPaymentDetail().getRequestId()).thenReturn(5234);

        payGetnetService.scheduledTaskForPendings();
    }
}
