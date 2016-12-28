package hx0049.customview.view.calendar.model;

/**
 * Created by hx on 2016/12/23.
 */

public class DateModel {
    private String numberForShow;
    private boolean hasThingToDeal;
    private boolean isCurrentMonth;
    private int year;
    private int month;
    private int day;

    public DateModel(int year, int month, int day, boolean isCurrentMonth) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.isCurrentMonth = isCurrentMonth;
        this.numberForShow = String.valueOf(day);
    }

    public String getNumberForShow() {
        return numberForShow;
    }

    public void setNumberForShow(String numberForShow) {
        this.numberForShow = numberForShow;
    }


    public boolean isHasThingToDeal() {
        return hasThingToDeal;
    }

    public void setHasThingToDeal(boolean hasThingToDeal) {
        this.hasThingToDeal = hasThingToDeal;
    }

    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }

    public void setCurrentMonth(boolean currentMonth) {
        isCurrentMonth = currentMonth;
    }

    @Override
    public String toString() {
        return "DateModel{" +
                "numberForShow='" + numberForShow + '\'' +
                ", hasThingToDeal=" + hasThingToDeal +
                ", isCurrentMonth=" + isCurrentMonth +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                '}';
    }
}
