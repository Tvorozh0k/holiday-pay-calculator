package com.app.service;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.util.HashSet;
import java.util.Set;

/**
 * Сервис для распознавания нерабочих праздничных дней
 */
@Service
@Getter
public class HolidayService {
    /**
     * Множество нерабочих праздничных дней
     */
    private final Set<MonthDay> holidays;

    /**
     * Конструктор с инициализацией множества нерабочих праздничных дней
     */
    public HolidayService() {
        holidays = new HashSet<>();

        // Новогодние каникулы и Рождество
        for (int i = 1; i <= 8; ++i) {
            holidays.add(MonthDay.of(Month.JANUARY, i));
        }

        // День защитника Отечества
        holidays.add(MonthDay.of(Month.FEBRUARY, 23));

        // Международный женский день
        holidays.add(MonthDay.of(Month.MARCH, 8));

        // Праздник Весны и Труда
        holidays.add(MonthDay.of(Month.MAY, 1));

        // День Победы
        holidays.add(MonthDay.of(Month.MAY, 9));

        // День России
        holidays.add(MonthDay.of(Month.JUNE, 12));

        // День народного единства
        holidays.add(MonthDay.of(Month.NOVEMBER, 4));
    }

    /**
     * Проверяет, является ли данный день нерабочим праздничным днем
     * @param date день (дата)
     * @return true, если date является нерабочим праздничным днем, иначе - false
     */
    public boolean isHoliday(LocalDate date) {
        return holidays.contains(MonthDay.of(date.getMonth(), date.getDayOfMonth()));
    }
}
