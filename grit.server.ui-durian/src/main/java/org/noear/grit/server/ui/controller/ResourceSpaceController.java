package org.noear.grit.server.ui.controller;

import org.noear.grit.client.comparator.ResourceComparator;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.grit.server.dso.service.ResourceAdminService;
import org.noear.grit.service.ResourceSchemaService;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * 资源空间
 *
 * @author noear
 * @since 1.0
 */
@Mapping("/grit/ui/resource/space")
@Controller
public class ResourceSpaceController extends BaseController {
    @Inject
    ResourceAdminService resourceAdminService;
    @Inject
    ResourceSchemaService schemaService;

    @Mapping
    public ModelAndView home() throws SQLException {
        List<ResourceSpace> list = resourceAdminService.getSpaceList();
        list.sort(ResourceComparator.instance);

        viewModel.put("list", list);

        return view("grit/ui/resource_space");
    }

    /**
     * 批量导出
     */
    @Mapping("ajax/export")
    public void exportDo(Context ctx, long space_id) throws Exception {
        if (space_id == 0) {
            return;
        }

        String json = schemaService.exportSpaceSchema(space_id);
        String filename = "grit_space" + space_id + "_" + LocalDate.now() + ".jsond";


        ctx.headerSet("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        ctx.outputAsJson(json);
    }
}
