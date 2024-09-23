package com.app.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * Класс с данными для подсчета отпускных
 */
@ToString
@Getter
public class HolidayPayRequest {
    /**
     * Средняя зарплата за 12 месяцев
     */
    private double averageSalary;

    /**
     * Длительность отпуска
     */
    private int holidayDuration;

    /**
     * Дата начала отпуска
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate holidayStart;

    /**
     * Дата конца отпуска
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate holidayEnd;

    public void setAverageSalary(double averageSalary) throws IllegalArgumentException {
        if (averageSalary < 0) {
            throw new IllegalArgumentException("Средняя заработная плата не может быть отрицательной");
        }

        this.averageSalary = averageSalary;
    }

    public void setHolidayDuration(int holidayDuration) throws IllegalArgumentException {
        if (holidayDuration <= 0) {
            throw new IllegalArgumentException("Продолжительность отпуска должна быть положительной");
        }

        this.holidayDuration = holidayDuration;
    }

    public void setHolidayStart(LocalDate holidayStart) {
        this.holidayStart = holidayStart;
    }

    public void setHolidayEnd(LocalDate holidayEnd) {
        this.holidayEnd = holidayEnd;
    }
}
