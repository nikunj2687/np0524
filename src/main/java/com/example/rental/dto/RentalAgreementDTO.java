package com.example.rental.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalAgreementDTO {

    @JsonProperty("Tool Code")
    private String toolCode;

    @JsonProperty("Tool Type")
    private String toolType;

    @JsonProperty("Brand")
    private String brand;

    @JsonProperty("Rental Days")
    private int rentalDays;

    @JsonProperty("Checkout Date")
    private String checkoutDate;

    @JsonProperty("Due Date")
    private String dueDate;

    @JsonProperty("Daily Rental Charge")
    private String dailyRentalCharge;

    @JsonProperty("Charge Days")
    private int chargeDays;

    @JsonProperty("Pre-Discount Charge")
    private String preDiscountCharge;

    @JsonProperty("Discount Percent")
    private String discountPercent;

    @JsonProperty("Discount Amount")
    private String discountAmount;

    @JsonProperty("Final Charge")
    private String finalCharge;
}