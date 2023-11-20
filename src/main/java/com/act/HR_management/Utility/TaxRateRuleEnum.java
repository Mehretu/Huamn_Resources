package com.act.HR_management.Utility;

import lombok.Getter;

@Getter

public enum TaxRateRuleEnum {
    RULE_1(0, 600, 0),
    RULE_2(601, 1650, 10),
    RULE_3(1651, 3200, 15),
    RULE_4(3201, 5250, 20),
    RULE_5(5251, 7800, 25),
    RULE_6(7801, 10900, 30),
    RULE_7(10901, 0, 35);



    private final double from;
    private final double to;
    private final double percentage;

    TaxRateRuleEnum(double from, double to, double percentage) {
        this.from = from;
        this.to =  to;
        this.percentage = percentage;
    }

}
