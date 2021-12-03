package org.noear.grit.server.ui.controller;

import org.noear.grit.client.comparator.SubjectComparator;
import org.noear.grit.client.utils.SujectTreeUtils;
import org.noear.grit.model.domain.SubjectEntity;
import org.noear.grit.model.domain.SubjectGroup;
import org.noear.grit.server.dso.service.SubjectAdminService;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;

import java.sql.SQLException;
import java.util.List;

/**
 * @author noear
 * @since 1.0
 */
@Mapping("/grit/ui/subject/entity")
@Controller
public class SubjectEntityController extends BaseController {
    @Inject
    SubjectAdminService subjectAdminService;

    @Mapping
    public ModelAndView home(Long group_id) throws SQLException {
        List<SubjectGroup> list = subjectAdminService.getGroupList();
        list = SujectTreeUtils.build(list, 0);

        if (group_id == null) {
            if (list.size() > 0) {
                group_id = list.get(0).subject_id;
            }
        }

        viewModel.put("group_id", group_id);
        viewModel.put("list", list);

        return view("grit/ui/subject_entity");
    }

    @Mapping("s")
    public ModelAndView entity_s(String key) throws SQLException {
        return showInner(0, key, "grit/ui/subject_entity_s");
    }

    @Mapping("inner")
    public ModelAndView inner(long group_id, String key) throws SQLException {
        return showInner(group_id, key, "grit/ui/subject_entity_inner");
    }

    private ModelAndView showInner(long group_id, String key, String viewName) throws SQLException {
        List<SubjectEntity> list = null;

        if (group_id == 0) {
            if (Utils.isEmpty(key)) {
                list = subjectAdminService.getSubjectEntityListByAll();
            } else {
                list = subjectAdminService.getSubjectEntityListByFind(key);
            }
        } else {
            list = subjectAdminService.getSubjectEntityListByGroup(group_id);
        }

        list.sort(SubjectComparator.instance);

        viewModel.put("key", key);
        viewModel.put("group_id", group_id);
        viewModel.put("list", list);

        return view(viewName);
    }
}
