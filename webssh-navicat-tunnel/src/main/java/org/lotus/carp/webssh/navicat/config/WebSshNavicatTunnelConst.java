package org.lotus.carp.webssh.navicat.config;

/**
 * <h3>webssh</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-05-08 10:30
 **/
public class WebSshNavicatTunnelConst {
    /**
     * webssh url prefix.
     */
    public static final String WEB_SSH_PREFIX = "/php";

    /**
     * navicat ntunnel prefix
     */
    public static final String WEB_SSH_NAVICAT_TUNNEL_PREFIX = "/navicat/ntunnel";

    /**
     * php controller mapping url
     */
    public static final String WEB_SSH_NAVICAT_MAPPING = WEB_SSH_PREFIX + WEB_SSH_NAVICAT_TUNNEL_PREFIX;

    /**
     * php file folder
     */
    public static final String WEB_SSH_NAVICAT_PHP_FOLDER = "php";

    /**
     * mysql ntunnel url
     */
    public static final String WEB_SSH_NAVICAT_MYSQL_FILE = WEB_SSH_NAVICAT_PHP_FOLDER + "/" + "ntunnel_mysql.php";
    /**
     * pgsql ntunnel url
     */
    public static final String WEB_SSH_NAVICAT_PGSQL_FILE = WEB_SSH_NAVICAT_PHP_FOLDER + "/" + "ntunnel_pgsql.php";
    /**
     * sqlite ntunnel url
     */
    public static final String WEB_SSH_NAVICAT_SQLITE_FILE = WEB_SSH_NAVICAT_PHP_FOLDER + "/" + "ntunnel_sqlite.php";

    /**
     * php info url
     */
    public static final String WEB_SSH_PHP_INFO_FILE = WEB_SSH_NAVICAT_PHP_FOLDER + "/" + "php_info.php";

    /**
     * mongo admin page URL
     */
    public static final String WEB_SSH_MONGO_ADMIN_URL = WEB_SSH_PREFIX + "/dbadmin/mongoAdmin";
    /**
     * mongo admin page source url
     */
    public static final String WEB_SSH_MONGO_ADMIN_PAGE = WEB_SSH_NAVICAT_PHP_FOLDER + "/mongo_admin/moadmin.php";
}
