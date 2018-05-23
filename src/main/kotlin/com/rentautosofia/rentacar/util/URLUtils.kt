package com.rentautosofia.rentacar.util

import javax.servlet.http.HttpServletRequest

object URLUtils {

    fun getBaseURl(request: HttpServletRequest): String {
        val scheme = request.scheme
        val serverName = request.serverName
        val serverPort = request.serverPort
        val contextPath = request.contextPath
        val url = StringBuffer()
        url.append(scheme).append("://").append(serverName)
        if (serverPort != 80 && serverPort != 443) {
            url.append(":").append(serverPort)
        }
        url.append(contextPath)
        if (url.toString().endsWith("/")) {
            url.append("/")
        }
        return url.toString()
    }

}