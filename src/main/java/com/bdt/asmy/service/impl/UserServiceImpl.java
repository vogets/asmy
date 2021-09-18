package com.bdt.asmy.service.impl;

import com.bdt.asmy.domain.User;
import com.bdt.asmy.repository.UserAccountRepository;
import com.bdt.asmy.service.UserService;
import com.bdt.asmy.user.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    UserAccountRepository userAccountRepository;
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User userDetails=userAccountRepository.findByEmail(userName);
        if(userDetails==null)
        {
            throw new UsernameNotFoundException("No User Found by UserName: "+userName);
        }
        else
        {
            userAccountRepository.save(userDetails);
            UserData userData=new UserData(userDetails);
            return userData;
        }

    }
}
