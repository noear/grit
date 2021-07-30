package gritdock.dso;


import org.noear.grit.client.model.Branch;
import org.noear.solon.Utils;
import org.noear.grit.client.GritClient;
import org.noear.grit.client.GritUtil;
import org.noear.grit.client.model.Group;
import org.noear.grit.client.model.Resource;
import gritdock.models.MenuViewModel;

import java.sql.SQLException;
import java.util.List;

public class MenuUtil {

    public static MenuViewModel buildSystemMenus() throws SQLException {
        MenuViewModel viewModel = new MenuViewModel();

        long userId = Session.current().getUserId();

        StringBuilder sball = new StringBuilder();

        List<Branch> branchedList = GritClient.branched().getBranchList();

        for (Branch groupBranched : branchedList) {
            List<Group> modList = GritClient.getUserModules(userId, groupBranched.group_id);
            int modSize = 0;

            StringBuilder sb = new StringBuilder();
            sb.append("<section>");
            sb.append("<header>").append(groupBranched.display_code).append("</header>");
            sb.append("<ul>");
            for (Group m : modList) {
                Resource res = GritClient.getUserMenusFirstOfModule(userId, m.group_id);
                if (Utils.isEmpty(res.link_uri) == false) {
                    sb.append("<li>")
                            .append("<a href='").append(GritUtil.buildDockFullUri(groupBranched, res)).append("' target='_top'>")
                            .append(m.display_name)
                            .append("</a>")
                            .append("</li>");
                    modSize++;
                }
            }

            sb.append("</ul>");
            sb.append("</section>");

            if (modSize > 0) {

                if ("#".equals(groupBranched.tags)) { //强制独占处理
                    sball.append("<div>");
                    sball.append(sb.toString());
                    sball.append("</div>");
                } else {
                    sball.append(sb.toString());
                }

                viewModel.count++;
            }
        }

        viewModel.code = sball.toString();

        return viewModel;
    }
}
