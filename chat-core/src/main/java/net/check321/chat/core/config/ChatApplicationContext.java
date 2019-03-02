package net.check321.chat.core.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ChatApplicationContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ChatApplicationContext.applicationContext = applicationContext;
    }

    protected ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    protected static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    protected static <T> T getBean(Class<T> beanType) {
        return applicationContext.getBean(beanType);
    }

    protected static <T> T getBean(String beanName, Class<T> beanType) {
        return applicationContext.getBean(beanName, beanType);
    }
}
