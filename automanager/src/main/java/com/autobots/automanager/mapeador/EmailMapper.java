package com.autobots.automanager.mapeador;

import com.autobots.automanager.dto.requisicao.EmailRequest;
import com.autobots.automanager.dto.resposta.EmailResponse;
import com.autobots.automanager.entidade.Email;
import org.springframework.stereotype.Component;

@Component
public class EmailMapper {

    public static EmailResponse toResponse(Email email) {
        EmailResponse r = new EmailResponse();
        r.setId(email.getId());
        r.setEndereco(email.getEndereco());
        return r;
    }

    public static Email toEntity(EmailRequest request) {
        Email email = new Email();
        email.setEndereco(request.getEndereco().trim().toLowerCase());
        return email;
    }
}
