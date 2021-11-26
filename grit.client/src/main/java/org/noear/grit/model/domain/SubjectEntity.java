package org.noear.grit.model.domain;

import org.noear.grit.model.type.SubjectType;

/**
 * 主体实体
 *
 * @author noear
 * @since 1.0
 */
public class SubjectEntity extends Subject {
    public SubjectEntity() {
        this(0);
    }

    public SubjectEntity(long subjectId) {
        super();

        subject_pid = -1L;
        subject_id = subjectId;
        subject_type = SubjectType.entity.code;
    }
}
