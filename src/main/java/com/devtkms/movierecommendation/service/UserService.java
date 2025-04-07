    package com.devtkms.movierecommendation.service;

    import com.devtkms.movierecommendation.dto.UserRegisterRequestDto;
    import com.devtkms.movierecommendation.entity.UserEntity;
    import com.devtkms.movierecommendation.mapper.UserMapper;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;

    import java.time.LocalDateTime;
    import java.util.UUID;

    @Service
    public class UserService {

        private final UserMapper userMapper;
        private final PasswordEncoder passwordEncoder;

        public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
            this.userMapper = userMapper;
            this.passwordEncoder = passwordEncoder;
        }

        public UserEntity registerUser(UserRegisterRequestDto userDto) {
            // メールの重複チェック
            if (userMapper.selectUser(userDto.getEmail()) != null) {
                throw new RuntimeException("このメールアドレスは既に登録されています");
            }

            // パスワードをハッシュ化
            String hashedPassword = passwordEncoder.encode(userDto.getPassword());

            // ユーザーエンティティにマッピング
            UserEntity user = new UserEntity();
            user.setEmail(userDto.getEmail());
            user.setPassword(hashedPassword);
            user.setNickname(userDto.getNickname());
            user.setUseProviderName(userDto.getUseProviderName());
            user.setUseProviderId(userDto.getUseProviderId());
            user.setFavoriteMovieName(userDto.getFavoriteMovieName());
            user.setFavoriteMovieId(userDto.getFavoriteMovieId());
            user.setGender(userDto.getGender());
            user.setAgeGroup(userDto.getAgeGroup());
            user.setProvider("local");
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());

            // DB に保存
            userMapper.insertUser(user);
            return user;
        }

        public UserEntity findByEmail(String email) {
            UserEntity user = userMapper.selectUser(email);
            if (user == null) {
                throw new RuntimeException("ユーザーが見つかりません");
            }
            return user;
        }
    }