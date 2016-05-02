package model;

import persistence.NotificationADO;


/**
 * Created by Alumne on 02/05/2016.
 */
public class NotificationModel {

    public void setNotificationAsReaded(NotificationClass n)
    {
        NotificationADO nADO = new NotificationADO();
        nADO.setNotificationAsReaded(n);

    }

}
