package org.noear.grit.server.ui.controller;

import org.noear.grit.client.GritClient;
import org.noear.grit.client.comparator.ResourceComparator;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;

import java.sql.SQLException;
import java.util.List;

/**
 * @author noear
 * @since 1.0
 */
@Mapping("/grit/resource/space")
@Controller
public class ResourceSpaceController extends BaseController{
    @Inject
    GritClient gritClient;
    
    @Mapping
    public Object home() throws SQLException {
        List<ResourceSpace> list = gritClient.resourceAdmin().getSpaceList();
        list.sort(ResourceComparator.instance);

        viewModel.put("list", list);

        return view("grit/ui/resource_space");
    }
}