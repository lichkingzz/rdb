package com.itranswarp.rdb;

import java.util.ArrayList;
import java.util.Map;

public class InsertSets {

    final InsertInfo insertInfo;

    InsertSets(InsertInfo insertInfo, Map<String, Object> map) {
        insertInfo.sets = new ArrayList<InsertFieldInfo>(map.size());
        for (String key : map.keySet()) {
            insertInfo.sets.add(new InsertFieldInfo(key, map.get(key)));
        }
        this.insertInfo = insertInfo;
    }

    public String dryRun() {
        return dryRun(false);
    }

    public String dryRun(boolean includeParams) {
        return new InsertRunner(this.insertInfo).dryRun(includeParams);
    }

    public void run() {
        new InsertRunner(this.insertInfo).run();
    }
}

