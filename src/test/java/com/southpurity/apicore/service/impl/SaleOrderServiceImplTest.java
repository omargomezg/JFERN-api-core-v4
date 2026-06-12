package com.southpurity.apicore.service.impl;

import com.southpurity.apicore.persistence.model.ConfigurationDocument;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import com.southpurity.apicore.persistence.repository.ConfigurationRepository;
import com.southpurity.apicore.persistence.repository.ProductRepository;
import com.southpurity.apicore.persistence.repository.SaleOrderRepository;
import com.southpurity.apicore.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashSet;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaleOrderServiceImplTest {

    @InjectMocks
    SaleOrderServiceImpl saleOrderService;

    @Mock
    private SaleOrderRepository saleOrderRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private MongoTemplate mongoTemplate;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ConfigurationRepository configurationRepository;

    @Test
    void asyncTaskForCheckIncompleteTransactions_with_null_payment_detail() {
        SaleOrderDocument saleOrderDocument = new SaleOrderDocument();
        saleOrderDocument.setId("1");
        saleOrderDocument.setProducts(new HashSet<>());
        ConfigurationDocument configurationDocument = mock(ConfigurationDocument.class);

        when(configurationRepository.findBySiteName(anyString())).thenReturn(Optional.of(configurationDocument));
        when(configurationDocument.getMillisecondsToExpirePayment()).thenReturn(1L);
        when(saleOrderRepository.findById(anyString())).thenReturn(Optional.of(saleOrderDocument));

        saleOrderService.asyncTaskForCheckIncompleteTransactions(saleOrderDocument.getId());
    }

}
