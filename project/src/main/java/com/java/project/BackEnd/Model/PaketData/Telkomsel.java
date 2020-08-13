package com.java.project.BackEnd.Model.PaketData;

import java.util.ArrayList;
import java.util.List;

public class Telkomsel {
    List<PaketData> paketDataList;

    public List<PaketData> getPaketDataList() {
        return paketDataList;
    }

    public void setPaketDataList(List<PaketData> paketDataList) {
        this.paketDataList = paketDataList;
    }

    public Telkomsel() {
        PaketData paketData = new PaketData((float) 1.5, 18000);
        PaketData paketData2 = new PaketData((float) 2.5, 25000);
        PaketData paketData3 = new PaketData((float) 3, 28000);
        PaketData paketData4 = new PaketData((float) 4.5, 35000);
        PaketData paketData5 = new PaketData((float) 5, 36000);
        PaketData paketData6 = new PaketData((float) 15, 80000);
        PaketData paketData7 = new PaketData((float) 25, 120000);
        paketDataList = new ArrayList<>();
        paketDataList.add(paketData);
        paketDataList.add(paketData2);
        paketDataList.add(paketData3);
        paketDataList.add(paketData4);
        paketDataList.add(paketData5);
        paketDataList.add(paketData6);
        paketDataList.add(paketData7);
    }
}
