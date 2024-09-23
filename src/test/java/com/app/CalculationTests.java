package com.app;

import com.app.controller.HolidayPayController;
import com.app.model.HolidayPayRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HolidayPayController.class)
public class CalculationTests {
    static Stream<Arguments> ExamplesData() {
        return Stream.of(
                Arguments.of(50000.0, 11, LocalDate.of(2024, 6, 5), LocalDate.of(2024, 6, 15), 17064.8),
                Arguments.of(35000.0, 8, LocalDate.of(2024, 1, 6), LocalDate.of(2024, 1, 13), 5972.7)
        );
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @ParameterizedTest
    @DisplayName("Подсчет отпускных без учета праздников")
    @CsvSource({"50000.0,10,17064.8", "35000.0,8,9556.32"})
    public void SimpleCalculationTest(double averageSalary, int holidayDuration, String expected) throws Exception {
        // Пустой запрос
        HolidayPayRequest request = new HolidayPayRequest();
        request.setAverageSalary(averageSalary);
        request.setHolidayDuration(holidayDuration);

        // then
        mockMvc.perform(get("/holiday-pay/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"vacationPay\":" + expected + "}"));
    }

    @ParameterizedTest
    @MethodSource("ExamplesData")
    @DisplayName("Подсчет отпускных с учетом праздников")
    public void FixedHolidayCalculationTest(double averageSalary, int holidayDuration, LocalDate holidayStart, LocalDate holidayEnd, double expected) throws Exception {
        HolidayPayRequest request = new HolidayPayRequest();
        request.setAverageSalary(averageSalary);
        request.setHolidayDuration(holidayDuration);
        request.setHolidayStart(holidayStart);
        request.setHolidayEnd(holidayEnd);

        // then
        mockMvc.perform(get("/holiday-pay/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"vacationPay\":" + expected + "}"));
    }
}
