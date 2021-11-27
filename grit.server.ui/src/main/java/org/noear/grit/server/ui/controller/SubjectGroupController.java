package org.noear.grit.server.ui.controller;

import org.noear.grit.client.GritClient;
import org.noear.grit.client.comparator.ResourceComparator;
import org.noear.grit.client.comparator.SubjectComparator;
import org.noear.grit.model.domain.ResourceGroup;
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
@Mapping("/grit/subject/group")
@Controller
public class SubjectGroupController extends BaseController{
    @Mapping
    public Object home(Integer state) throws SQLException {
        if(state == null){
            state = 1;
        }

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


        viewModel.put("state", state);
        viewModel.put("list", list);

        return view("grit/ui/subject_group");
    }
}
