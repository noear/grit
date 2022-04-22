package org.noear.grit.server.ui.controller;

import org.noear.grit.client.comparator.ResourceComparator;
import org.noear.grit.model.data.ResourceDo;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.grit.server.dso.service.ResourceAdminService;
import org.noear.grit.service.ResourceSchemaService;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.solon.core.handle.Result;
import org.noear.solon.core.handle.UploadedFile;

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
    ResourceSchemaService resourceSchemaService;

    final String jsondTable = "grit_space";

    @Mapping
    public ModelAndView home() throws SQLException {
        List<ResourceSpace> list = resourceAdminService.getSpaceList();
        list.sort(ResourceComparator.instance);

        viewModel.put("list", list);

        return view("grit/ui/resource_space");
    }

    /**
     * 批量导入
     */
    @Mapping("ajax/import")
    public Result importDo(UploadedFile file) throws Exception {
        if (file == null) {
            return Result.failure();
        }

        try {
            //转换数据
            String data = Utils.transferToString(file.content, "UTF-8");

            //开始导入
            resourceSchemaService.importSchema(data);

            return Result.succeed();
        } catch (Throwable e) {
            return Result.failure(e.getLocalizedMessage());
        }
    }

    /**
     * 批量导出
     */
    @Mapping("ajax/export")
    public void exportDo(Context ctx, long space_id, String fmt) throws Exception {
        if (space_id == 0) {
            return;
        }

        //导出结构
        String data = resourceSchemaService.exportSchema(space_id, fmt);
        ResourceDo space = resourceAdminService.getResourceById(space_id);

        String filename = null;

        //开始输出
        if ("json".equals(fmt)) {
            filename = jsondTable + "_" + space.resource_code + "_" + LocalDate.now() + ".json";
        } else {
            filename = jsondTable + "_" + space.resource_code + "_" + LocalDate.now() + ".jsond";
        }

        ctx.headerSet("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        ctx.output(data);
    }
}
