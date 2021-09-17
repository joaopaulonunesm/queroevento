package com.queroevento.controllers;

import javax.servlet.ServletException;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.queroevento.models.Login;
import com.queroevento.services.LoginService;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @RequestMapping(value = "/logins", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Login> postLogin(@RequestBody Login login) throws ServletException {
        return new ResponseEntity<>(loginService.postLogin(login), HttpStatus.CREATED);
    }

    @RequestMapping(value = "v1/logins/password", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Login> putLoginPassword(@RequestHeader(value = "Authorization") String token,
                                                  @RequestBody Login login) throws ServletException {

        Login existenceLogin = loginService.validateLogin(token);

        return new ResponseEntity<>(loginService.putLoginPassword(login, existenceLogin), HttpStatus.OK);
    }

    @RequestMapping(value = "v1/logins/active", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Login> putLoginActive(@RequestHeader(value = "Authorization") String token,
                                                @RequestBody Login login) throws ServletException {

        Login existenceLogin = loginService.validateLogin(token);

        return new ResponseEntity<>(loginService.putLoginActive(login, existenceLogin), HttpStatus.OK);
    }

    @RequestMapping(value = "v1/logins", method = RequestMethod.DELETE)
    public ResponseEntity<Login> deleteLogin(@RequestHeader(value = "Authorization") String token) throws ServletException {
        Login existenceLogin = loginService.validateLogin(token);
        loginService.delete(existenceLogin);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/logins/authenticate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Login> authenticatedLogin(@RequestBody Login login) throws ServletException {
        return new ResponseEntity<>(loginService.authenticate(login), HttpStatus.OK);
    }

    @RequestMapping(value = "v1/logins", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Login> getOneEvent(@RequestHeader(value = "Authorization") String token) throws ServletException {
        return new ResponseEntity<>(loginService.validateLogin(token), HttpStatus.OK);
    }
}