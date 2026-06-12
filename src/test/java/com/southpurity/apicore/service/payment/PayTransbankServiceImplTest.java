package com.southpurity.apicore.service.payment;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.southpurity.apicore.persistence.model.constant.SaleOrderStatusEnum;
import com.southpurity.apicore.persistence.model.saleorder.PaymentDetail;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import com.southpurity.apicore.persistence.repository.ConfigurationRepository;
import com.southpurity.apicore.persistence.repository.SaleOrderRepository;

@ExtendWith(MockitoExtension.class)
class PayTransbankServiceImplTest {

	@InjectMocks
	PayTransbankServiceImpl payTransbankService;
	
	@Mock
	ConfigurationRepository configurationRepository;

	@Mock
	SaleOrderRepository saleOrderRepository;
	
	@Test
	void scheduledTaskForPendings() {

		var saleOrder = new SaleOrderDocument();
		var paymentDetail = PaymentDetail.builder()
		.token("01aba253f168680d5ae87eaaf5d4774a0ece6f118d210137313069a637e5a9c8")
		.build();
		saleOrder.setStatus(SaleOrderStatusEnum.PENDING);
		saleOrder.setPaymentDetail(paymentDetail);



		when(saleOrderRepository.findAllByStatusIn(SaleOrderStatusEnum.isPending())).thenReturn(List.of(saleOrder));
		payTransbankService.scheduledTaskForPendings();
	}
	
}
