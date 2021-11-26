package gritdock.dso;


import org.noear.grit.model.domain.ResourceSpace;
import org.noear.solon.Utils;
import org.noear.grit.client.GritClient;
import org.noear.grit.client.GritUtil;
import org.noear.grit.model.domain.ResourceGroup;
import org.noear.grit.model.domain.Resource;
import gritdock.model.MenuViewModel;

import java.sql.SQLException;
import java.util.List;

public class MenuUtil {

    /**
     * 构建跨系统菜单
     * */
    public static MenuViewModel buildSystemMenus() throws SQLException {
        MenuViewModel viewModel = new MenuViewModel();

        long userId = Session.current().getUserId();

        StringBuilder buf = new StringBuilder();

        List<ResourceSpace> branchedList = GritClient.resourceSpace().getSpaceList();

        for (ResourceSpace groupBranched : branchedList) {
            List<ResourceGroup> modList = GritClient.auth().getUriGroupListBySpace(userId, groupBranched.resource_id);
            int modSize = 0;

            StringBuilder sb = new StringBuilder();
            sb.append("<section>");
            sb.append("<header>").append(groupBranched.display_name).append("</header>");
            sb.append("<ul>");
            for (ResourceGroup m : modList) {
                Resource res = GritClient.auth().getUriFristBySpace(userId, m.resource_id);
                if (Utils.isNotEmpty(res.link_uri)) {
                    sb.append("<li>")
                            .append("<a href='").append(GritUtil.buildDockSpaceUri(groupBranched, res)).append("' target='_top'>")
                            .append(m.display_name)
                            .append("</a>")
                            .append("</li>");
                    modSize++;
                }
            }

            sb.append("</ul>");
            sb.append("</section>");

            if (modSize > 0) {
                if ("#".equals(groupBranched.link_tags)) { //强制独占处理
                    buf.append("<div>");
                    buf.append(sb.toString());
                    buf.append("</div>");
                } else {
                    buf.append(sb.toString());
                }

                viewModel.count++;
            }
        }

        viewModel.code = buf.toString();

        return viewModel;
    }
}
