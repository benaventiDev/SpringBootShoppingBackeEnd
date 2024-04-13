package com.example.gtshop.dto;

import com.example.gtshop.model.Cart;
import com.example.gtshop.model.Order;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<OrderDto> order;
    private CartDto cart;
}
