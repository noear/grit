package org.noear.grit.server.ui.controller;

import org.noear.grit.client.GritClient;
import org.noear.grit.client.comparator.SubjectComparator;
import org.noear.grit.model.domain.SubjectEntity;
import org.noear.grit.model.domain.SubjectGroup;
import org.noear.grit.server.model.view.TreeNodeVo;
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
public class SubjectEntityController extends BaseController {
    @Mapping
    public Object home(Long group_id) throws SQLException {
        List<SubjectGroup> list = GritClient.global().subjectAdmin().getGroupList();
        List<TreeNodeVo<SubjectGroup>> list2 = new ArrayList<>(list.size());

        list.stream().filter(r -> r.subject_pid == 0)
                .sorted(SubjectComparator.instance)
                .forEachOrdered(r -> {
                    list2.add(new TreeNodeVo<>(r,0));
                    list.stream().filter(r2 -> r2.subject_pid == r.subject_id)
                            .sorted(SubjectComparator.instance)
                            .forEachOrdered(r2 -> list2.add(new TreeNodeVo<>(r2,1)));
                });

        if (group_id == null) {
            if (list2.size() > 0) {
                group_id = list2.get(0).getData().subject_id;
            }
        }

        viewModel.put("group_id", group_id);
        viewModel.put("list", list2);

        return view("grit/ui/subject_entity");
    }

    @Mapping("inner")
    public Object inner(long group_id, String key, Integer state) throws SQLException {
        if (state == null) {
            state = 1;
        }

        List<SubjectEntity> list = GritClient.global().subjectAdmin().getSubjectEntityListByGroup(group_id);
        List<SubjectEntity> list2 = new ArrayList<>(list.size());

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
