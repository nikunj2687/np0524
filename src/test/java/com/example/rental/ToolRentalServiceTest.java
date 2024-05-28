package com.example.rental;

import com.example.rental.dto.RentalAgreementDTO;
import com.example.rental.model.Tool;
import com.example.rental.repository.ToolRepository;
import com.example.rental.service.ToolRentalService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ToolRentalServiceTest {

	@Autowired
	private ToolRentalService rentalService;

	@Autowired
	private ToolRepository toolRepository;

	@BeforeEach
	void setUp() {
		toolRepository.save(new Tool("CHNS", "Chainsaw", "Stihl", 1.49, true, false, true));
		toolRepository.save(new Tool("LADW", "Ladder", "Werner", 1.99, true, true, false));
		toolRepository.save(new Tool("JAKD", "Jackhammer", "DeWalt", 2.99, true, false, false));
		toolRepository.save(new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, true, false, false));
	}

	@Test
	void testInvalidRentalDays() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
				rentalService.checkout("JAKR", 0, 10, LocalDate.of(2015, 9, 3))
		);
		assertEquals("Rental day count must be 1 or greater.", exception.getMessage());
	}

	@Test
	void testInvalidDiscountPercent() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
				rentalService.checkout("JAKR", 5, 101, LocalDate.of(2015, 9, 3))
		);
		assertEquals("Discount percent must be between 0 and 100.", exception.getMessage());
	}

	@Test
	void testCheckout1() {
		RentalAgreementDTO agreement = rentalService.checkout("LADW", 3, 10, LocalDate.of(2020, 7, 2));
		assertNotNull(agreement);
		assertEquals("LADW", agreement.getToolCode());
		assertEquals("Ladder", agreement.getToolType());
		assertEquals("Werner", agreement.getBrand());
		assertEquals(3, agreement.getRentalDays());
		assertEquals("07/02/20", agreement.getCheckoutDate());
		assertEquals("07/05/20", agreement.getDueDate());
		assertEquals("$1.99", agreement.getDailyRentalCharge());
		assertEquals(2, agreement.getChargeDays());
		assertEquals("$3.98", agreement.getPreDiscountCharge());
		assertEquals("10%", agreement.getDiscountPercent());
		assertEquals("$0.40", agreement.getDiscountAmount());
		assertEquals("$3.58", agreement.getFinalCharge());
	}

	@Test
	void testCheckout2() {
		RentalAgreementDTO agreement = rentalService.checkout("CHNS", 5, 25, LocalDate.of(2015, 7, 2));
		assertNotNull(agreement);
		assertEquals("CHNS", agreement.getToolCode());
		assertEquals("Chainsaw", agreement.getToolType());
		assertEquals("Stihl", agreement.getBrand());
		assertEquals(5, agreement.getRentalDays());
		assertEquals("07/02/15", agreement.getCheckoutDate());
		assertEquals("07/07/15", agreement.getDueDate());
		assertEquals("$1.49", agreement.getDailyRentalCharge());
		assertEquals(3, agreement.getChargeDays());
		assertEquals("$4.47", agreement.getPreDiscountCharge());
		assertEquals("25%", agreement.getDiscountPercent());
		assertEquals("$1.12", agreement.getDiscountAmount());
		assertEquals("$3.35", agreement.getFinalCharge());
	}

	@Test
	void testCheckout3() {
		RentalAgreementDTO agreement = rentalService.checkout("JAKD", 6, 0, LocalDate.of(2015, 9, 3));
		assertNotNull(agreement);
		assertEquals("JAKD", agreement.getToolCode());
		assertEquals("Jackhammer", agreement.getToolType());
		assertEquals("DeWalt", agreement.getBrand());
		assertEquals(6, agreement.getRentalDays());
		assertEquals("09/03/15", agreement.getCheckoutDate());
		assertEquals("09/09/15", agreement.getDueDate());
		assertEquals("$2.99", agreement.getDailyRentalCharge());
		assertEquals(4, agreement.getChargeDays());
		assertEquals("$8.97", agreement.getPreDiscountCharge());
		assertEquals("0%", agreement.getDiscountPercent());
		assertEquals("$0.00", agreement.getDiscountAmount());
		assertEquals("$8.97", agreement.getFinalCharge());
	}

	@Test
	void testCheckout4() {
		RentalAgreementDTO agreement = rentalService.checkout("JAKR", 9, 0, LocalDate.of(2015, 7, 2));
		assertNotNull(agreement);
		assertEquals("JAKR", agreement.getToolCode());
		assertEquals("Jackhammer", agreement.getToolType());
		assertEquals("Ridgid", agreement.getBrand());
		assertEquals(9, agreement.getRentalDays());
		assertEquals("07/02/15", agreement.getCheckoutDate());
		assertEquals("07/11/15", agreement.getDueDate());
		assertEquals("$2.99", agreement.getDailyRentalCharge());
		assertEquals(5, agreement.getChargeDays());
		assertEquals("$14.95", agreement.getPreDiscountCharge());
		assertEquals("0%", agreement.getDiscountPercent());
		assertEquals("$0.00", agreement.getDiscountAmount());
		assertEquals("$14.95", agreement.getFinalCharge());
	}

	@Test
	void testCheckout5() {
		RentalAgreementDTO agreement = rentalService.checkout("JAKR", 4, 50, LocalDate.of(2020, 7, 2));
		assertNotNull(agreement);
		assertEquals("JAKR", agreement.getToolCode());
		assertEquals("Jackhammer", agreement.getToolType());
		assertEquals("Ridgid", agreement.getBrand());
		assertEquals(4, agreement.getRentalDays());
		assertEquals("07/02/20", agreement.getCheckoutDate());
		assertEquals("07/06/20", agreement.getDueDate());
		assertEquals("$2.99", agreement.getDailyRentalCharge());
		assertEquals(1, agreement.getChargeDays());
		assertEquals("$5.98", agreement.getPreDiscountCharge());
		assertEquals("50%", agreement.getDiscountPercent());
		assertEquals("$2.99", agreement.getDiscountAmount());
		assertEquals("$2.99", agreement.getFinalCharge());
	}
}
