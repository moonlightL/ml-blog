package com.extlight.aspect;

import com.extlight.model.Log;
import com.extlight.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.util.Date;

@Component
@Aspect
@Slf4j
public class LogAspect {


    @Autowired
    private LogService logService;


    @Pointcut("@annotation(com.extlight.aspect.SysLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        Date date = new Date();
        //执行方法
        Object result = point.proceed();
        // 保存日志
        this.saveSysLog(point,date);

        return result;
    }

    private void saveSysLog(ProceedingJoinPoint point,Date date) throws Exception {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        SysLog syslog = method.getAnnotation(SysLog.class);

        if(syslog != null){
            Log logObj = new Log();
            logObj.setMethod(signature.getDeclaringTypeName() + "." + method.getName());
            logObj.setDescr(syslog.value());
            logObj.setOperator("admin");
            logObj.setCreateTime(date);
            log.info("保存日志:{}",logObj);
            this.logService.save(logObj);
        }
    }
}
