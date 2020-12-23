package pro.nvart.apps.transportCitypoint.controller;

import org.springframework.web.bind.annotation.*;
import pro.nvart.apps.transportCitypoint.exceptions.AuthException;
import pro.nvart.apps.transportCitypoint.exceptions.InternalErrorException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


@RestController
@RequestMapping("/api/v1/refuels")
public class RefuelsController {
    Delor session;
    @GetMapping("{id}")
    public ArrayList<HashMap<Delor.enumDetail, String>> list(@PathVariable long id,
                                                         @CookieValue(value = "token", defaultValue="") String token) {
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

        ArrayList<HashMap<Delor.enumDetail, String>> list = new ArrayList<HashMap<Delor.enumDetail, String>>();
        try {
            list = session.getDetail(id);
        } catch (IOException e) {
            throw new InternalErrorException();
        } catch (NullPointerException e) {
        //    throw new AuthException();
            return list;
        }
        return list;
    }
}
