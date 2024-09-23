package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс-ответ на запрос о получении суммы отпускных
 */
@AllArgsConstructor
@Setter
@Getter
public class HolidayPayResponse {
    /**
     * Сумма отпускных
     */
    private double vacationPay;
}
