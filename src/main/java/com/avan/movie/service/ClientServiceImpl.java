package com.avan.movie.service;

import com.avan.movie.NotFoundException;
import com.avan.movie.dao.ClientRepository;
import com.avan.movie.po.Client;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Transactional
    @Override
    public Client checkClient(String phone, String password) {
        Client client = clientRepository.findByPhoneAndPassword(phone, password);
        return client;
    }
    @Transactional
    @Override
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }
    @Transactional
    @Override
    public Client getClient(Long id) {
        return clientRepository.findOne(id);
    }
    @Transactional
    @Override
    public Client getClientByPhone(String phone) {
        return clientRepository.findByPhone(phone);
    }
    @Transactional
    @Override
    public Client updateClient(Long id, Client client) {
        Client temp = clientRepository.findOne(id);
        if (temp == null) {
            throw new NotFoundException("the client does not exist");
        }
        BeanUtils.copyProperties(client, temp);
        return clientRepository.save(temp);
    }
}
