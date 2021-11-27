package org.noear.grit.server.ui.controller;

import org.noear.grit.client.GritClient;
import org.noear.grit.client.comparator.ResourceComparator;
import org.noear.grit.client.comparator.SubjectComparator;
import org.noear.grit.model.domain.ResourceGroup;
import org.noear.grit.model.domain.Subject;
import org.noear.grit.model.domain.SubjectGroup;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author noear 2021/11/27 created
 */
@Mapping("/grit/subject/entity")
@Controller
public class SubjectController extends BaseController {
    @Mapping
    public Object home(Long group_id) throws SQLException {
        List<SubjectGroup> list = GritClient.global().subjectAdmin().getGroupList();

        if (group_id == null) {
            if (list.size() > 0) {
                group_id = list.get(0).subject_id;
            }
        }

        viewModel.put("group_id", group_id);
        viewModel.put("list", list);

        return view("grit/ui/subject_entity");
    }

    @Mapping("inner")
    public Object inner(long group_id, String key, Integer state) throws SQLException {
        if (state == null) {
            state = 1;
        }

        List<Subject> list = GritClient.global().subjectAdmin().getSubSubjectListByPid(group_id);
        List<Subject> list2 = new ArrayList<>(list.size());

        list.stream().sorted(SubjectComparator.instance)
                .forEachOrdered(r -> {
                    list2.add(r);
                });


        viewModel.put("key", key);
        viewModel.put("state", state);
        viewModel.put("group_id", group_id);
        viewModel.put("list", list2);

        return view("grit/ui/subject_entity_inner");
    }
}
