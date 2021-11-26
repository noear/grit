package gritdock.dso;

import gritdock.model.MenuViewModel;
import org.noear.grit.client.GritClient;
import org.noear.grit.client.GritUtil;
import org.noear.grit.model.domain.Resource;
import org.noear.grit.model.domain.ResourceGroup;
import org.noear.grit.model.domain.ResourceSpace;
import org.noear.solon.Utils;
import org.noear.solon.data.annotation.Cache;
import org.noear.solon.extend.aspect.annotation.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * @author noear
 * @since 1.0
 */
@Service
public class MenuService {

    @Cache
    public MenuViewModel getSpaceMenus(long userId) throws SQLException {
        MenuViewModel viewModel = new MenuViewModel();

        StringBuilder buf = new StringBuilder();

        List<ResourceSpace> resourceSpaceList = GritClient.global().resource().getSpaceList();

        for (ResourceSpace resourceSpace : resourceSpaceList) {
            List<ResourceGroup> groupList = GritClient.global().auth().getUriGroupListBySpace(userId, resourceSpace.resource_id);
            int groupSize = 0;

            StringBuilder groupBuf = new StringBuilder();
            groupBuf.append("<section>");
            groupBuf.append("<header>").append(resourceSpace.display_name).append("</header>");
            groupBuf.append("<ul>");
            for (ResourceGroup m : groupList) {
                Resource res = GritClient.global().auth().getUriFristBySpace(userId, m.resource_id);
                if (Utils.isNotEmpty(res.link_uri)) {
                    groupBuf.append("<li>")
                            .append("<a href='").append(GritUtil.buildDockSpaceUri(resourceSpace, res)).append("' target='_top'>")
                            .append(m.display_name)
                            .append("</a>")
                            .append("</li>");
                    groupSize++;
                }
            }

            groupBuf.append("</ul>");
            groupBuf.append("</section>");

            if (groupSize > 0) {
                if ("#".equals(resourceSpace.link_tags)) { //强制独占处理
                    buf.append("<div>");
                    buf.append(groupBuf);
                    buf.append("</div>");
                } else {
                    buf.append(groupBuf);
                }

                viewModel.count++;
            }
        }

        viewModel.code = buf.toString();

        return viewModel;
    }
}
