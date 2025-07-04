package com.devtkms.movierecommendation.security;

import com.devtkms.movierecommendation.entity.UserEntity;
import com.devtkms.movierecommendation.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service to load user details from the database for Spring Security authentication.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    /**
     * Loads the user by their user ID.
     *
     * @param userId the ID of the user to look up
     * @return UserDetails object used by Spring Security
     * @throws UsernameNotFoundException if no user is found
     */
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        try {
            UserEntity user = userMapper.selectUser(userId);
            if (user == null) {
                throw new UsernameNotFoundException("User not found with userId: " + userId);
            }
            return new CustomUserDetails(user);

        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("Invalid userId format: " + userId, e);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Error occurred while fetching user with userId: " + userId, e);
        }
    }
}