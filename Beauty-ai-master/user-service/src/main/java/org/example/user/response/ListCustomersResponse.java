package org.example.user.response;

import lombok.Data;
import org.example.user.entity.Customers;

import java.util.List;

@Data
public class ListCustomersResponse {
    private char letter;
    List<Customers> customers;
}
