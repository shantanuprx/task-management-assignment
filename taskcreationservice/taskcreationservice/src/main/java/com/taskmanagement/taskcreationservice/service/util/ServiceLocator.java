package com.taskmanagement.taskcreationservice.service.util;

import org.hibernate.resource.beans.container.internal.NoSuchBeanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/* *
 * Generic service bean locator.
 * Gets a service name as a parameter, search in spring context
 * if bean is available then sent back otherwise throws custom ServiceBeanException
 * */
@Component
public class ServiceLocator {

	@Autowired
	private ApplicationContext applicationContext;

	public Object locateServiceBean(String serviceName) {
		try {
			return applicationContext.getBean(serviceName);
		} catch (Exception ex) {
			throw new NoSuchBeanException(ex);
		}
	}
}