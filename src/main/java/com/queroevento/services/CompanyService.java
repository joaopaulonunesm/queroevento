package com.queroevento.services;

import javax.servlet.ServletException;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.queroevento.models.Company;
import com.queroevento.models.Login;
import com.queroevento.repositories.CompanyRepository;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final LoginService loginService;

    public Company save(Company user) {
        return companyRepository.save(user);
    }

    public Company findByNameIgnoreCase(String name) {
        return companyRepository.findByNameIgnoreCase(name).orElseThrow(() -> new RuntimeException("Erro ao buscar a Company por nome. Nome: " + name));
    }

    public Company validateCompanyByToken(String token) throws ServletException {

        Login login = loginService.validateLogin(token);

        return login.getCompany();
    }

    public Company validateCompanyModeratorByToken(String token) throws ServletException {

        Company company = validateCompanyByToken(token);

        if (!company.getModerator()) {
            throw new ServletException("Acesso negado! Entre em contato com o moderador do Quero Evento.");
        }

        return company;
    }

    public Company putCompany(Company company, Company existenceCompany) {

        company.setId(existenceCompany.getId());

        return save(company);
    }

    public Company putCompanyModerator(Company existenceCompany, Company company) {
        company.setId(existenceCompany.getId());
        company.setModerator(!company.getModerator());
        return save(company);
    }

    public Company findCompanyByUrlName(String urlName) {
        return companyRepository.findByUrlName(urlName).orElseThrow(() -> new RuntimeException("Empresa não encontrada pelo Nome URL. UrlName: " + urlName));
    }
}