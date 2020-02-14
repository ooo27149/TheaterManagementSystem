package com.avan.movie.vo;

import com.avan.movie.po.Screen;

import java.util.List;

public class SeatQuery {

    private Long screenId;
    private List<Integer> maintenances;
    private List<Integer> roads;


    public SeatQuery() {
    }

    public Long getScreenId() {
        return screenId;
    }

    public void setScreenId(Long screenId) {
        this.screenId = screenId;
    }

    public List<Integer> getMaintenances() {
        return maintenances;
    }

    public void setMaintenances(List<Integer> maintenances) {
        this.maintenances = maintenances;
    }

    public List<Integer> getRoads() {
        return roads;
    }

    public void setRoads(List<Integer> roads) {
        this.roads = roads;
    }
}
