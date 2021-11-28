package org.noear.grit.server.ui.controller;

import org.noear.grit.client.GritClient;
import org.noear.grit.client.utils.SujectTreeUtils;
import org.noear.grit.model.domain.SubjectGroup;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
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
    @Inject
    GritClient gritClient;

    @Mapping
    public Object home(Integer state) throws SQLException {
        if(state == null){
            state = 1;
        }

        List<SubjectGroup> list = gritClient.subjectAdmin().getGroupList();
        list = SujectTreeUtils.build(list, 0);


        viewModel.put("state", state);
        viewModel.put("list", list);

        return view("grit/ui/subject_group");
    }
}
