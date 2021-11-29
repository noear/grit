package org.noear.grit.server.ui.controller;

import org.noear.grit.client.utils.SujectTreeUtils;
import org.noear.grit.model.domain.SubjectGroup;
import org.noear.grit.server.service.SubjectAdminService;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;

import java.sql.SQLException;
import java.util.List;

/**
 * @author noear
 * @since 1.0
 */
@Mapping("/grit/subject/group")
@Controller
public class SubjectGroupController extends BaseController{
    @Inject
    SubjectAdminService subjectAdminService;

    @Mapping
    public Object home(Integer state) throws SQLException {
        if(state == null){
            state = 1;
        }

        List<SubjectGroup> list = subjectAdminService.getGroupList();
        list = SujectTreeUtils.build(list, 0);


        viewModel.put("state", state);
        viewModel.put("list", list);

        return view("grit/ui/subject_group");
    }
}
