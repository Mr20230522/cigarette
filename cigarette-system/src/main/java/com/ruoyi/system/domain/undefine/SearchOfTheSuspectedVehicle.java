package com.ruoyi.system.domain.undefine;
import lombok.Data;

import java.util.Date;
@Data
public class SearchOfTheSuspectedVehicle {

    private String plate;
    private int suspicionMin;
    private int suspicionMax;
    private String startDate;
    private String endDate;

    public SearchOfTheSuspectedVehicle() {
    }

    public SearchOfTheSuspectedVehicle(String plate, int suspicionMin, int suspicionMax, String startDate, String endDate) {
        this.plate = plate;
        this.suspicionMin = suspicionMin;
        this.suspicionMax = suspicionMax;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public int getSuspicionMin() {
        return suspicionMin;
    }

    public void setSuspicionMin(int suspicionMin) {
        this.suspicionMin = suspicionMin;
    }

    public int getSuspicionMax() {
        return suspicionMax;
    }

    public void setSuspicionMax(int suspicionMax) {
        this.suspicionMax = suspicionMax;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "SearchOfTheSuspectedVehicle{" +
                "plate='" + plate + '\'' +
                ", suspicionMin=" + suspicionMin +
                ", suspicionMax=" + suspicionMax +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
