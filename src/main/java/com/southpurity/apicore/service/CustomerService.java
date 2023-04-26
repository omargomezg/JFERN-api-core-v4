package com.southpurity.apicore.service;

import com.southpurity.apicore.dto.customer.MyOrderResponseDTO;
import com.southpurity.apicore.model.OrderDocument;
import com.southpurity.apicore.repository.OrderRepository;
import com.southpurity.apicore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public List<MyOrderResponseDTO> getMyOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.info("Getting orders for {}", authentication.getName());
        var customer = userRepository.findByEmail(authentication.getName()).orElseThrow();
        return customer.getOrders().stream().map(this::orderDocumentToDTO).collect(Collectors.toList());
    }

    protected MyOrderResponseDTO orderDocumentToDTO(OrderDocument order) {
        return MyOrderResponseDTO.builder().id(order.getId()).correlative(order.getCorrelative()).build();
    }

}
