package org.noear.grit.model.domain;

/**
 * 主体分组
 *
 * @author noear
 * @since 1.0
 */
public class SubjectGroup extends Subject {
    public SubjectGroup() {

    }

    public SubjectGroup(long subjectGroupId) {
        super();

        subject_id = subjectGroupId;
        subject_type = SubjectType.group.code;
    }
}
