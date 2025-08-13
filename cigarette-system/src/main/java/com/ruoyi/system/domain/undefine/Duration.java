package com.ruoyi.system.domain.undefine;

public class Duration {
    private String begin;
    private String end;
    private int degreeSuspicion;

    public Duration() {
    }

    public Duration(String begin, String end) {
        this.begin = begin;
        this.end = end;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getDegreeSuspicion() {
        return degreeSuspicion;
    }

    public void setDegreeSuspicion(int degreeSuspicion) {
        this.degreeSuspicion = degreeSuspicion;
    }

    @Override
    public String toString() {
        return "Duration{" +
                "begin='" + begin + '\'' +
                ", end='" + end + '\'' +
                ", degreeSuspicion=" + degreeSuspicion +
                '}';
    }
}
