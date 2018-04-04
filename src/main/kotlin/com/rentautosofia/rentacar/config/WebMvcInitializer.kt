package com.rentautosofia.rentacar.config

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer

class WebMvcInitializer : AbstractAnnotationConfigDispatcherServletInitializer() {

    override fun getRootConfigClasses(): Array<Class<*>> {
        return arrayOf(CustomWebSecurityConfigurerAdapter::class.java)
    }

    override fun getServletConfigClasses(): Array<Class<*>?> {
        return arrayOfNulls(0)
    }

    override fun getServletMappings(): Array<String?> {
        return arrayOfNulls(0)
    }
}

