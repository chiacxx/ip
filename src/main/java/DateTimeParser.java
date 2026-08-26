import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

/** Parses supported task dates and formats them for display. */
public final class DateTimeParser {
    /** Format accepted for a date without a time. */
    public static final String DATE_FORMAT = "dd-mm-yyyy";

    /** Format accepted for an optional 24-hour time. */
    public static final String TIME_FORMAT = "HH:MM";

    /** Full format accepted when a time is supplied. */
    public static final String DATE_TIME_FORMAT = DATE_FORMAT + " " + TIME_FORMAT;

    private static final String DATE_PATTERN = "[0-9]{2}-[0-9]{2}-[0-9]{4}";
    private static final String DATE_TIME_PATTERN = DATE_PATTERN + " [0-9]{2}:[0-9]{2}";
    private static final DateTimeFormatter INPUT_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter INPUT_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm")
                    .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MMM d uuuu", Locale.ENGLISH);
    private static final DateTimeFormatter DISPLAY_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("h:mm", Locale.ENGLISH);

    private DateTimeParser() {
    }

    /**
     * Validates a date with an optional 24-hour time.
     *
     * @param value date, or date and time, entered by the user
     * @throws DateTimeParseException if the value has an unsupported shape or is invalid
     */
    public static void validate(String value) throws DateTimeParseException {
        parse(value);
    }

    /**
     * Formats a supported task date for display, retaining unsupported legacy text unchanged.
     *
     * @param value stored task date
     * @return formatted date text
     */
    public static String formatForDisplay(String value) {
        try {
            if (value.matches(DATE_TIME_PATTERN)) {
                LocalDateTime dateTime = LocalDateTime.parse(value, INPUT_DATE_TIME_FORMATTER);
                return formatDate(dateTime.toLocalDate()) + ", " + formatTime(dateTime.toLocalTime());
            }
            return formatDate(LocalDate.parse(value, INPUT_DATE_FORMATTER));
        } catch (DateTimeParseException | NullPointerException exception) {
            return value;
        }
    }

    /** Parses a value according to the date-only or date-time format. */
    private static void parse(String value) throws DateTimeParseException {
        if (value.matches(DATE_TIME_PATTERN)) {
            LocalDateTime.parse(value, INPUT_DATE_TIME_FORMATTER);
            return;
        }
        if (value.matches(DATE_PATTERN)) {
            LocalDate.parse(value, INPUT_DATE_FORMATTER);
            return;
        }
        throw new DateTimeParseException("Unsupported date-time format", value, 0);
    }

    /** Formats a date without a time component. */
    private static String formatDate(LocalDate date) {
        return date.format(DISPLAY_DATE_FORMATTER);
    }

    /** Formats a time with a lower-case am/pm suffix. */
    private static String formatTime(LocalTime time) {
        String meridiem = time.getHour() < 12 ? "am" : "pm";
        return time.format(DISPLAY_TIME_FORMATTER) + meridiem;
    }
}
