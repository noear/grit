package org.noear.grit.server.ui.controller;

import org.noear.grit.client.GritClient;
import org.noear.grit.client.comparator.ResourceComparator;
import org.noear.grit.model.domain.ResourceGroup;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.grit.server.dso.ResourceSpaceCookie;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author noear
 * @since 1.0
 */
@Mapping("/grit/resource/group")
@Controller
public class ResourceGroupController extends BaseController{
    @Mapping
    public Object home(long space_id) throws SQLException {

        List<ResourceSpace> list = GritClient.global().resourceAdmin().getSpaceList();
        list.sort(ResourceComparator.instance);

        space_id = ResourceSpaceCookie.build(space_id, list);

        viewModel.put("space_id", space_id);
        viewModel.put("list", list);

        return view("grit/ui/resource_group");
    }

    @Mapping("inner")
    public Object inner(long space_id) throws SQLException {
        List<ResourceGroup> list = GritClient.global().resourceAdmin().getResourceGroupListBySpace(space_id);
        List<ResourceGroup> list2 = new ArrayList<>(list.size());

        ResourceSpaceCookie.set(space_id);

        list.stream().filter(r -> r.resource_pid == space_id)
                .sorted(ResourceComparator.instance)
                .forEachOrdered(r -> {
                    r.level = 0;
                    list2.add(r);
                    list.stream().filter(r2 -> r2.resource_pid == r.resource_id)
                            .sorted(ResourceComparator.instance)
                            .forEachOrdered(r2 -> {
                                r2.level = 1;
                                list2.add(r2);
                            });
                });


        viewModel.put("space_id", space_id);
        viewModel.put("list", list2);

        return view("grit/ui/resource_group_inner");
    }
}
