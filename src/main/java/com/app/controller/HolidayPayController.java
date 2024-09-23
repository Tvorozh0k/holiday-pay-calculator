package com.app.controller;

import com.app.exception.RequestError;
import com.app.model.HolidayPayRequest;
import com.app.model.HolidayPayResponse;
import com.app.service.HolidayPayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.temporal.ChronoUnit;

/**
 * Контроллер для обработки запросов подсчета отпускных
 */
@RestController
@RequestMapping("/holiday-pay")
public class HolidayPayController {
    @GetMapping("/calculate")
    public ResponseEntity<?> calculate(@RequestBody HolidayPayRequest request) {
        // Если в запросе нет значения средней зарплаты или длительности отпуска
        if (Math.abs(request.getAverageSalary()) < 1e-5 || request.getHolidayDuration() == 0) {
            return new ResponseEntity<>(new RequestError(
                    HttpStatus.BAD_REQUEST.value(),
                    "Нет средней зарплаты или длительности отпуска"
            ), HttpStatus.BAD_REQUEST);
        }

        // 2 случая:
        // 1. Дано только число дней отпуска
        // 2. Дано только день начала отпуска и число дней отпуска
        //    (в таком случае отпуск продлевается в зависимости от числа
        //     праздничных дней, но сами праздничные дни не оплачиваются)
        if (request.getHolidayEnd() == null) {
            return new ResponseEntity<>(new HolidayPayResponse(
                    HolidayPayService.simpleCalculation(
                            request.getAverageSalary(),
                            request.getHolidayDuration()
                    )
            ), HttpStatus.OK);
        }

        // Если даты начала отпуска нет или если дата начала отпуска позже даты конца отпуска
        if (request.getHolidayStart() == null || request.getHolidayStart().isAfter(request.getHolidayEnd())) {
            return new ResponseEntity<>(new RequestError(
                    HttpStatus.BAD_REQUEST.value(),
                    "Неправильные даты начала и конца отпуска"
            ), HttpStatus.BAD_REQUEST);
        }

        // Проверка дат отпуска и значения продолжительности отпуска
        if (request.getHolidayStart().until(request.getHolidayEnd(), ChronoUnit.DAYS) + 1 != request.getHolidayDuration()) {
            return new ResponseEntity<>(new RequestError(
                    HttpStatus.BAD_REQUEST.value(),
                    "Даты отпуска не совпадают с продолжительностью отпуска"
            ), HttpStatus.BAD_REQUEST);
        }

        // Случай с точными днями ухода в отпуск
        return new ResponseEntity<>(new HolidayPayResponse(
                HolidayPayService.fixedHolidayCalculation(
                        request.getAverageSalary(),
                        request.getHolidayStart(),
                        request.getHolidayEnd()
                )
        ), HttpStatus.OK);
    }
}
