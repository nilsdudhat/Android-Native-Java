package com.iphoto.iphonegallery.iosgallerylauncher.photoios.iosgallery.forandroid.util;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.common.base.Strings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class DateUtils {

    @SuppressLint("NewApi")
    public static boolean isDateFormat(String date) {
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(date, format);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressLint("NewApi")
    public static boolean isForYouDateFormat(String title) {
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd MMMM, yyyy");
            LocalDate.parse(title, format);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String manageForYouDateForLast7Days(String inputDate) {
        Log.d("--date--", "manageDateForLast7Days: " + inputDate);
        String outputDate = "";

        SimpleDateFormat todayFormat = new SimpleDateFormat("'Today'", Locale.ENGLISH);
        SimpleDateFormat yesterdayFormat = new SimpleDateFormat("'Yesterday'", Locale.ENGLISH);
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        SimpleDateFormat onlyDateFormat = new SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH);

        String[] weekDay = new String[7];

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String date = onlyDateFormat.format(cal.getTime());
        Date currentDate = null;
        try {
            currentDate = onlyDateFormat.parse(date);

            long oneDay = 1000 * 3600 * 24;

            boolean isLast7Days = false;

            for (int i = 0; i < 7; i++) {
                Calendar tempCalender = Calendar.getInstance();
                tempCalender.add(Calendar.DATE, -1);
                String tempDate = onlyDateFormat.format(cal.getTime());
                Date tmp = onlyDateFormat.parse(tempDate);

                Objects.requireNonNull(tmp).setTime(Objects.requireNonNull(currentDate).getTime() - oneDay * i);
                String tmpDate = onlyDateFormat.format(tmp);
                if (tmpDate.equals(inputDate)) {
                    if (i == 0) {
                        outputDate = weekDay[6 - i] = todayFormat.format(tmp);
                        isLast7Days = true;
                        break;
                    } else if (i == 1) {
                        outputDate = weekDay[6 - i] = yesterdayFormat.format(tmp);
                        isLast7Days = true;
                        break;
                    } else {
                        outputDate = weekDay[6 - i] = dayFormat.format(tmp);
                        isLast7Days = true;
                        break;
                    }
                }
            }

            if (!isLast7Days) {
                outputDate = inputDate;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return outputDate;
    }

    public static String manageFullDateForLast7Days(String inputDate) {
        Log.d("--date--", "manageForYouDateForLast7Days: " + inputDate);
        String outputDate = "";

        SimpleDateFormat todayformat = new SimpleDateFormat("'Today'", Locale.ENGLISH);
        SimpleDateFormat yesterdayformat = new SimpleDateFormat("'Yesterday'", Locale.ENGLISH);
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        SimpleDateFormat onlydateformat = new SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH);

        String[] weekDay = new String[7];
        Date currentdate = new Date();
        long oneDay = 1000 * 3600 * 24;

        boolean isLast7Days = false;

        for (int i = 0; i < 7; i++) {
            Date tmp = new Date();
            tmp.setTime(currentdate.getTime() - oneDay * i);
            String tmpdate = onlydateformat.format(tmp);
            if (tmpdate.equals(inputDate)) {
                if (i == 0) {
                    outputDate = weekDay[6 - i] = todayformat.format(tmp);
                    isLast7Days = true;
                    break;
                } else if (i == 1) {
                    outputDate = weekDay[6 - i] = yesterdayformat.format(tmp);
                    isLast7Days = true;
                    break;
                } else {
                    outputDate = weekDay[6 - i] = dayFormat.format(tmp);
                    isLast7Days = true;
                    break;
                }
            }
        }

        if (!isLast7Days) {
            outputDate = inputDate;
        }
        return outputDate;
    }

    @SuppressLint("NewApi")
    public static String getTitleOfForYou(String date) {
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate currentDate = LocalDate.parse(date, format);
            int day = currentDate.getDayOfMonth();
            String strDay = String.valueOf(day);
            if (String.valueOf(day).length() == 1) {
                strDay = "0" + strDay;
            }
            Month month = currentDate.getMonth();
            int year = currentDate.getYear();

            String strMonth = String.valueOf(month);
            String modifiedByUpperLower = strMonth.substring(0, 1).toUpperCase() + strMonth.substring(1).toLowerCase();

            return strDay + " " + modifiedByUpperLower + ", " + year;
        } catch (Exception exception) {
            try {
                date = "01/" + date;
                DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate currentDate = LocalDate.parse(date, format);
                Month month = currentDate.getMonth();
                int year = currentDate.getYear();

                String strMonth = String.valueOf(month);
                String modifiedByUpperLower = strMonth.substring(0, 1).toUpperCase() + strMonth.substring(1).toLowerCase();

                return modifiedByUpperLower + ", " + year;
            } catch (Exception exception1) {
                try {
                    date = "01/" + date;
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate currentDate = LocalDate.parse(date, format);
                    int year = currentDate.getYear();

                    return "" + year;
                } catch (Exception exception2) {
                    exception2.printStackTrace();
                    return date;
                }
            }
        }
    }

    @SuppressLint("NewApi")
    public static boolean isMonthFormat(String month) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/yyyy", Locale.ENGLISH);
        //To make strict date format validation
        formatter.setLenient(false);
        Date parsedDate = null;
        try {
            parsedDate = formatter.parse(month);
            Log.d("--date--", "isMonthFormat: " + Objects.requireNonNull(parsedDate).getTime());

            return true;
        } catch (ParseException e) {
            //Handle exception
            e.printStackTrace();
            return false;
        }
    }

    public static String getRecentWeekDate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH);

        Calendar calYesterday = Calendar.getInstance();
        calYesterday.add(Calendar.DATE, -1);
        String yesterday = dateFormat.format(calYesterday.getTime());

        Calendar calFirstDay = Calendar.getInstance();
        calFirstDay.add(Calendar.DATE, -7);
        String firstDay = dateFormat.format(calFirstDay.getTime());

        Log.d("--week--", "getRecentWeekDate: " + firstDay + " to " + yesterday);

        return firstDay + " to " + yesterday;
    }

    public static ArrayList<String> getRecent7DaysList() {
        ArrayList<String> last7DaysList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            String date = dateFormat.format(cal.getTime());
            try {
                Date day = dateFormat.parse(date);
                long oneDay = 1000 * 3600 * 24;

                Objects.requireNonNull(day).setTime(day.getTime() - oneDay * i);
                String strDay = dateFormat.format(day);
                last7DaysList.add(strDay);
                Log.d("--last_days--", "createLastWeekForYou: " + strDay);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return last7DaysList;
    }

    public static String getRecent12Months() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM, yyyy");

        Calendar calYesterday = Calendar.getInstance();
        calYesterday.add(Calendar.MONTH, -1);
        String lastMonth = dateFormat.format(calYesterday.getTime());

        Calendar calFirstMonth = Calendar.getInstance();
        calFirstMonth.add(Calendar.MONTH, -12);
        String firstMonth = dateFormat.format(calFirstMonth.getTime());

        Log.d("--week--", "getRecentWeekDate: " + firstMonth + " to " + lastMonth);

        return firstMonth + " to " + lastMonth;
    }

    public static ArrayList<String> getRecent12MonthsList() {
        ArrayList<String> recent12MonthsList = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");

        for (int i = 12; i >= 1; i--) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -i);
            String date = dateFormat.format(cal.getTime());
            recent12MonthsList.add(date);
            Log.d("--recent_months----", "getRecent12MonthsList: " + date);
        }

        return recent12MonthsList;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getFullDateFromLong(long longDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        return dateFormat.format(new Date(longDate * 1000));
    }

    public static String convertDateFormat(String currentFormat, String requiredFormat, String dateString) {
        String result = "";
        if (Strings.isNullOrEmpty(dateString)) {
            return result;
        }
        SimpleDateFormat formatterOld = new SimpleDateFormat(currentFormat, Locale.getDefault());
        SimpleDateFormat formatterNew = new SimpleDateFormat(requiredFormat, Locale.getDefault());
        Date date = null;
        try {
            date = formatterOld.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            result = formatterNew.format(date);
        }
        return result;
    }

    public static String manageDateForLast7Days(String inputDate, String inputDateFormat) {
        Log.d("--date--", "manageForYouDateForLast7Days: " + inputDate);
        String outputDate = "";

        SimpleDateFormat todayformat = new SimpleDateFormat("'Today'");
        SimpleDateFormat yesterdayformat = new SimpleDateFormat("'Yesterday'");
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
        SimpleDateFormat onlydateformat = new SimpleDateFormat(inputDateFormat);

        String[] weekDay = new String[7];
        Date currentdate = new Date();
        long oneDay = 1000 * 3600 * 24;

        boolean isLast7Days = false;

        for (int i = 0; i < 7; i++) {
            Date tmp = new Date();
            tmp.setTime(currentdate.getTime() - oneDay * i);
            String tmpdate = onlydateformat.format(tmp);
            if (tmpdate.equals(inputDate)) {
                if (i == 0) {
                    outputDate = weekDay[6 - i] = todayformat.format(tmp);
                    isLast7Days = true;
                    break;
                } else if (i == 1) {
                    outputDate = weekDay[6 - i] = yesterdayformat.format(tmp);
                    isLast7Days = true;
                    break;
                } else {
                    outputDate = weekDay[6 - i] = dayFormat.format(tmp);
                    isLast7Days = true;
                    break;
                }
            }
        }

        if (!isLast7Days) {
            outputDate = inputDate;
        }
        return outputDate;
    }
}
