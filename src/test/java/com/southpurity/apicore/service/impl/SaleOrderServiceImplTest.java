package com.southpurity.apicore.service.impl;

import com.southpurity.apicore.persistence.model.ConfigurationDocument;
import com.southpurity.apicore.persistence.model.ProductDocument;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import com.southpurity.apicore.persistence.repository.ConfigurationRepository;
import com.southpurity.apicore.persistence.repository.ProductRepository;
import com.southpurity.apicore.persistence.repository.SaleOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaleOrderServiceImplTest {

    @InjectMocks
    SaleOrderServiceImpl saleOrderService;

    @Mock
    ConfigurationRepository configurationRepository;

    @Mock
    SaleOrderRepository saleOrderRepository;

    @Mock
    ProductRepository productRepository;

    @Test
    void asyncTaskForCheckIncompleteTransactions_with_null_payment_detail() {
        SaleOrderDocument saleOrderDocument = new SaleOrderDocument();
        saleOrderDocument.setId("1");
        saleOrderDocument.setProducts(List.of(
                mock(ProductDocument.class),
                mock(ProductDocument.class),
                mock(ProductDocument.class)
        ));
        ConfigurationDocument configurationDocument = mock(ConfigurationDocument.class);

        when(configurationRepository.findBySiteName(anyString())).thenReturn(Optional.of(configurationDocument));
        when(configurationDocument.getMillisecondsToExpirePayment()).thenReturn(1L);
        when(saleOrderRepository.findById(anyString())).thenReturn(Optional.of(saleOrderDocument));
        when(productRepository.save(any())).thenReturn(mock(ProductDocument.class));

        saleOrderService.asyncTaskForCheckIncompleteTransactions(saleOrderDocument.getId());
    }

}
