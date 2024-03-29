package com.github.duc010298.transferfile.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, ModelMap modelMap) {
        String errorCode = "Error";
        String message = "Unknown error";

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if(status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            errorCode = statusCode.toString();
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                message = "Page not found";
            } else if(statusCode == HttpStatus.FORBIDDEN.value()) {
                message = "Access Denied You don’t have permission to access";
            } else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                message = "Internal Server Error";
            } else if (statusCode == HttpStatus.METHOD_NOT_ALLOWED.value()) {
                message = "Method not allowed";
            } else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                message = "Bad request";
            }
        }

        modelMap.addAttribute("errorCode", errorCode);
        modelMap.addAttribute("message", message);
        return "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}

