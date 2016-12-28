package hx0049.customview.view.calendar.util;

/**
 * Created by hx on 2016/12/23.
 */

public class TimeUtil {

    public static int getDayNumForMonth(int year, int month) {
        switch (month) {
            case 0:
                return 31;
            case 1:
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    return 29;
                }else{
                    return 28;
                }
            case 2:
                return 31;
            case 3:
                return 30;
            case 4:
                return 31;
            case 5:
                return 30;
            case 6:
                return 31;
            case 7:
                return 31;
            case 8:
                return 30;
            case 9:
                return 31;
            case 10:
                return 30;
            case 11:
                return 31;
            default:
                return 0;
        }
    }

}
