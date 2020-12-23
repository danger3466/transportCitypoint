package pro.nvart.apps.transportCitypoint.controller;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.nvart.apps.transportCitypoint.exceptions.AuthException;
import pro.nvart.apps.transportCitypoint.exceptions.InternalErrorException;

import java.io.IOException;


@RestController
@RequestMapping("/api/v1/cards")
public class CardsController {
    Delor session;
    @GetMapping
    public long[] list(@CookieValue(value = "token", defaultValue="") String token) {
        if (!token.equals(System.getenv("token")))
            throw new AuthException();

        if (session == null) {
            session = new Delor();
            session.setLogin(System.getenv("DELOR_login"));
            session.setPassword(System.getenv("DELOR_password"));
            session.setBaseURL(System.getenv("DELOR_url"));
            try {
                session.getSession();
            } catch (IOException e) {
                throw new InternalErrorException();
            } catch (IllegalArgumentException e){
                throw new InternalErrorException();
            }
        }

        long[] cards = null;
        try {
            cards = session.getCards();
        } catch (IOException e) {
            throw new InternalErrorException();
        } catch (NullPointerException e) {
            throw new AuthException();
        }
        //System.out.println(cards);
        return  cards;
    }
}
