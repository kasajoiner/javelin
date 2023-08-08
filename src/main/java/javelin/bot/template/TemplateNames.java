package javelin.bot.template;

public class TemplateNames {

    private TemplateNames() {}

    public static final String HELP = "help";
    public static final String OPTION = "option";
    public static final String WELCOME = "welcome";
    public static final String REGISTER = "register";
    public static final String COMMUNICATION = "communication";
    public static final String COMMUNICATION_ACCEPT = COMMUNICATION + "/accept";
    public static final String COMMUNICATION_CANCEL = COMMUNICATION + "/cancel";

    private static final String STATUS = "status";

    private static final String NOTIFICATION = "notification/";
    private static final String ORDER = "order/";
    public static final String ORDER_ADMIN_ACCEPTED = ORDER + "admin/cooked";
    public static final String ORDER_ADMIN_COOKED = ORDER + "admin/done";
    public static final String ORDER_NEW = ORDER + "new";
    public static final String ORDER_ACCEPT = ORDER + "accepted";
    public static final String ORDER_COOKING = ORDER + "cooking";
    public static final String ORDER_COOKED = ORDER + "cooked";
    public static final String ORDER_DELIVERING = ORDER + "delivering";
    public static final String ORDER_DONE = ORDER + "done";

}
