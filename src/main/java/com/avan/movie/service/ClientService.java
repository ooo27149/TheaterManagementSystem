package com.avan.movie.service;

import com.avan.movie.po.Client;

public interface ClientService {

    Client checkClient(String phone, String password);

    Client saveClient(Client client);

    Client getClient(Long id);

    Client getClientByPhone(String phone);

    Client updateClient(Long id, Client client);

//    void deleteAdmin(Long id);
}
