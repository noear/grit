package example1.dso;

import org.noear.solon.auth.AuthProcessor;
import org.noear.solon.auth.annotation.Logical;
import org.noear.solon.core.handle.Context;
import org.noear.grit.client.GritClient;

/**
 * @author noear 2021/5/28 created
 */
public class AuthProcessorImpl implements AuthProcessor {
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
            if (GritClient.resource().hasResourcePath(path)) {
                return GritClient.userHasPath(puid(), path);
            } else {
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verifyPermissions(String[] permissions, Logical logical) {
        int puid = puid();

        try {
            if (logical == Logical.AND) {
                boolean isOk = true;

                for (String p : permissions) {
                    isOk = isOk && GritClient.userHasPermission(puid(), p);
                }

                return isOk;
            } else {
                for (String p : permissions) {
                    if (GritClient.userHasPermission(puid(), p)) {
                        return true;
                    }
                }
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verifyRoles(String[] roles, Logical logical) {
        int puid = puid();

        try {
            if (logical == Logical.AND) {
                boolean isOk = true;

                for (String p : roles) {
                    isOk = isOk && GritClient.userHasRole(puid, p); //BcfClient.isUserInGroup(puid, p);
                }

                return isOk;
            } else {
                for (String p : roles) {
                    if (GritClient.userHasRole(puid, p)) {
                        return true;
                    }
                }
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
