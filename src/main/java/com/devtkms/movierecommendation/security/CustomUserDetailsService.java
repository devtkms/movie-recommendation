package com.devtkms.movierecommendation.security;

import com.devtkms.movierecommendation.entity.UserEntity;
import com.devtkms.movierecommendation.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper; // DAO for accessing user data

    /**
     * Loads user details by username (user ID).
     *
     * @param userId the username (user ID) of the user to be loaded
     * @return UserDetails containing the user's information
     * @throws UsernameNotFoundException if the user cannot be found
     */
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        try {
            UserEntity user = userMapper.selectUser(userId); // Retrieve user from the database
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
