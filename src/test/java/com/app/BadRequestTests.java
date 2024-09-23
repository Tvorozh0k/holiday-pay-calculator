package com.app;

import com.app.controller.HolidayPayController;
import com.app.model.HolidayPayRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HolidayPayController.class)
public class BadRequestTests {

    static Stream<Arguments> ExamplesData() {
        return Stream.of(
                Arguments.of(LocalDate.of(2024, 6, 15), LocalDate.of(2024, 6, 14)),
                Arguments.of(LocalDate.of(2024,6,10), LocalDate.of(2024, 6, 15))
        );
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Пустой запрос")
    public void EmptyRequestTest() throws Exception {
        // Пустой запрос
        HolidayPayRequest request = new HolidayPayRequest();

        // Ожидается Bad Request
        mockMvc.perform(get("/holiday-pay/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @DisplayName("Некорректные значения зарплаты и продолжительности отпуска")
    @CsvSource({"-15.0,1", "15.0,0", "15.0,-1"})
    public void IncorrectSalaryDurationValues(double averageSalary, int holidayDuration) throws Exception {
        // Пустой запрос
        HolidayPayRequest request = new HolidayPayRequest();

        // then
        assertThatThrownBy(() -> {
            request.setAverageSalary(averageSalary);
            request.setHolidayDuration(holidayDuration);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @MethodSource("ExamplesData")
    @DisplayName("Некорректные даты начала и конца отпуска")
    public void IncorrectDates(LocalDate holidayStart, LocalDate holidayEnd) throws Exception {
        HolidayPayRequest request = new HolidayPayRequest();
        request.setAverageSalary(10000.0);
        request.setHolidayDuration(10);
        request.setHolidayStart(holidayStart);
        request.setHolidayEnd(holidayEnd);

        // Ожидается Bad Request
        mockMvc.perform(get("/holiday-pay/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}