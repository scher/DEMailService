package com.tsystems.ejb;

import com.tsystems.dao.MailAddressDAO;
import com.tsystems.dao.UserDAO;
import com.tsystems.dto.UserDTO;
import com.tsystems.exception.DEMailException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: apronin
 * Date: 22.03.13
 * Time: 13:52
 * To change this template use File | Settings | File Templates.
 */

@Startup
@Singleton
public class DatabaseBean {

    @EJB
    private MailAddressDAO mailAddressDAO;
    @EJB
    private UserBean userBean;

    @PostConstruct
    public void checkNoreplyUser() {
        if (mailAddressDAO.find("noreply@demail.com") == null) {
            UserDTO noreplyUser = new UserDTO();
            noreplyUser.setBirthday(new Date());
            noreplyUser.setMailAddress("noreply@demail.com");
            noreplyUser.setName("Noreply");
            noreplyUser.setSurname("Noreply");
            noreplyUser.setPassword("1");
            try {
                userBean.register(noreplyUser);
            } catch (DEMailException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

//            UserDTO newUser = new UserDTO();
//            newUser.setBirthday(new Date());
//            newUser.setMailAddress("v@m.com");
//            newUser.setName("Vasya");
//            newUser.setSurname("Pupkin");
//            newUser.setPassword("1");
//            try {
//                userBean.register(newUser);
//            } catch (DEMailException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
//
//            newUser = new UserDTO();
//            newUser.setBirthday(new Date());
//            newUser.setMailAddress("t@m.com");
//            newUser.setName("Tolya");
//            newUser.setSurname("Pahalov");
//            newUser.setPassword("1");
//            try {
//                userBean.register(newUser);
//            } catch (DEMailException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
//
//            newUser = new UserDTO();
//            newUser.setBirthday(new Date());
//            newUser.setMailAddress("i@m.com");
//            newUser.setName("Ian");
//            newUser.setSurname("Vorovsky");
//            newUser.setPassword("1");
//            try {
//                userBean.register(newUser);
//            } catch (DEMailException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
        }
    }

    public DatabaseBean() {

    }
}
