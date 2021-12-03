package org.noear.grit.server.ui.controller;

import org.noear.grit.client.comparator.ResourceComparator;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.grit.server.dso.service.ResourceAdminService;
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
@Mapping("/grit/ui/resource/space")
@Controller
public class ResourceSpaceController extends BaseController{
    @Inject
    ResourceAdminService resourceAdminService;
    
    @Mapping
    public ModelAndView home() throws SQLException {
        List<ResourceSpace> list = resourceAdminService.getSpaceList();
        list.sort(ResourceComparator.instance);

        viewModel.put("list", list);

        return view("grit/ui/resource_space");
    }
}
