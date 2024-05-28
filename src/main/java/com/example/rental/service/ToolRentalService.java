package com.example.rental.service;

import com.example.rental.dto.RentalAgreementDTO;
import com.example.rental.model.Tool;
import com.example.rental.repository.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class ToolRentalService {

    @Autowired
    private ToolRepository toolRepository;

    public RentalAgreementDTO checkout(String toolCode, int rentalDays, int discountPercent, LocalDate checkoutDate) {
        if (rentalDays < 1) {
            throw new IllegalArgumentException("Rental day count must be 1 or greater.");
        }
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100.");
        }

        Tool tool = getToolByCode(toolCode);
        LocalDate dueDate = checkoutDate.plusDays(rentalDays);
        int chargeDays = calculateChargeDays(tool, checkoutDate, dueDate);
        double preDiscountCharge = chargeDays * tool.getDailyCharge();
        double discountAmount = preDiscountCharge * discountPercent / 100;
        double finalCharge = preDiscountCharge - discountAmount;

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        String formattedCheckoutDate = checkoutDate.format(dateFormatter);
        String formattedDueDate = dueDate.format(dateFormatter);
        String formattedDailyRentalCharge = String.format("$%.2f", tool.getDailyCharge());
        String formattedPreDiscountCharge = String.format("$%.2f", preDiscountCharge);
        String formattedDiscountAmount = String.format("$%.2f", discountAmount);
        String formattedFinalCharge = String.format("$%.2f", finalCharge);
        String formattedDiscountPercent = discountPercent + "%";

        return new RentalAgreementDTO(
                tool.getToolCode(),
                tool.getToolType(),
                tool.getBrand(),
                rentalDays,
                formattedCheckoutDate,
                formattedDueDate,
                formattedDailyRentalCharge,
                chargeDays,
                formattedPreDiscountCharge,
                formattedDiscountPercent,
                formattedDiscountAmount,
                formattedFinalCharge
        );
    }

    private int calculateChargeDays(Tool tool, LocalDate checkoutDate, LocalDate dueDate) {
        int chargeDays = 0;
        for (LocalDate date = checkoutDate.plusDays(1); !date.isAfter(dueDate); date = date.plusDays(1)) {
            boolean isHoliday = isHoliday(date);
            if ((!isHoliday && tool.isWeekdayCharge() && isWeekday(date)) ||
                    (tool.isWeekendCharge() && isWeekend(date)) ||
                    (tool.isHolidayCharge() && isHoliday)) {
                chargeDays++;
            }
        }
        return chargeDays;
    }

    private boolean isWeekday(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    private boolean isHoliday(LocalDate date) {
        LocalDate independenceDay = LocalDate.of(date.getYear(), Month.JULY, 4);
        if (independenceDay.getDayOfWeek() == DayOfWeek.SATURDAY) {
            independenceDay = independenceDay.minusDays(1);
        } else if (independenceDay.getDayOfWeek() == DayOfWeek.SUNDAY) {
            independenceDay = independenceDay.plusDays(1);
        }
        LocalDate laborDay = LocalDate.of(date.getYear(), Month.SEPTEMBER, 1).with(DayOfWeek.MONDAY);

        return date.equals(independenceDay) || date.equals(laborDay);
    }

    private Tool getToolByCode(String toolCode) {
        Optional<Tool> tool = toolRepository.findById(toolCode);
        if (!tool.isPresent()) {
            throw new IllegalArgumentException("Invalid tool code.");
        }
        return tool.get();
    }
}