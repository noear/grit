package org.noear.grit.server.ui.controller;

import org.noear.grit.client.comparator.ResourceComparator;
import org.noear.grit.client.utils.ResourceTreeUtils;
import org.noear.grit.model.domain.ResourceGroup;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.grit.server.dso.ResourceSpaceCookie;
import org.noear.grit.server.dso.service.ResourceAdminService;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;

import java.sql.SQLException;
import java.util.List;

/**
 * @author noear
 * @since 1.0
 */
@Mapping("/grit/ui/resource/group")
@Controller
public class ResourceGroupController extends BaseController{
    @Inject
    ResourceAdminService resourceAdminService;
    
    @Mapping
    public Object home(long space_id) throws SQLException {

        List<ResourceSpace> list = resourceAdminService.getSpaceList();
        list.sort(ResourceComparator.instance);

        space_id = ResourceSpaceCookie.build(space_id, list);

        viewModel.put("space_id", space_id);
        viewModel.put("list", list);

        return view("grit/ui/resource_group");
    }

    @Mapping("inner")
    public Object inner(long space_id) throws SQLException {
        List<ResourceGroup> list = resourceAdminService.getResourceGroupListBySpace(space_id);
        list = ResourceTreeUtils.build(list,space_id);

        ResourceSpaceCookie.set(space_id);

        viewModel.put("space_id", space_id);
        viewModel.put("list", list);

        return view("grit/ui/resource_group_inner");
    }
}
