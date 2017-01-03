
package com.talentwalker.game.md.core.timing;

import java.util.TimerTask;

import com.talentwalker.game.md.core.service.statistics.ActiveAndPersistenceService;

public class StatisticsActiveAndPersistenceTimerTask extends TimerTask {
    private ActiveAndPersistenceService activeAndPersistenceService;

    public StatisticsActiveAndPersistenceTimerTask(ActiveAndPersistenceService activeAndPersistenceService) {
        super();
        this.activeAndPersistenceService = activeAndPersistenceService;
    }

    @Override
    public void run() {
        activeAndPersistenceService.timerRecordTodayData();
    }

}
