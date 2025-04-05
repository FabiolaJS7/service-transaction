package com.bootcamp.service.transaction.util;

import com.bootcamp.service.transaction.model.AuditData;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AuditDataUtil {

    public static AuditData create() {
        AuditData auditData = new AuditData();
        auditData.setCreatedAt(new Date());
        auditData.setUpdatedAt(new Date());
        return auditData;
    }

    public static void update(AuditData auditData) {
        auditData.setUpdatedAt(new Date());
    }
}
