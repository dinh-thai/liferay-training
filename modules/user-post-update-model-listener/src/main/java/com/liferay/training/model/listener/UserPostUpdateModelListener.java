package com.liferay.training.model.listener;

import com.liferay.mail.kernel.model.MailMessage;
import com.liferay.mail.kernel.service.MailService;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.model.User;

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
		// TODO enter required service properties
	},
	service = ModelListener.class
)
public class UserPostUpdateModelListener extends BaseModelListener<User> {
    
    
    @Override
    public void onAfterUpdate(User model) throws ModelListenerException {
        try {
            System.out.println("User post update model listener");
            MailMessage mailMessage = new MailMessage();
            
            mailMessage.setSubject("Security Alert: Account Settings");
            mailMessage.setBody("Liferay has detected that your account settings has been changed.");
            
            InternetAddress toAddress = new InternetAddress(model.getEmailAddress());
            InternetAddress fromAddress = new InternetAddress("do-not-reply@liferay.com");
            
            mailMessage.setTo(toAddress);
            mailMessage.setFrom(fromAddress);
            
            _mailService.sendEmail(mailMessage);
        } catch (AddressException e) {
            e.printStackTrace();
        }
    }

	@Reference
	private MailService _mailService;

}