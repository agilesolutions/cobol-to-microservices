package com.agilesolutions.customer.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
@Builder
public class Customer {

    @Id
    private String id;

    @NotBlank(message = "customer number must be present")
    private String customerNumber;

    @NotBlank(message = "customer type must be present")
    private String customerType;

    @NotBlank(message = "customer name must be present")
    private String customerName;

}
