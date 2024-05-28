package com.example.rental.controller;

import com.example.rental.dto.RentalAgreementDTO;
import com.example.rental.service.ToolRentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/rental")
public class ToolRentalController {

    @Autowired
    private ToolRentalService rentalService;

    @PostMapping("/checkout")
    public RentalAgreementDTO checkout(@RequestParam String toolCode,
                                       @RequestParam int rentalDays,
                                       @RequestParam int discountPercent,
                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkoutDate) {
        return rentalService.checkout(toolCode, rentalDays, discountPercent, checkoutDate);
    }
}
