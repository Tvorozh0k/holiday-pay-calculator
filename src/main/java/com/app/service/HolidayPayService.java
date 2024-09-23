package com.app.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Сервис подсчета отпускных сотрудника
 */
@Service
public class HolidayPayService {
    /**
     * Сервис для распознавания нерабочих праздничных дней
     */
    private static final HolidayService holidayService = new HolidayService();

    /**
     * Среднее число дней в месяце
     */
    private static final double averageDaysPerMonth = 29.3;

    /**
     * Точность работы с вещественными числами
     */
    private static final int precision = 2;

    /**
     * Округление вещественного числа
     * @param salary вещественное число
     * @return округленное вещественное число
     */
    private static double roundSalary(double salary) {
        BigDecimal bigDecimal = new BigDecimal(salary).setScale(precision, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    /**
     * Среднедневной заработок
     * @param averageSalary средняя зарплата за 12 месяцев
     * @return среднедневной заработок
     */
    public static double getAverageDailySalary(double averageSalary) {
        return roundSalary(averageSalary / averageDaysPerMonth);
    }

    /**
     * Простой подсчет отпускных без учета праздников
     * @param averageSalary средняя зарплата сотрудника за 12 месяцев
     * @param holidayDuration длительность отпуска
     * @return сумма отпускных
     */
    public static double simpleCalculation(double averageSalary, int holidayDuration) {
        return getAverageDailySalary(averageSalary) * holidayDuration;
    }

    /**
     * Подсчет отпускных с учетом праздников и фиксированным
     * периодом отпуска (его началом и концом)
     * <p>
     * Примечание: длительность отпуска не увеличивается из-за
     * праздничных дней в нем, праздничные дни не оплачиваются во
     * время отпуска
     * @param averageSalary средняя зарплата сотрудника за 12 месяцев
     * @param holidayStart дата начала отпуска
     * @param holidayEnd дата конца отпуска
     * @return сумма отпускных
     */
    public static double fixedHolidayCalculation(double averageSalary, LocalDate holidayStart, LocalDate holidayEnd) {
        int paidDays = 0;

        while (!holidayStart.isAfter(holidayEnd)) {
            if (!holidayService.isHoliday(holidayStart)) {
                ++paidDays;
            }

            holidayStart = holidayStart.plusDays(1);
        }

        return getAverageDailySalary(averageSalary) * paidDays;
    }
}
