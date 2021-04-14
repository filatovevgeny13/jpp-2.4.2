package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dao.UserDao;
import web.model.Role;
import web.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@Qualifier("UserServiceImpl")
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserDao userDao;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public void delete(int id) {
        userDao.delete(id);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public void encodePassword(User user){
        user.setPassword(encoder.encode(user.getPassword()));
    }

    @Override
    public void setActiveAndRoles(User user, String Active, String isAdmin, String isUser){
        if (Active != null && Active.equals("true")){
            user.setActive(true);
        }
        if (isAdmin != null && isAdmin.equals("true")){
            user.addRoleToUser(new Role("ROLE_ADMIN"));
        }
        if (isUser != null && isUser.equals("true")){
            user.addRoleToUser(new Role("ROLE_USER"));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(int id) {
        return userDao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email){
        return userDao.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userDao.findByEmail(s);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for(Role role : user.getRoles()){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return new org.springframework.security.core.userdetails.User
                (user.getEmail(), user.getPassword(), grantedAuthorities);
    }
}
