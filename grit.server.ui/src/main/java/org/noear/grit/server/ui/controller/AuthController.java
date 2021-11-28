package org.noear.grit.server.ui.controller;

import org.noear.grit.client.GritClient;
import org.noear.grit.client.comparator.SubjectComparator;
import org.noear.grit.client.utils.SujectTreeUtils;
import org.noear.grit.model.domain.SubjectEntity;
import org.noear.grit.model.domain.SubjectGroup;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;

import java.sql.SQLException;
import java.util.List;

/**
 * @author noear
 * @since 1.0
 */
@Mapping("/grit/auth")
@Controller
public class AuthController extends BaseController{
    @Mapping
    public Object home(Long group_id) throws SQLException {
        List<SubjectGroup> list = GritClient.global().subjectAdmin().getGroupList();
        list = SujectTreeUtils.build(list, 0);

        if (group_id == null) {
            if (list.size() > 0) {
                group_id = list.get(0).subject_id;
            }
        }

        viewModel.put("group_id", group_id);
        viewModel.put("list", list);

        return view("grit/ui/auth");
    }

    @Mapping("inner")
    public Object inner(long group_id, String key, Integer state) throws SQLException {
        if (state == null) {
            state = 1;
        }

        List<SubjectEntity> list = GritClient.global().subjectAdmin().getSubjectEntityListByGroup(group_id);
        list.sort(SubjectComparator.instance);


        viewModel.put("key", key);
        viewModel.put("state", state);
        viewModel.put("group_id", group_id);
        viewModel.put("list", list);

        return view("grit/ui/auth_inner");
    }
}
