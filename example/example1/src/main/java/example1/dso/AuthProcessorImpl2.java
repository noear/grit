package example1.dso;

import org.noear.solon.auth.AuthProcessorBase;
import org.noear.solon.core.handle.Context;
import org.noear.grit.client.GritClient;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author noear 2021/6/24 created
 */
public class AuthProcessorImpl2 extends AuthProcessorBase {
    private int puid() {
        return Context.current().session("puid", 0);
    }

    @Override
    public boolean verifyIp(String ip) {
        return true;
    }

    @Override
    public boolean verifyLogined() {
        return puid() > 0;
    }

    @Override
    public boolean verifyPath(String path, String method) {
        try {
            if (GritClient.resource().hasResourceUri(path)) {
                return GritClient.auth().subjectHasUri(puid(), path);
            } else {
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected List<String> getPermissions() {
        try {
            return GritClient.auth().getSubjectPermissionList(puid()).stream().map(m -> m.resource_code).collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected List<String> getRoles() {
        try {
            return GritClient.auth().getSubjectRoleList(puid()).stream().map(m -> m.subject_code).collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
