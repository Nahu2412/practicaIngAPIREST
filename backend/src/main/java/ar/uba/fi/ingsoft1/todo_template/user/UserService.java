package ar.uba.fi.ingsoft1.todo_template.user;

import ar.uba.fi.ingsoft1.todo_template.common.exception.ItemNotFoundException;
import ar.uba.fi.ingsoft1.todo_template.config.security.JwtService;
import ar.uba.fi.ingsoft1.todo_template.config.security.JwtUserDetails;
import ar.uba.fi.ingsoft1.todo_template.user.refresh_token.RefreshToken;
import ar.uba.fi.ingsoft1.todo_template.user.refresh_token.RefreshTokenService;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.access.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
class UserService implements UserDetailsService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    UserService(
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            RefreshTokenService refreshTokenService
    ) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> {
                    var msg = String.format("Username '%s' not found", username);
                    return new UsernameNotFoundException(msg);
                });
    }

    Optional<TokenDTO> createUser(UserCreateDTO data) {
        if (userRepository.findByUsername(data.username()).isPresent()) {
            return loginUser(data);
        } else {
            var user = data.asUser(passwordEncoder::encode);
            userRepository.save(user);
            return Optional.of(generateTokens(user));
        }
    }

    Optional<TokenDTO> loginUser(UserCredentials data) {
        Optional<User> maybeUser = userRepository.findByUsername(data.username());
        return maybeUser
                .filter(user -> passwordEncoder.matches(data.password(), user.getPassword()))
                .map(this::generateTokens);
    }

    Optional<TokenDTO> refresh(RefreshDTO data) {
        return refreshTokenService.findByValue(data.refreshToken())
                .map(RefreshToken::user)
                .map(this::generateTokens);
    }

    private TokenDTO generateTokens(User user) {
        String accessToken = jwtService.createToken(new JwtUserDetails(
                user.getUsername(),
                user.getRole()
        ));
        RefreshToken refreshToken = refreshTokenService.createFor(user);
        return new TokenDTO(accessToken, refreshToken.value());
    }

    public UserProfileDTO getUserProfile(Long id) throws ItemNotFoundException {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("user", id));
        return new UserProfileDTO(user);
    }

    public UserProfileDTO editUserProfile(Long id, UserProfileDTO userProfileDTO) throws ItemNotFoundException {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("user", id));

        if (userProfileDTO.username() != null && !userProfileDTO.username().isEmpty()) {
            user.setUsername(userProfileDTO.username());
        }
        if (userProfileDTO.firstName() != null && !userProfileDTO.firstName().isEmpty()) {
            user.setFirstName(userProfileDTO.firstName());
        }
        if (userProfileDTO.lastName() != null && !userProfileDTO.lastName().isEmpty()) {
            user.setLastName(userProfileDTO.lastName());
        }
        if (userProfileDTO.avatarUrl() != null && !userProfileDTO.avatarUrl().isEmpty()) {
            user.setAvatarUrl(userProfileDTO.avatarUrl());
        }

        userRepository.save(user);
        return new UserProfileDTO(user);
    }

    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        refreshTokenService.deleteByUser(user);
        userRepository.delete(user);
    }


    List<UserDTO> getFollowers(long id) throws ItemNotFoundException {
        List<UserDTO> followers = new ArrayList<>();
        var user = userRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("user",id));
        var user_followers = user.getFollowers();
        for (Long fID: user_followers){
            User f = userRepository.findById(fID).orElseThrow(() -> new ItemNotFoundException("user", fID));
            var follower = new UserDTO(f);
            followers.add(follower);
        }
        return followers;
    }

    List<UserDTO> getFollowing(long id) throws ItemNotFoundException {
        List<UserDTO> following = new ArrayList<>();
        var user = userRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("user",id));
        var user_following = user.getFollowing();
        for (Long fID: user_following){
            User f = userRepository.findById(fID).orElseThrow(() -> new ItemNotFoundException("user", fID));
            var follow = new UserDTO(f);
            following.add(follow);
        }
        return following;
    }

    UserDTO followTo(long id, long target) throws ItemNotFoundException{
        var user = userRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("user", id));
        var user_target = userRepository.findById(target).orElseThrow(() -> new ItemNotFoundException("user", target));
        user.addFollowing(target);
        user_target.addFollower(id);
        return new UserDTO(user);
    }

    public void createAdmin(UserCreateDTO data){
        User user = data.asUser(passwordEncoder::encode);
        user.promover();
        userRepository.save(user);
    }

    public void deleteAdmin(String username){
        User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("No existe"));
        if(!user.getRole().equals("ADMIN")){
            throw new AccessDeniedException("No es un administrador");
        }
        refreshTokenService.deleteByUser(user);
        userRepository.delete(user);
    }

}
