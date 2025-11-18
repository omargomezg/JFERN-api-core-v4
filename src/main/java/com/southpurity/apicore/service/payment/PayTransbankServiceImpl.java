package com.southpurity.apicore.service.payment;

import cl.transbank.common.IntegrationApiKeys;
import cl.transbank.common.IntegrationCommerceCodes;
import cl.transbank.common.IntegrationType;
import cl.transbank.webpay.common.WebpayOptions;
import cl.transbank.webpay.webpayplus.WebpayPlus;
import com.southpurity.apicore.dto.PaymentResponse;
import com.southpurity.apicore.dto.payment.PaymentRequest;
import com.southpurity.apicore.persistence.model.PlaceDocument;
import com.southpurity.apicore.persistence.model.constant.PaymentTypeEnum;
import com.southpurity.apicore.persistence.repository.ConfigurationRepository;
import com.southpurity.apicore.persistence.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PayTransbankServiceImpl implements PayService {

    private final ConfigurationRepository configurationRepository;
    private final PlaceRepository placeRepository;

    @Override
    public PaymentResponse getPayment(PaymentRequest request) {
        PlaceDocument place = placeRepository.findById(request.getPlace().getId()).orElseThrow();
        var configuration = configurationRepository.findBySiteName("southpurity").orElseThrow();
        var returnUrl = configuration.getReturnUrl() + "/" + "SESIONISD";
        String buyOrder = "BUYORDER";
        String sessionId = place.getId();
        double amount = 3400.0;
        var tx = new WebpayPlus.Transaction(new WebpayOptions(
                IntegrationCommerceCodes.WEBPAY_PLUS,
                IntegrationApiKeys.WEBPAY,
                IntegrationType.TEST)
        );
        try {
            var resp = tx.create(buyOrder, sessionId, amount, returnUrl);
            return PaymentResponse.builder()
                    .url(resp.getUrl())
                    .message(resp.getToken())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return PaymentResponse.builder().build();
    }

    @Override
    public PaymentResponse getPaymentStatus(String saleOrderId) {
        return null;
    }

    @Override
    public void scheduledTaskForPendings() {
        //Nothing here
    }

    @Override
    public void updatePendingPayments() {
        //Nothing here
    }

    @Override
    public PaymentTypeEnum getPaymentType() {
        return PaymentTypeEnum.TRANSBANK;
    }
}
