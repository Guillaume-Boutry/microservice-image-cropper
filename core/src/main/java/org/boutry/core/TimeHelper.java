package org.boutry.core;


import io.smallrye.common.constraint.NotNull;
import org.boutry.common.BoutryUtilities.Locale;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;


@ApplicationScoped
public class TimeHelper {

    public String getDateLocalized(@NotNull Locale locale) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
        switch (locale) {
            case FRANCE:
                dateTimeFormatter = dateTimeFormatter.withLocale(java.util.Locale.FRANCE);
                break;
            case JAPAN:
                dateTimeFormatter = dateTimeFormatter.withLocale(java.util.Locale.JAPAN);
                break;
            case ENGLAND:
            default:
                dateTimeFormatter = dateTimeFormatter.withLocale(java.util.Locale.ENGLISH);
        }

        return LocalDate
                .now()
                .format(dateTimeFormatter);

    }

}
