package org.noear.grit.server.ui.controller;

import org.noear.grit.client.GritClient;
import org.noear.grit.model.data.SubjectDo;
import org.noear.grit.model.domain.Subject;
import org.noear.grit.model.type.SubjectType;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Result;

import java.sql.SQLException;

/**
 * @author noear
 * @since 1.0
 */
@Mapping("/grit/subject")
@Controller
public class SubjectController extends BaseController {
    @Inject
    GritClient gritClient;
    
    @Mapping("edit")
    public Object edit(long subject_id, long group_id, int type) throws SQLException {
        Subject m1 = gritClient.subjectAdmin().getSubjectById(subject_id);

        if (m1.subject_id == null) {
            m1.subject_type = type;
            m1.is_disabled = false;
            m1.is_visibled = true;
            m1.subject_pid = group_id;
        }

        viewModel.put("group_id",group_id);
        viewModel.put("m1", m1);

        return view("grit/ui/subject_edit");
    }

    @Mapping("edit/ajax/save")
    public Object edit_ajax_save(long subject_id, long group_id,SubjectDo subject) throws SQLException {
        if(subject.is_disabled == null){
            subject.is_disabled = false;
        }

        if(subject.is_visibled == null){
            subject.is_visibled = false;
        }

        //必填
        if(Utils.isEmpty(subject.display_name)){
            return Result.failure("The display name cannot be empty");
        }

        if (subject_id > 0) {
            gritClient.subjectAdmin()
                    .updSubjectById(subject_id, subject);
        } else {
            if(subject.subject_type == SubjectType.entity.code){
                gritClient.subjectAdmin()
                        .addSubjectEntity(subject, group_id);
            }else {
                gritClient.subjectAdmin()
                        .addSubject(subject);
            }
        }

        return Result.succeed();
    }

    @Mapping("edit/ajax/del")
    public Object edit_ajax_del(long subject_id) throws SQLException{
        gritClient.subjectAdmin().delSubjectById(subject_id);

        return Result.succeed();
    }
}
