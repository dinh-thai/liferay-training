package com.liferay.training.login.events.post;

import com.liferay.mail.kernel.model.MailMessage;
import com.liferay.mail.kernel.service.MailService;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.LifecycleAction;
import com.liferay.portal.kernel.events.LifecycleEvent;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserService;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author ces-user
 */
@Component(
	immediate = true,
	property = {
		"key=login.events.post"
	},
	service = LifecycleAction.class
)
public class PostLoginEventListener implements LifecycleAction {

    @Reference
    protected MailService _mailService;
    
    @Reference
    protected UserService _userService;

    @Override
    public void processLifecycleEvent(LifecycleEvent lifecycleEvent) throws ActionException {
        
        try {
            System.out.println("processLifecycleEvent()");
            
            User user = _userService.getCurrentUser();
            
            MailMessage mailMessage = new MailMessage();
            
            mailMessage.setSubject("Security Alert");
            mailMessage.setBody("Liferay has detected that you logged in at " + user.getLastLoginDate());
            
            InternetAddress toAddress = new InternetAddress(user.getEmailAddress());
            InternetAddress fromAddress = new InternetAddress("do-not-reply@liferay.com");
            
            mailMessage.setTo(toAddress);
            mailMessage.setFrom(fromAddress);
            
            _mailService.sendEmail(mailMessage);
            
        } catch (PortalException e) {
            e.printStackTrace();
        } catch (AddressException e) {
            e.printStackTrace();
        }
        
    }
}