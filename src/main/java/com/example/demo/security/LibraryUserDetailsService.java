package com.example.demo.security;

import com.example.demo.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//import com.example.wl.datamodel.dao.Library;

@Component
public class LibraryUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //这里应该和数据库交互
        com.example.demo.entity.User user = userDao.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("找不到USER");
        }
        int id = user.getId();
        String pwd = user.getPassword();
        int root = user.getRoot();//0用户，1小，2大ROLE_USER,ROLE_JUNIOR,ROLE_SENIOR
        int state = user.getState();//0正常，1禁言，2封禁,ROLE_FREE,ROLE_SILENCE,ROLE_BAN
        if (state == 2) {
            System.out.println("throw!!!!");
            throw new UsernameNotFoundException("您已被封号");
//            try {
//                throw new MyException(4,"您已被封号");
//            } catch (MyException e) {
//                e.printStackTrace();
//            }
        }
        List<GrantedAuthority> authorities = new ArrayList<>();

        switch (root) {
            case 0:
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                break;
            case 1:
                authorities.add(new SimpleGrantedAuthority("ROLE_JUNIOR"));
                break;
            case 2:
                authorities.add(new SimpleGrantedAuthority("ROLE_SENIOR"));
        }
        switch (state) {
            case 0:
                authorities.add(new SimpleGrantedAuthority("ROLE_FREE"));
                break;
            case 1:
                authorities.add(new SimpleGrantedAuthority("ROLE_SILENCE"));
                break;
            case 2:
                authorities.add(new SimpleGrantedAuthority("ROLE_BAN"));
        }
//                List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN","ROLE_USER");//这样就可以有两种权限了

//        com.example.demo.datamodel.domainmodel.User user = Library.users.get(username);
//        String pwd = user.getPassword();
//
//        if (pwd == null) {
//            throw new UsernameNotFoundException(String.format("User with the username %s doesn't exist", username));
//        }
//
//        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(user.getRoot());
////        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN","ROLE_USER");//这样就可以有两种权限了
////        if(username.equals("Jack")) return null;//或者？抛出异常？？？
//
        return new User(String.valueOf(id), pwd, authorities);//这里的pwd是正确的应该的pwd，拿去跟输入的实际的比较。
//        return null;
    }
}

