package com.southpurity.apicore.service;

import com.southpurity.apicore.dto.CartRequest;
import com.southpurity.apicore.dto.ProductDTO;
import com.southpurity.apicore.dto.PageDTO;
import com.southpurity.apicore.dto.profile.ProfileResponse;
import com.southpurity.apicore.persistence.model.ProductDocument;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ProductService {

    PageDTO<ProductDTO> getAllAvailable(String placeId, Pageable pageable);

    ProductDocument create(ProductDTO order);

    /**
     * Un cliente hace un pedido y toma la orden
     * quedando bloqueada mientras se realiza el pago.
     *
     * @return El total de la compra
     */
    SaleOrderDocument takeOrder(String addressId, List<CartRequest> cartRequest, ProfileResponse profile);

    void cancelOrder(String userId);



}
