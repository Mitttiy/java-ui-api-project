package ru.ibs.gasu.client.widgets;

import com.google.gwt.core.client.GWT;
import ru.ibs.gasu.common.models.PlanFactIndicator;

import java.util.stream.Collectors;

public class CreationInvestmentsWidget extends InvestmentsWidget {
//    @Override
//    protected void setUpBaseIndicators() {
//        super.setUpBaseIndicators();
//
//        getYearBox().deselectAll();
//
//        PlanFactIndicator planFactIndicator4 = new PlanFactIndicator();
//        planFactIndicator4.setGid(4L);
//        planFactIndicator4.setNameOrYear("Размер аванса, выплачиваемого заказчиком");
//        getTreeStore().add(planFactIndicator4);
//    }
//
//    @Override
//    public void addRootRowById(Long id) {
//        super.addRootRowById(id);
//
//        PlanFactIndicator planFactIndicator4 = new PlanFactIndicator();
//        planFactIndicator4.setGid(4L);
//        planFactIndicator4.setNameOrYear("Размер аванса, выплачиваемого заказчиком");
//
//        if (!getTreeStore().getRootItems().stream().map(i -> i.getGid()).collect(Collectors.toList()).contains(id)) {
//            GWT.log("not contains ");
//            if (4L == id) {
//                getTreeStore().add(planFactIndicator4);
//                addCurrentYearsToRootIndicator(planFactIndicator4);
//            }
//        }
//    }
}
